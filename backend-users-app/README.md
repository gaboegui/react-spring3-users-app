# Users App - Backend

This project is the backend for a user management application, built with Spring Boot 3. It provides a robust and secure API for managing user data, including authentication, CRUD operations for users, and role-based access control.

## Features

*   **User Authentication & Authorization:** Secure JWT-based authentication and role-based access control.
*   **User Management API:**
    *   Create, retrieve, update, and delete user accounts.
    *   Assign and manage user roles.
*   **Data Validation:** Robust input validation to ensure data integrity.
*   **Error Handling:** Centralized and consistent error handling.
*   **Database Integration:** Persistence layer using Spring Data JPA.

## Technologies Used

*   **Spring Boot 3:** Framework for building stand-alone, production-grade Spring applications.
*   **Spring Security:** Powerful and customizable authentication and access-control framework.
*   **Spring Data JPA:** Simplifies data access with JPA repositories.
*   **JWT (JSON Web Tokens):** For secure authentication and authorization.
*   **MySql:** Relational database for storing user data.
*   **Maven:** Dependency management and build automation tool.
*   **Lombok:** Reduces boilerplate code for Java classes.
*   **Docker:** For containerization and easy deployment.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   Java Development Kit (JDK) 17 or higher.
*   Maven 3.6 or higher.
*   Docker (optional, for containerized deployment).
*   MySql database instance.

### Installation

1.  Clone the repo
    ```sh
    git clone https://your-repository-link.com
    ```
2.  Navigate to the `backend-usersapp` directory
    ```sh
    cd backend-usersapp
    ```
3.  Configure your PostgreSQL database connection in `src/main/resources/application.properties` (or `application.yml`).

    