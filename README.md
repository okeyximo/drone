Sure, if you are using Maven, Java, and Spring Boot for your project, I can provide a modified version of the README file tailored to that technology stack.

# Drone Service (Java Spring Boot)

This project implements a REST API service for managing a fleet of drones used for delivering medications. The service provides functionalities such as registering a drone, loading medications onto a drone, checking loaded medications for a given drone, checking available drones for loading, and checking the drone's battery level.

## Build Instructions

### Prerequisites

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) installed
- [Maven](https://maven.apache.org/download.cgi) installed
- [Docker](https://www.docker.com/) (optional, for running the database in a container)

### Steps

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/drone-service.git
    ```

2. Navigate to the project directory:

    ```bash
    cd drone-service
    ```

3. Set up the database:

    - If using Docker, you can run a MongoDB container with the following command:

      ```bash
      docker run -d -p 27017:27017 --name drone-db mongo
      ```

      Make sure to update the database connection configuration in the `application.properties` file if needed.

4. Build the application:

    ```bash
    mvn clean install
    ```

5. Run the application:

    ```bash
    java -jar target/drone-service-1.0.jar
    ```

   The API server should now be running.

## API Endpoints

### 1. Register a Drone

- **Endpoint:** `POST /drones`
- **Request Body:**
  ```json
  {
    "serialNumber": "ABC123",
    "model": "Lightweight",
    "weightLimit": 500,
    "batteryCapacity": 80
  }
  ```
- **Response:**
  ```json
  {
    "message": "Drone registered successfully",
    "drone": {
      "serialNumber": "ABC123",
      "model": "Lightweight",
      "weightLimit": 500,
      "batteryCapacity": 80,
      "state": "IDLE"
    }
  }
  ```

### 2. Load Medications onto a Drone

- **Endpoint:** `POST /drones/{serialNumber}/load`
- **Request Body:**
  ```json
  {
    "medications": [
      {
        "name": "Medication1",
        "weight": 100,
        "code": "M123",
        "image": "medication1.jpg"
      },
      {
        "name": "Medication2",
        "weight": 150,
        "code": "M456",
        "image": "medication2.jpg"
      }
    ]
  }
  ```
- **Response:**
  ```json
  {
    "message": "Medications loaded successfully",
    "drone": {
      "serialNumber": "ABC123",
      "loadedMedications": [
        {
          "name": "Medication1",
          "weight": 100,
          "code": "M123",
          "image": "medication1.jpg"
        },
        {
          "name": "Medication2",
          "weight": 150,
          "code": "M456",
          "image": "medication2.jpg"
        }
      ]
    }
  }
  ```

### 3. Check Loaded Medications for a Given Drone

- **Endpoint:** `GET /drones/{serialNumber}/medications`
- **Response:**
  ```json
  {
    "drone": {
      "serialNumber": "ABC123",
      "loadedMedications": [
        {
          "name": "Medication1",
          "weight": 100,
          "code": "M123",
          "image": "medication1.jpg"
        },
        {
          "name": "Medication2",
          "weight": 150,
          "code": "M456",
          "image": "medication2.jpg"
        }
      ]
    }
  }
  ```

### 4. Check Available Drones for Loading

- **Endpoint:** `GET /drones/available`
- **Response:**
  ```json
  {
    "drones": [
      {
        "serialNumber": "ABC123",
        "model": "Lightweight",
        "weightLimit": 500,
        "batteryCapacity": 80,
        "state": "IDLE"
      },
      // ... other available drones
    ]
  }
  ```

### 5. Check Drone Battery Level

- **Endpoint:** `GET /drones/{serialNumber}/battery`
- **Response:**
  ```json
  {
    "drone": {
      "serialNumber": "ABC123",
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

- The periodic task for checking drone battery levels and creating a history/audit event log is implemented as a background job using a scheduler.
- The application uses [Spring Boot](https://spring.io/projects/spring-boot) framework and [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) for MongoDB integration.
- Input and output data for API requests are in JSON format.

Feel free to reach out if you have any questions or need further assistance!
