# Courier Payment Calculation REST API

This project is a **REST API** application that automates courier payment calculations and tracking. Courier shifts, operations, and payments are interrelated to compute and record the payments.

---

## 📋 Project Features

- **Courier Payment Calculation**: Calculates payments based on shift details.
- **CRUD Operations**: Provides operations for couriers, shifts, and operations.
- **Payment Tracking**: Computes payments for each shift and records them in the database.
- **Summary Reporting**: Reports total payments for couriers.
- **RESTful API**: Includes endpoints following REST standards.

---

## 📂 Project Structure

The project is built using **Java Spring Boot 3** and has the following structure:

- **Controller**: Defines API endpoints.
- **Service**: Contains business logic.
- **Repository**: Handles database operations.
- **Model**: Entity definitions.
- **Database**: Uses H2.

---

## 🚀 Installation Instructions

### 1. Prerequisites
- **Java 21+**
- **Maven** (To manage project dependencies)
- **IDE**: IntelliJ, Eclipse, or VS Code
- **Database**: H2 (in-memory) or PostgreSQL
- **Spring Boot 3.4.1+**
---

### 2. Running the Project
1. **Clone the project repository:**
   ```bash
   git clone https://github.com/marufakan/courier-payment-calculation-rest-api.git
   cd courier-payment-calculation
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Test the API** using the endpoints below:

### 🌐 API Endpoints

| Method   | URL                                      | Description                        |
|----------|-----------------------------------------|------------------------------------|
| POST     | `/api/v1/couriers/{courierId}/shifts`   | Creates a new shift                |
| GET      | `/api/v1/couriers/{courierId}/shifts`   | Lists shifts for a courier         |
| PUT      | `/api/v1/couriers/{courierId}/shifts/{shiftId}` | Updates a shift record       |
| GET      | `/api/v1/couriers/{courierId}/payments` | Retrieves payments for a given date|

---

## 📊 Database Schema

| Table         | Fields                                                                  |
|---------------|-------------------------------------------------------------------------|
| **couriers**  | courier_id (PK), name                                                  |
| **shifts**    | shift_id (PK), courier_id (FK), operation_id (FK), date, package_count, hours_worked |
| **operations**| operation_id (PK), package_rate, hourly_rate                           |
| **payments**  | payment_id (PK), shift_id (FK), courier_id (FK), payment_amount, date  |

---

## **Test Scenario: Update Shift for a Courier**

### **Precondition**  
- H2 database should be set up with the following data:

```sql
-- Insert dummy data into the Courier table
INSERT INTO couriers (courier_id, name) VALUES (1, 'John Doe');

-- Insert dummy data into the Operation table
INSERT INTO operations (operation_id, package_rate, hourly_rate) VALUES (1, 5.00, 20.00);

-- Insert dummy data into the Shift table
INSERT INTO shifts (shift_id, courier_id, operation_id, package_count, hours_worked, date) VALUES
(1, 1, 1, 10, 10, '2024-12-17'),
(2, 1, 1, 5, 8, '2024-12-18');
```

---

### **Test Request**  
```bash
curl -X 'PUT' \
  'http://localhost:8080/api/v1/couriers/1/shifts/1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "operationId": 1,
  "date": "2024-12-17",
  "packageCount": 5,
  "hoursWorked": 5
}'
```

---

### **Expected Response**  
```json
{
  "id": 1,
  "courierId": 1,
  "operationId": 1,
  "packageCount": 5,
  "hoursWorked": 5,
  "date": "2024-12-17"
}
```

---

### **Database Check**  
After the PUT request, verify that the shift data is updated in the `shifts` table:

```sql
SELECT * FROM shifts;
```

| shift_id | courier_id | operation_id | package_count | hours_worked | date       |
|----------|------------|--------------|---------------|--------------|------------|
| 1        | 1          | 1            | 5             | 5            | 2024-12-17 |
| 2        | 1          | 1            | 5             | 8            | 2024-12-18 |

---

## **Test Scenario: Get Shifts for a Courier with Pagination**

### **Precondition**
```sql
INSERT INTO couriers (courier_id, name) VALUES (1, 'John Doe');
INSERT INTO operations (operation_id, package_rate, hourly_rate) VALUES (1, 5.00, 20.00);
INSERT INTO shifts (shift_id, courier_id, operation_id, package_count, hours_worked, date) VALUES
(1, 1, 1, 10, 10, '2024-12-17'),
(2, 1, 1, 5, 8, '2024-12-18'),
(3, 1, 1, 8, 9, '2024-12-19');
```

### **Test Request**
```bash
curl -X GET 'http://localhost:8080/api/v1/couriers/1/shifts?page=1&size=2'
```

### **Expected Response**
```json
[{
    "shiftId": 1,
    "date": "2024-12-17",
    "packageCount": 10
  },
  {
    "shiftId": 2,
    "date": "2024-12-18",
    "packageCount": 5
}]
```

---

## **Test Scenario: Get Payment for Courier on Specific Date**

### **Precondition**
```sql
INSERT INTO payments (payment_id, courier_id, date, payment_amount, shift_id) VALUES
(1, 1, '2024-12-17', 250.00, 1);
```

### **Test Request**
```bash
curl -X GET 'http://localhost:8080/api/v1/couriers/1/payments?date=2024-12-17'
```

### **Expected Response**
```json
{
    "totalPayment": 250.00,
    "shiftPayments": [
        {
            "shiftId": 1,
            "date": "2024-12-17",
            "paymentAmount": 250.00
        }
    ]
}
```

## **Test Scenario: Adding Records to Shift and Payment Tables**

### **Precondition**  
- The H2 database must be ready.  
- The following SQL commands should be executed:

```sql
-- Insert dummy data into the Courier table
INSERT INTO couriers (courier_id, name) VALUES
(1, 'John Doe'),
(2, 'Jane Smith'),
(3, 'Ali Veli');

-- Insert dummy data into the Operation table
INSERT INTO operations (operation_id, package_rate, hourly_rate) VALUES
(1, 5.00, 20.00),
(2, 7.50, 25.00),
(3, 6.00, 22.50);
```

---

### **Step: Endpoint Test**  
**Request:**

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/couriers/1/shifts' \
  -H 'Content-Type: application/json' \
  -d '{
  "operationId": 1,
  "date": "2024-12-17",
  "packageCount": 10,
  "hoursWorked": 10
}'
```

---

### **Expected Response**  
```json
{
  "id": 1,
  "courierId": 1,
  "operationId": 1,
  "packageCount": 10,
  "hoursWorked": 10,
  "date": "2024-12-17"
}
```

---

### **Database Verification**  

### **1. Verify Shifts Table:**  
```sql
SELECT * FROM shifts;
```

| id | courier_id | operation_id | package_count | hours_worked | date       |  
|----|------------|--------------|---------------|--------------|------------|  
| 1  | 1          | 1            | 10            | 10           | 2024-12-17 |  

### **2. Verify Payments Table:**  
```sql
SELECT * FROM payments;
```

| payment_id | courier_id | date       | payment_amount | shift_id |  
|------------|------------|------------|----------------|----------|  
| 1          | 1          | 2024-12-17 | 250.00         | 1        |  

---

## **Validation Document: Verifying Courier, Operation, Shift, and Payment Records**

---

### **Step: Endpoint Test**  
**Request:**

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/couriers/1/shifts' \
  -H 'Content-Type: application/json' \
  -d '{
  "operationId": 0,
  "date": "2024-12-30",
  "packageCount": 0,
  "hoursWorked": -1
}'
```

---

### **Expected Response**  
```json
{
    "date": "Date must be in the past or present",
    "operationId": "Operation Id count must be at least 1",
    "packageCount": "Package count must be at least 1",
    "hoursWorked": "Hours worked must be at least 1"
}
```

---



## **H2 Console Access**

To access the **H2 console**, run the application through your IDE (by running the `CourierPaymentApiApplication` class). Then:

1. Open your browser and go to:
   ```
   http://localhost:8080/h2-console
   ```

2. Use the following login credentials:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **User Name**: `sa`
   - **Password**: *(leave blank)*

3. You can now view tables and execute queries in the H2 database.

---

## 📃 License

This project is licensed under the [MIT License](LICENSE).

