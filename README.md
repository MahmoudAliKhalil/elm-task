# Elm Task

## About The Project

This project is an API service designed to efficiently manage users and products. It provides essential endpoints for
creating, retrieving, and updating user and product data, enabling seamless integration with various applications.

## Features

- User management
- Product management
- RESTful API architecture
- Docker containerization for easy deployment

## Getting Started

Follow these instructions to set up the project on your local machine.

### Prerequisites

Make sure you have the following software installed:

- Java 21 or Higher
- [Docker](https://www.docker.com/get-started)

### Installation

1. **Clone the repository:**

   ```sh
   git clone https://github.com/MahmoudAliKhalil/elm-task.git
   ```
2. **Navigate to the project directory:**
   ```sh
   cd /elm-task
   ```

3. **Build the Project:**
    - **For Linux and Unix-based systems:** Execute the following command:
       ```sh
       ./mvnw /elm-task
       ```
    - **For Windows:** Run the command below:
       ```sh
       ./mvnw.cmd /elm-task
       ```

4. **Run the application using Docker Compose:**
   ```sh
   docker-compose up
   ```
5. **Access the API:**  
   Send requests to the API at `http://localhost:8080/api`.

### API Documentation

For detailed information on the available endpoints and how to use them, refer to
the [API Documentation](src/main/resources/contract/openapi.yaml).
