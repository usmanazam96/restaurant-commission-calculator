# restaurant-commission-calculator

This service manages a restaurant's menu, orders, and revenue. It uses an in-memory H2 database for data storage and is built with Spring Data JPA. Testing is done with JUnit 5 and Mockito, and Lombok is used for reducing boilerplate code. The application is built using Maven and Java 21.

## Running the Application

To run the application, use one of the following commands:

1. `java -jar restaurant-commission-calculator-0.0.1-SNAPSHOT.jar`
2. `mvn spring-boot:run`

## Overview

The service allows adding menu items, managing stock, creating orders, and calculating revenue and commission. It ensures stock levels are correctly updated when orders are made and provides error messages for invalid orders, menu items, or insufficient stock.

### Features

- **Menu Management**: Add, update, delete, and retrieve menu items.
- **Order Management**: Create, update, delete, and retrieve orders. Update stock levels based on orders.
- **Revenue Calculation**: Calculate total revenue and commission for all orders or specific orders.

### Entities

- **MenuItem**: Represents an item available on the menu.
  - Fields: `id`, `name`, `price`, `stock`.

- **Order**: Represents a customer's order.
  - Fields: `id`, `orderItems` (list of `OrderItem`), `totalPrice`.

- **OrderItem**: Represents an item within an order.
  - Fields: `id`, `menuItem` (reference to `MenuItem`), `quantity`.

### Services

- **MenuItemService**: Manages addition, retrieval, updating, and deletion of menu items. Ensures correct stock levels and uses `MenuItemRepository` implementing `JPARepository`.
- **OrderService**: Manages creation, retrieval, updating, and deletion of orders. Updates stock levels for menu items and uses `OrderRepository` implementing `JPARepository`.
- **RevenueService**: Calculates total revenue and commission for all orders and specific orders.

### Controllers

#### MenuController

- **POST /menu**: Adds a new `MenuItem` to the menu. Expects a `MenuItem` object in the request body. Returns the created `MenuItem` with status 201 Created.
- **GET /menu**: Retrieves all menu items. Returns a list of `MenuItems` with status 200 OK.
- **GET /menu/{id}**: Retrieves a `MenuItem` by its ID. Returns the `MenuItem` with status 200 OK if found, 404 Not Found if not found.
- **DELETE /menu/{id}**: Deletes a `MenuItem` by its ID. Returns status 204 No Content if deleted, 404 Not Found if not found.

#### OrderController

- **POST /orders**: Creates a new `Order`. Expects an `Order` object in the request body. Returns the created `Order` with status 201 Created.
- **GET /orders**: Retrieves all orders. Returns a list of `Orders` with status 200 OK.
- **GET /orders/{id}**: Retrieves an `Order` by its ID. Returns the `Order` with status 200 OK if found, 404 Not Found if not found.
- **DELETE /orders/{id}**: Deletes an `Order` by its ID. Returns status 204 No Content if deleted, 404 Not Found if not found.
- **PUT /orders/{orderId}/add-item**: Adds an `OrderItem` to an existing `Order`. Expects an `OrderItem` object in the request body and `orderId` in the path. Returns the updated `Order` with status 200 OK if the order exists, 404 Not Found if the order does not exist.
- **DELETE /orders/{orderId}/remove-item/{orderItemId}**: Removes an `OrderItem` from an existing `Order`. Expects `orderId` and `orderItemId` in the path. Returns the updated `Order` with status 200 OK if the order and order item exist, 404 Not Found if the order or order item does not exist.

#### RevenueController

- **GET /revenue/total**: Returns the total revenue from all orders with status 200 OK.
- **GET /revenue/commission**: Returns the total commissioned revenue (12%) from all orders with status 200 OK.
- **GET /revenue/order/{orderId}**: Returns the revenue of a specific order by `orderId` with status 200 OK if found, 404 Not Found if not found.
- **GET /revenue/order/{orderId}/commission**: Returns the commissioned revenue (12%) of a specific order by `orderId` with status 200 OK if found, 404 Not Found if not found.

### Exception Handling

- **MenuItemNotFoundException**: Thrown when a `MenuItem` is not found by its ID.
- **OrderNotFoundException**: Thrown when an `Order` is not found by its ID.
- **InvalidStockException**: Thrown when an attempt to update the stock of a `MenuItem` results in negative stock.

**GenericExceptionalHandler**: Handles exceptions and returns appropriate response and message.
- `MenuItemNotFoundException` and `OrderNotFoundException` return code 404.
- `InvalidStockException` returns code 400.

## Sample Inputs and Outputs

### Sample Input for MenuItem

```json
{
  "name": "Sample Product 1",
  "description": "This is a sample product description.",
  "price": 99.99,
  "stock": 50
}
```
### Sample Output for MenuItem

```json
{
  "id": 1,
  "name": "Sample Product 1",
  "description": "This is a sample product description.",
  "price": 99.99,
  "stock": 50
}

```
### Sample Output of Menu

```json
[
  {
    "id": 1,
    "name": "Sample Product 1",
    "description": "This is a sample product description.",
    "price": 99.99,
    "stock": 50
  },
  {
    "id": 2,
    "name": "Sample Product 2",
    "description": "This is a sample product description.",
    "price": 99.99,
    "stock": 50
  }
]
```

### Sample Input for Order Creation Request

```json

{
  "id": 1,
  "items": [
    {
      "menuItem": {
        "id": 1
      },
      "quantity": 2
    },
    {
      "menuItem": {
        "id": 2
      },
      "quantity": 2
    }
  ]
}
```
### Sample Input of OrderItem

```json

{
  "menuItem": {
    "id": 1
  },
  "quantity": 2
}
```
### Sample Output of Order

```json
{
  "items": [
    {
      "id": 1,
      "menuItem": {
        "id": 1,
        "name": "Sample Product 1",
        "description": "This is a sample product description.",
        "price": 99.99,
        "stock": 48
      },
      "quantity": 2
    },
    {
      "id": 2,
      "menuItem": {
        "id": 2,
        "name": "Sample Product 2",
        "description": "This is a sample product description.",
        "price": 99.99,
        "stock": 48
      },
      "quantity": 2
    }
  ]
}

```
