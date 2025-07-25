# **Animal Shelter Management System**

## **About the Project**
**_The project is currently on hold for approximately 3–4 months due to the development of a private Android application. I will still be committing occasionally during this period._**

This project is part of my continuous learning and professional growth as a software developer. While I have solid experience in software development, I always strive to explore new technologies and refine my expertise. Working on this application allows me to stay up to date with modern development approaches, including Spring Boot, React, Docker, GraphQL, and PostgreSQL.

The entire architecture and requirements were designed and are being implemented by me, following best practices for building scalable web applications. The project demonstrates my ability to work across the full development lifecycle — from database design and backend logic to frontend development and deployment.
## **Project Description**

The **Animal Shelter Management System** is a comprehensive application designed for managing animal
shelters. It consists of a **frontend** and **two microservices** that handle animals, vaccinations,
users, and newsletter subscribers.

Project's Kanban board with actual tasks: https://github.com/users/NatOlk/projects/1/views/1

---

## **Core Features**

### **1. Frontend (React + TypeScript + Vite)**

- **Animal and Vaccination Management**:
    - CRUD operations.
- **User Management**:
    - User registration (will be implemented soon);
    - View user profiles.
- **Newsletter Subscriptions**:
    - Approve or reject subscription requests, including those
      from external applications where the subscriber does not have an explicit approver.
    - Unsubscribe a subscriber

### **2. Animal Shelter Service (Spring Boot + Kafka + GraphQL)**

- **Entity Management**:
    - CRUD operations for animals and vaccinations.
    - Data storage in **PostgreSQL**.
- **Integration with Kafka**:
    - Produces messages events (e.g., animal additions, deletions).
- **Sending Notifications**:
    - Generates and sends messages to the **Notification Service**.

### **3. Notification Service (Spring Boot + Kafka + Redis)**

- **Subscriber Management**:
    - Stores subscriber lists in **Redis**.
    - Handles subscription requests.
- **Notification Delivery**:
    - Processes messages from **Kafka**.
    - Sends emails to subscribers.

### **4. Statistics Service (Kotlin + Spring Boot + MongoDB + Kafka)**

- **Event Ingestion:**

    Written in Kotlin using Spring Boot.

    Listens to Kafka topics for animal and subscription events.

    Converts raw messages into structured domain events and stores them in MongoDB.

- **Statistical Aggregation:**

    Uses MongoDB Aggregation Pipelines for efficient querying.

    Calculates:

     - Animals added per date

     - Vaccinations added per date

     - Subscription stats grouped by topic or approver

     - Animal lifespan (days between add/remove events)

- **REST API Endpoints:**

    Exposes clean, typed JSON APIs for frontend data visualization.

    Optimized for use with charts and dashboards.
---
Here you can see a diagram showing the subscription approval and acceptance process.

>Anyone can request a subscription to their email. There will be several subscription options, 
starting with a widget on an external website that shows a small image and allows users to enter
their email—like on a news site or a city website. The subscription request may also include a manager 
for approval, or it could be submitted without one. 
Additionally, requests can come through a shelter's website via a phone call or letter.
> 
![](diagrams/subscription_components.jpg)

Here you can see a diagram showing the notification process.

![](diagrams/notification_components.jpg)
## **Technologies**

- **Frontend**: React, Vite, Apollo Client (for GraphQL operations), NextUI (for styling) Renamed to HeroUI
- **Backend**: Spring Boot, PostgreSQL (database), Redis (subscriber cache), Apache Kafka (message queue)
- **Containerization**: Docker & Docker Compose

---

## **Installation and Running**

### **Prerequisites**

- Docker and Docker Compose installed on your machine.

### **Steps to Run**

1. **Clone the repository**:
   ```bash
   git clone https://github.com/NatOlk/animal-shelter-app.git
   cd animal-shelter-app
   ```

2.**Configure the environment variables file:**
   
   An .env file will be created in the appropriate directory, where you need to update the following
   details:

   ```bash
     POSTGRES_PASSWORD=your_postgres_password
     POSTGRES_PASSWORD_NOTIFICATION=your_postgres_password_notification
     GMAIL_USERNAME=your_gmail_username  
     GMAIL_PASSWORD=your_gmail_password  
     REDIS_PASSWORD=your_redis_password  
     NOTIFICATION_API_KEY=your_api_key  
     JWT_SECRET_KEY=your_jwt_key
     SSL_KEY_STORE_PASSWORD=your_ssl_key_store_password   
   ```

#### *POSTGRES_PASSWORD and POSTGRES_PASSWORD_NOTIFICATION*

>These are the passwords for your PostgreSQL databases. You need to set these to securely connect
your application to the databases.
How to set up PostgreSQL:
Install PostgreSQL on your machine or use a Docker container to run it.
Create the new databases using the following commands:
>```bash
>      CREATE DATABASE Animal_Shelter;
>      CREATE DATABASE animal_shelter_notification;
>  ```
>Set the POSTGRES_PASSWORD for the main application database (Animal_Shelter) and the
POSTGRES_PASSWORD_NOTIFICATION for the notification
application database (animal_shelter_notification) in your .env file to the passwords you wish to
use for each database.

#### *GMAIL_USERNAME and GMAIL_PASSWORD*

>These are your Gmail credentials used for sending email notifications. In this example, these
credentials are used to send emails related to animal shelter updates.
>
>Note: This is a temporary
solution for development purposes. For production environments, it is recommended to use a more
secure and reliable email service provider instead of a personal Gmail account, such as a dedicated
email server or a third-party email service like SendGrid, Amazon SES, or Mailgun.
>
>**Important!** Additionally, this email will be used for the test admin account and will appear on the website as the employee's contact email.
> 
>How to configure:
> 
>Use your Gmail email address and password to configure these variables.
If you have 2-step verification enabled on your Gmail account, generate an App Password and use that
instead of your regular Gmail password.
> 
>For more detailed instructions on how to set up your Gmail account for sending emails and generating
an App Password, you can refer to the official Google documentation here:
[Google Account Help - App Passwords](https://support.google.com/a/answer/176600?hl=en)

#### *REDIS_PASSWORD* 

>This password is used to secure the Redis instance that your application uses for caching
subscribers and other data.
> You can provide any password of your choice, but ensure it is strong and secure to protect your data.

#### *NOTIFICATION_API_KEY*

>This is an API key used for secure communication between services for sending and receiving
messages. 
> You can generate any key for this purpose, but it should be kept confidential to maintain the security of your system.

#### *JWT_SECRET_KEY*

>This secret key is used for generating and verifying JWT (JSON Web Tokens) in your application. JWT
is typically used for user authentication. 
> You can choose any secret key, but ensure it is complex and stored securely to prevent unauthorized access.

By setting these environment variables correctly, your application will be properly configured for
database access, email notifications, Redis caching,
inter-service communication, and secure authentication.

#### *SSL_KEY_STORE_PASSWORD*

> This password protects access to the keystore and truststore from unauthorized access, which contains your private keys and certificates.
> Certificates and keystores ensure secure, encrypted communication.
> You can generate any key for this purpose, but it should be kept confidential to maintain the security of your system.

Follow the steps in the instruction to create and manage SSL certificates for your server.
[What Are Certificates and Why Do You Need Them](#what-are-certificates-and-why-do-you-need-them)


3. **Launch the application using Docker Compose:**

   Build and start the services using the following command:

 ```bash
    docker-compose up --build
 ```

4. **Open the application in your browser**

   Navigate to https://localhost
   Now you will be able to log in with the user admin.

## **Future Plans**

- Add different roles for customers

  >The system will be extended to include multiple customer roles, providing different access levels and functionalities based on the user's role.

- Add a personal animal page
  >A personal page will be added for each animal, allowing users to view detailed information about the animal, including adoption status, vaccination records, and other relevant details.

- Enable management of animal adaptation
  >The system will be enhanced to allow managing the adaptation process of animals, including tracking their progress and providing necessary information and updates to potential adopters.
  
## **What Are Certificates and Why Do You Need Them?**
SSL certificates ensure encrypted communication between clients and servers, protecting data from being intercepted by unauthorized parties.

Key Terms:
- Private Key (cert.key): A secret key used to decrypt data on the server.
- Certificate Signing Request (CSR, cert.csr): A request file containing information about the entity requesting the certificate.
- Certificate (cert.crt): A file that proves the server's authenticity.
- Keystore (keystore.p12): A storage file containing the server's private key and certificate.
- Truststore (truststore.p12): A storage file containing certificates that the server or client trusts.
### 🛠 Steps to Create SSL Certificates
To organize your project securely and efficiently, SSL certificates should be generated and stored in a dedicated folder.

Follow these steps:

### 🔧 Step 1: Create a Folder for Certificates
In the root of your project, create a folder named **config**. All commands below must be executed in this folder.

### 🔑 Step 2. Creates the private key (cert.key) and a CSR (cert.csr) with entity details such as domain, organization, and country.

```bash
openssl req -newkey rsa:2048 -nodes -keyout cert.key -out cert.csr -subj "/C=DE/ST=Berlin/L=Berlin/O=PetProject/OU=IT/CN=nginx-proxy-ansh" -addext "subjectAltName=DNS:nginx-proxy-ansh,DNS:localhost"
```
- 🔑 cert.key: The private key used for encryption.
- 📜 cert.csr: The CSR contains details such as domain, organization, and country.

### 🛡️ Step 3. Create a Self-Signed Certificate

```bash
  openssl x509 -req -days 365 -in cert.csr -signkey cert.key -out cert.crt
```

###  🔒 Step 4. Generates a PKCS12 keystore (keystore.p12) containing the private key and self-signed certificate.

```bash
keytool -genkeypair -alias ansh-cert -keyalg RSA -keysize 2048 -validity 365 -dname "CN=nginx-proxy-ansh, OU=IT, O=PetProject, L=Berlin, ST=Berlin, C=DE" -ext "SAN=dns:nginx-proxy-ansh" -keystore keystore.p12 -storetype PKCS12 -storepass your_ssl_key_store_password
```
- 📂 keystore.p12: Stores the private key and self-signed certificate.
- 🏷️ Alias (ansh-cert): A unique name for the certificate.

###  🛡 Step 5. Creates a self-signed certificate (cert.crt) valid for 365 days.

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout cert.key -out cert.crt -subj "/C=DE/ST=Berlin/L=Berlin/O=PetProject/OU=IT/CN=nginx-proxy-ansh"
```
- 🔑 cert.key: The private key.
- 📜 cert.crt: A self-signed certificate valid for 365 days.

###  📜 Step 6. Imports the self-signed certificate into a truststore (truststore.p12), making it trusted.

```bash
keytool -import -trustcacerts -file cert.crt -alias ansh-cert -keystore truststore.p12 -storepass your_ssl_key_store_password
```

- 🛡 truststore.p12: Stores trusted certificates.
- 📜 cert.crt: The certificate being trusted.
- 🏷️ Alias (ansh-cert): A unique name for the certificate in the truststore.
###  📥 Step 6. Import an External Certificate
To trust an external certificate, such as Google's SMTP certificate, follow these steps:

In Mozilla Firefox:

- Visit https://www.google.com.
- Click the 🔒 icon in the address bar.
- Select More Information.
- Click View Certificate.
- Go to the Details tab.
- Click Export....
- Save the file as mail-google-com.pem in PEM format.
- Put this file to the config folder 
- Run below command
```bash
keytool -import -trustcacerts -file mail-google-com.pem -alias smtp-gmail -keystore truststore.p12 -storepass your_ssl_key_store_password
```
- 📜 mail-google-com.pem: The external certificate.
- 🛡 truststore.p12: Adds the external certificate to the truststore.
- 🏷️ Alias (smtp-gmail): Identifies this certificate in the truststore.
