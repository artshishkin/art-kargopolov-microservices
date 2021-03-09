package net.shyshkin.study.photoapp.api.albums.PhotoAppApiAlbums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"eureka.client.register-with-eureka=false",
		"spring.cloud.discovery.enabled=false",
		"spring.cloud.config.enabled=false"
})
public class PhotoAppApiAlbumsApplicationTests {

	@Test
	public void contextLoads() {
	}

}

