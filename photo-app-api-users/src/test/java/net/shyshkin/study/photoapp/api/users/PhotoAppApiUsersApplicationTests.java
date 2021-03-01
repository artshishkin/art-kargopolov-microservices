package net.shyshkin.study.photoapp.api.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "eureka.client.register-with-eureka=false",
        "spring.cloud.discovery.enabled=false",
        "gateway.ip=127.0.0.1",
        "spring.cloud.config.enabled=false"
})
class PhotoAppApiUsersApplicationTests {

    @Test
    void contextLoads() {
    }

}
