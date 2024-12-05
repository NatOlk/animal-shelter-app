# **Notification Service**

## **Overview**

The **Notification Service** is a crucial component of the Animal Shelter Management System. Its main purpose is to handle and process notifications related to events in the system, such as animal additions, updates, and deletions. 
This service is responsible for managing subscriber lists, sending notifications.

## **Technologies Used**

- **Spring Boot**: A Java-based framework for building microservices.
- **Kafka**: A distributed message queue used for receiving events (e.g., animal updates, user subscription changes) from the **Animal Shelter Service**.
- **Redis**: A high-performance key-value store used for caching subscribers data.
- **PostgreSQL**: A relational database used to store subscribers data.
- **SMTP (Gmail)**: Email service used for sending notifications to users.

## **Core Features**

### 1. **Subscriber Management**
- **Store subscriber information** in Redis for quick access and efficient delivery of notifications.
- **Add new subscribers** through a REST API endpoint. Subscription can be done via the frontend by users who wish to receive updates about animal shelter events.
- **Approve/Reject subscriptions**: Admins can approve or reject subscription requests, ensuring that only legitimate users receive notifications.
- **Unsubscribe users**: Users can unsubscribe from receiving notifications at any time.

### 2. **Notification Processing**
- **Listen to events from Kafka**: The Notification Service subscribes to the Kafka topic for incoming events (e.g., animal added, animal removed).
- **Send email notifications**: Based on events received, the Notification Service generates and sends email notifications to subscribers via SMTP (using Gmail, in this case).
- **Template management**: The Notification Service can integrate templates to customize the content of the notifications (e.g., animal adoption, animal vaccination reminders).

### 3. **Kafka Integration**
- The service listens for events on the Kafka message queue, which is produced by the **Animal Shelter Service**. This includes updates on animal data, such as additions, removals, and vaccination updates.
- Kafka ensures that notification delivery is decoupled from the main application logic, allowing for more scalable and resilient systems.

### 4. **Database and Caching**
- **PostgreSQL** stores subscribers information.
- **Redis** caches data for approved and accepted subscribers who are eligible to receive emails, helping to reduce database load and improve response times. 
   This cache does not store all subscribers, but only those who have been explicitly approved for email notifications.
## **Open endpoints**

### 1. **Subscribe request**
   Endpoint for users to subscribe for notifications via email.
  
   This is not the subscription itself but rather a subscription request. Once it is approved by an admin in the main Animal Shelter application, 
    the user will receive a subscription invitation with a link to confirm the subscription. The link will include a token generated for their email.

   Any user can make a subscription request without needing an approver initially. This request can be made from any widget where this endpoint is integrated.

   Additionally, this request can also be made from the main application through the React frontend. In this case, the approver will be the logged-in user who was asked to initiate the subscription request.
  
   This endpoint is publicly accessible and is not protected by any security settings.
- **URL**: `POST /external/animal-notify-subscribe`
- **Request Body**:
  ```json
     { 
      "email":"subscriber@mail.ru",
      "approver":"" 
      }
  ```
- **Response**: `200 OK`

### 2. **Approve subscription**
   This endpoint is designed to handle approval requests generated via email. 
   
   The link containing this endpoint is included in the subscription approval email sent to the user. It includes a unique token associated with the user's email address.

   The token ensures secure validation and prevents unauthorized approval of subscription requests. Only users with access to the generated email link can approve the request.

   This endpoint is publicly accessible and is not protected by any security settings.
- **URL**: `GET /external/animal-notify-subscribe-check/{token}`
- **Response**: `Subscription with token *token* is valid`

### 3. **Unsubscribe** 

  This endpoint handles requests for unsubscribing users from notifications. 
  
  The unsubscription process is based on a unique token generated for the specific email address.

  Typically, the link to this endpoint is included in notification emails sent to subscribers. By clicking on the link, the subscriber can easily opt out of the mailing list at any time, ensuring they maintain control over their subscription preferences.
  
  This endpoint is publicly accessible and is not protected by any security settings.
- **URL**: `GET /external/animal-notify-unsubscribe/{token}`
- **Response**: `Subscription with token *token* removed`


## **Architecture**

- **Kafka**: Used as the communication medium between the **Animal Shelter Service** and **Notification Service**. The Notification Service subscribes to Kafka topics for real-time event streaming.
- **PostgreSQL**: Stores subscribers data.
- **Redis**: Caches subscriber data for faster access and notification delivery.
- **Email Service**: Gmail SMTP (or other services) is used to send the actual emails to subscribers. For production, you may want to replace this with a more robust service like SendGrid, Amazon SES, or Mailgun.

## **Security**

- **Encryption**: Sensitive data (e.g., email addresses) should be encrypted or hashed before storage, especially if any personal data is involved.

## **Future Enhancements**

- **Template-based Notifications**: Integrate a more flexible template system for email content customization.
- **Advanced Filtering**: Allow users to subscribe to specific types of notifications, such as only new animal arrivals or animal adoption status updates.
