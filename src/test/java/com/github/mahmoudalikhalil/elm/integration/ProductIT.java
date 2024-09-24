package com.github.mahmoudalikhalil.elm.integration;

import com.github.mahmoudalikhalil.elm.model.dto.ProductCreationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.ProductResponse;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsResponse;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import com.github.mahmoudalikhalil.elm.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIT {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private JWTUtil jwtUtil;

	@Test
	void shouldRetrieveAllDealerProductsSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(3L).role(UserRole.DEALER).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<ProductResponse> response = restTemplate.exchange("/products", HttpMethod.GET, httpEntity, ProductResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull()
				.satisfies(body -> {
					assertThat(body.total())
							.isEqualTo(3);

					assertThat(body.items())
							.hasSize(3);
				});
	}

	@Test
	void shouldRetrieveAllProductsSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<ProductResponse> response = restTemplate.exchange("/products/admin", HttpMethod.GET, httpEntity, ProductResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull()
				.satisfies(body -> {
					assertThat(body.total())
							.isEqualTo(5);

					assertThat(body.items())
							.hasSize(5);
				});
	}

	@Test
	void shouldRetrieveAllActiveProductsSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(3L).role(UserRole.CLIENT).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<ProductResponse> response = restTemplate.exchange("/products/user", HttpMethod.GET, httpEntity, ProductResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull()
				.satisfies(body -> {
					assertThat(body.total())
							.isEqualTo(3);

					assertThat(body.items())
							.hasSize(3);
				});
	}

	@Test
	@DirtiesContext
	void shouldCreateProductsSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(3L).role(UserRole.DEALER).build());

		ProductCreationRequest productCreationRequest = new ProductCreationRequest("Dummy Test Product", BigDecimal.valueOf(1.00));

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<ProductCreationRequest> httpEntity = new HttpEntity<>(productCreationRequest, httpHeaders);
		ResponseEntity<Void> response = restTemplate.exchange("/products/add", HttpMethod.POST, httpEntity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DirtiesContext
	void shouldChangeProductStatusSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(3L).role(UserRole.DEALER).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Void> response = restTemplate.exchange("/products/{id}/change-status", HttpMethod.POST, httpEntity, Void.class, 1L);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldRetrieveStatisticsWithoutDateRangeSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<StatisticsResponse> response = restTemplate.exchange("/products/statistics", HttpMethod.GET, httpEntity, StatisticsResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull();

		assertThat(response.getBody().products())
				.isNotNull()
				.satisfies(productsStatistics -> {
					assertThat(productsStatistics.total())
							.isEqualTo(5L);

					assertThat(productsStatistics.active())
							.isEqualTo(3L);

					assertThat(productsStatistics.inactive())
							.isEqualTo(2L);

					assertThat(productsStatistics.totalPrice())
							.isEqualTo(new BigDecimal("13000.00"));

					assertThat(productsStatistics.lowest())
							.isNotNull()
							.hasNoNullFieldsOrPropertiesExcept("status")
							.hasFieldOrPropertyWithValue("id", 4L)
							.hasFieldOrPropertyWithValue("name", "Test Product 4")
							.hasFieldOrPropertyWithValue("price", new BigDecimal("4000.00"))
							.hasFieldOrPropertyWithValue("dealerName", "dealerTest2");

					assertThat(productsStatistics.highest())
							.isNotNull()
							.hasNoNullFieldsOrPropertiesExcept("status")
							.hasFieldOrPropertyWithValue("id", 2L)
							.hasFieldOrPropertyWithValue("name", "Test Product 2")
							.hasFieldOrPropertyWithValue("price", new BigDecimal("5000.00"))
							.hasFieldOrPropertyWithValue("dealerName", "dealerTest");
				});

		assertThat(response.getBody().dealers())
				.isNotNull()
				.satisfies(dealersStatistics -> {
					assertThat(dealersStatistics.total())
							.isEqualTo(2L);

					assertThat(dealersStatistics.hasProducts())
							.isEqualTo(2L);

					assertThat(dealersStatistics.hasNoProducts())
							.isZero();
				});

		assertThat(response.getBody().clients())
				.isNotNull()
				.satisfies(clientsStatistics -> {
					assertThat(clientsStatistics.total())
							.isEqualTo(1L);

					assertThat(clientsStatistics.active())
							.isEqualTo(1L);

					assertThat(clientsStatistics.inactive())
							.isZero();
				});
	}

	@Test
	void shouldRetrieveStatisticsWithDateRangeSuccessfully() {
		String jwt = jwtUtil.generateJWT(new User().builder().id(1L).role(UserRole.ADMIN).build());

		LocalDate from = LocalDate.of(2024, 9, 21);
		LocalDate to = LocalDate.of(2024, 9, 22);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(jwt);
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<StatisticsResponse> response = restTemplate.exchange("/products/statistics?form={form}&to={to}", HttpMethod.GET, httpEntity, StatisticsResponse.class, from, to);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(response.getBody()).isNotNull();

		assertThat(response.getBody().products())
				.isNotNull()
				.satisfies(productsStatistics -> {
					assertThat(productsStatistics.total())
							.isEqualTo(3L);

					assertThat(productsStatistics.active())
							.isEqualTo(2L);

					assertThat(productsStatistics.inactive())
							.isEqualTo(1L);

					assertThat(productsStatistics.totalPrice())
							.isEqualTo(new BigDecimal("9000.00"));

					assertThat(productsStatistics.lowest())
							.isNotNull()
							.hasNoNullFieldsOrPropertiesExcept("status")
							.hasFieldOrPropertyWithValue("id", 1L)
							.hasFieldOrPropertyWithValue("name", "Test Product 1")
							.hasFieldOrPropertyWithValue("price", new BigDecimal("4000.00"))
							.hasFieldOrPropertyWithValue("dealerName", "dealerTest");

					assertThat(productsStatistics.highest())
							.isNotNull()
							.hasNoNullFieldsOrPropertiesExcept("status")
							.hasFieldOrPropertyWithValue("id", 2L)
							.hasFieldOrPropertyWithValue("name", "Test Product 2")
							.hasFieldOrPropertyWithValue("price", new BigDecimal("5000.00"))
							.hasFieldOrPropertyWithValue("dealerName", "dealerTest");
				});

		assertThat(response.getBody().dealers())
				.isNotNull()
				.satisfies(dealersStatistics -> {
					assertThat(dealersStatistics.total())
							.isEqualTo(2L);

					assertThat(dealersStatistics.hasProducts())
							.isEqualTo(2L);

					assertThat(dealersStatistics.hasNoProducts())
							.isZero();
				});

		assertThat(response.getBody().clients())
				.isNotNull()
				.satisfies(clientsStatistics -> {
					assertThat(clientsStatistics.total())
							.isEqualTo(1L);

					assertThat(clientsStatistics.active())
							.isEqualTo(1L);

					assertThat(clientsStatistics.inactive())
							.isZero();
				});
	}
}
