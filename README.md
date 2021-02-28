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

#####  129. Adding Properties File to Git Repository

-  GET `localhost:8012/all/default` -> receive global config
```json
{
  "name": "all",
  "profiles": [
    "default"
  ],
  "label": null,
  "version": "5c40e68d8952de18f4202a1e355f4c751c04cafd",
  "state": null,
  "propertySources": []
}
```
-  added `application.yml` file into `art-kargopolov-microservices-photo-app-repository`
-  modified token expiration time
-  push `art-kargopolov-microservices-photo-app-repository`
-  start `PhotoAppApiConfigServerApplication`
-  GET `localhost:8012/all/default` -> receive global config
```json
{
  "name": "all",
  "profiles": [
    "default"
  ],
  "label": null,
  "version": "84c34699277485e67d2ab8ea2ea420607fd67f0f",
  "state": null,
  "propertySources": [
    {
      "name": "https://github.com/artshishkin/art-kargopolov-microservices-photo-app-repository/file:C:\\Users\\Admin\\AppData\\Local\\Temp\\config-repo-7475044306187383547\\application.yml",
      "source": {
        "gateway.ip": "192.168.99.1",
        "token.expiration_time": 86400000,
        "token.secret": "blablabla",
        "login.url.path": "/users/login"
      }
    }
  ]
}
```

#####  135. Download and Run Rabbit MQ

I use Docker to run RabbitMQ with management console
-  `docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 rabbitmq:3-management`
-  curl to `localhost:15672`
    -  username: `guest`
    -  password: `guest`

#####  136. Rabbit MQ Default Connection Details

For connection we need port 5672 also:
-  first remove running container
    -  `docker container rm some-rabbit -f`       
-  then start new container
    -  `docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management`

#####  137. Trying how Spring Cloud Bus Works

-  start all services
-  create new user (POST through gateway to `users-ws`)  
-  log in -> copy JWT
-  curl to status check endpoint -> view tokenSecret
-  POST to http://localhost:8012/actuator/busrefresh with empty body
-  curl to status check endpoint -> view tokenSecret
    -  `users-ws is running on port: 61916. Token secret: modifiedTokenSecret`  

#####  138. Change default Rabbit MQ Password

-  Approach 1:
    -  `docker run -d --hostname my-rabbit --name some-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password  -p 15672:15672 -p 5672:5672 rabbitmq:3-management`
-  Approach 2:
    -  log in into management console: localhost:15672
    -  Admin -> Add a user
        -  Name: art
        -  Password: 123
        -  Role: administrator
        -  Add user
    -  Set up permissions for this user
        -  Click on name
        -  Choose permissions
        -  Update user
    -  Log out
    -  Log in as new user
    
#####  141. Previewing Values Returned by Config Server

-  start config server app
-  curl to
-  `http://localhost:8012/all/default`
-  `http://localhost:8012/all/native`
-  `http://localhost:8012/config-server/native`
-  `http://localhost:8012/any-app/any-profile`
```json
{
    "name": "any-app",
    "profiles": [
        "any-profile"
    ],
    "label": null,
    "version": null,
    "state": null,
    "propertySources": [
        {
            "name": "file:C:\\Users\\Admin\\IdeaProjects\\Study\\SergeyKargopolov\\MicroservicesCourse\\art-kargopolov-microservices\\photo-app-api-config-native-repo\\application.yml",
            "source": {
                "gateway.ip": "192.168.99.1",
                "token.expiration_time": 86400000,
                "token.secret": "modifiedTokenSecretFromNativeRepository1",
                "login.url.path": "/users/login"
            }
        }
    ]
}
```
**We have ONLY common application properties file now.** So it is included into all the apps and profiles.

####  Section 20: Spring Cloud Config - Configuration for Multiple Microservices

#####  144. Shared and a Microservice specific properties

-  go to http://localhost:8010/
-  view users-ws info endpoint
    -  "settings":"Settings from local repository OK users-ws yaml"
-  modify users-ws.yml settings field
-  POST to http://localhost:8012/actuator/busrefresh
-  view users-ws info endpoint
    -  "settings":"Settings from local repository UPDATED users-ws yaml"
-  view users-ws config
    -  localhost:8012/users-ws/default    
       
####  Section 21: Spring Boot Actuator - A Quick Start

#####  146. Add Spring Boot Actuator to API Gateway

-  `http://192.168.99.1:8011/actuator/gateway/globalfilters`
```json
{
  "net.shyshkin.study.photoapp.api.gateway.MyPreFilter@6f9323e2": 0,
  "net.shyshkin.study.photoapp.api.gateway.GlobalFilterConfiguration$$Lambda$563/0x0000000800564c40@44791e7a": null,
  "net.shyshkin.study.photoapp.api.gateway.GlobalFilterConfiguration$$Lambda$562/0x0000000800564840@67b4a00d": null,
  "org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter@4d525ac6": 10150,
  "net.shyshkin.study.photoapp.api.gateway.MyPostFilter@ed8ef21": 0,
  "org.springframework.cloud.gateway.filter.ForwardPathFilter@11bddaff": 0,
  "net.shyshkin.study.photoapp.api.gateway.GlobalFilterConfiguration$$Lambda$561/0x0000000800565440@1c0dc436": null,
  "org.springframework.cloud.gateway.filter.NettyRoutingFilter@4e4529fe": 2147483647,
  "org.springframework.cloud.gateway.filter.RemoveCachedBodyFilter@29146dd4": -2147483648,
  "org.springframework.cloud.gateway.filter.ForwardRoutingFilter@65fa9289": 2147483647,
  "net.shyshkin.study.photoapp.api.gateway.GlobalFilterConfiguration$1@358ef294": null,
  "org.springframework.cloud.gateway.filter.GatewayMetricsFilter@6b027981": 0,
  "org.springframework.cloud.gateway.filter.NettyWriteResponseFilter@1df78d75": -1,
  "org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter@1a4ce3f0": 10000,
  "org.springframework.cloud.gateway.filter.WebsocketRoutingFilter@1e2b78d7": 2147483646,
  "org.springframework.cloud.gateway.filter.AdaptCachedBodyGlobalFilter@55ecd0e6": -2147482648
}
```
-  `http://192.168.99.1:8011/actuator/gateway/routefilters`
```json
{
  "[AddResponseHeaderGatewayFilterFactory@51905668 configClass = AbstractNameValueGatewayFilterFactory.NameValueConfig]": null,
  "[PrefixPathGatewayFilterFactory@2cf7ef55 configClass = PrefixPathGatewayFilterFactory.Config]": null,
  "[RewriteLocationResponseHeaderGatewayFilterFactory@26155cb2 configClass = RewriteLocationResponseHeaderGatewayFilterFactory.Config]": null,
  "[AuthorizationHeaderFilter@201494b8 configClass = AuthorizationHeaderFilter.Config]": null,
  "[AddRequestParameterGatewayFilterFactory@7f9796fb configClass = AbstractNameValueGatewayFilterFactory.NameValueConfig]": null,
  "[ModifyRequestBodyGatewayFilterFactory@873eaa5 configClass = ModifyRequestBodyGatewayFilterFactory.Config]": null,
  "[SetResponseHeaderGatewayFilterFactory@5a0d84c configClass = AbstractNameValueGatewayFilterFactory.NameValueConfig]": null,
  "[RequestHeaderSizeGatewayFilterFactory@7a3fa9d8 configClass = RequestHeaderSizeGatewayFilterFactory.Config]": null,
  "[RequestSizeGatewayFilterFactory@3be784db configClass = RequestSizeGatewayFilterFactory.RequestSizeConfig]": null,
  "[ModifyResponseBodyGatewayFilterFactory@41a018b3 configClass = ModifyResponseBodyGatewayFilterFactory.Config]": null,
  "[SetPathGatewayFilterFactory@6bb14bee configClass = SetPathGatewayFilterFactory.Config]": null,
  "[DedupeResponseHeaderGatewayFilterFactory@43f6513 configClass = DedupeResponseHeaderGatewayFilterFactory.Config]": null,
  "[RemoveRequestParameterGatewayFilterFactory@661cb690 configClass = AbstractGatewayFilterFactory.NameConfig]": null,
  "[RemoveResponseHeaderGatewayFilterFactory@16381bca configClass = AbstractGatewayFilterFactory.NameConfig]": null,
  "[SetRequestHostHeaderGatewayFilterFactory@d7a694e configClass = SetRequestHostHeaderGatewayFilterFactory.Config]": null,
  "[RetryGatewayFilterFactory@15344ef6 configClass = RetryGatewayFilterFactory.RetryConfig]": null,
  "[SpringCloudCircuitBreakerResilience4JFilterFactory@3b78ccad configClass = SpringCloudCircuitBreakerFilterFactory.Config]": null,
  "[SetRequestHeaderGatewayFilterFactory@3f3efee6 configClass = AbstractNameValueGatewayFilterFactory.NameValueConfig]": null,
  "[FallbackHeadersGatewayFilterFactory@3e911756 configClass = FallbackHeadersGatewayFilterFactory.Config]": null,
  "[SaveSessionGatewayFilterFactory@2a529a28 configClass = Object]": null,
  "[RewriteResponseHeaderGatewayFilterFactory@7e15d44a configClass = RewriteResponseHeaderGatewayFilterFactory.Config]": null,
  "[RemoveRequestHeaderGatewayFilterFactory@1c03049b configClass = AbstractGatewayFilterFactory.NameConfig]": null,
  "[StripPrefixGatewayFilterFactory@22a8d0d5 configClass = StripPrefixGatewayFilterFactory.Config]": null,
  "[PreserveHostHeaderGatewayFilterFactory@b2861eb configClass = Object]": null,
  "[RewritePathGatewayFilterFactory@10e60330 configClass = RewritePathGatewayFilterFactory.Config]": null,
  "[SecureHeadersGatewayFilterFactory@37a21cad configClass = SecureHeadersGatewayFilterFactory.Config]": null,
  "[RequestHeaderToRequestUriGatewayFilterFactory@5edcc5af configClass = AbstractGatewayFilterFactory.NameConfig]": null,
  "[MapRequestHeaderGatewayFilterFactory@5b258dcf configClass = MapRequestHeaderGatewayFilterFactory.Config]": null,
  "[SetStatusGatewayFilterFactory@2a3acfc6 configClass = SetStatusGatewayFilterFactory.Config]": null,
  "[AddRequestHeaderGatewayFilterFactory@506c2d8 configClass = AbstractNameValueGatewayFilterFactory.NameValueConfig]": null,
  "[RedirectToGatewayFilterFactory@2cfb85fc configClass = RedirectToGatewayFilterFactory.Config]": null
}
```
-  `http://192.168.99.1:8011/actuator/gateway/routes`
```json
[
  {
    "predicate": "((Paths: [/users-ws/users/status/check], match trailing slash: true && Methods: [GET]) && Header: Authorization regexp=Bearer (.*))",
    "route_id": "users-status-check",
    "filters": [
      "[[RemoveRequestHeader name = 'Cookie'], order = 1]",
      "[[RewritePath /users-ws/(?<segment>/?.*) = '/${segment}'], order = 2]",
      "[net.shyshkin.study.photoapp.api.gateway.AuthorizationHeaderFilter$1@72011d38, order = 3]"
    ],
    "uri": "lb://users-ws",
    "order": 0
  },
  {
    "predicate": "(Paths: [/users-ws/users], match trailing slash: true && Methods: [POST])",
    "route_id": "users-ws",
    "filters": [
      "[[RemoveRequestHeader name = 'Cookie'], order = 1]",
      "[[RewritePath /users-ws/(?<segment>/?.*) = '/${segment}'], order = 2]"
    ],
    "uri": "lb://users-ws",
    "order": 0
  },
  {
    "predicate": "(Paths: [/users-ws/users/login], match trailing slash: true && Methods: [POST])",
    "route_id": "users-ws-login",
    "filters": [
      "[[RemoveRequestHeader name = 'Cookie'], order = 1]",
      "[[RewritePath /users-ws/(?<segment>/?.*) = '/${segment}'], order = 2]"
    ],
    "uri": "lb://users-ws",
    "order": 0
  },
  {
    "predicate": "((Paths: [/users-ws/users/**], match trailing slash: true && Methods: [GET, PUT, DELETE]) && Header: Authorization regexp=Bearer (.*))",
    "route_id": "users-ws-get-update-delete",
    "filters": [
      "[[RemoveRequestHeader name = 'Cookie'], order = 1]",
      "[[RewritePath /users-ws/(?<segment>/?.*) = '/${segment}'], order = 2]",
      "[net.shyshkin.study.photoapp.api.gateway.AuthorizationHeaderFilter$1@5bc0d5fe, order = 3]"
    ],
    "uri": "lb://users-ws",
    "order": 0
  }
]
```
-  curl to `http://192.168.99.1:8011/actuator/httptrace` twice
```json
{
  "traces": [
    {
      "timestamp": "2021-02-28T14:23:13.119005200Z",
      "principal": null,
      "session": null,
      "request": {
        "method": "GET",
        "uri": "http://192.168.99.1:8011/actuator/httptrace",
        "headers": {
          "Cache-Control": [
            "max-age=0"
          ],
          "Accept": [
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
          ],
          "Upgrade-Insecure-Requests": [
            "1"
          ],
          "Connection": [
            "keep-alive"
          ],
          "User-Agent": [
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36"
          ],
          "Host": [
            "192.168.99.1:8011"
          ],
          "Accept-Encoding": [
            "gzip, deflate"
          ],
          "Accept-Language": [
            "en-US,en;q=0.9,uk;q=0.8,ru;q=0.7"
          ]
        },
        "remoteAddress": null
      },
      "response": {
        "status": 200,
        "headers": {
          "transfer-encoding": [
            "chunked"
          ],
          "Content-Length": [
            "13"
          ],
          "Content-Type": [
            "application/vnd.spring-boot.actuator.v3+json"
          ]
        }
      },
      "timeTaken": 97
    }
  ]
}
```

####  Section 22: Using MySQL Instead of In-Memory Database

#####  150. Download and Install MySQL

I use Docker to start MySQL server (even though I have MySQL installed)
-  `docker run --name photo-app-api -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_USER=photo_app_user -e MYSQL_PASSWORD=photo_app_password -e MYSQL_DATABASE=photo_app_db -v photo-app-volume:/var/lib/mysql -p 23306:3306 -d mysql:8.0.23`
    -  I have port 3306 is busy so I choose to use 23306
    
#####  157. Use H2 Console to Access MySQL Database

-  go through gateway
    -  localhost:8011/users-ws/h2-console
-  use settings for MySQL connection

       