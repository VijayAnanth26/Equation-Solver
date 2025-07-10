# Equation Solver

A Spring Boot Web Application for Storing and Evaluating Algebraic Equations Using a Postfix Tree.

## Overview

This application provides a RESTful API for managing algebraic equations:

1. **Store Algebraic Equations**: Parse and save equations in a postfix (Reverse Polish Notation) tree structure.
2. **Retrieve Stored Equations**: Fetch a list of stored equations reconstructed from the postfix tree.
3. **Evaluate Equations**: Substitute provided variable values and calculate the result using the postfix tree.

## Technical Implementation

### Expression Tree (Postfix Tree)
The application uses a postfix expression tree to represent and evaluate algebraic equations:

- **Tree Structure**: Each operator node has two children (operands), and leaf nodes represent variables or constants.
- **Parsing Process**:
  1. Convert infix equation (e.g., "3x + 2y") to postfix notation (e.g., "3x 2y +")
  2. Build the expression tree by processing the postfix notation
  3. Operators become parent nodes, operands become child nodes

### Equation Parsing
- **Tokenization**: Breaks the equation into tokens (numbers, variables, operators)
- **Infix to Postfix**: Uses the Shunting Yard algorithm to convert infix to postfix notation
- **Validation**: Performs syntax validation including:
  - Balanced parentheses
  - Valid characters
  - Proper operator usage
  - No empty expressions

### Storage and Evaluation
- **In-Memory Storage**: Stores equations in an in-memory map with unique IDs
- **Evaluation**: Traverses the expression tree, substituting variables with their values
- **Variable Handling**: Supports variables and coefficients (e.g., "3x" is recognized as "3 * x")

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
2. Navigate to the project directory
3. Build the project:
```bash
mvn clean install
```
4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing with Postman

You can test the API endpoints using Postman:

1. **Store an Equation**:
   - Method: POST
   - URL: `http://localhost:8080/api/equations/store`
   - Headers: `Content-Type: application/json`
   - Body:
     ```json
     {
       "equation": "3x + 2y - z"
     }
     ```

2. **Get All Equations**:
   - Method: GET
   - URL: `http://localhost:8080/api/equations`

3. **Evaluate an Equation**:
   - Method: POST
   - URL: `http://localhost:8080/api/equations/1/evaluate`
   - Headers: `Content-Type: application/json`
   - Body:
     ```json
     {
       "variables": {
         "x": 2,
         "y": 3,
         "z": 1
       }
     }
     ```

## Error Handling

The application provides appropriate error responses for:
- **Invalid equation syntax**: Unbalanced parentheses, invalid characters, etc.
- **Missing variables**: When required variables are not provided during evaluation
- **Division by zero**: When evaluation would result in division by zero
- **Non-existent equation IDs**: When trying to access an equation that doesn't exist

Error responses follow a consistent format:
```json
{
  "error": "Error message describing the issue"
}
```

## Design Patterns Used

- **MVC Pattern**: Separation of concerns with Controllers, Services, and Models
- **DTO Pattern**: For request/response data transfer
- **Singleton Pattern**: For managing the equation storage 