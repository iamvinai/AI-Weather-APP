Here's a structured README file based on the provided information:

---

# WeatherApp

WeatherApp is a Spring Boot application with a Vaadin frontend that provides weather information for various cities. This project demonstrates how to create a modern web application using Spring Boot and Vaadin.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- Java 11 or higher
- Maven 3.6.0 or higher
- Node.js and npm (for frontend development)

### Installation

Follow these steps to set up and run the WeatherApp application:

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/weatherapp.git
   cd weatherapp
   ```

2. **Build the project:**
   ```sh
   mvn clean install
   ```

3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

### Usage

Once the application is running, open your web browser and navigate to [http://localhost:8080](http://localhost:8080). You will see the main view where you can enter a city name and select the temperature unit (Celsius or Fahrenheit) to get the current weather information.

### Project Structure

- `src/main/java/com/weatherapp/weatherapp/WeatherappApplication.java`: The main class for the Spring Boot application.
- `src/main/java/com/weatherapp/weatherapp/views/HomeView.java`: The main view of the application using Vaadin components.
- `src/main/resources/application.properties`: Configuration properties for the Spring Boot application.
- `src/test/java/com/weatherapp/weatherapp/WeatherappApplicationTests.java`: Test class to ensure the application context loads successfully.

### Contributing

If you would like to contribute to this project, please fork the repository and submit a pull request with your changes.

### License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Feel free to customize this README with additional details or instructions specific to your project.
