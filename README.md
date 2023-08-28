# Requirement :

- Java 17
- MySQL
- [Mailpit](https://github.com/axllent/mailpit) (Optional, for testing email.)

Please adjust the SMTP configuration in the application.properties file if you're not using Mailpit to send test emails
locally.

# Installation :

```
git clone https://github.com/nickoandreas/order-management.git
```

Note: Please use your MySQL username and password for the following command.

```
mysql -u YourUsername -pYourPassword -e "CREATE DATABASE order_management_db;"
```

```
mvn install
```

```
mvn spring-boot:run
```

After installation you have an admin account and six products.

```
email    : admin@gmail.com
password : Password@123

SKU products : IDM-001, IDM-002, IDM-003, IDM-004, IDM-005, IDM-006
```

# API Documentation

## Users

### Register

Endpoint : POST /api/users/register

Request Body :

```json
{
  "name": "Nicko Andreas",
  "email": "nickoandreas@gmail.com",
  "password": "Password@123"
}
```

Response Body (Success) :

```json
{
  "status": "success",
  "message": "Registration successful."
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Email is already registered."
}
```

### Login

Endpoint : POST /api/users/login

Request Body :

```json
{
  "email": "nickoandreas@gmail.com",
  "password": "Password@123"
}
```

Response Body (Success) :

```json
{
  "data": {
    "token": "a60bb104-7043-4002-ac37-e08f21d6dc37",
    "token_expired_at": "2023-09-27 03:58:31"
  },
  "status": "success"
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Username or Password is invalid."
}
```

### Logout

Endpoint : POST /api/users/logout

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "status": "success",
  "message": "Logout successful."
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Unauthorized access. The 'X-API-TOKEN' field in the request headers is required."
}
```

## Order

### Create Order

Endpoint : POST /api/order

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "order_number": "INV2308280001",
  "ordered_at": "2023-08-28 15:30:15",
  "items": [
    {
      "sku": "IDM-001",
      "qty": 1
    },
    {
      "sku": "IDM-002",
      "qty": 2
    },
    {
      "sku": "IDM-003",
      "qty": 3
    }
  ]
}
```

Response Body (Success) :

```json
{
  "status": "success",
  "message": "Order with number INV2308280001 successfully created."
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Allocation Not Found : IDM-002."
}
```

### Get Order

Endpoint : GET /api/order

Request Header :

- X-API-TOKEN : Token (Mandatory)

Query Param :

- current_page : Integer, start from 0, default 0
- page_size : Integer, default 10

Response Body (Success) :

```json
{
  "data": [
    {
      "id": 13,
      "status": "new",
      "items": [
        {
          "id": 15,
          "name": "Indomie Kebab Rendang",
          "sku": "IDM-004",
          "price": 4000.00,
          "product_id": 4,
          "qty_ordered": 3,
          "raw_total": 12000.00,
          "image_url": "http://localhost:8080/assets/image/indomie-kebab-rendang.jpg"
        },
        {
          "id": 16,
          "name": "Indomie Matcha",
          "sku": "IDM-005",
          "price": 4000.00,
          "product_id": 5,
          "qty_ordered": 2,
          "raw_total": 8000.00,
          "image_url": "http://localhost:8080/assets/image/indomie-matcha.jpg"
        }
      ],
      "order_number": "INV2308270001",
      "ordered_at": "2023-08-27 08:30:15",
      "grand_total": 20000.00
    },
    {
      "id": 14,
      "status": "new",
      "items": [
        {
          "id": 17,
          "name": "Indomie Ayam Bawang",
          "sku": "IDM-001",
          "price": 3500.00,
          "product_id": 1,
          "qty_ordered": 3,
          "raw_total": 10500.00,
          "image_url": "http://localhost:8080/assets/image/indomie-ayam-bawang.jpg"
        },
        {
          "id": 18,
          "name": "Indomie Jumbo",
          "sku": "IDM-003",
          "price": 5000.00,
          "product_id": 3,
          "qty_ordered": 2,
          "raw_total": 10000.00,
          "image_url": "http://localhost:8080/assets/image/indomie-jumbo.png"
        }
      ],
      "order_number": "INV2308270002",
      "ordered_at": "2023-08-28 08:30:15",
      "grand_total": 20500.00
    }
  ],
  "page_info": {
    "page_size": 2,
    "current_page": 0,
    "total_pages": 4
  },
  "total_count": 7
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Page size must not be less than one"
}
```

### Export Order To CSV

Endpoint : GET /api/order/export

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": {
    "url": "http://localhost:8080/temp/order_20230828_111521551.csv"
  },
  "status": "success"
}
```

Response Body (Failed) :

```json
{
  "status": "failed",
  "message": "Unauthorized access. Please use an admin account."
}
```