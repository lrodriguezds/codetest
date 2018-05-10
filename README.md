# MAvha code test

A Spring Boot restful api for MAvha code test

### Technical test
[Link to document](https://docs.google.com/document/d/1hR-ri1I2wK9K2t8xDi1wN-jVBu7NYymtPZtWqhxiz0M)

### Technology stack:
* Spring Boot
* Spring Data JPA
* HSQLDB
* Junit
* Apache Tomcat (embedded)


## Getting Started

### Prerequisites

* JRE 1.8
* Maven (for development)


## Build
To build the application, run:

```
$ ./mvnw package
```
## Run
To launch the application, run:

```
$ java -jar target/codetest-0.0.1-SNAPSHOT.jar
```


## Testing
To launch the application tests, run:

```
$ ./mvnw clean test
```


## Example payloads
Creating a person

```
curl -d '{"dni":31734206, "firstname": "Leandro", "lastname": "Rodriguez", "age": 32}' -H "Content-Type: application/json" -X POST http://localhost:8080/personas
```

Fetching people

```
curl http://localhost:8080/personas
```

