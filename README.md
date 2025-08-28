# UPI Payment System - MSc Project

A comprehensive UPI (Unified Payments Interface) system implementation with microservices architecture, monitoring, and RBAC (Role-Based Access Control).

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UPI Client    │────│  UPI Central    │────│   Bank Services │
│   (Postman)     │    │   (Switch)      │    │  (Bank A & B)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                    ┌─────────┼─────────┐
                    │         │         │
            ┌───────▼───┐ ┌───▼───┐ ┌───▼────────┐
            │ Prometheus│ │Grafana│ │AlertManager│
            │(Monitoring│ │(Viz)  │ │(Alerts)    │
            └───────────┘ └───────┘ └────────────┘
```

## Features Implemented

### Core UPI Functionality
- User registration and validation
- Money transfer between different banks
- Transaction status tracking
- Transaction history

### Architecture Requirements
- **Centralized UPI Switch**: Handles routing and validation
- **Multiple Bank Services**: Bank A and Bank B with independent databases
- **REST API**: All communications via RESTful APIs
- **Database Integration**: MySQL with separate databases for each service

### Monitoring & Alerting
- **Prometheus**: Metrics collection and monitoring
- **Grafana**: Visualization dashboards
- **AlertManager**: Notification system for failures
- **Custom Metrics**: Transaction success/failure rates, response times

### RBAC (Role-Based Access Control)
- **USER Role**: Can perform transfers, view own transactions
- **ADMIN Role**: Can view all users, transactions, disable users
- **Spring Security**: HTTP Basic authentication

### Docker Compose Setup
- Complete containerized deployment
- Service discovery and networking
- Persistent data volumes
- Health checks

## Project Structure


upi-payment-system/
├── docker-compose.yml           # Complete Docker setup
├── sql/                        # Database initialization scripts
├── monitoring/                 # Prometheus, Grafana, AlertManager configs
├── upi-central/               # Main UPI switch service
├── bank-a/                    # Bank A service
├── bank-b/                    # Bank B service
└── postman-collection/        # API testing collection


### Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 11+ (for local development)
- Maven (for building)
- Postman (for API testing)

### 1. Clone and Setup
```bash
git clone <your-repo>
cd upi-payment-system
```

### 2. Build Services (Local Development)
```bash
# Build UPI Central
cd upi-central
mvn clean package
cd ..

# Build Bank A
cd bank-a
mvn clean package
cd ..

# Build Bank B
cd bank-b
mvn clean package
cd ..
```

### 3. Start with Docker Compose
```bash
docker-compose up -d
```

### 4. Wait for Services to Start
```bash
# Check service health
curl http://localhost:8080/actuator/health  # UPI Central
curl http://localhost:8081/actuator/health  # Bank A
curl http://localhost:8082/actuator/health  # Bank B
```

### 5. Access Monitoring
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **AlertManager**: http://localhost:9093

## API Testing

### Authentication
- **User**: `user` / `user123`
- **Admin**: `admin` / `admin123`

### Sample API Calls

#### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/upi/register \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "upiId": "testuser@paytm",
    "phoneNumber": "9876543213",
    "name": "Test User",
    "bankCode": "BANK_A",
    "accountNumber": "ACC004"
  }'
```

#### 2. Validate User
```bash
curl -u user:user123 \
  "http://localhost:8080/api/upi/validate?identifier=john@paytm"
```

#### 3. Transfer Money
```bash
curl -X POST http://localhost:8080/api/upi/transfer \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "senderUpiId": "john@paytm",
    "receiverUpiId": "jane@phonepe",
    "amount": 1000.00,
    "description": "Payment for dinner"
  }'
```

#### 4. Check Transaction Status
```bash
curl -u user:user123 \
  "http://localhost:8080/api/upi/transaction/{transactionId}"
```

## Sample Data

### Pre-loaded Users
| UPI ID | Phone | Name | Bank | Account | Balance |
|--------|-------|------|------|---------|---------|
| john@paytm | 9876543210 | John Doe | BANK_A | ACC001 | ₹10,000 |
| jane@phonepe | 9876543211 | Jane Smith | BANK_B | ACC002 | ₹5,000 |
| admin@upi | 9876543212 | Admin User | BANK_A | ACC003 | ₹50,000 |

## Transaction Flow

1. **User Validation**: Client validates recipient using UPI ID/Phone
2. **Transfer Initiation**: Client sends transfer request to UPI Central
3. **Balance Check**: UPI Central debits from sender's bank
4. **Credit Processing**: UPI Central credits to receiver's bank
5. **Transaction Recording**: All steps are logged and tracked
6. **Notifications**: Success/failure notifications sent
7. **Monitoring**: Metrics recorded for monitoring

## Monitoring & Alerts

### Metrics Tracked
- `upi.register.requests`: User registration attempts
- `upi.validate.requests`: User validation requests  
- `upi.transfer.requests`: Money transfer requests

### Alerts Configured
- **High Failure Rate**: >10% transaction failures
- **Service Down**: Any service becomes unavailable
- **High Response Time**: 95th percentile >2 seconds

## RBAC Implementation

### Roles and Permissions
- **USER Role**:
  - Register new users
  - Validate users
  - Transfer money
  - View own transaction history

- **ADMIN Role**:
  - All USER permissions
  - View all users
  - View all transactions
  - Disable users

### Security Configuration
- HTTP Basic Authentication
- Method-level security with `@PreAuthorize`
- Separate credentials for different roles

## Database Schema

### UPI Central DB
- **users**: UPI ID mapping to bank details
- **transactions**: Transaction records and status

### Bank Databases
- **accounts**: Account details and balances
- **bank_transactions**: Bank-level transaction logs

## Troubleshooting

### Common Issues
1. **Services not starting**: Check Docker logs with `docker-compose logs <service-name>`
2. **Database connection errors**: Ensure MySQL containers are ready
3. **Authentication errors**: Verify username/password combinations
4. **Port conflicts**: Ensure ports 8080-8082, 3000, 9090, 9093 are free

### Logs
```bash
# View all logs
docker-compose logs

# View specific service"# UPI-System" 
