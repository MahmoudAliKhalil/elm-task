package com.github.mahmoudalikhalil.elm.integration;

import com.github.mahmoudalikhalil.elm.model.dto.LoginRequest;
import com.github.mahmoudalikhalil.elm.model.dto.LoginResponse;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldLoginSuccessfully() {
		ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/login", new LoginRequest("adminTest", "1234567890"), LoginResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull()
				.extracting(LoginResponse::token, as(InstanceOfAssertFactories.STRING))
				.isNotEmpty();
	}
}
