# Stage 1: Build the application
FROM maven:3.8.4-jdk-11 as builder

# Copy the project files to the container
COPY . /app

# Set the working directory
WORKDIR /app

# Compile and package the application
RUN mvn clean package

# Stage 2: Set up Payara Server
FROM payara/server-full:5.2022.5-jdk11

# Copy the WAR file from the builder stage
COPY --from=builder /app/target/*.war $DEPLOY_DIR

EXPOSE 8080 4848

# Optional: If you have post-boot commands to set up data sources
COPY post-boot-commands.asadmin $PREBOOT_COMMANDS

COPY postgresql-42.7.3.jar  .

# The default command runs Payara Server in the foreground