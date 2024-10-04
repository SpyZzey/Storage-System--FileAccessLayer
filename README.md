# Dynamic Storage System
This repository is part of a dynamic storage system, representing the FILE ACCESS Layer.
The USER ACCESS Layer can be found here: [User Access Layer](https://github.com/SpyZzey/Storage-System--UserAccessLayer)

Note: README is not finished...

## Features
Buckets, Directories and Files
- Support for common object storage structures used by hyperscalers like AWS S3.
Scalable Service
- Add new servers to increase storage and request capacities
File Encryption
- Files are encrypted with a user specific secret key
Access Control (WIP)
- Grant certain access permissions to specific users and buckets
Redundancy (WIP)
- Store files redundantly to improve reliability and data integrity

# Prerequisites
- Java 20
- Maven 3.6.3
- MySQL 8.0.23

# Installation
Please clone the repository and rename the file `example.application.properties` to `application.properties` and
adjust the values to your needs (e.g. database connection).

You can start the application with the following command:
`mvn spring-boot:run` if you have Maven installed or `./mvnw spring-boot:run` if you don't have Maven installed.
