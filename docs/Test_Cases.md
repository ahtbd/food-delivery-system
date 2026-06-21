# Test Cases Document - Food Delivery Management System

## 1. Order Service Tests

### Unit Tests

| Test ID | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| UT-001 | createOrder_ShouldReturnOrderResponse | Create order with valid data | Order created with PENDING status | Order created successfully | ✅ PASS |
| UT-002 | getOrderById_ShouldReturnOrderResponse | Get order by valid ID | Order details returned | Order details returned | ✅ PASS |
| UT-003 | getOrderById_WhenNotFound_ShouldReturnError | Get order by invalid ID | RuntimeException thrown | RuntimeException thrown | ✅ PASS |
| UT-004 | getOrdersByUser_ShouldReturnOrderResponses | Get orders by user with pagination | List of orders returned | List of orders returned | ✅ PASS |
| UT-005 | updateOrderStatus_ShouldReturnUpdatedOrder | Update order status to CONFIRMED | Order status updated | Order status updated | ✅ PASS |
| UT-006 | getOrderCountByStatus_ShouldReturnCount | Count orders by status | Count returned | Count returned | ✅ PASS |

### Integration Tests (Semi-Manual)

| Test ID | API Endpoint | Method | Input | Expected Output | Actual Output | Status |
|---|---|---|---|---|---|---|
| IT-001 | /api/orders | POST | Valid OrderRequest | 201 Created | 201 Created | ✅ PASS |
| IT-002 | /api/orders | POST | Invalid userId | 400 Bad Request | 400 Bad Request | ✅ PASS |
| IT-003 | /api/orders/{id} | GET | Valid order ID | 200 OK | 200 OK | ✅ PASS |
| IT-004 | /api/orders/{id} | GET | Invalid order ID | 404 Not Found | 404 Not Found | ✅ PASS |
| IT-005 | /api/orders/user/{userId} | GET | Valid userId, page=0, size=10 | 200 OK | 200 OK | ✅ PASS |
| IT-006 | /api/orders/{id}/status | PUT | Valid order ID, status=CONFIRMED | 200 OK | 200 OK | ✅ PASS |
| IT-007 | /api/orders/{id} | DELETE | Valid order ID (PENDING) | 204 No Content | 204 No Content | ✅ PASS |
| IT-008 | /api/orders/{id} | DELETE | Delivered order | 400 Bad Request | 400 Bad Request | ✅ PASS |

---

## 2. Restaurant Service Tests

### Unit Tests

| Test ID | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| UT-007 | createRestaurant_ShouldReturnRestaurantResponse | Create restaurant with valid data | Restaurant created | Restaurant created | ✅ PASS |
| UT-008 | getRestaurantById_ShouldReturnRestaurantResponse | Get restaurant by ID | Restaurant details returned | Restaurant details returned | ✅ PASS |
| UT-009 | getRestaurantById_WhenNotFound_ShouldReturnError | Get restaurant by invalid ID | RuntimeException thrown | RuntimeException thrown | ✅ PASS |
| UT-010 | getAllRestaurants_ShouldReturnRestaurantResponses | Get all restaurants with pagination | List of restaurants | List of restaurants | ✅ PASS |
| UT-011 | getRestaurantsByCuisine_ShouldReturnRestaurantResponses | Get restaurants by cuisine | List of restaurants | List of restaurants | ✅ PASS |
| UT-012 | getActiveRestaurants_ShouldReturnRestaurantResponses | Get active restaurants | List of active restaurants | List of active restaurants | ✅ PASS |
| UT-013 | searchRestaurants_ShouldReturnRestaurantResponses | Search restaurants by name | List of matching restaurants | List of matching restaurants | ✅ PASS |
| UT-014 | addMenuItem_WhenRestaurantExists_ShouldReturnMenuItemResponse | Add menu item to existing restaurant | Menu item created | Menu item created | ✅ PASS |
| UT-015 | addMenuItem_WhenRestaurantNotExists_ShouldReturnError | Add menu item to non-existent restaurant | RuntimeException thrown | RuntimeException thrown | ✅ PASS |
| UT-016 | getMenuItemsByRestaurant_ShouldReturnMenuItemResponses | Get menu items by restaurant | List of menu items | List of menu items | ✅ PASS |
| UT-017 | getAvailableMenuItems_ShouldReturnMenuItemResponses | Get available menu items | List of available items | List of available items | ✅ PASS |

### Integration Tests (Semi-Manual)

| Test ID | API Endpoint | Method | Input | Expected Output | Actual Output | Status |
|---|---|---|---|---|---|---|
| IT-009 | /api/restaurants | POST | Valid RestaurantRequest | 201 Created | 201 Created | ✅ PASS |
| IT-010 | /api/restaurants/{id} | GET | Valid restaurant ID | 200 OK | 200 OK | ✅ PASS |
| IT-011 | /api/restaurants?page=0&size=10 | GET | Pagination parameters | 200 OK | 200 OK | ✅ PASS |
| IT-012 | /api/restaurants/cuisine/Italian | GET | Cuisine type | 200 OK | 200 OK | ✅ PASS |
| IT-013 | /api/restaurants/active | GET | - | 200 OK | 200 OK | ✅ PASS |
| IT-014 | /api/restaurants/top?limit=5 | GET | Limit parameter | 200 OK | 200 OK | ✅ PASS |
| IT-015 | /api/restaurants/search?name=Pizza | GET | Search query | 200 OK | 200 OK | ✅ PASS |
| IT-016 | /api/menu-items | POST | Valid MenuItemRequest | 201 Created | 201 Created | ✅ PASS |
| IT-017 | /api/menu-items/restaurant/{id} | GET | Valid restaurant ID | 200 OK | 200 OK | ✅ PASS |

---

## 3. Notification Service Tests

### Unit Tests

| Test ID | Test Method | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| UT-018 | createNotification_ShouldReturnNotificationResponse | Create notification | Notification created | Notification created | ✅ PASS |
| UT-019 | getNotificationById_ShouldReturnNotificationResponse | Get notification by ID | Notification details | Notification details | ✅ PASS |
| UT-020 | getNotificationById_WhenNotFound_ShouldReturnError | Get notification by invalid ID | RuntimeException thrown | RuntimeException thrown | ✅ PASS |
| UT-021 | getNotificationsByUser_ShouldReturnNotificationResponses | Get notifications by user | List of notifications | List of notifications | ✅ PASS |
| UT-022 | getNotificationsByOrder_ShouldReturnNotificationResponses | Get notifications by order | List of notifications | List of notifications | ✅ PASS |
| UT-023 | getPendingNotifications_ShouldReturnNotificationResponses | Get pending notifications | List of pending notifications | List of pending notifications | ✅ PASS |
| UT-024 | updateNotificationStatus_ShouldReturnUpdatedNotification | Update notification status | Status updated | Status updated | ✅ PASS |
| UT-025 | markAsSent_ShouldComplete | Mark notification as sent | Complete | Complete | ✅ PASS |
| UT-026 | markAsFailed_ShouldComplete | Mark notification as failed | Complete | Complete | ✅ PASS |

### Integration Tests (Semi-Manual)

| Test ID | API Endpoint | Method | Input | Expected Output | Actual Output | Status |
|---|---|---|---|---|---|---|
| IT-018 | /api/notifications | POST | Valid NotificationRequest | 201 Created | 201 Created | ✅ PASS |
| IT-019 | /api/notifications/{id} | GET | Valid notification ID | 200 OK | 200 OK | ✅ PASS |
| IT-020 | /api/notifications/user/{userId} | GET | Valid userId | 200 OK | 200 OK | ✅ PASS |
| IT-021 | /api/notifications/order/{orderId} | GET | Valid orderId | 200 OK | 200 OK | ✅ PASS |
| IT-022 | /api/notifications/pending | GET | - | 200 OK | 200 OK | ✅ PASS |
| IT-023 | /api/notifications/{id}/status | PUT | Valid ID, status=SENT | 200 OK | 200 OK | ✅ PASS |

---

## 4. Kafka Integration Tests

| Test ID | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|
| KT-001 | Order created → Kafka event sent | Event published to order-events topic | Event published successfully | ✅ PASS |
| KT-002 | Notification Service consumes Kafka event | Event consumed and notification created | Notification created | ✅ PASS |

## 5. RabbitMQ Integration Tests

| Test ID | Description | Expected Result | Actual Result | Status |
|---|---|---|---|---|
| RT-001 | Notification sent to RabbitMQ queue | Message published to queue | Message published | ✅ PASS |
| RT-002 | RabbitMQ consumer processes notification | Notification marked as SENT | Notification processed | ✅ PASS |

---

## Test Execution Summary

| Category | Total Tests | Passed | Failed | Success Rate |
|---|---|---|---|---|
| Unit Tests | 26 | 26 | 0 | 100% |
| Integration Tests | 23 | 23 | 0 | 100% |
| Kafka Tests | 2 | 2 | 0 | 100% |
| RabbitMQ Tests | 2 | 2 | 0 | 100% |
| **Total** | **53** | **53** | **0** | **100%** |