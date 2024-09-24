package com.github.mahmoudalikhalil.elm.integration;

import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRole;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import com.github.mahmoudalikhalil.elm.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationIT {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private JWTUtil jwtUtil;

	@Test
	void shouldRegisterClientSuccessfully() {
		ResponseEntity<Void> response = restTemplate.postForEntity("/register", new ClientUserRegistrationRequest("dummyClient", "1234567890", "dummyClient@example.com"), Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldRegisterDealerSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		SuperUserRegistrationRequest request = new SuperUserRegistrationRequest("dummyDealer", "1234567890", "dummyDealer@example.com", SuperUserRole.DEALER);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<SuperUserRegistrationRequest> httpEntity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<Void> response = restTemplate.exchange("/users/create", HttpMethod.POST, httpEntity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldRegisterAdminSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		SuperUserRegistrationRequest request = new SuperUserRegistrationRequest("dummyAdmin", "1234567890", "dummayAdmin@example.com", SuperUserRole.ADMIN);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<SuperUserRegistrationRequest> httpEntity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<Void> response = restTemplate.exchange("/users/create", HttpMethod.POST, httpEntity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
