### Animal Shelter

## Description

Animal Shelter App is a comprehensive solution for managing animals in a shelter. With this application, users can:

- **Add New Animals:** Easily register new animals with details such as name, species, color, breed, and more.
- **Manage Vaccinations:** Track and manage vaccinations for each animal, including vaccine details, batch numbers, and vaccination dates.
- **Update Animal Information:** Edit and update the information of existing animals, such as their primary color, breed, and other relevant details.
- **View and Organize Data:** Access a user-friendly interface to view and organize information about animals and their vaccinations.

The application is designed to streamline the management of shelter animals and ensure their health records are kept up-to-date.

## Installation

## Deployment Instructions

To deploy the application using the provided docker-compose.yml file, follow these steps:

Pre-requisites
 Ensure Docker and Docker Compose are installed on your system.
 To check if Docker is installed, run: docker --version and docker-compose --version.
 If Docker is not installed, download and install it from the official Docker website.

##    Clone the project repository

  ```
     git clone <repository-url>
     cd <repository-name>
   ```

##    Check the project structure

Ensure that the folders animals-shelter-service, animals-shelter-notification-service, and animals-shelter-react are in the root directory and contain a Dockerfile for each service.

##    Configure environment variables

Make sure there is an .env file in the animals-shelter-react folder for configuring the React app.
If the .env file is missing, create it and add the necessary configurations.

##     Start the application using Docker Compose

In the root directory of the project, run:

```
  docker-compose up --build
```

This command will build and start all the services defined in the docker-compose.yml.

Verify that the services are running

The application should be accessible at the following URLs:
- Animal Shelter Service: http://localhost:8080
- Notification Service: http://localhost:8081
- React App: http://localhost:3000

##   Manage the containers

To check the status of the running containers:

```
docker-compose ps
```

##  To stop all the services:

```
docker-compose down
```

##  To restart specific services:

```
docker-compose restart <service_name>
```

##  Important Notes:

Zookeeper and Kafka: Used for handling notifications via message queues.
PostgreSQL Databases: Two separate databases are set up for the main service and the notification service.
Email Configuration (Spring Mail): Make sure that the correct email server credentials are provided.

Following these steps will allow you to deploy and run the application successfully.
