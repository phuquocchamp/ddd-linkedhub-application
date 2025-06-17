# Copilot Customization for LinkedHub Java Spring Boot Microservices

## Project Context
- This project is a Java Spring Boot monorepo with multiple microservices.
- Follows Domain-Driven Design (DDD) principles.
- Each microservice is in its own directory (e.g., `auth-service/`, `profile-service/`).
- Shared configuration/scripts are in the root or `docker/`.

## 

## Coding Conventions
- Use Java 17+ features where possible.
- Follow standard Java naming conventions (camelCase for variables/methods, PascalCase for classes).
- Organize code by DDD layers: domain, application, infrastructure, and interfaces.
- Place domain models, repositories, and services in the `domain` package.
- Place REST controllers in the `interfaces` or `web` package.
- Use DTOs for API boundaries.
- Write unit and integration tests for all business logic.
- Use meaningful, descriptive names for classes, methods, and variables.
- Prefer immutability and constructor injection.

## Git & PR Best Practices

- Follow the branching and commit conventions in [git.instructions.md](/.github/instrucions/git.instructions.md).
- PRs must include tests and pass all CI checks.

## Copilot Suggestions
- Prefer generating code that:
  - Respects DDD boundaries and package structure.
  - Uses Spring Boot idioms (e.g., `@Service`, `@Repository`, `@RestController`).
  - Follows SOLID and clean code principles.
  - Includes JavaDoc for public classes and methods.
  - Uses exception handling best practices (custom exceptions, global handlers).
  - Uses Lombok for boilerplate reduction where appropriate.
  - Uses MapStruct for mapping between entities and DTOs.
- For tests, use JUnit 5 and Mockito.
- For configuration, use `application.yml` and environment variables.

## Example Structure
- `src/main/java/com/example/service/domain/` – Entities, value objects, repositories, domain services
- `src/main/java/com/example/service/application/` – Application services, use cases
- `src/main/java/com/example/service/infrastructure/` – Data access, external integrations
- `src/main/java/com/example/service/interfaces/` – REST controllers, API endpoints

## Documentation
- Add JavaDoc to all public APIs.
- Update `docs/` for architectural or workflow changes.

- Add OpenAPI Documentation follow by [openapi.instructions.md](/.github/instructions/openapi.instructions.md)


## References

- [https://spring.io/projects/spring-framework](https://spring.io/projects/spring-framework)

---


