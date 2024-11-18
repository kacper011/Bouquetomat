# Bouquetomat Application

## Overview
Bouquetomat is a simple web application that allows users to view and purchase bouquets. The application supports up to 6 bouquets at a time, and when a user purchases a bouquet, a notification is sent via email. Additionally, the slot is automatically freed so a new bouquet can be added. 

The application runs inside a Docker container and uses Java Spring Boot.

## Technologies
- Java 21
- Spring Boot
- Docker
- MySQL
- Postman (for API testing)
- Hibernate ORM

## Features
- Display up to 6 bouquets with the possibility of purchasing them.
- Sends email notifications when a bouquet is bought.
- Automatically frees the slot for a new bouquet after a purchase.

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
docker run --env-file .env -p 8080:8080 bouquetomat
```

### 3. Using Postman

You can interact with the API through Postman. Here are some of the main endpoints:
<ul>
<li>GET localhost:8080/api/bouquets - List all available bouquets.</li>
<li>POST localhost:8080/api/bouquets/buy/{id} - Place an order for a bouquet. (Sends an email notification to the admin).</li>
<li>POST localhost:8080/api/bouquets -  Addition of a bouquet.</li>
</ul>
Example JSON to add bouquet {<br>
    "name": "Bukiet lato",<br>
    "slotNumber": 4,<br>
    "price": 79.99<br>
}

