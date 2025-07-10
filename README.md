# Equation Solver

A Spring Boot Web Application for Storing and Evaluating Algebraic Equations Using a Postfix Tree.

## Overview

This application provides a RESTful API for managing algebraic equations:

1. **Store Algebraic Equations**: Parse and save equations in a postfix (Reverse Polish Notation) tree structure.
2. **Retrieve Stored Equations**: Fetch a list of stored equations reconstructed from the postfix tree.
3. **Evaluate Equations**: Substitute provided variable values and calculate the result using the postfix tree.

## Technical Implementation

- **Expression Tree**: Implements a postfix (expression) tree where operators are parent nodes and operands are child nodes.
- **Equation Parsing**: Converts infix notation to postfix notation and builds an expression tree.
- **In-Memory Storage**: Stores equations in an in-memory map with unique IDs.
- **REST API**: JSON-based RESTful endpoints for equation operations.

## API Endpoints

### 1. Store an Algebraic Equation

- **URL**: `/api/equations/store`
- **HTTP Method**: POST
- **Request Body**:
```json
{
  "equation": "3x + 2y - z"
}
```
- **Response**:
```json
{
  "message": "Equation stored successfully",
  "equationId": "1"
}
```

### 2. Retrieve Stored Equations

- **URL**: `/api/equations`
- **HTTP Method**: GET
- **Response**:
```json
{
  "equations": [
    {
      "equationId": "1",
      "equation": "3x + 2y - z"
    },
    {
      "equationId": "2",
      "equation": "x^2 + y^2 - 4"
    }
  ]
}
```

### 3. Evaluate an Equation

- **URL**: `/api/equations/{equationId}/evaluate`
- **HTTP Method**: POST
- **Request Body**:
```json
{
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  }
}
```
- **Response**:
```json
{
  "equationId": "1",
  "equation": "3x + 2y - z",
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  },
  "result": 11
}
```

## Setup and Running

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build and Run

1. Clone the repository
2. Build the project:
```bash
mvn clean install
```
3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

### Unit Tests

Run the unit tests with:
```bash
mvn test
```

### Testing with Postman

1. Import the provided Postman collection (if available)
2. Or create new requests to test the API endpoints

Example Postman requests:

1. Store an equation:
   - POST `http://localhost:8080/api/equations/store`
   - Body: `{ "equation": "3x + 2y - z" }`

2. Get all equations:
   - GET `http://localhost:8080/api/equations`

3. Evaluate an equation:
   - POST `http://localhost:8080/api/equations/1/evaluate`
   - Body: `{ "variables": { "x": 2, "y": 3, "z": 1 } }`

## Error Handling

The application provides appropriate error responses for:
- Invalid equation syntax
- Missing variables during evaluation
- Division by zero
- Non-existent equation IDs

## Design Patterns Used

- **Factory Pattern**: For creating expression trees
- **Strategy Pattern**: For evaluating different types of operations
- **MVC Pattern**: Separation of concerns with Controllers, Services, and Models
- **DTO Pattern**: For request/response data transfer 