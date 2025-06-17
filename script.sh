#!/bin/bash

# LinkedHub Application Startup Script
# This script starts all services in the correct order to ensure proper dependency resolution

set -e  # Exit on any error

# Configuration file
SERVICES_CONFIG="services.conf"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/$SERVICES_CONFIG"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Array to store service information
declare -a SERVICES=()

# List of docker-compose files
DOCKER_COMPOSE_FILES=(
    "docker/general/docker-compose.yml"
    "docker/auth/docker-compose.yml"
    "docker/profile/docker-compose.yml"
    "docker/gateway/docker-compose.yml"
)

# Function to load services from config file
load_services() {
    if [[ ! -f "$CONFIG_FILE" ]]; then
        print_error "Configuration file $CONFIG_FILE not found!"
        exit 1
    fi
    
    while IFS= read -r line || [[ -n "$line" ]]; do
        # Skip empty lines and comments
        if [[ -z "$line" ]] || [[ "$line" =~ ^[[:space:]]*# ]]; then
            continue
        fi
        
        SERVICES+=("$line")
    done < "$CONFIG_FILE"
    
    print_status "Loaded ${#SERVICES[@]} services from configuration"
}

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_attempts=${3:-30}
    local attempt=0
    
    print_status "Waiting for $service_name to be ready on port $port..."
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -s -f "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
            print_success "$service_name is ready!"
            return 0
        fi
        
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    print_error "$service_name failed to start within expected time"
    return 1
}

# Function to start a service
start_service() {
    local service_info=$1
    
    # Parse service info: name:directory:port:delay
    IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
    
    print_status "Starting $service_name from directory $service_dir on port $port..."
    
    # Check if service directory exists
    if [[ ! -d "$service_dir" ]]; then
        print_error "Service directory $service_dir does not exist!"
        return 1
    fi
    
    cd "$service_dir"
    
    # Kill existing process if running
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        print_warning "$service_name is already running on port $port. Stopping it first..."
        pkill -f "$service_name" || true
        sleep 3
    fi
    
    # Check if pom.xml exists
    if [[ ! -f "pom.xml" ]]; then
        print_error "No pom.xml found in $service_dir"
        cd ..
        return 1
    fi
    
    # Clean and compile first
    print_status "Cleaning and compiling $service_name..."
    mvn clean compile -q > "../logs/${service_name}-build.log" 2>&1
    
    if [[ $? -ne 0 ]]; then
        print_error "Failed to compile $service_name. Check ../logs/${service_name}-build.log for details"
        cd ..
        return 1
    fi
    
    # Start the service in background with proper Maven goal
    print_status "Starting Spring Boot application for $service_name..."
    mvn spring-boot:run -Dspring-boot.run.fork=true > "../logs/${service_name}.log" 2>&1 &
    local pid=$!
    echo $pid > "../logs/${service_name}.pid"
    
    print_status "$service_name started with PID $pid"
    
    # Return to root directory
    cd ..
    
    # Wait for service to be ready
    if wait_for_service "$service_name" "$port"; then
        print_success "$service_name is fully started and ready!"
        # Wait for specified startup delay before proceeding to next service
        if [[ $startup_delay -gt 0 ]]; then
            print_status "Waiting ${startup_delay}s before starting next service..."
            sleep $startup_delay
        fi
    else
        print_error "Failed to start $service_name properly"
        return 1
    fi
}

# Function to start all docker-compose services
start_docker_compose() {
    print_status "Starting all docker-compose services..."
    for compose_file in "${DOCKER_COMPOSE_FILES[@]}"; do
        if [[ -f "$compose_file" ]]; then
            print_status "Starting services in $compose_file ..."
            docker compose -f "$compose_file" up -d
        else
            print_warning "File $compose_file not found, skipping."
        fi
    done
    print_success "All docker-compose services started."
}

# Function to stop all docker-compose services
stop_docker_compose() {
    print_status "Stopping all docker-compose services..."
    for compose_file in "${DOCKER_COMPOSE_FILES[@]}"; do
        if [[ -f "$compose_file" ]]; then
            print_status "Stopping services in $compose_file ..."
            docker compose -f "$compose_file" down
        else
            print_warning "File $compose_file not found, skipping."
        fi
    done
    print_success "All docker-compose services stopped."
}

# Function to restart all docker-compose services
restart_docker_compose() {
    stop_docker_compose
    start_docker_compose
}

# Function to stop all services
stop_all_services() {
    print_status "Stopping all services..."
    
    # Load services config to get the list
    load_services
    
    # Stop services in reverse order
    for ((i=${#SERVICES[@]}-1; i>=0; i--)); do
        local service_info="${SERVICES[i]}"
        IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
        
        if [ -f "logs/${service_name}.pid" ]; then
            local pid=$(cat "logs/${service_name}.pid")
            if ps -p $pid > /dev/null 2>&1; then
                print_status "Stopping $service_name (PID: $pid)..."
                kill $pid
                sleep 2
            fi
            rm -f "logs/${service_name}.pid"
        fi
    done
    
    # Kill any remaining Java processes related to our services
    pkill -f "spring-boot:run" || true
    
    print_success "All services stopped"
}

# Function to start a specific service by name
start_specific_service() {
    local target_service=$1
    
    if [[ -z "$target_service" ]]; then
        print_error "Service name is required"
        print_status "Usage: ./script.sh start-service <service-name>"
        return 1
    fi
    
    # Load services config
    load_services
    
    # Find the service in config
    local found=false
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
        if [[ "$service_name" == "$target_service" ]]; then
            found=true
            print_status "Starting specific service: $service_name"
            start_service "$service_info"
            break
        fi
    done
    
    if [[ "$found" == false ]]; then
        print_error "Service '$target_service' not found in configuration"
        print_status "Available services:"
        for service_info in "${SERVICES[@]}"; do
            IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
            echo "  - $service_name"
        done
        return 1
    fi
}

# Function to stop a specific service by name
stop_specific_service() {
    local target_service=$1
    
    if [[ -z "$target_service" ]]; then
        print_error "Service name is required"
        print_status "Usage: ./script.sh stop-service <service-name>"
        return 1
    fi
    
    # Load services config
    load_services
    
    # Find the service in config
    local found=false
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
        if [[ "$service_name" == "$target_service" ]]; then
            found=true
            print_status "Stopping specific service: $service_name"
            
            if [ -f "logs/${service_name}.pid" ]; then
                local pid=$(cat "logs/${service_name}.pid")
                if ps -p $pid > /dev/null 2>&1; then
                    print_status "Stopping $service_name (PID: $pid)..."
                    kill $pid
                    sleep 2
                    print_success "$service_name stopped successfully"
                else
                    print_warning "$service_name is not running (PID file exists but process not found)"
                fi
                rm -f "logs/${service_name}.pid"
            else
                print_warning "$service_name is not running (no PID file found)"
            fi
            break
        fi
    done
    
    if [[ "$found" == false ]]; then
        print_error "Service '$target_service' not found in configuration"
        print_status "Available services:"
        for service_info in "${SERVICES[@]}"; do
            IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
            echo "  - $service_name"
        done
        return 1
    fi
}

# Function to show service status
show_status() {
    print_status "Service Status:"
    echo "===================="
    
    # Load services config
    load_services
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name service_dir port startup_delay <<< "$service_info"
        if curl -s -f "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
            echo -e "$service_name: ${GREEN}RUNNING${NC} (port $port)"
        else
            echo -e "$service_name: ${RED}STOPPED${NC} (port $port)"
        fi
    done
}

# Create logs directory if it doesn't exist
mkdir -p logs

# Main script logic
case "${1:-start}" in
    "start")
        print_status "Starting LinkedHub Application Services..."
        print_status "========================================="
        # Start docker-compose services first
        start_docker_compose
        # Load services configuration
        load_services
        
        # Start services in order from config file
        for service_info in "${SERVICES[@]}"; do
            start_service "$service_info"
        done
        
        print_success "All services started successfully!"
        print_status "You can check the status with: ./script.sh status"
        print_status "You can stop all services with: ./script.sh stop"
        ;;
        
    "start-service")
        start_specific_service "$2"
        ;;
        
    "stop-service")
        stop_specific_service "$2"
        ;;
        
    "start-docker")
        start_docker_compose
        ;;
        
    "stop-docker")
        stop_docker_compose
        ;;
        
    "restart-docker")
        restart_docker_compose
        ;;
        
    "stop")
        stop_all_services
        stop_docker_compose
        ;;
        
    "restart")
        print_status "Restarting all services..."
        stop_all_services
        stop_docker_compose
        sleep 5
        $0 start
        ;;
        
    "status")
        show_status
        ;;
        
    "logs")
        if [ -n "$2" ]; then
            if [ -f "logs/$2.log" ]; then
                tail -f "logs/$2.log"
            else
                print_error "Log file for $2 not found"
                exit 1
            fi
        else
            print_status "Available log files:"
            ls -la logs/*.log 2>/dev/null || print_warning "No log files found"
            print_status "Usage: ./script.sh logs <service-name>"
        fi
        ;;
        
    "help"|"-h"|"--help")
        echo "LinkedHub Application Management Script"
        echo "======================================"
        echo
        echo "Usage: ./script.sh [command] [options]"
        echo
        echo "Commands:"
        echo "  start             - Start all docker-compose and Java services in correct order (default)"
        echo "  stop              - Stop all Java services and docker-compose services"
        echo "  restart           - Restart all Java services and docker-compose services"
        echo "  status            - Show current status of all Java services"
        echo "  logs [service]    - Show available log files or tail a specific service log"
        echo "  start-service <service>   - Start a specific Java service by name (from services.conf)"
        echo "  stop-service <service>    - Stop a specific Java service by name (from services.conf)"
        echo "  start-docker      - Start all docker-compose services only"
        echo "  stop-docker       - Stop all docker-compose services only"
        echo "  restart-docker    - Restart all docker-compose services only"
        echo "  help              - Show this help message"
        echo
        echo "Examples:"
        echo "  ./script.sh start"
        echo "  ./script.sh stop"
        echo "  ./script.sh restart"
        echo "  ./script.sh status"
        echo "  ./script.sh logs auth-service"
        echo "  ./script.sh start-service profile-service"
        echo "  ./script.sh stop-service gateway-service"
        echo "  ./script.sh start-docker"
        echo "  ./script.sh stop-docker"
        echo "  ./script.sh restart-docker"
        echo
        echo "Configuration:"
        echo "  Java services are configured in: services.conf (format: service_name:directory:port:startup_delay)"
        echo "  Docker Compose files are managed in:"
        for compose_file in "${DOCKER_COMPOSE_FILES[@]}"; do
            echo "    - $compose_file"
        done
        echo
        echo "How it works:"
        echo "  - 'start' will start all docker-compose services first, then all Java services in order."
        echo "  - 'stop' will stop all Java services, then all docker-compose services."
        echo "  - You can start/stop/restart only docker-compose services with the respective commands."
        echo "  - You can start/stop a specific Java service by name using start-service/stop-service."
        echo "  - Log files for each Java service are stored in the logs/ directory."
        echo
        echo "Note:"
        echo "  - Ensure Docker is running for docker-compose commands."
        echo "  - Ensure ports in services.conf and docker-compose files do not conflict."
        echo
        ;;
        
    *)
        print_error "Unknown command: $1"
        print_status "Use './script.sh help' for usage information"
        exit 1
        ;;
esac