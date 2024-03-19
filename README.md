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

2. **Backend Setup**:
   - Ensure you have MySQL installed and running.
   - Update the database configuration in `application.properties` with your database credentials.
   - Build and run the backend using Maven:
     ```
     ./mvnw spring-boot:run
     ```

3. **Frontend Setup**:
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

4. **Access the Application**:
   - Once both the backend and frontend servers are running, access the application through a web browser.

## API Endpoints

- **Users**:
  - `GET /users`: Retrieve all users.
  - `POST /users`: Register a new user.
  - `GET /users/{idAccount}`: Get user details by account ID.

- **Accounts**:
  - `POST /accounts`: Create a new account.

- **Transfers**:
  - `GET /transfers`: Retrieve all transfers.
  - `POST /transfers`: Initiate a transfer.
  - `GET /transfers/{idAccount}`: Get all transfers by account ID.
  - `GET /transfers/sent`: Get all transfers sent by an account.
  - `GET /transfers/received`: Get all transfers received by an account.

- **Loans**:
  - `POST /loans`: Create a new loan.
  - `GET /loans/{idAccount}`: Get loan details by account ID.

- **Investments**:
  - `POST /investments`: Create a new investment.
  - `GET /investments/{idAccount}`: Get investment details by account ID.

 ## Author
 - Klaudiusz Szklarkowski (@klaudek123)
