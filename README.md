# Spring Boot Microservices and Spring Cloud

## Tutorial on Microservices from Sergey Kargopolov (Udemy)

#### Section 9: Spring Cloud API Gateway

#####  61. Automatic Mapping of Gateway Routes

-  curl `http://localhost:8011/USERS-WS/users/status/check` -> Working
-  curl `http://192.168.99.1:8011/USERS-WS/actuator/info` - **CAPITAL LETTERS**
```json
{
  "app": {
    "name": "photo-app-api-users",
    "version": "0.0.1-SNAPSHOT",
    "encoding": "UTF-8"
  },
  "service": {
    "name": "users-ws"
  }
}
```
-  fix this
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          lower-case-service-id: true 
```
-  curl `http://192.168.99.1:8011/account-management-ws/actuator/info`
```json
{"app":{"name":"account-management-service","version":"0.0.1-SNAPSHOT","encoding":"UTF-8"},"service":{"name":"account-management-ws"}}
```

####  Section 10: Spring Cloud API Gateway as a Load Balancer

#####  68. Starting Up More Microservices

-  start 2 instances of `users-service`
-  start another instance by using command line arguments
    -  `mvn spring-boot:run -Dspring-boot.run.arguments=--spring.application.instance_id=art`
    -  **and one more** with port
    -  `mvn spring-boot:run -Dspring-boot.run.arguments="'--spring.application.instance_id=art2' '--server.port=8123'"`
    -  **OR**
    -  `mvn spring-boot:run -Dspring-boot.run.arguments="--spring.application.instance_id=art3 --PORT=8321"`

#####  72. Adding Support for the H2 Database

just using H2-console to operate MySQL Database
1.  Start services  
    -  start eureka
    -  start user service
    -  start API gateway
2.  Login to h2-console    
    -  browse to `localhost:8011/users-ws/h2-console`
    -  Setting Name: `Generic MySQL` -> use DB from previous project
    -  JDBC URL: `jdbc:mysql://localhost:3306/beerservice?serverTimezone=UTC`
    -  User name: `beer_service`
    -  Password: `password`
    -  Test connection
        -  Got an Error
            -  `Класс "com.mysql.jdbc.Driver" не найден`
            -  `Class "com.mysql.jdbc.Driver" not found [90086-200]`    
        -  Need to add MySQL to classpath -> **just skip this test for now**
3.  Trying Generic H2 in-memory Database
    -  JDBC URL: `jdbc:h2:mem:testdb`
    -  Test connection
        -  Got an Error
        -  `Database "mem:testdb" not found, either pre-create it or allow remote database creation (not recommended in secure environments) [90149-200]`
4.  Add Spring Data JPA
    -  Test it -> OK        

####  Section 13: Users Microservice - Implementing User Login

#####  95. Implementing loadUserByUserName()

-  testing
    -  create user first
```shell script
curl --location --request POST 'http://localhost:8011/users-ws/users' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{    
    "firstName": "Art",
    "lastName": "Shyshkin",
    "email": "myemail@example.com",
    "password": "my super secret password with 1 number and A capital letter"
}'
```
    -  test present user -> Status OK 200
```shell script
curl --location --request POST 'http://localhost:8011/users-ws/login' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "myemail@example.com",
    "password": "my super secret password with 1 number and A capital letter"
}'
```    
    -  test absent user -> Status UNAUTHORIZED 401
```shell script
curl --location --request POST 'http://localhost:8011/users-ws/login' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "myemailAbsent@example.com",
    "password": "my super secret password with 1 number and A capital letter"
}'
```
```json
{
    "timestamp": "2021-02-24T17:03:22.347+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/login"
}
```        

####  Section 15: Spring Cloud API Gateway - Creating a Custom Filter.

#####  107. Using Header Predicate

-  curl users-ws status check endpoint through gateway
```shell script
curl --location --request GET 'http://localhost:8011/users-ws/users/status/check' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer 123AnyNum'
```
-  got a response 200 OK
    -  users-ws is running on port: 51997
-  curl without `Authorization` header -> 404 Error

#####  114. Accessing Protected Microservices with Access Token
    
-  create new user
```shell script
curl --location --request POST 'http://localhost:8011/users-ws/users' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{    
    "firstName": "Art",
    "lastName": "Shyshkin",
    "email": "myemail@example.com",
    "password": "my super secret password with 1 number and A capital letter"
}'
```
-  get JSON Web Token by login
```shell script
curl --location --request POST 'http://localhost:8011/users-ws/users/login' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "myemail@example.com",
    "password": "my super secret password with 1 number and A capital letter"
}'
```
-  token is `eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDg5OTIxMS02NmM4LTRhN2YtOTc5Ni1mYTBkNGVmZjRhMWYiLCJleHAiOjE2MTUxNDk2NzN9.GJDV2PAWMnQDtc_BfqqzhQsqTXu_J14v10P786NwWeQiW7nUmTM4hW2VqhSacIKLe6MJMlcBgG11wOg1AFfVDA` 
-  curl status endpoint
```shell script
curl --location --request GET 'http://localhost:8011/users-ws/users/status/check' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDg5OTIxMS02NmM4LTRhN2YtOTc5Ni1mYTBkNGVmZjRhMWYiLCJleHAiOjE2MTUxNDk2NzN9.GJDV2PAWMnQDtc_BfqqzhQsqTXu_J14v10P786NwWeQiW7nUmTM4hW2VqhSacIKLe6MJMlcBgG11wOg1AFfVDA'
```

####  Section 17: Spring Cloud Config Server - Git Backend

#####  126. Create Private GitHub Repository

-  create **private** Git repository `art-kargopolov-microservices-photo-app-repository`    