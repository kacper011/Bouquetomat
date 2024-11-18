# Bouquetomat Application

## Overview
Bouquetomat is a simple web application that allows users to view and purchase up to 6 bouquets. When a bouquet is purchased, you receive an email notification, and the slot becomes available for a new bouquet.

The application runs inside a Docker container and uses Java Spring Boot.

## Technologies
- Java 21
- Spring Boot
- Docker
- MySQL
- Postman (for API testing)

## Setup

### 1. Set Environment Variables

Create a `.env` file in the root of the project with the following content:

```bash
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
EMAIL_USERNAME=your_email@example.com
EMAIL_PASSWORD=your_email_password
```

### 2. Build the Application

```bash
./mvnw clean install
```

```bash
docker build -t bouquetomat .
```

```bash
docker run -p 8080:8080 bouquetomat
```

### 3. Using Postman




