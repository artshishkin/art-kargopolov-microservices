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
    
