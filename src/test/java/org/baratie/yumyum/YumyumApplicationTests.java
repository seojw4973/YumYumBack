package org.baratie.yumyum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class YumyumApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("@@@@@@");
	}

}
