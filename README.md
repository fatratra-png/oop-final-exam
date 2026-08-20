# oop-final-exam

A small Spring-based REST API for a simple bank account system. It exposes endpoints to list transactions, create transactions, and fetch account balances.

## Quick overview
- GET /ping → {"ping":"pong"} (basic health check)
- GET /accounts/{id}/transactions → list of Transaction objects
- GET /account/{id}/balance → returns a BalanceDTO { accountId, balance }
- POST /transaction → create a Transaction (returns 201)

## Stack and dependencies
- Java 26
- Spring Boot 4.1.0 (parent)
- spring-boot-starter-webmvc (HTTP REST controllers)
- postgresql (Postgres JDBC driver, runtime scope)
- lombok (compile-time code generation for getters/constructors)

These are declared in pom.xml. No other external API clients are used.

## How to run locally (developer-friendly)
1. Clone the repository:
   git clone https://github.com/fatratra-png/oop-final-exam.git
   cd oop-final-exam

2. Configure the database connection. The app reads the following properties (set in src/main/resources/application.properties or via environment variables):
   - db.url (e.g. jdbc:postgresql://localhost:5432/bankdb)
   - db.username
   - db.password

   Example application.properties snippet:
   db.url=jdbc:postgresql://localhost:5432/bankdb
   db.username=bank_user
   db.password=change_me

   NOTE: Do NOT commit real credentials. Use local secrets or env vars for collaborators.

3. Prepare the PostgreSQL database (schema files described below).
   - Create the database and user (example):
     createdb bankdb
     psql -c "CREATE USER bank_user WITH PASSWORD 'change_me';"
     psql -d bankdb -c "GRANT ALL PRIVILEGES ON DATABASE bankdb TO bank_user;"

4. Build and run:
   - Using Maven (recommended for development):
     mvn clean package
     mvn spring-boot:run

   - Or run packaged JAR:
     java -jar target/oop-final-exam-0.0.1-SNAPSHOT.jar

The server will start on the default port (8080) unless overridden via properties.

## Database schema and SQL queries
- At the moment the repository does not contain migration files. Place schema and seed SQL files under:
  `src/main/resources/sql/` (recommended filenames: `schema.sql`, `data.sql`).

- To apply locally:
  psql -U <username> -d <dbname> -f src/main/resources/sql/schema.sql
  psql -U <username> -d <dbname> -f src/main/resources/sql/data.sql

- The repository's DatabaseConnection reads `db.url`, `db.username`, `db.password`. The TransactionRepository expects a `transactions` table with columns: id (uuid/text), account_id (uuid/text), created_at (timestamp), transaction_type (text: IN/OUT), amount (numeric), reason (text).

## Notes, edge cases & safety
- The API currently relies on JDBC and manual SQL mapping. Null/invalid DB values can cause runtime exceptions (UUID parsing, Timestamp nulls). Validate inputs and ensure schema enforces NOT NULL where appropriate.
- No secrets are present in the repository. Do not store credentials in source control.
- Lombok requires annotation processing enabled in IDE/build.

## Contribution
- Create feature branches from main, follow conventional commit messages (feat/, fix/, docs/...).
- Run tests (if added) and ensure no credentials are committed.

If you want, a migration tool (Flyway/Liquibase) and Spring Actuator can be added for production readiness.
