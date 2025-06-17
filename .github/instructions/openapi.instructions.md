# OpenAPI/Swagger Instructions for Copilot Customization

## Purpose
This file provides Copilot with best practices and conventions for generating OpenAPI (Swagger) specifications for LinkedHub's Java Spring Boot microservices, following the official OpenAPI Specification (https://swagger.io/specification/) and project standards.

## General Guidelines
- Use OpenAPI 3.0.3 or newer.
- All APIs must be described using OpenAPI YAML or JSON format.
- Use clear, descriptive titles, summaries, and descriptions for all endpoints, parameters, and schemas.
- Document all request/response bodies, query/path parameters, and error responses.
- Use meaningful tags to group related endpoints.
- Reference DTOs and domain models in `components/schemas`.
- Use standard HTTP status codes and document possible responses for each endpoint.
- Provide example values for all fields and responses.
- Use camelCase for property names in schemas.
- Use enums for fields with a fixed set of values.
- Document authentication/authorization requirements using `securitySchemes`.

## Project-Specific Conventions
- Each microservice should have its own OpenAPI spec file (e.g., `api-docs.yml`).
- Place OpenAPI files in `src/main/resources/` of each service.
- All endpoints must be versioned (e.g., `/api/v1/...`).
- Use tags to reflect DDD boundaries (e.g., `Authentication`, `Profile`).
- Reference DTO schemas for request/response bodies.
- Document error models and standardize error responses.
- Add contact and license info in the `info` section.

## Example Structure
```yaml
openapi: 3.0.3
info:
  title: Auth Service API
  version: 1.0.0
  description: OpenAPI spec for the Authentication microservice
  contact:
    name: LinkedHub Team
    email: support@linkedhub.com
  license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
servers:
  - url: /api/v1/auth
paths:
  /login:
    post:
      summary: User login
      tags: [Authentication]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AuthenticationRequest'
      responses:
        '200':
          description: Login successful
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/TokenResponse'
        '401':
          description: Invalid credentials
components:
  schemas:
    AuthenticationRequest:
      type: object
      properties:
        username:
          type: string
        password:
          type: string
    TokenResponse:
      type: object
      properties:
        accessToken:
          type: string
        refreshToken:
          type: string
```

## Best Practices
- Keep the spec up to date with code changes.
- Use `$ref` to avoid duplication.
- Add examples for all DTOs and responses.
- Use `description` fields for all properties and endpoints.
- Validate OpenAPI files using tools like Swagger Editor or Spectral.

---

For more details, see the official [OpenAPI Specification](https://swagger.io/specification/) and project documentation in `docs/`.
