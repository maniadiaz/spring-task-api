# task-api

REST API for personal task management built with Spring Boot, applying SOLID principles, Repository Pattern, DTOs, global exception handling, and unit/integration testing with JUnit 5 and Mockito.

## Stack

- Java 21
- Spring Boot 4.1.0
- Maven
- Lombok
- JUnit 5 + Mockito
- JaCoCo

## Endpoints

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| GET | `/api/tasks` | Get all tasks | 200 |
| GET | `/api/tasks?status=pending` | Filter by status | 200 |
| GET | `/api/tasks/{id}` | Get task by ID | 200 / 404 |
| POST | `/api/tasks` | Create a new task | 201 |
| PUT | `/api/tasks/{id}/complete` | Mark as completed | 200 / 404 |
| DELETE | `/api/tasks/{id}` | Delete a task | 204 / 404 |

## Running the project

```bash
./mvnw spring-boot:run
