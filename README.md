# 🔨 Real-Time Online Auction Platform (Microservices)

An enterprise-grade, event-driven auction system built with a **Java Spring Boot Microservices architecture**.  
This platform automates the entire auction lifecycle — from product listing and real-time bidding to automated winner determination and email notifications.

---

## 🚀 Key Features

- **Service Discovery & Gateway**
  - Centralized routing via Spring Cloud Gateway
  - Service registration using Netflix Eureka

- **Automated Auction Closing**
  - Custom Spring Scheduler runs every minute
  - Detects expired auctions and triggers closing logic

- **Inter-Service Communication**
  - OpenFeign for synchronous communication
  - RabbitMQ (CloudAMQP) for asynchronous messaging

- **Real-Time Bidding**
  - Supports high-concurrency bidding
  - Automatic status updates (Active → Sold / Unsold)

- **Email Notification Engine**
  - Sends:
    - "Congratulations" emails to winners
    - "Sales Reports" to sellers
  - Implemented using JavaMailSender

- **Security**
  - JWT (JSON Web Tokens) based stateless authentication

---

## 🏗️ System Architecture

The project is divided into **5 core microservices**:

- **Discovery Server (Eureka)**  
  → Acts as a service registry (the "Map")

- **API Gateway**  
  → Routes all incoming requests (Port 8080)

- **User Service**  
  → Handles user registration, login, and JWT generation

- **Product Service**  
  → Manages auction items and contains the Auction Scheduler

- **Bidding Service**  
  → Handles bidding logic and highest bidder tracking

- **Notification Service**  
  → Consumes RabbitMQ events and sends emails

---

## 🛠️ Tech Stack

| Category        | Technology |
|----------------|-----------|
| Backend        | Java 17, Spring Boot 3.x |
| Microservices  | Spring Cloud (Eureka, Gateway, OpenFeign, Config) |
| Messaging      | RabbitMQ (CloudAMQP) |
| Database       | MySQL |
| Security       | Spring Security, JWT |
| Build Tool     | Maven |

## 📂 Project Structure

- **discovery-server/** → Eureka Server  
- **api-gateway/** → Spring Cloud Gateway  
- **user-service/** → Authentication & User Management  
- **product-service/** → Inventory & Auction Scheduler  
- **bidding-service/** → Bid Management Logic  
- **notification-service/** → RabbitMQ Listener & Email Engine  


## 🔧 Installation & Setup

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL Server
- CloudAMQP Account (or local RabbitMQ)

---

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/online-auction-platform.git
2. **Configure Databases**
Create the following databases in MySQL:
auction_user_db
auction_product_db
bidding_db

3. **Update Properties**
Add MySQL credentials
Add CloudAMQP URL in each service's application.properties

4. **Build the Project**
   ```bash
   mvn clean install

5. **Run Services in Order**
Start Discovery Server
Start API Gateway
Start all other microservices

# 📧 Automated Flow Example
- **Auction Ends**
  → ProductService scheduler detects:
  ```bash
   auctionEndTime < currentTime
- **Winner Fetch**
  → ProductService calls BiddingService using Feign
- **Event Published**
 → JSON message sent to auction.exchange in RabbitMQ
- **Email Sent**
 → NotificationService consumes the message
 → Sends email to:
    Buyer (Winner)
    Seller

# 👩‍💻 Author
Prasanna Lakshmi Motati
Final Year B.Tech (AI) Student
Backend Developer
