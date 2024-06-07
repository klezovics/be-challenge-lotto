# Description
This is a REST API which allows to store transaction data for customers in a multi-tenant environment

# Dependencies
- Postgres DB

# What is implemented and what is not
When doing the 1st interview I was told at the end to NOT implement at least one requirement.
This is done to leave something for a pair programming session. The part which was not implemented was auditing.
Conceptually, it should be done using Hibernate Envers library.

# How to use the project

## How to run the project locally

1. Run `docker compose up -d` to run dependencies of backend
2. Run command `./gradlew bootRun --args='--spring.profiles.active=local'` to run the backend itself
3. The API will run on `localhost:8080`

## How to use the project locally

1. Once the app starts it will load started data into postgres db from `data.sql` file
2. Every endpoint is protected with basic authentication
3. You can find the list of endpoints in the `TransactionController` class
4. You can use the following credentials for testing purposes

They are configured in memory in this class `CustomUserDetailsService`
The linkage which customer belongs to each tenant is done in `data.sql` and checked by the `SecurityManager` bean at runtime

User: customer1
Password: password-customer1
Description: Customer with customerNumber 1 and tenantId a3e1b4c0-37b2-4c6f-a232-3f0f8e637e39. Can create and rollback transactions

User: admin
Password: password-admin
Description: Admin user. Can get recent transactions for any user.

## How to run automated tests

1. Run command `./gradlew test` to run the tests

## How to package and run the app as a docker container
The app comes with a Dockerfile and a scripts to build and run the app as a docker container

### Package the app into Docker container
1. Run `./scripts/docker_build.sh` to build the docker image

### Run the docker image
1. Run `./scripts/docker_run.sh` to run the docker image
2. Run `docker ps` to check the running container

# Various notes about implementation

## General Description
- Language: `Kotlin`
- Framework: `Spring Boot 3.X`
- Build tool: `Gradle Kotlin`

## Security
- Spring Security is used with HTTP Basic Authentication
- `@PreAuthorize` annotation is used to check for the correct role
- `CustomUserDetailsService` is used to load in memory users for testing in the database
- In tests either mock users are used or security is mocked out. This is to separate business logic tests from security tests
- Permission are checked in the business logic. Suboptimal. Ideally a custom `PermissionEvaluator` should be implemented https://www.baeldung.com/spring-security-create-new-custom-security-expression

## Controller layer
- `SpringMVC` is used to create the REST API
- Controllers are slim. They simply receive the request and delegate the processing to the `service layer`.
- A request/response DTO pair is created for every endpoint. Keeps everything organized.
- `MapStruct` is used to convert between DTOs and entities.
- `@Valid` annotation is used to validate the request DTOs.
- Controller tests are written using `MockMvc`. All services are mocked out using `@MockBean`. This is done to test serialization/deserialization in isolation.
- All endpoints are covered by a test

## Service layer
- Constructor injection is used in `main` and field injection is used in `test`. As recommended by the Spring team.
- All methods that modify data run inside a `@Transactional` scope
- All custom exceptions extends the `ApiException` base class for consistency
- Service tests are done without any mocking to test interaction with the database. This is because interactions with the DB amount for a significant amount of bugs in the service layer
- All service methods are covered by a test

## Persistence layer
- The project uses `Postgres` as a database
- `Flyway` is used for database migration provider
- `Hibernate` is used a persistence provider
- `Spring Data JPA` is used to implement repositories
- All repositories extend `JpaRepository` since it has the richest feature set
- All entities extend the `BaseEntity` class. This is done for consistency.
- Pessimistic locking is used to update the balance of the customer. This is done with the `@Lock` annotation
- When creating a transaction `created_at` field is set by the database, unless it is provided by the user

## Testing
- `ObjectMother` pattern is used to create POJOs for test
- `Factory` pattern is used to create persisted entity graphs which are common in tests. Increases maintainability and easier to optimize CI speed if neccasary.
- `ZoinkyDB` is used to run a `dockerized Postgres`. Significantly more reliable than `H2`
- `ZoinkyDB` clear the database before every test to ensure data isolation and preventt tests which work locally but fail on the CI