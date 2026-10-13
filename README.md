# Merchant Commerce Platform (MCP) — Backend

A REST API built with Java and Spring Boot for managing merchants, product catalogs, customer orders, and M-Pesa payments.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA & Hibernate
- PostgreSQL
- Maven
- Safaricom M-Pesa Daraja API

## Features

- Merchant and category management
- Product creation, retrieval, updating, and deletion
- Customer order management
- M-Pesa STK Push integration
- Payment callback processing
- DTOs for API requests and responses

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL
- Git

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/Jurgy00/Merchant-Commerce-Platform.git
   cd Merchant-Commerce-Platform
   ```

2. Create a PostgreSQL database named `merchantplatform`.

3. Configure your database credentials and M-Pesa sandbox settings in `src/main/resources/application.yml`.

4. Run the application:
   ```

   use `mvn spring-boot:run` if Maven is installed.

The backend runs on port `8080` by default.

## Project Structure

- `controller` — handles HTTP requests
- `services` — contains business logic
- `repositories` — handles database operations
- `entities` — database entities
- `dto` — API request and response objects

## Author

Jurgens Kips
