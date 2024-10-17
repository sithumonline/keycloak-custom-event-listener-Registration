# # Stage 1: Build the JAR file using Maven
FROM maven:3.8.4-openjdk-11 AS build

# # Set the working directory inside the container
WORKDIR /app

# # Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# # Build the JAR file
RUN mvn clean package

# Stage 2: Use the Keycloak image and copy the built JAR file
FROM quay.io/keycloak/keycloak:26.0

# Copy the JAR file from the build stage
COPY --from=build /app/target/custom-event-listener.jar /opt/keycloak/providers
# COPY custom-event-listener.jar /opt/keycloak/providers

# Copy the test data
COPY testdata/gocloak-realm.json /opt/keycloak/data/import/gocloak-realm.json

# Copy the script

RUN /opt/keycloak/bin/kc.sh build --features="preview"

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--optimized"]

# Expose the ports
EXPOSE 8080
