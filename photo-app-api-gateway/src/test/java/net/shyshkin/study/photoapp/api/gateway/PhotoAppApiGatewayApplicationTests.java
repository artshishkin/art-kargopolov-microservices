package net.shyshkin.study.photoapp.api.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "eureka.client.register-with-eureka=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.config.enabled=false"
})
class PhotoAppApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
