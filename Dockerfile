# Stage 1: (No longer needed to build here, Maven task handles it)

# Stage 2: Create the final image
FROM registry.access.redhat.com/ubi8/openjdk-21-runtime
WORKDIR /work/
# Copy the built application from the source workspace (where Maven placed it)

COPY target/account-management-service-1.0.0-SNAPSHOT-runner.jar /work/application.jar

# Expose the port Quarkus listens on (default is 8080)
EXPOSE 8080
# Command to run the Quarkus application
CMD ["java", "-jar", "application.jar"]