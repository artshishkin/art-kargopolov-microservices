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

#### Section 23: Encryption and Decryption

#####  161. Symmetric Encryption of Properties

-  Encrypt 
    -  add `encrypt.key`
    -  curl config server
        -  POST `localhost:8012/encrypt`
        -  body `photo_app_password` (database password)
```shell script
curl --location --request POST 'http://localhost:8012/encrypt' \
--header 'Content-Type: application/json' \
--data-raw 'photo_app_password'
```    
    -  `550a97f979335f7b2b42d63218f46e3f290f098984b7bd1a2802f18099608f47d94612935859c70e17effa6f08262cdf`  
-  Decrypt
    -  curl config server
        -  POST `localhost:8012/decrypt`
        -  body `550a97f979335f7b2b42d63218f46e3f290f098984b7bd1a2802f18099608f47d94612935859c70e17effa6f08262cdf`
-  Return decrypted values
    -  modify properties files by using `{cipher}`
    -  curl http://localhost:8012/users-ws/mysql -> properties decrypted
-  Test errors in encryptipn
    -  if wrong encrypted value -> modify `token.secret` to have wrong password
        -  "invalid.token.secret": "<n/a>"
        -  java.lang.IllegalArgumentException: Detected a Non-hex character at 129 or 130 position    
    -  if encrypt.key different
        -  "invalid.spring.datasource.password": "<n/a>"
        -  "invalid.token.secret": "<n/a>"
        -  java.lang.IllegalStateException: Unable to invoke Cipher due to bad padding
        -  Caused by: javax.crypto.BadPaddingException: Given final block not properly padded. Such issues can arise if a bad key is used during decryption.

#####  162. Creating a Keystore for Asymmetric Encryption

-  use [encryption.ps1](encryption\encryption.ps1) for PowerShell
-  use [encryption.sh](encryption\encryption.sh) for Bash

####  Section 24: Microservices Communication

#####  177. Handle FeignException

-  temporarily change URL of Feign Client endpoint to produce 404 error
-  curl to get user
    -  `http://localhost:8011/users-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e`
-  got an error
    -  `"trace": "feign.FeignException$NotFound: [404] during [GET] to [http://albums-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums404] [AlbumsServiceClient#getUserAlbums(UUID)]:`    

#####  181. Trying How Resilience4j ~~Hystrix~~ Circuit Breaker & Feign work

-  start all without `albums-ws`
-  curl user `http://localhost:8011/users-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e`
```shell script
curl --location --request GET 'http://localhost:8011/users-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkNDA1ZjVjMC1lM2NhLTQwNTItYTQ5Ny03N2Y1ZDI1MTQ2M2UiLCJleHAiOjE2MTQ5NjgxNDZ9.RTt2Y6tjk07J_hoSoYINhKJGVRFp1_1Tn03cm_tlcOQp_xRwqmLJFxlGgTNM8ch799CpcJh92zDnjJzHhRZhuA'
```
-  receive response with
    -  ` "albums": []` 
    -  and logs 
```
2021-03-03 12:34:59.550 DEBUG 23488 --- [pool-1-thread-4] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> GET http://albums-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums HTTP/1.1
2021-03-03 12:34:59.550 DEBUG 23488 --- [pool-1-thread-4] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> END HTTP (0-byte body)
2021-03-03 12:34:59.552  WARN 23488 --- [oundedElastic-3] o.s.c.l.core.RoundRobinLoadBalancer      : No servers available for service: albums-ws
2021-03-03 12:34:59.552  WARN 23488 --- [pool-1-thread-4] .s.c.o.l.FeignBlockingLoadBalancerClient : Service instance was not resolved, executing the original request
2021-03-03 12:35:00.562 DEBUG 23488 --- [o-auto-1-exec-8] n.s.s.p.a.u.s.AlbumsServiceFallback      : AlbumsFallback service called for user with id `d405f5c0-e3ca-4052-a497-77f5d251463e`
```
-  start `albums-ws`
-  curl again
    -  receive response with 3 albums
    -  and logs
```
2021-03-03 12:48:23.001 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> GET http://albums-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums HTTP/1.1
2021-03-03 12:48:23.001 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> END HTTP (0-byte body)
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] <--- HTTP/1.1 200 (6ms)
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] connection: keep-alive
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] content-type: application/json
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] date: Wed, 03 Mar 2021 10:48:23 GMT
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] keep-alive: timeout=60
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] transfer-encoding: chunked
2021-03-03 12:48:23.007 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] 
2021-03-03 12:48:23.008 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] [{"albumId":"album01Id","userId":"d405f5c0-e3ca-4052-a497-77f5d251463e","name":"album 1 name","description":"album 1 description"},{"albumId":"album02Id","userId":"d405f5c0-e3ca-4052-a497-77f5d251463e","name":"album 2 name","description":"album 2 description"},{"albumId":"album03Id","userId":"d405f5c0-e3ca-4052-a497-77f5d251463e","name":"album 3 name","description":"album 3 description"}]
2021-03-03 12:48:23.008 DEBUG 22796 --- [pool-1-thread-1] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] <--- END HTTP (391-byte body)
```    
-  stop `albums-ws`    
-  curl again
-  receive response with
    -  ` "albums": []` 
    -  and logs with exception
```
2021-03-03 12:49:54.867 DEBUG 22796 --- [o-auto-1-exec-5] n.s.s.p.a.u.s.AlbumsServiceFallback      : AlbumsFallback service called for user with id `d405f5c0-e3ca-4052-a497-77f5d251463e`
2021-03-03 12:49:55.906  WARN 22796 --- [pool-1-thread-2] .c.RetryAwareServiceInstanceListSupplier : No instances found after removing previously used service instance from the search ([EurekaServiceInstance@15fe485c instance = InstanceInfo [instanceId = albums-ws:ae219b5981c413da04f7b4e7c0c8d9a5, appName = ALBUMS-WS, hostName = localhost, status = UP, ipAddr = 192.168.99.1, port = 63871, securePort = 443, dataCenterInfo = com.netflix.appinfo.MyDataCenterInfo@7835b049]). Returning all found instances.
2021-03-03 12:49:57.939 DEBUG 22796 --- [pool-1-thread-2] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] <--- ERROR ConnectException: Connection refused: connect (4084ms)
2021-03-03 12:49:57.941 DEBUG 22796 --- [pool-1-thread-2] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] java.net.ConnectException: Connection refused: connect
	at java.base/java.net.PlainSocketImpl.waitForConnect(Native Method)
	at java.base/java.net.PlainSocketImpl.socketConnect(PlainSocketImpl.java:107)
	at java.base/java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:399)
	at java.base/java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:242)
	at java.base/java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:224)
	at java.base/java.net.Socket.connect(Socket.java:608)
	at java.base/sun.net.NetworkClient.doConnect(NetworkClient.java:177)
	at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:474)
	at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:569)
	at java.base/sun.net.www.http.HttpClient.<init>(HttpClient.java:242)
	at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:341)
	at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:362)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1253)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1187)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1081)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:1015)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1592)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1520)
	at java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:527)
	at feign.Client$Default.convertResponse(Client.java:108)
	at feign.Client$Default.execute(Client.java:104)
	at org.springframework.cloud.openfeign.loadbalancer.LoadBalancerUtils.executeWithLoadBalancerLifecycleProcessing(LoadBalancerUtils.java:56)
	at org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient.lambda$execute$2(RetryableFeignBlockingLoadBalancerClient.java:156)
	at org.springframework.retry.support.RetryTemplate.doExecute(RetryTemplate.java:329)
	at org.springframework.retry.support.RetryTemplate.execute(RetryTemplate.java:225)
	at org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient.execute(RetryableFeignBlockingLoadBalancerClient.java:103)
	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:119)
	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:89)
	at org.springframework.cloud.openfeign.FeignCircuitBreakerInvocationHandler.lambda$asSupplier$1(FeignCircuitBreakerInvocationHandler.java:99)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)

2021-03-03 12:49:57.941 DEBUG 22796 --- [pool-1-thread-2] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] <--- END ERROR
```
-  another curl does not trigger exception
```
2021-03-03 12:53:36.837 DEBUG 22796 --- [pool-1-thread-3] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> GET http://albums-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums HTTP/1.1
2021-03-03 12:53:36.837 DEBUG 22796 --- [pool-1-thread-3] n.s.s.p.a.u.s.AlbumsServiceClient        : [AlbumsServiceClient#getUserAlbums] ---> END HTTP (0-byte body)
2021-03-03 12:53:36.839  WARN 22796 --- [oundedElastic-3] o.s.c.l.core.RoundRobinLoadBalancer      : No servers available for service: albums-ws
2021-03-03 12:53:36.840  WARN 22796 --- [pool-1-thread-3] .s.c.o.l.FeignBlockingLoadBalancerClient : Service instance was not resolved, executing the original request
2021-03-03 12:53:37.842 DEBUG 22796 --- [o-auto-1-exec-9] n.s.s.p.a.u.s.AlbumsServiceFallback      : AlbumsFallback service called for user with id `d405f5c0-e3ca-4052-a497-77f5d251463e`
```

#####  182. Error Handling with Feign ~~Hystrix~~ Resilience4j FallbackFactory

1.  Start without `albums-ws`
    -  Expected `FeignException` (Kargopolov had with Hystrix) but was
        -  `java.util.concurrent.TimeoutException: TimeLimiter 'UNDEFINED' recorded a timeout exception.`
2.  Start with `albums-ws` but with wrong Endpoint
    -  `"/users/{userId}/albums404"`
    -  `2021-03-04 16:21:18.029 ERROR 4452 --- [o-auto-1-exec-1] s.s.p.a.u.s.AlbumsServiceFallbackFactory : Other Error took place: 404 NOT_FOUND "User's albums are not found"`
    -  was `org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND "User's albums are not found"`
    -  we throw it in FeignErrorDecoder
3.  Disable FeignErrorDecoder
    -  comment out `@Component` from FeignErrorDecoder
    -  got expected logs
```
2021-03-04 16:29:09.395 ERROR 14160 --- [o-auto-1-exec-2] .s.p.a.u.s.DetailedAlbumsServiceFallback : 404 error took place when getAlbums was called with userId `d405f5c0-e3ca-4052-a497-77f5d251463e`. Error message: [404] during [GET] to [http://albums-ws/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums404] [AlbumsServiceClient#getUserAlbums(UUID)]: [{"timestamp":"2021-03-04T14:29:09.370+00:00","status":404,"error":"Not Found","message":"No message available","path":"/users/d405f5c0-e3ca-4052-a497-77f5d251463e/albums404"}]
```        
4.  Rollback to right endpoint  
    -  shut down `albums-ws`
    -  got an error  
    -  `2021-03-04 16:48:18.541 ERROR 6188 --- [o-auto-1-exec-3] s.s.p.a.u.s.AlbumsServiceFallbackFactory : Other Error took place: `TimeLimiter 'UNDEFINED' recorded a timeout exception.``

####  Section 25: Distributed Tracing with Sleuth and Zipkin

#####  186. Startup Zipkin Server

I used Docker to start Zipkin server:
-  `docker run -d -p 9411:9411 openzipkin/zipkin`
-  **OR** use [docker-compose.yml](compose\docker-compose.yml) file
-  also test official prebuild images
    -  [compose-zipkin-official-learning.yml](compose/compose-zipkin-official-learning.yml)

#####  187. View Traces in Zipkin

-  We can save traces in JSON like [96ebd8dcbf55db2c.json](zipkin/96ebd8dcbf55db2c.json)
-  And then upload it to view trace by another team member 

####  Section 26: Aggregating Log Files with ELK Stack

#####  193. Run Search Query

-  curl `localhost:9200/_cat`
-  `localhost:9200/_cat/indices`
```
yellow open users-ws-2021.03.06  NYsOhpi-THWAQriAOjzt3A 1 1 1 0 7.6kb 7.6kb
yellow open albums-ws-2021.03.06 s01uWUeLR0ahtEyqQEebAA 1 1 1 0 7.6kb 7.6kb
```
-  search for all logs
    -  `localhost:9200/users-ws-2021.03.06/_search?q=*` -> use Firefox to view
    -  **OR**
    -  `localhost:9200/users-ws-2021.03.06/_search?q=*&format&pretty`
-  search for logs with message that contains Eureka
    -  `http://localhost:9200/users-ws-2021.03.06/_search?q=message:Eureka`    

#####  195. View Aggregated Logs in Kibana

-  Create index patterns
    -  `http://localhost:5601` ->
        -  Kibana -> Discover -> Index patterns -> Create index pattern
    -  **OR**
    -  `http://localhost:5601/app/management/kibana/indexPatterns`
    -  Create index pattern for
        -  `users-ws` :  `users-ws-*`                
        -  `albums-ws` :  `albums-ws-*`
    -  Timefield -> `@timestamp`
-  Search
    -  Kibana -> Discover
    -  Choose Index (users or albums)
    -  Available fields -> Add
    -  Search for message to contain Eureka -> `message : Eureka`

####  Section 28: Running Microservices in Docker Containers to AWS EC2

#####  206. Install Docker on AWS EC2

use [UserData](ec2\UserDataDocker.sh) to create EC2 instance with Docker 

#####  208. Run RabbitMQ Docker Container

-  `docker run -d   --restart unless-stopped  --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management`
-  update security group `microservices-sg` to allow management port (15672) from MyIp and 5672 port from security group

#####  212. Run Config Server on EC2 from Docker Hub

-  `docker container inspect some-rabbit` -> view "IPAddress": "172.17.0.2"
-  set env variable to access git 
    -  `export SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD=<insert your password or Token>`
    -  test that variable is set properly
        -  echo $SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD
        -  echo ${SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD}
-  `docker run -d --restart unless-stopped  -p 8012:8012 -e SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD=${SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD} -e SPRING_RABBITMQ_HOST=172.17.0.2 artarkatesoft/photo-app-api-config-server`
-  modify Security Group to access port 8012 from MyIp
-  curl to it
    -  `http://ec2-52-47-172-113.eu-west-3.compute.amazonaws.com:8012/all/default`
    -  `http://ec2-52-47-172-113.eu-west-3.compute.amazonaws.com:8012/all/asymmetric`

#####  Security group modification

1. Create Security Groups
    -  microservices-sg (sg-001d7a0c986a520e9)
        -  SSH: 22 from Everywhere
    -  micro-config (sg-00fbc332b3514d98d)
        -  TCP	8012	93.170.219.17/32	Config port from MyIP
        -  TCP	8012	sg-001d7a0c986a520e9 (microservices-sg)	Config port from SG microservices-sg
    -  micro-eureka (sg-049dbc54434aaf068)
        -  TCP	8010	93.170.219.17/32	Eureka 8010 from MyIP
        -  TCP	8010	sg-001d7a0c986a520e9 (microservices-sg)	Eureka 8010 from microservices-sg        
    -  micro-rabbit (sg-070fb72fa9ad2541e)
        -  TCP	15672	93.170.219.17/32	RabbitMQ management port from MyIP	    
        -  TCP	5672	sg-001d7a0c986a520e9 (microservices-sg)	RabbitMQ AMQP port 5672 from microservices-sg
2.  Assign security groups to EC2 instances
    -  Config Server
        -  microservices-sg
        -  micro-config
        -  micro-rabbit
    -  Eureka Server
        -  microservices-sg
        -  micro-eureka
3.  **OR** we can avoid using microservices-sg and just allow access from CIDR (range of private IPs)
    -  VPC -> CIDRs ->
    -  172.31.0.0/16
    -  and allow access from any IP of this group        

#####  215. Run Eureka in Docker container

-  Create another EC2 instance with [UserData](ec2\UserDataDocker.sh)
-  Then SSH into it and run docker container
```shell script
docker run -d -p 8010:8010  --restart unless-stopped -e SPRING_CLOUD_CONFIG_URI=http://172.31.38.141:8012 artarkatesoft/photo-app-discovery-service 
```
-  172.31.38.141 - private IP of Config Server

#####  216. Elastic IP address for EC2 Instance

-  EC2 Console ->
    -  Elastic IPs -> Allocate Elastic IP address -> Allocate
    -  Change Name: Eureka Server
    -  Actions -> Associate Elastic IP address ->
        -  Instance: Eureka Server
-  Same for Config Server          

#####  218. Run Api Gateway in Docker Container

1. Create new EC2 instance 
    -  EC2 Console -> choose `Eureka Server` ->
    -  Actions -> Image and templates -> Launch more like this
    -  into UserData add command to run `gateway`
    -  `docker run -d --restart unless-stopped -p 8011:8011 -e SPRING_CLOUD_CONFIG_URI=http://172.31.38.141:8012 -e SPRING_PROFILES_ACTIVE=asymmetric,aws artarkatesoft/photo-app-api-gateway` 
    -  Tags
        -  Name: API Gateway
    -  Security groups
        -  microservices-sg
        -  micro-gateway (create before, allow 8011 from anywhere)
2.  Test it
    -  curl to http://35.181.51.163:8011/actuator
    -  view in discovery service - should register
    
#####  219. Run Elastic Search in Docker container

-  create Security Group `micro-elastic`
    -  allow ports 9200 and 9300 to sg `microservices-sg`
-  `docker run -d --restart unless-stopped -p 9200:9200 -p 9300:9300 -v esdata1:/usr/share/elasticsearch/data -e "discovery.type=single-node" --name elastic docker.elastic.co/elasticsearch/elasticsearch:7.10.1`

#####  220. Run Kibana in Docker Container

1.  create Security Group `micro-kibana`
    -  allow port 5601 from MyIP
    -  allow port 5601 from Virtual IP (CIDR 172.31.0.0/16)    
2.  Commands to start Kibana Docker container (link is **deprecated**)
    -  `docker run --link <YOUR_ELASTICSEARCH_CONTAINER_NAME_OR_ID>:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1`    
    -  `docker run -d  --restart unless-stopped --link elastic:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1`
3.  Create EC2 from Eureka EC2
    -  change to t2.small
    -  use [UserDataElasticKibana_deprecated.sh](ec2\UserDataElasticKibana_deprecated.sh)
    -  attach this EC2 to security groups
        -  microservices-sg
        -  micro-elastic
        -  micro-kibana 

#####  221. Run Kibana and Elastic Search on the same Network

1.  Create network first
    -  `docker network create --driver bridge api-network`
2.  Run containers with attached network
    -  `docker run -d --restart unless-stopped --network api-network -p 9200:9200 -p 9300:9300 -v esdata1:/usr/share/elasticsearch/data -e "discovery.type=single-node" --name elasticsearch docker.elastic.co/elasticsearch/elasticsearch:7.10.1`
    -  `docker run -d --restart unless-stopped --network api-network -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1` 
3.  When creating new EC2 instance
    -  use [UserDataElasticKibana.sh](ec2\UserDataElasticKibana.sh)        

                      