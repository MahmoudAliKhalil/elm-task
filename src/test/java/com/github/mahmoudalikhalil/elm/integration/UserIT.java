package com.github.mahmoudalikhalil.elm.integration;

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
class UserIT {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private JWTUtil jwtUtil;

	@Test
	void shouldAdminChangeUserStatusSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Void> response = restTemplate.exchange("/users/{id}/change-status", HttpMethod.POST, httpEntity, Void.class, 3L);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
