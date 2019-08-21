# Klass Subsets backend

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4712502e8cc241a88ae4b7914b8e9a60)](https://app.codacy.com/app/statisticsnorway/klass-subsets-backend?utm_source=github.com&utm_medium=referral&utm_content=statisticsnorway/klass-subsets-backend&utm_campaign=Badge_Grade_Settings)

### Development Requirements
*  Java 11
*  Ide with support for Lombok plugin (IntelliJ recommended) 

#### Note on Lombok
Lombok is a plugin that simplifies and handles a lot boilerplate code using annotations.
In this project we have used the `@Data` annotation heavily which automatically generates getters, setters, equals etc. for us.
For more info and a quick introduction see the 4 min intro video on [projectlombok.org](https://projectlombok.org/)

### Deployment
This application is intended to run as a self-contained java 11 application and service configuration and auto start must be configured on the server that the application will be deployed to.

Examples on how to set up a service can be found here [Create an Init Script](https://www.linode.com/docs/development/java/how-to-deploy-spring-boot-applications-nginx-ubuntu-16-04/#create-an-init-script)

and info on how to gracefully shutdown the application can be found here
[Gracefully killing a Java process managed by systemd](https://stegard.net/2016/08/gracefully-killing-a-java-process-managed-by-systemd/)

### Testing
If you run the application locally you will find documentation for the 2 APIs that the application provides on the following urls. 
(note that you need to build the application with maven for the documentation to be generated)

`http://localhost:8080/subsets/v1/view/api-guide.html` (view)

`http://localhost:8080/subsets/v1/edit/api-guide.html` (edit)

View API is intended for consumers that just need the data and has more consumer friendly endpoints.

Edit API is intended for other applications that want to write or update data and will (eventually) require authentication.

You can use `SubsetsApplication` to run the application in intelliJ, or run `TestSubsetsApplication` if you want some simple test data to be added during startup.

## Current state of the application
Most of the application is Work In Progress, the Edit API is mostly a proof of concept and is expected to change a lot, 
there are currently no security mechanisms protecting the data but authentication and login is on the road map.

The View API has currently very limited features but depending on demand it may be expanded to support some of the same 
features as Klass API provides for ClassificationSeries.
