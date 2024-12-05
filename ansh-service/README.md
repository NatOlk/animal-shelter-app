# **Animal Shelter Service**

## **Overview**
This application is designed for managing an animal shelter, allowing the shelter to manage animal records, track vaccinations, and send email notifications to subscribers. The system interacts with a notification service to send email alerts about various events, such as the addition of new animals or updates to existing animals.

## **Technologies Used**

- **Spring Boot**: A Java-based framework for building microservices.
- **Kafka**: A distributed message queue used for sending events (e.g., animal updates, user subscription changes) to the **Notification Service**.
- **PostgreSQL**: A relational database used to store animals, vaccinations and profiles data.
- **GraphQL**: A query language for APIs and a runtime for executing those queries by using a type system. It provides a more efficient, powerful, and flexible alternative to REST. In this application, GraphQL is used for managing data related to animals, vaccinations, and user subscriptions. 
  This allows for precise querying and mutation of data, reducing over-fetching and under-fetching, and providing more control to the clients.

## **Core Features**
- **Animal Management**:
   - Add, update, and remove animals in the shelter's database.
   - Manage animal details like name, species, breed, and vaccination records.

- **Vaccination Tracking**:
   - Track vaccinations for each animal, with detailed information on vaccine types, dates, and statuses.

- **Profile Management**:
  - Users can review their profile data.
  - Users can view their roles and permissions (Note: rights management is still under development).
  - Users can opt-in to receive notifications about animals via email.
  
  - **Subscription Management**:
     - Admin can approve or reject subscriber requests.
     - Admin can unsubscribe user from notifications.

   >  ***Data Exchange Between Animal Service and Notification Service:***
   > 
   >  **Subscription Approval Process:**
  >  The Notification Service sends an event to the Animal Service, and pending subscribers are stored in the internal database.
  >
  >  When an admin visits their profile page, they can see a list of subscribers awaiting their approval or rejection. Additionally, the admin can view subscribers who have requested a subscription from an external service without an assigned approver. These also require approval or rejection.
  >
  >  Subscribers without an assigned approver are displayed in a separate list. While the admin is not obligated to approve them as their own subscribers, it is recommended to process these requests for better user experience and system consistency.
  >
  >  **Approval/Reject Decision Communication:**
  >  Once the admin makes a decision (approve or reject), this decision is sent back to the Notification Service.
  >  Based on the admin's decision, the subscriber will either be approved or removed from the subscription list in the Notification Service.
  >
  >  **Admin Access to Subscriber List:**
  >  Admins will have access to a list of all subscribers for whom they are the approvers, allowing them to manage and monitor the subscription status.
- **Event Dispatch for Application Events**:

  - The application generates events related to various actions (e.g., animal arrivals, vaccinations) and sends them to a Kafka queue. Currently, there is a single topic for these events, but separate topics can be created for different types of notifications.
  - The notification service listens to this queue, processes the events, and sends the emails. The main application does not directly send emails but triggers the process by publishing events to the Kafka queue with the appropriate topic name.

## API Endpoints
  The application primarily uses GraphQL to handle operations related to animals and vaccinations. This allows for flexible and efficient querying and mutation of data.
   
   All endpoints are protected by Spring Security settings and are accessible only to logged-in users. Only the login and logout endpoints are publicly available.

   To access any of the protected endpoints, a user must have a valid JWT token, which is generated only after the user provides correct login credentials (username and password). This token must be included in the request headers for all protected endpoints.
## Security
  - **Endpoint Protection**:
    
   Almost all endpoints in the Animal Service application are secured using Spring Security configurations. Only the login and logout endpoints are publicly accessible. To access secured endpoints, users must authenticate themselves with valid credentials to obtain a JWT token, which is required for subsequent requests.

  - **Inter-Service Communication**:
    
    Communication between the Animal Service and the Notification Service is secured using custom HTTP headers. These headers include an API key that verifies the authenticity of requests exchanged between the services. This mechanism ensures that only authorized services can interact with each other, protecting against unauthorized access or tampering.


## **Future Plans**

- **Template-based Notifications**: Integrate a more flexible template system for email content customization.
- **Advanced Filtering**: Allow users to subscribe to specific types of notifications, such as only new animal arrivals or animal adoption status updates.
