package net.shyshkin.study.photo.discovery.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.cloud.config.enabled=false"
        }
)
class PhotoAppDiscoveryServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
