package com.cognizant.EventPlanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventPlannerApplicationTests {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Test
	void contextLoads() {
		System.out.println("Database URL: " + dbUrl);
	}

}
