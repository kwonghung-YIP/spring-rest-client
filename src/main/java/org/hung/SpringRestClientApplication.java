package org.hung;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestClientApplication.class, args);
	}

	@Bean
	CommandLineRunner runRestClient(RestTemplateBuilder builder) {
		return (args) -> {
			log.info("Run RestTemplate client");
			RestTemplate client = builder.build();
			
			String result = client.getForObject("https://localhost:8080/echo", String.class);
		};
	}
}
