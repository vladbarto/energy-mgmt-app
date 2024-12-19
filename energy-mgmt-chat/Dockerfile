# Start with a base image that includes Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from target directory into the container
COPY target/energy_mgmt_chat-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 6587

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
