Sure, if you are using Maven, Java, and Spring Boot for your project, I can provide a modified version of the README file tailored to that technology stack.

# Drone Service (Java Spring Boot)

This project implements a REST API service for managing a fleet of boxes used for delivering item. The service provides functionalities such as registering a box, loading item onto a box, checking loaded item for a given box, checking available boxes for loading, and checking the box's battery level.

## Build Instructions

### Prerequisites

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) installed
- [Maven](https://maven.apache.org/download.cgi) installed
- [Docker](https://www.docker.com/) (optional, for running the database in a container)

### Steps


1. Navigate to the project directory:

    ```bash
    cd box-service
    ```
   
2. Build the application:

    ```bash
    mvn clean install
    ```

3. Run the application:

    ```bash
    java -jar target/box-service-1.0.jar
    ```

   The API server should now be running.

## API Endpoints

### 1. Register a Box

- **Endpoint:** `POST /boxes`
- **Request Body:**
  ```json
  {
    "txRef": "ABC123",
    "model": "Lightweight",
    "weightLimit": 500,
    "batteryCapacity": 80
  }
  ```
- **Response:**
  ```json
  {
    "message": "Box registered successfully",
    "box": {
      "txRef": "ABC123",
      "model": "Lightweight",
      "weightLimit": 500,
      "batteryCapacity": 80,
      "state": "IDLE"
    }
  }
  ```

### 2. Load Item onto a Box

- **Endpoint:** `POST /boxes/{txRef}/load`
- **Request Body:**
  ```json
  {
    "item": [
      {
        "name": "Medication1",
        "weight": 100,
        "code": "M123",
        "image": "item1.jpg"
      },
      {
        "name": "Medication2",
        "weight": 150,
        "code": "M456",
        "image": "item2.jpg"
      }
    ]
  }
  ```
- **Response:**
  ```json
  {
    "message": "Item loaded successfully",
    "box": {
      "txRef": "ABC123",
      "loadedItems": [
        {
          "name": "Medication1",
          "weight": 100,
          "code": "M123",
          "image": "item1.jpg"
        },
        {
          "name": "Medication2",
          "weight": 150,
          "code": "M456",
          "image": "item2.jpg"
        }
      ]
    }
  }
  ```

### 3. Check Loaded Items for a Given Box

- **Endpoint:** `GET /boxes/{txRef}/item`
- **Response:**
  ```json
  {
    "box": {
      "txRef": "ABC123",
      "loadedItems": [
        {
          "name": "Medication1",
          "weight": 100,
          "code": "M123",
          "image": "item1.jpg"
        },
        {
          "name": "Medication2",
          "weight": 150,
          "code": "M456",
          "image": "item2.jpg"
        }
      ]
    }
  }
  ```

### 4. Check Available Boxs for Loading

- **Endpoint:** `GET /boxes/available`
- **Response:**
  ```json
  {
    "boxes": [
      {
        "txRef": "ABC123",
        "model": "Lightweight",
        "weightLimit": 500,
        "batteryCapacity": 80,
        "state": "IDLE"
      },
      // ... other available boxes
    ]
  }
  ```

### 5. Check Box Battery Level

- **Endpoint:** `GET /boxes/{txRef}/battery`
- **Response:**
  ```json
  {
    "box": {
      "txRef": "ABC123",
      "batteryLevel": 80
    }
  }
  ```

## Testing

To run unit tests:

```bash
mvn test
```

Make sure the application is not running while running the tests.

## Miscellaneous

- The application uses [Spring Boot](https://spring.io/projects/spring-boot) framework and H2 in-memory database for testing.
- Input and output data for API requests are in JSON format.

Feel free to reach out if you have any questions or need further assistance!
