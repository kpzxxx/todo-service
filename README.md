# Todo Service

## Description

This project implements a simple RESTful backend service for managing todo items.

Each todo item contains a description, status, creation time, due time and completion time.

Supported operations include:
- creating a todo
- retrieving todos
- updating the description
- marking a todo as done or not done

If the due time passes and the todo is not completed, it will be marked as `PAST_DUE`.
A scheduled task periodically checks and updates overdue items.

### Assumptions

- Todos have a due time.
- Overdue todos cannot be modified.
- Business errors are returned with a unified error response containing status, code and message.
- H2 in-memory database is used for simplicity.
- The `dueAt` field is treated as the source of truth for determining whether a task is overdue.
- The `status` field is persisted for convenience and updated periodically by a scheduled job.
- When returning API responses, the overdue state may be derived from `dueAt` to avoid temporary inconsistencies between scheduler runs.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 database
- Maven
- Lombok
- JUnit 5 / Spring Boot Test

The service can also be run in a Docker container.

---

## Build the Service

Build the project with Maven:

```bash
mvn clean package
```

The executable jar will be generated under:

```
target/todo-service-0.0.1-SNAPSHOT.jar
```

---

## Run Tests

Run automated tests using:

```bash
mvn test
```

Tests include controller tests and service tests to verify the main business logic.

---

## Run Locally

Run the service with Maven:

```bash
mvn spring-boot:run
```

The service will start at:

http://localhost:8080

Or run the packaged jar:

```bash
java -jar target/todo-service-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

Build the Docker image:

```bash
docker build -t todo-service .
```

Run the container:

```bash
docker run -p 8080:8080 todo-service
```

The service will be available at:

http://localhost:8080

## API Overview

| Method | Endpoint | Description |
|------|------|------|
| POST | `/todos` | Create a todo |
| GET | `/todos` | Retrieve todos (default returns not-done items) |
| GET | `/todos?all=true` | Retrieve all todos |
| GET | `/todos/{id}` | Retrieve a specific todo |
| PATCH | `/todos/{id}/description` | Update description |
| PATCH | `/todos/{id}/done` | Mark todo as done |
| PATCH | `/todos/{id}/not-done` | Mark todo as not done |