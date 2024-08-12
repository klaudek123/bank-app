# Bank Application

This repository contains the backend code for a simple bank application. The application provides basic functionalities for managing users, accounts, transfers, loans, and investments. Additionally, there is a frontend developed using Angular for seamless user interaction.

## Features

- **User Management**: Allows users to register and view their details.
- **Account Management**: Enables users to create accounts and perform various banking operations.
- **Transfer Handling**: Facilitates the transfer of funds between accounts.
- **Loan Management**: Provides the ability to create and manage loans.
- **Investment Handling**: Supports the creation and management of investments.

## Technologies Used

- **Java**: The primary programming language used for backend development.
- **Spring Boot**: Provides a framework for building robust and scalable applications.
- **Spring Data JPA**: Simplifies data access using the Java Persistence API (JPA).
- **Spring Web**: Facilitates the development of web applications using Spring MVC.
- **MySQL**: Used as the relational database management system for storing application data.
- **Angular**: A frontend framework used for developing the user interface.

## Setup

1. **Clone the Repository**: 
   ```
   git clone https://github.com/klaudek123/bank-app.git
   ```

2. **Database Setup**:
   - Ensure you have Docker installed.
   - Pull the MySQL Docker image:
     ```
     docker pull mysql:latest
     ```
   - Run the MySQL container:
     ```
     docker run --name bankapp-mysql -e MYSQL_ROOT_PASSWORD=your_password -d mysql:latest
     ```
   - Log in to the MySQL container:
     ```
     docker exec -it bankapp-mysql mysql -u root -p
     ```
     - Copy and paste the following SQL script to create the database structure:
       ```sql
       use bankapp;
   
       CREATE TABLE user (
           personal_id      BIGINT NOT NULL,
           firstname        VARCHAR(30) NOT NULL,
           lastname         VARCHAR(30) NOT NULL,
           date_of_birth    DATE NOT NULL,
           email            VARCHAR(30) NOT NULL,
           address          VARCHAR(40),
           PRIMARY KEY (personal_id)
       );
   
       CREATE TABLE account (
           id_account         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
           number             BIGINT NOT NULL,
           password           VARCHAR(50) NOT NULL,
           balance            DECIMAL(10, 2) NOT NULL,
           date_of_creation   DATE NOT NULL,
           type               CHAR(15) NOT NULL,
           status             ENUM('ACTIVE', 'INACTIVE') NOT NULL,
           id_user            BIGINT NOT NULL,
           FOREIGN KEY (id_user) REFERENCES user(personal_id)
       );
     
       ALTER TABLE account AUTO_INCREMENT = 1000001;
   
       CREATE TABLE transfer (
            id_transfer  BIGINT AUTO_INCREMENT PRIMARY KEY,
            sender       BIGINT NOT NULL,
            recipient    BIGINT NOT NULL,
            title        VARCHAR(255) NOT NULL,
            date         TIMESTAMP NOT NULL,
            amount       DECIMAL(10, 2) NOT NULL,
            id_account   BIGINT NOT NULL,
            FOREIGN KEY (id_account) REFERENCES account(id_account)
       );

       CREATE TABLE loan (
            id_loan        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            amount         DECIMAL(10, 2) NOT NULL,
            interest_rate  DECIMAL(5, 2) NOT NULL,
            start_date     TIMESTAMP NOT NULL,
            end_date       TIMESTAMP NOT NULL,
            status         ENUM('ACTIVE', 'INACTIVE') NOT NULL,
            id_account     BIGINT NOT NULL,
            FOREIGN KEY (id_account) REFERENCES account(id_account)
        );

       CREATE TABLE investment (
            id_investment   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            name            VARCHAR(40) NOT NULL,
            type            ENUM('FUND', 'GOLD', 'SILVER') NOT NULL,
            amount          DECIMAL(10, 2) NOT NULL,
            interest_rate   DECIMAL(5, 2) NOT NULL,
            start_date      TIMESTAMP NOT NULL,
            end_date        TIMESTAMP NOT NULL,
            status          ENUM('ACTIVE', 'INACTIVE') NOT NULL,
            id_account      BIGINT NOT NULL,
            FOREIGN KEY (id_account) REFERENCES account(id_account)
        );
     
       ```

3. **Backend Setup**:
   - Ensure you have MySQL installed and running.
   - Update the database configuration in `application.properties` with your database credentials.
   - Build and run the backend using Maven:
     ```
     ./mvnw spring-boot:run
     ```

4. **Frontend Setup**:
   - Navigate to the frontend directory:
     ```
     cd frontend
     ```
   - Install dependencies:
     ```
     npm install
     ```
   - Run the frontend server:
     ```
     ng serve
     ```

5. **Access the Application**:
   - Once both the backend and frontend servers are running, access the application through a web browser.

## API Endpoints

- **Users**:
  - `GET /users`: Retrieve all users.
  - `POST /users`: Register a new user.
  - `GET /users/{idAccount}`: Get user details by account ID.
  

- **Accounts**:
  - `POST /accounts`: Create a new account.
  - `GET /accounts/{idAccount}`: Get account details by account ID.
  - `POST /accounts/login`: Account authentication to log into the system.

- **Transfers**:
  - `GET  /accounts/{idAccount}/transfers?type={type}`: Retrieve all transfers with optional parameter - type (sent, received) for resource filtering.
  - `POST /accounts/{idAccount}/transfers`: Create a new transfer.
  - `GET /accounts/{idAccount}/transfers/{idTransfer}`: Get transfer details by transfer ID.

- **Loans**:
  - `GET /accounts/{idAccount}/loans`: Get loan details by account ID.
  - `POST /accounts/{idAccount}/loans`: Create a new loan.
  
- **Investments**:
  - `GET /accounts/{idAccount}/investments`: Get investment details by account ID.
  - `POST /accounts/{idAccount}/investments`: Create a new investment.
  

 ## Author
 - Klaudiusz Szklarkowski (@klaudek123)
