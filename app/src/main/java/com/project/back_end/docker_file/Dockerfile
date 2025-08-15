# -----------------------
# Stage 1: Build the app
# -----------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy Maven descriptor and resolve dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# -----------------------
# Stage 2: Run the app
# -----------------------
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/clinic-management-system-*.jar app.jar

# Expose the application port (adjust if not 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
