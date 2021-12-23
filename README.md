# Employees management system back-end API

A system to manage employee on-boarding

## Prerequisites

- Docker
- JDK 11 (If a local build and deploy required)
- Maven 3+ (If a local build and deploy required)
- Postman (In order to test the system behaviour)

## Tech & Spec
- Language: Java 11
- Runtime: Tomcat (embedded)
- Spring 5
- H2 in-memory database
- Spring state machine
- Spring JPA

## Build or Run

### Using docker
```bash
git clone git@github.com:manish2aug/emp-mgmt.git
cd emp-mgmt
docker build -t emp-mgmt .
docker run -p 8081:8081 emp-mgmt
```
### Building and running locally
```bash
git clone git@github.com:manish2aug/emp-mgmt.git
cd emp-mgmt
mvn spring-boot:run
```
### Fat jar
```bash
git clone git@github.com:manish2aug/emp-mgmt.git
cd emp-mgmt
mvn clean install
java -jar target/*.jar
```

## Testing or accessing API
1. Launch postman 
2. File -> import -> Select "Link" tab and enter openApi doc url http://localhost:8081/v3/api-docs (if running with defaults)
3. Click "continue" and then "Import", it would create a new collection named "Employee On-boarding API" with all existing exposed APIs

## Access in-memory database
If running with all defaults shipped with application, db can be accessed on the following url

http://localhost:8081/h2

## Documentation
- [Swagger UI](http://localhost:8080/swagger)
- [OPEN API DOC](http://localhost:8081/v3/api-docs)

