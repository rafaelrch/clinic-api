# Clinic API

A REST API for managing a medical clinic: patients, doctors and appointment scheduling. Built with Java 21 and Spring Boot 3, secured with stateless JWT authentication and role-based authorization, with business rules enforced in the service layer and a fully versioned database schema.

[![CI](https://github.com/rafaelrch/clinic-api/actions/workflows/ci.yml/badge.svg)](https://github.com/rafaelrch/clinic-api/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Live demo:** [clinic-api-gfku.onrender.com/swagger-ui/index.html](https://clinic-api-gfku.onrender.com/swagger-ui/index.html)

> The demo runs on Render's free tier and sleeps after 15 minutes of inactivity. The first request may take up to a minute while the service wakes up.

---

## Try it in 60 seconds

1. Open the [Swagger UI](https://clinic-api-gfku.onrender.com/swagger-ui/index.html).
2. Call `POST /login` with one of the demo accounts below and copy the `token` from the response.
3. Click **Authorize**, paste the token and confirm.
4. Call any endpoint allowed for that role.

| Role | Login | Password | What it can do |
|---|---|---|---|
| `ADMIN` | `admin@clinicapi.com` | `password` | Full access to every endpoint |
| `DOCTOR` | `doctor@clinicapi.com` | `password` | View patients and appointments |
| `PATIENT` | `patient@clinicapi.com` | `password` | View doctors, book and cancel appointments |

Or from the terminal:

```bash
curl -X POST https://clinic-api-gfku.onrender.com/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin@clinicapi.com", "password": "password"}'
```

```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```

The demo database is shared, so records created by other visitors may appear.

---

## API Endpoints

Every endpoint except `/login` and the Swagger documentation requires a `Bearer` token. Listing endpoints are paginated (`?page=0&size=20&sort=name,asc`).

| Method | Endpoint | Roles | Success | Description |
|---|---|---|---|---|
| `POST` | `/login` | public | `200` | Authenticates and returns a JWT |
| `POST` | `/patients` | ADMIN | `201` | Registers a patient |
| `GET` | `/patients` | ADMIN, DOCTOR | `200` | Lists patients |
| `GET` | `/patients/{id}` | ADMIN, DOCTOR | `200` | Finds a patient |
| `PUT` | `/patients/{id}` | ADMIN | `200` | Partially updates a patient |
| `DELETE` | `/patients/{id}` | ADMIN | `204` | Deletes a patient |
| `POST` | `/doctors` | ADMIN | `201` | Registers a doctor |
| `GET` | `/doctors` | ADMIN, PATIENT | `200` | Lists active doctors |
| `GET` | `/doctors/{id}` | ADMIN, PATIENT | `200` | Finds an active doctor |
| `PUT` | `/doctors/{id}` | ADMIN | `200` | Partially updates a doctor |
| `DELETE` | `/doctors/{id}` | ADMIN | `204` | Deactivates a doctor (soft delete) |
| `POST` | `/appointments` | ADMIN, PATIENT | `201` | Books an appointment |
| `GET` | `/appointments` | ADMIN, DOCTOR | `200` | Lists appointments |
| `GET` | `/appointments/{id}` | ADMIN, DOCTOR | `200` | Finds an appointment |
| `PUT` | `/appointments/{id}` | ADMIN | `200` | Reschedules an appointment |
| `PATCH` | `/appointments/{id}/cancel` | ADMIN, PATIENT | `200` | Cancels an appointment |

`POST` endpoints return `201 Created` with a `Location` header pointing to the new resource.

---

## Business Rules

**Booking an appointment** runs these checks in order and stops at the first failure:

1. The patient must exist (`404`).
2. The doctor must exist (`404`) and be active (`400`).
3. The doctor must not have another appointment at the same time (`400`).
4. The patient must not have another appointment at the same time (`400`).
5. The date must be in the future (`400`, enforced by Bean Validation).

Cancelled appointments are ignored by the conflict checks, so a cancelled slot can be booked again. New appointments always start as `SCHEDULED`.

**Rescheduling** only changes the date. Only `SCHEDULED` or `CONFIRMED` appointments can be rescheduled, and the conflict checks exclude the appointment being edited, so moving it to the same time never conflicts with itself.

**Cancelling** is allowed only for `SCHEDULED` or `CONFIRMED` appointments and at least 24 hours in advance.

**Doctors** are soft deleted: deactivation sets `active = false` and keeps the record, so appointment history stays intact. Inactive doctors disappear from listings and cannot be updated, deactivated again or booked.

**Patients** are hard deleted. If a patient still has linked appointments, the database foreign key blocks the deletion and the API returns `409 Conflict` instead of leaving orphaned records.

---

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Validation, Security) |
| Persistence | Hibernate 6, PostgreSQL |
| Migrations | Flyway |
| Security | Spring Security 6, JWT (`java-jwt`, HMAC256), BCrypt |
| Documentation | springdoc-openapi (Swagger UI) with Bearer authentication |
| Testing | JUnit 5, Mockito, AssertJ, Spring Security Test, H2 |
| Build | Maven (wrapper included) |
| Containers | Docker (multi-stage build), Docker Compose |
| CI/CD | GitHub Actions, Render |
| Database hosting | Neon (serverless PostgreSQL) |

---

## Architecture

Requests flow through a JWT filter and then through three layers. Controllers handle HTTP concerns only, services hold every business rule, and repositories talk to the database.

```mermaid
flowchart LR
    Client -->|Bearer token| Filter[SecurityFilter<br/>JWT validation]
    Filter --> Controller[Controller<br/>@PreAuthorize]
    Controller --> Service[Service<br/>business rules]
    Service --> Repository[Repository<br/>Spring Data JPA]
    Repository --> DB[(PostgreSQL)]
```

```
com.clinicapi
├── domain
│   ├── person        # Abstract base (@MappedSuperclass) shared by Patient and Doctor
│   ├── patient       # Entity, controller, DTOs, repository
│   ├── doctor        # Entity, controller, DTOs, repository, Specialty enum
│   ├── appointment   # Entity, controller, DTOs, repository, AppointmentStatus enum
│   ├── user          # UserDetails implementation and roles
│   └── service       # Business logic and domain exceptions
└── infra
    ├── security      # JWT filter, token service, security configuration, /login
    ├── exceptions    # Global exception handler and error payloads
    └── springdoc     # OpenAPI configuration
```

---

## Running Locally

### With Docker (recommended)

The only requirement is [Docker](https://www.docker.com/).

```bash
git clone https://github.com/rafaelrch/clinic-api.git
cd clinic-api
docker compose up --build
```

On the first start, Flyway creates the schema and seeds the three demo accounts. Then:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- PostgreSQL: `localhost:5433` (database `clinicapi`, user `postgres`, password `clinicapi_dev`)

`docker compose down` stops everything and keeps the data. `docker compose down -v` also deletes the database volume for a clean start.

### Without Docker for the application

Requires Java 21. The simplest way to get a database is to start only the `db` service from Compose:

```bash
docker compose up -d db

export DB_PASSWORD=clinicapi_dev
export JWT_SECRET=any-long-random-string
./mvnw spring-boot:run
```

### Configuration

All configuration comes from environment variables, so the same build runs locally, in Docker and in production.

| Variable | Required | Default | Description |
|---|---|---|---|
| `DATASOURCE_URL` | no | `jdbc:postgresql://localhost:5433/clinicapi` | JDBC URL of the database |
| `DB_USERNAME` | no | `postgres` | Database user |
| `DB_PASSWORD` | **yes** | none | Database password |
| `JWT_SECRET` | **yes** | none | Key used to sign and verify tokens |

Secrets intentionally have no default value: if one is missing, the application refuses to start instead of silently running with an insecure fallback.

---

## Error Handling

Every error returns a consistent JSON body.

```json
{
  "timestamp": "2026-09-23T21:40:00Z",
  "status": 400,
  "error": "Business rule violation",
  "message": "Appointments must be cancelled at least 24 hours in advance",
  "path": "/appointments/7/cancel"
}
```

Validation failures list every invalid field:

```json
{
  "timestamp": "2026-09-23T21:40:00Z",
  "status": 400,
  "error": "Validation error",
  "path": "/patients",
  "errors": [
    { "field": "email", "message": "must be a well-formed email address" }
  ]
}
```

| Status | When |
|---|---|
| `400` | Invalid payload or business rule violation |
| `401` | Token present but invalid, expired or tampered with |
| `403` | No token, or the role is not allowed for the endpoint |
| `404` | Resource not found (includes inactive doctors) |
| `409` | Operation blocked by a linked record |

---

## Testing

```bash
./mvnw test
```

41 tests, all running on every push through GitHub Actions.

| Test class | Tests | Scope |
|---|---|---|
| `AppointmentServiceTest` | 17 | Booking, rescheduling and cancellation rules |
| `DoctorServiceTest` | 8 | CRUD, soft delete, partial updates |
| `PatientServiceTest` | 8 | CRUD, partial updates |
| `DoctorControllerTest` | 5 | HTTP status, JSON payloads and authorization per role |
| `AppointmentTest` | 2 | Entity construction and initial status |
| `ClinicApiApplicationTests` | 1 | Application context smoke test |

Service tests are pure unit tests with Mockito, following the Arrange/Act/Assert pattern. They assert both the returned DTO and the state of the mutated entity, and use `verify(never())` to prove that nothing is saved or deleted when a rule fails. Controller tests use `@WebMvcTest` with the real `SecurityConfig` imported, so authorization is actually enforced in the test instead of being bypassed.

---

## CI/CD

- **Continuous integration:** GitHub Actions builds the project and runs the full test suite on every push and pull request to `main`.
- **Continuous deployment:** Render rebuilds the Docker image and redeploys on every push to `main`.
- **Database:** Neon serverless PostgreSQL. Flyway applies pending migrations automatically on startup.

---

## Technical Decisions

**Flyway instead of `ddl-auto=update`.** The schema lives in versioned SQL migrations, so every environment is built the same way from an empty database. Hibernate runs in `validate` mode: if an entity drifts from the schema, the application fails at startup instead of in production.

**Errors handled inside the JWT filter.** `@RestControllerAdvice` only sees exceptions thrown after the `DispatcherServlet`, and security filters run before it. Invalid tokens are therefore handled inside `SecurityFilter`, which writes the same `StandardError` body directly to the response.

**`401` and `403` are different things.** A request without credentials goes through unauthenticated and is rejected by the security rules with `403`. A request with an invalid or expired token is rejected with `401`. This keeps `/login` public while still telling clients when their credentials are broken.

**Method-level authorization.** Every endpoint declares its allowed roles with `@PreAuthorize`, so the access rules sit next to the code they protect.

**Enums stored as strings.** `@Enumerated(EnumType.STRING)` everywhere. Ordinal storage silently corrupts data when enum constants are reordered.

**DTOs at the boundary.** Entities are never serialized. Java records model request and response payloads, and Bean Validation rejects bad input before it reaches the service layer.

**Multi-stage Docker build.** The first stage compiles with the JDK and Maven; the final image contains only a JRE and the application JAR. Dependencies are resolved in their own cached layer, so rebuilding after a code change takes seconds.

**Configuration through the environment.** Following the Twelve-Factor approach, nothing environment-specific is hardcoded. Production swapped the database provider without a single code change.

---

## Roadmap

- **Integration tests against real PostgreSQL.** Tests currently use H2 with a Hibernate-generated schema, so the Flyway migrations are not exercised. The next step is Testcontainers, whose dependencies are already in the build.
- **Resource ownership.** Roles are enforced, but a `PATIENT` is not yet linked to a patient record, so ownership of appointments is not checked.
- **Appointment duration.** Conflicts are detected by exact start time. Adding a duration would enable real overlap detection.
- **Richer domain model.** The "only scheduled or confirmed" rule is repeated in two service methods and could move into the `Appointment` entity.
- **User management.** An admin endpoint to create users and one to reactivate doctors.

---

## Author

**Rafael Rocha** · [LinkedIn](https://www.linkedin.com/in/rafael-rocha-708393234/) · [GitHub](https://github.com/rafaelrch)

---

## License

This project is licensed under the [MIT License](LICENSE).
