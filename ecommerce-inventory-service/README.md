# E-commerce Cart Service

Standalone Spring Boot Cart microservice scaffold.

## Technology
- Java 17
- Spring Boot 3.5.3
- Spring Web
- Spring Data JPA
- MySQL 8
- Maven

## Port and database
- Port: `8083`
- Initial database: `ecommerce` (shared transitional database)
- Tables expected: `cart_status`, `cart`, `cart_item`
- Existing referenced tables: `oprauth_users`, `product_item`, `product_variation`

## Before running
1. Execute `cart_service_schema.sql` against the existing `ecommerce` database.
2. Update `spring.datasource.username` and `spring.datasource.password` in `src/main/resources/application.properties`.
3. In Eclipse: File > Import > Maven > Existing Maven Projects; select this folder; Finish.
4. Right-click project > Maven > Update Project.
5. Run `CartServiceApplication` as Spring Boot App.

Health check:
`http://localhost:8083/actuator/health`

## Current scope
This is the project foundation: application, configuration, entities and repositories.
The eight Cart REST APIs are NOT implemented yet. We will add and test them one at a time.
