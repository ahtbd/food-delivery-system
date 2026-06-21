# 🍕 Food Delivery Management System

## Cloud-Native Reactive Microservices Application

---

### 📌 Course Information
- **Course:** Software Service Engineering (YN3012180059)
- **Institution:** Yunnan University, School of Software and AI
- **Semester:** Spring 2026
- **Teacher:** Ahmed Zahir
- **Assignment:** Final Project
- **Topic:** Option B - Food Delivery Management System

---

### 👨‍💻 Student Information
- **Name:** Tuhin Md Abu Hamza
- **Student ID:** 20233120013
- **Major:** Software Engineering

---

## 🏗️ Architecture

![Architecture Diagram](docs/zahir.drawio.png)


---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 3.2.5 |
| Reactive | Spring WebFlux |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Database (SQL) | PostgreSQL 16 |
| Database (NoSQL) | MongoDB 7 |
| Reactive SQL | Spring Data R2DBC |
| Object Mapping | MapStruct 1.5.5 |
| Messaging | Apache Kafka, RabbitMQ |
| API Documentation | SpringDoc OpenAPI 2.5.0 |
| Security | JWT + Spring Security |
| Resilience | Resilience4j |
| Tracing | Zipkin + Micrometer |
| Build Tool | Gradle 8.5 |
| Containerization | Docker + Docker Compose |
| Java Version | 17 / 21 |


---

## 🔌 API Endpoints

### Order Service (`/api/orders`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create new order |
| GET | `/{id}` | Get order by ID |
| GET | `/user/{userId}` | Get orders by user (with pagination) |
| GET | `/restaurant/{restaurantId}` | Get orders by restaurant |
| PUT | `/{id}/status` | Update order status |
| DELETE | `/{id}` | Cancel order |
| GET | `/count/{status}` | Get order count by status |
