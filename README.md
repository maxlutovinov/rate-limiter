# Rate Limiter

<!-- TOC -->
* [Rate Limiter](#rate-limiter)
  * [Description](#description)
  * [Features](#features)
  * [Structure](#structure)
  * [Technology stack](#technology-stack)
  * [Usage](#usage)
    * [Clone](#clone)
    * [Build and run on the command line](#build-and-run-on-the-command-line)
    * [Run in IntelliJ IDEA](#run-in-intellij-idea)
    * [Use](#use)
<!-- TOC -->

## Description

This is a Java implementation of the rate limiter using token bucket algorithm for incoming HTTP requests
without using third party dependencies.
The primary purpose of the project is to explore how this algorithm functions.

## Features

Performs rate limiting of the incoming HTTP requests to URL (`GET: /`) with a configurable number of requests
per time period specified in [application.properties](src/main/resources/application.properties) per individual
client IP address.

The [test](src/test/java/app/ratelimiter/service/impl/TokenBucketRateLimiterTest.java) simulates attempts
different users with unique keys to acquire the access tokens.

## Structure

The project has a layered system architecture that includes the following layers of functionality:

- [Filter](src/main/java/app/ratelimiter/filter) filters access to specific endpoints by limiting the number of requests
  per period from a unique IP address.
- [Controller](src/main/java/app/ratelimiter/controller) processes HTTP requests sent from the user interface and
  responds to the user.
- [Service](src/main/java/app/ratelimiter/service) is responsible for the business logic of rate limiting.
- [Model](src/main/java/app/ratelimiter/model) keeps the state of rate limiting.

## Technology stack

Java 21, Spring Boot, Maven, Lombok.

## Usage

### Clone

    git clone https://github.com/maxlutovinov/rate-limiter.git

### Build and run on the command line

    ./mvnw package

If this fails, install Maven and execute the following build command:

    mvn package

Then run the jar:

    java -jar target/rate-limiter-0.0.1-SNAPSHOT.jar

### Run in IntelliJ IDEA

Alternatively, open the project in IntelliJ IDEA and
run [RateLimiterApplication](src/main/java/app/ratelimiter/RateLimiterApplication.java).

### Use

Once you launch the app, open the url (http://localhost:8080/) in a browser and reload the page with different
frequency to get rate limiting of HTTP requests and the result status on the page - "Success" or "Too many requests per
period".
