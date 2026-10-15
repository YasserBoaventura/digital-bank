# Digital Bank — Digital Banking Backend

**A modular digital banking backend built with Java and Spring Boot.**

> 🚧 **Project Status: In Development**
>
> Digital Bank is an ongoing backend development project. Core banking modules are being implemented incrementally, with additional features, testing, security improvements, and integrations planned. The long-term goal is to evolve the application into a microservices-based architecture.

## About the Project

Digital Bank is a backend application designed to simulate the core operations of a digital banking platform. It provides a foundation for RESTful APIs that manage customers, bank accounts, financial transactions, transfers, payments, cards, loans, and savings.

The project focuses on practical backend engineering, modular architecture, REST API design, database persistence, business logic, validation, exception handling, and financial operation consistency.

It is being developed as an educational and portfolio project to strengthen practical skills in Java backend development and prepare the application for future architectural evolution.

## Main Objectives

- Build a modular banking backend using Java and Spring Boot.
- Develop RESTful APIs for core banking operations.
- Implement database persistence using Spring Data JPA and Hibernate.
- Apply input validation and centralized exception handling.
- Maintain consistency across financial operations.
- Improve application security and access control.
- Test endpoints and business rules using Postman.
- Strengthen code quality, maintainability, and scalability.
- Prepare the application for a future transition to microservices.

## Technology Stack

The project uses or plans to use the following technologies, depending on the implementation stage.

### Backend Development

- **Java** — Backend programming language.
- **Spring Boot** — Framework for building the backend application.
- **Spring Web** — REST API development.
- **Spring Security** — Authentication, authorization, and application security.
- **Spring Data JPA** — Database access and persistence.
- **Hibernate** — Object-relational mapping.
- **Bean Validation** — Request and data validation.

### Database and Build Tools

- **SQL Database** — Relational data persistence.
- **Maven** — Dependency management and project builds.

### Development and Testing

- **Git** — Version control.
- **GitHub** — Source code hosting and project collaboration.
- **Postman** — API endpoint testing.

### Planned Technologies

- **Docker** — Containerization of the application and future services.
- **RabbitMQ** — Asynchronous messaging and communication between services.
- **API Gateway** — Centralized routing for microservices.
- **Service Discovery and Centralized Configuration** — To be evaluated as the distributed architecture evolves.
- **Automated Testing and CI/CD Tools** — To improve reliability and deployment workflows.

> **Note:** Planned technologies are part of the project's future roadmap and should not be interpreted as fully implemented features.

## Modules

The application is organized into domain-focused modules.

| Module | Description |
|---|---|
| Customer | Customer registration and management |
| Account | Bank account creation and management |
| Transaction | Deposits, withdrawals, and transaction history |
| Transfer | Transfers between bank accounts |
| Payment | Payment processing and management |
| Card | Bank card management |
| Loan | Loan applications, reviews, disbursement, and repayment |
| Savings | Savings account management and related operations |
| Reports | Reporting and analytics for banking operations, including transaction summaries, account activity, payment reports, and loan statistics. |
Modules are being developed incrementally. Their implementation, testing, and completeness may vary.
| Audit | Tracks and records important system activities, including account changes, financial operations, administrative actions, and security-related events for traceability and accountability. |
| Notifications | Manages banking notifications for transactions, transfers, payments, loan status updates, and other important account activities. |

## Current Features and Scope

The current development scope includes:

- Customer and account management.
- Deposits and withdrawals.
- Transfers between accounts.
- Payment operations.
- Card management.
- Loan application and review workflows.
- Loan disbursement and repayment.
- Savings management.
- Request validation and exception handling.
- Paginated queries for selected resources.
- REST API testing with Postman.

Additional functionality, integrations, and improvements will be introduced as development progresses.

## Architecture

The current application follows a **modular monolithic architecture** using Spring Boot. Modules are organized by business responsibility while running within the same application.

The backend follows a layered design:

- **Controller Layer:** Handles incoming HTTP requests and returns API responses.
- **Service Layer:** Implements business rules and application logic.
- **Repository Layer:** Provides database access through Spring Data JPA.
- **Entity Layer:** Represents persistent domain objects.
- **DTO Layer:** Defines request and response structures.
- **Exception Handling:** Provides consistent error handling and validation feedback.

This approach supports clear separation of responsibilities and provides a foundation for future architectural improvements.

## Future Architecture: Microservices

One of the long-term goals of Digital Bank is to evolve from a modular monolithic application into a **microservices architecture**.

The current approach makes it possible to develop and validate the core banking functionality before introducing the additional complexity of distributed systems.

The transition will be gradual, with services separated according to business responsibilities.

### Planned Improvements

- **Microservices Architecture:** Separate capabilities such as Customer, Account, Transaction, Transfer, Payment, Card, Loan, and Savings into independently deployable services.
- **API Gateway:** Introduce a centralized entry point for routing and managing client requests.
- **Inter-Service Communication:** Use REST APIs for synchronous communication and messaging for asynchronous workflows where appropriate.
- **Apache Kafka Integration:** Explore event-driven communication for operations that benefit from asynchronous processing.
- **Docker:** Containerize the application and future services for consistent development and deployment.
- **Database Strategy:** Define appropriate data ownership and database boundaries for each service.
- **Security:** Strengthen authentication and authorization with Spring Security and evaluate centralized identity management where appropriate.
- **Service Discovery and Configuration:** Introduce suitable solutions as the number of services grows.
- **Observability:** Add structured logging, monitoring, and health checks.
- **Automated Testing and CI/CD:** Improve test coverage, build automation, and deployment processes.

The migration will prioritize maintainability, security, reliable financial operations, and clear service boundaries.

**Long-term vision:** Build a modular, secure, containerized, and scalable banking backend using Java, Spring Boot, and microservices.

## Getting Started

### Prerequisites

Before running the application, ensure you have:

- Java JDK compatible with the project's configured version.
- Maven or the Maven Wrapper.
- A compatible relational database.
- Git.
- Postman or another HTTP client for API testing.

### Clone the Repository

```bash
git clone https://github.com/YasserBoaventura/digital-bank.git
cd digital-bank
```

### Database Configuration

Configure the database connection in `src/main/resources/application.properties` or `application.yml`, depending on your project setup.

Typical configuration values include:

- Database URL.
- Database username.
- Database password.
- JPA and Hibernate properties.

Keep credentials in environment variables or an untracked local configuration file. Never commit database passwords, API keys, or other secrets to the repository.

### Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

If the repository includes the Maven Wrapper, you can use the following commands.

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux or macOS:**

```bash
./mvnw spring-boot:run
```

By default, Spring Boot uses port `8080` unless another port is configured.

## API Testing

Use Postman to test the available REST API endpoints.

Examples of operations to test include:

- Creating and retrieving customers and accounts.
- Depositing and withdrawing funds.
- Transferring money between accounts.
- Creating and reviewing loan applications.
- Disbursing loans and processing repayments.
- Managing cards and savings.
- Testing validation rules and error responses.

The available endpoints, parameters, and response structures depend on the current implementation.

## Development Roadmap

The following checklist represents the project's development direction. Update it as tasks are verified and completed.

- [x] Develop initial customer and account management functionality.
- [x] Implement core transaction and transfer operations.
- [x] Develop the initial payment module.
- [x] Develop the initial card management module.
- [x] Implement the initial loan management workflow.
- [ ] Complete and refine the savings module.
- [ ] Expand unit and integration test coverage.
- [ ] Improve API documentation.
- [ ] Review authentication and authorization with Spring Security.
- [ ] Strengthen financial operation consistency and concurrency handling.
- [ ] Improve application configuration and deployment readiness.
- [ ] Introduce Docker-based development and deployment.
- [ ] Explore RabbitMQ integration.
- [ ] Design and implement the transition to microservices.
- [ ] Introduce monitoring, logging, and CI/CD improvements.

## Project Status

**Digital Bank is actively under development.**

The application is being built incrementally, module by module. Some functionality has been implemented, while other features require additional development, testing, refinement, and integration.

The current focus is on strengthening the core banking functionality and establishing a maintainable architecture before gradually introducing distributed services.

This project is intended for educational and portfolio purposes. It is not a production-ready banking platform and must not be used to process real customer funds or sensitive financial information.

## Contributing

This is currently a personal development project. Suggestions, feedback, and constructive discussions are welcome.

## Author

**Yasser Boaventura Mucavele**

GitHub: [@YasserBoaventura](https://github.com/YasserBoaventura)

Repository: [digital-bank](https://github.com/YasserBoaventura/digital-bank)

## Disclaimer

Digital Bank is an educational software project and is not affiliated with any licensed financial institution.

It is not intended for production banking operations. Real-world financial systems require rigorous security controls, regulatory compliance, auditing, risk management, operational monitoring, and extensive testing.

---

*Built with Java and Spring Boot as part of an ongoing backend development journey.*
