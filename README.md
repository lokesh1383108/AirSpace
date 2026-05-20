# AirSpace

AirSpace is a Spring Boot-based backend application for an airspace/property booking platform. It provides REST APIs, authentication, booking management, email/OTP flows, and integration with MongoDB, WebSocket, RabbitMQ, and Spring Security.

## Key Features

- Spring Boot 3.3.4 application using Java 21
- MongoDB persistence with Spring Data MongoDB
- User authentication and authorization via Spring Security
- JWT-based auth support
- OAuth2 login support
- OTP generation and email verification flows
- Booking request and property management APIs
- Email templates for OTP and welcome messages
- WebSocket support for real-time messaging
- Spring Cloud Stream with RabbitMQ binder
- Actuator endpoints for health and monitoring

## Technology Stack

- Java 21
- Spring Boot 3.3.4
- Spring Security
- Spring Data MongoDB
- Spring Boot Web
- Spring Boot WebSocket
- Spring Cloud Stream + RabbitMQ
- Spring Boot Actuator
- Thymeleaf templates for email content
- Jakarta Validation
- JSON Web Token (jjwt)
- Lombok (provided)

## Project Structure

- `src/main/java/com/example/airspace/AirSpace`
  - `controllers/` - REST controllers for auth, booking, customer, host, user flows
  - `DTOs/` - request and response transfer objects
  - `models/` - domain entities, OTP records, property, booking, ratings
  - `repositories/` - MongoDB repositories
  - `security/` - application security configuration and JWT handling
  - `service/` - business logic and integration services
  - `constants/` - email templates and OTP purpose constants
- `src/main/resources/` - Spring properties and email templates
  - `application.properties`
  - `application-dev.properties`
  - `application-prod.properties`
  - `templates/email/` - HTML email templates
- `src/test/java/` - application tests

## Local Setup

1. Install Java 21 and Maven.
2. Configure MongoDB locally or remotely.
3. Update `src/main/resources/application.properties` or the active profile file for database, email, security, and RabbitMQ settings.
4. Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Build

```bash
./mvnw clean package
```

## Testing

```bash
./mvnw test
```

## Configuration

- `application.properties` contains default Spring Boot settings.
- `application-dev.properties` and `application-prod.properties` support environment-specific overrides.
- Email templates are stored under `src/main/resources/templates/email/`.

## Notes

- Ensure MongoDB is running and accessible before starting the app.
- If RabbitMQ integration is used, configure the broker connection and Spring Cloud Stream properties.
- The application enables Mongo auditing via `@EnableMongoAuditing`.

## Contact

For additional details or troubleshooting, inspect the controller, service, and repository packages under `src/main/java/com/example/airspace/AirSpace/`.
