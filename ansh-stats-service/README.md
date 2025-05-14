# **Statistics Service**

## **Overview**

The **Statistics Service** is a supporting component of the Animal Shelter Management System. Its primary role is to collect and expose statistics about the shelter’s operations — including animals, vaccinations, and user subscriptions.

It aggregates data from events coming through Kafka, processes them, and stores them in MongoDB for retrieval via REST endpoints, which are consumed by the frontend dashboard.

## **Technologies Used**
- **Kotlin + Spring Boot:** Microservice framework used for API and event handling

- **MongoDB:** Stores incoming event documents (animal and subscription events)

- **Kafka:** Processes real-time events related to animals and subscriptions

- **REST API:** Serves statistical data to the frontend

- **Recharts + React:** Used in the UI to visualize statistics (bar charts)

## **📦 Features**
- 📊 Total count of added animals and vaccinations

- 📆 Animals/vaccinations grouped by date

- ⏳ Animal lifespan (from add to removal)

- 📬 Subscription decisions (approved/rejected) per topic

- 📩 Subscription request statistics (by topic)


## **📁 Endpoints**

- /stats/animals/count	Total number of animals added
- /stats/vaccinations/count	Total number of vaccinations
- /stats/animals/added-by-date	Animals added grouped by date
- /stats/vaccinations/added-by-date	Vaccinations added grouped by date
- /stats/animals/lifespans	Lifespan of each animal (in days)
- /stats/subscription/decision/count	Total number of subscription decisions
- /stats/subscription/decision/by-topic	Approved / rejected subscriptions per topic
- /stats/subscription/request/count	Total number of subscription requests
- /stats/subscription/request/by-topic	Requests per topic

More statistical data will be added over time.
The service will be extended with new endpoints and metrics.
