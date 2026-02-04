# 🛒 E-Commerce Management System (Spring Boot)

A backend **E-Commerce Management System** built using **Spring Boot**, **Spring Data JPA**, and **MySQL**.  
This project exposes RESTful APIs to manage users, categories, products, cart, orders, reviews, and order tracking.

---

## 🚀 Features
- User Management
- Category Management (parent–child categories)
- Product Management
- Cart Management
- Order Management
- Review Management
- Order Tracking
- CRUD Operations (Create, Read, Update, Delete)
- RESTful APIs
- MySQL Database Integration

---

## 🏗️ Tech Stack
- Java
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Lombok
- Git & GitHub

---

## 📂 Project Structure
```
com.example
 ├── Controller
 ├── Model
 ├── Repository
 └── Service
```

---

## 🗂️ ER Diagram
Place your ER Diagram image here:
```
er-diagram.png
```

---

## ⚙️ Database Schema
**Database Name:** `ecommerncedb`
## Query:
```
CREATE DATABASE ecommerncedb;
use ecommerncedb;
show tables;
/* ================= USERS ================= */
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* ================= CATEGORY ================= */
select * from category;
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES category(id)
);

/* ================= PRODUCT ================= */
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    brand VARCHAR(100),
    sku VARCHAR(100) UNIQUE,
    rating_avg DOUBLE DEFAULT 0,
    rating_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

/* ================= CART ================= */
select * from cart;
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_added DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    UNIQUE (user_id, product_id)
);

/* ================= ORDERS ================= */
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_group_id VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(50) DEFAULT 'PLACED',
    is_reviewed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

/* ================= ORDER TRACKING ================= */
select * from order_tracking;
CREATE TABLE order_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_group_id VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    location VARCHAR(100),
    updated_by VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* ================= REVIEW ================= */
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    UNIQUE (user_id, order_id)
);

/* ================= INDEXES ================= */
CREATE INDEX idx_product_category ON product(category_id);
CREATE INDEX idx_product_price ON product(price);
CREATE INDEX idx_cart_user ON cart(user_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_group ON orders(order_group_id);
CREATE INDEX idx_tracking_group ON order_tracking(order_group_id);

----------------------------------------------------------------
-- INSERT DATA
----------------------------------------------------------------

INSERT INTO users (name, email, password) VALUES
('Alice', 'alice@gmail.com', 'pass1'),
('Bob', 'bob@gmail.com', 'pass2'),
('Charlie', 'charlie@gmail.com', 'pass3'),
('David', 'david@gmail.com', 'pass4'),
('Eva', 'eva@gmail.com', 'pass5');

-- Parent categories
INSERT INTO category (name, description, parent_id) VALUES
('Electronics', 'Electronic items', NULL),
('Clothing', 'Wearable items', NULL),
('Books', 'All kinds of books', NULL),
('Home', 'Home appliances', NULL),
('Sports', 'Sports accessories', NULL);

-- Child categories
INSERT INTO category (name, description, parent_id) VALUES
('Mobiles', 'Smart phones', 1),
('Laptops', 'Portable computers', 1),
('Men Wear', 'Mens clothing', 2),
('Fiction', 'Story books', 3),
('Kitchen', 'Kitchen appliances', 4);

-- Products linked to CHILD categories
INSERT INTO product (name, description, price, stock_quantity, brand, sku, category_id) VALUES
('iPhone 15', 'Apple smartphone', 79999, 10, 'Apple', 'SKU101', 6),
('Dell XPS', 'Laptop computer', 95000, 8, 'Dell', 'SKU102', 7),
('Men T-Shirt', 'Cotton t-shirt', 599, 40, 'Puma', 'SKU103', 8),
('Harry Potter', 'Fantasy novel', 899, 25, 'Bloomsbury', 'SKU104', 9),
('Mixer Grinder', 'Kitchen appliance', 3499, 12, 'Philips', 'SKU105', 10);

INSERT INTO orders (order_group_id, user_id, product_id, quantity, price) VALUES
('ORD001', 1, 1, 1, 79999),
('ORD002', 2, 2, 1, 95000),
('ORD003', 3, 3, 2, 1198),
('ORD004', 4, 4, 1, 899),
('ORD005', 5, 5, 1, 3499);

INSERT INTO review (user_id, product_id, order_id, rating, comment) VALUES
(1, 1, 1, 5, 'Excellent'),
(2, 2, 2, 4, 'Good quality'),
(3, 3, 3, 5, 'Very useful'),
(4, 4, 4, 3, 'Average'),
(5, 5, 5, 4, 'Nice product');

INSERT INTO cart (user_id, product_id, quantity, price_at_added) VALUES
(1, 1, 1, 79999),
(2, 2, 1, 95000),
(3, 3, 2, 599),
(4, 4, 1, 899),
(5, 5, 1, 3499);

INSERT INTO order_tracking (order_group_id, status, description, location, updated_by) VALUES
('ORD001', 'SHIPPED', 'Order shipped', 'Chennai', 'Admin'),
('ORD002', 'DELIVERED', 'Delivered successfully', 'Coimbatore', 'Admin'),
('ORD003', 'PACKED', 'Packed in warehouse', 'Madurai', 'Staff'),
('ORD004', 'PLACED', 'Order placed', 'Salem', 'System'),
('ORD005', 'CANCELLED', 'Order cancelled', 'Trichy', 'User');

SELECT 'product → category' AS fk_check, p.id AS child_id
FROM product p
LEFT JOIN category c ON p.category_id = c.id
WHERE p.category_id IS NOT NULL AND c.id IS NULL

UNION ALL

SELECT 'cart → users', c.id
FROM cart c
LEFT JOIN users u ON c.user_id = u.id
WHERE u.id IS NULL

UNION ALL

SELECT 'cart → product', c.id
FROM cart c
LEFT JOIN product p ON c.product_id = p.id
WHERE p.id IS NULL

UNION ALL

SELECT 'orders → users', o.id
FROM orders o
LEFT JOIN users u ON o.user_id = u.id
WHERE u.id IS NULL

UNION ALL

SELECT 'orders → product', o.id
FROM orders o
LEFT JOIN product p ON o.product_id = p.id
WHERE p.id IS NULL

UNION ALL

SELECT 'review → users', r.id
FROM review r
LEFT JOIN users u ON r.user_id = u.id
WHERE u.id IS NULL

UNION ALL

SELECT 'review → product', r.id
FROM review r
LEFT JOIN product p ON r.product_id = p.id
WHERE p.id IS NULL

UNION ALL

SELECT 'review → orders', r.id
FROM review r
LEFT JOIN orders o ON r.order_id = o.id
WHERE o.id IS NULL

UNION ALL

SELECT 'category (parent_id) → category(id)', c.id
FROM category c
LEFT JOIN category p ON c.parent_id = p.id
WHERE c.parent_id IS NOT NULL AND p.id IS NULL;

ALTER TABLE cart
ADD CONSTRAINT uq_user_product UNIQUE (user_id, product_id);

ALTER TABLE order_tracking
ADD CONSTRAINT uq_order_tracking_order_group UNIQUE (order_group_id);

SELECT order_group_id, COUNT(*)
FROM order_tracking
GROUP BY order_group_id
HAVING COUNT(*) > 1;


show tables ;
select * from users;
select * from orders;
select * from product;
select * from category;
select * from cart;
select * from review;
select * from order_tracking;

ALTER TABLE category
DROP COLUMN category_name;

```
### Tables
- users
- category
- product
- cart
- orders
- review
- order_tracking

### Relationships
- Product → Category
- Cart → Users, Product
- Orders → Users, Product
- Review → Users, Product, Orders
- Category → Category (self reference)

---

## ⚙️ Configuration (`application.properties`)
```
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerncedb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## ⚠️ Exception Handling
Centralized exception handling using `@RestControllerAdvice`.

| Exception | Status |
|-----------|--------|
| ResourceNotFoundException | 404 |
| BadRequestException | 400 |
| DuplicateResourceException | 409 |
| Validation Error | 400 |
| Generic Exception | 500 |

---

## 📡 API Endpoints

### 👤 Users
- **POST /api/users/add**
```json
{ "name": "John", "email": "john@gmail.com", "password": "1234" }
```

- **GET /api/users**
- **GET /api/users/{id}**
- **PUT /api/users/{id}**
```json
{ "name": "John Updated", "email": "john@gmail.com", "password": "4321" }
```
- **DELETE /api/users/{id}**

---

### 📦 Categories
- **POST /api/categories/add**
```json
{
  "name": "Mobile Phones",
  "description": "All smartphones",
  "isActive": true,
  "parent": {
    "id": 1
  }
}
```
- **GET /api/categories**
- **GET /api/categories/{id}**
- **PUT /api/categories/{id}**
```json
{
  "name": "updated Mobile Phones",
  "description": "All smartphones",
  "isActive": true,
  "parent": {
    "id": 1
  }
}
```
- **DELETE /api/categories/{id}**

---

### 🛍️ Products
- **POST /api/products/add**
```json
{
  "name": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 79999.00,
  "stockQuantity": 10,
  "brand": "Apple",
  "sku": "SKU302",
  "ratingAvg": 5,
  "ratingCount": 5,
  "isActive": true,
  "category": {
    "id": 6
  }
}

```
- **GET /api/products**
- **GET /api/products/{id}**
- **PUT /api/products/{id}**
```json
{
  "name": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 79999.00,
  "stockQuantity": 10,
  "brand": "Apple",
  "sku": "SKU302",
  "ratingAvg": 5,
  "ratingCount": 5,
  "isActive": true,
  "category": {
    "id": 6
  }
}
```
- **DELETE /api/products/{id}**

---

### 🛒 Cart
- **POST /api/cart/add**
```json
{
  "users": {
    "id": 5
  },
  "product": {
    "id": 3
  },
  "quantity": 2,
  
  "priceAtAdded":1000
}

```
- **GET /api/cart**
- **GET /api/cart/{id}**
- **PUT /api/cart/{id}**
```json
{
  "users": {
    "id": 5
  },
  "product": {
    "id": 3
  },
  "quantity": 2,
  
  "priceAtAdded":1000
}

```
- **DELETE /api/cart/{id}**

---

### 📑 Orders
- **POST /api/orders/addorder**
```json
{
  "users": { "id": 1 },
  "product": { "id": 2 },
  "quantity": 1,
  "price": 499.00,
  "orderStatus": "PLACED"
}


```
- **GET /api/orders**
- **GET /api/orders/{id}**
- **PUT /api/orders/{id}**
```json
{
  "users": { "id": 1 },
  "product": { "id": 2 },
  "quantity": 1,
  "price": 499.00,
  "orderStatus": "SHIPPED",
  "orderGroupId": "ORD1770206869633",
  "isReviewed": false
}

```
- **DELETE /api/orders/{id}**

---

### ⭐ Reviews
- **POST /api/reviews/add**
```json
{
  "users": {
    "id": 1
  },
  "product": {
    "id": 2
  },
  "order": {
    "id": 12
  },
  "rating": 5,
  "comment": "Very good product"
}


```
- **GET /api/reviews**
- **GET /api/reviews/{id}**
- **PUT /api/reviews/{id}**
```json
{
  "users": {
    "id": 5
  },
  "product": {
    "id": 2
  },
  "order": {
    "id": 3
  },
  "rating": 4,
  "comment": "Updated review - good product"
}

```
- **DELETE /api/reviews/{id}**

---

### 🚚 Order Tracking
- **POST /api/order-tracking**
```json
{
  "orderGroupId": "ORD999",
  "status": "updated SHIPPED",
  "description": "Order shipped",
  "location": "Chennai",
  "updatedBy": "Admin"
}


```
- **GET /api/order-tracking**
- **GET /api/order-tracking/{id}**
- **PUT /api/order-tracking/{id}**
```json
{
  "orderGroupId": "ORD999",
  "status": "updated SHIPPED",
  "description": "Order shipped",
  "location": "Chennai",
  "updatedBy": "Admin"
}

```
- **DELETE /api/order-tracking/{id}**

---

## ▶️ How to Run
Clone the repository:
```
git clone https://github.com/EzhilsreeJ/E-Commerce.git
```

Create database:
```
CREATE DATABASE ecommerncedb;
```

Run the application:
```
mvn spring-boot:run
```

---

## 👨‍💻 Author
**Ezhilsree J**  
GitHub: https://github.com/EzhilsreeJ

---

## 📜 License
This project is for learning and educational purposes only.
