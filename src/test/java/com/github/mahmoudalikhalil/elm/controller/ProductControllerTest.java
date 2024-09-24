package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.model.dto.ProductCreationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.ProductResponse;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsResponse;
import com.github.mahmoudalikhalil.elm.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    private final Principal principal = () -> "testUser";

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldReturnSuccessfulResponseWhenRetrieveDealerProductsSucceed() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponse expectedResponse = new ProductResponse(0L, Collections.emptyList());
        when(productService.retrieveDealerProducts(pageable, principal)).thenReturn(expectedResponse);

        ResponseEntity<ProductResponse> response = productController.retrieveDealerProducts(pageable, principal);

        assertThat(response).isNotNull()
                .satisfies(responseEntity -> {
                    assertThat(responseEntity.getStatusCode())
                            .isEqualTo(HttpStatus.OK);

                    assertThat(responseEntity.getBody())
                            .isNotNull()
                            .isEqualTo(expectedResponse);
                });

        verify(productService).retrieveDealerProducts(pageable, principal);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenRetrieveAllProductsSucceed() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponse expectedResponse = new ProductResponse(0L, Collections.emptyList());
        when(productService.retrieveAllProducts(pageable)).thenReturn(expectedResponse);

        ResponseEntity<ProductResponse> response = productController.retrieveAllProducts(pageable);

        assertThat(response).isNotNull()
                .satisfies(responseEntity -> {
                    assertThat(responseEntity.getStatusCode())
                            .isEqualTo(HttpStatus.OK);

                    assertThat(responseEntity.getBody())
                            .isNotNull()
                            .isEqualTo(expectedResponse);
                });

        verify(productService).retrieveAllProducts(pageable);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenRetrieveActiveProductsSucceed() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponse expectedResponse = new ProductResponse(0L, Collections.emptyList());
        when(productService.retrieveActiveProducts(pageable)).thenReturn(expectedResponse);

        ResponseEntity<ProductResponse> response = productController.retrieveActiveProducts(pageable);

        assertThat(response).isNotNull()
                .satisfies(responseEntity -> {
                    assertThat(responseEntity.getStatusCode())
                            .isEqualTo(HttpStatus.OK);

                    assertThat(responseEntity.getBody())
                            .isNotNull()
                            .isEqualTo(expectedResponse);
                });

        verify(productService).retrieveActiveProducts(pageable);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenCreateProductSucceed() {
        ProductCreationRequest request = new ProductCreationRequest("name", BigDecimal.ONE);

        ResponseEntity<Void> response = productController.createProduct(request, principal);

        assertThat(response).isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isSameAs(HttpStatus.OK);

        verify(productService).createProduct(request, principal);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenChangeProductStatusSucceed() {
        Long productId = 1L;

        ResponseEntity<Void> response = productController.changeProductStatus(productId, principal);

        assertThat(response).isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isSameAs(HttpStatus.OK);

        verify(productService).changeProductStatus(productId, principal);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenGetProductsStatisticsSucceed() {
        StatisticsResponse expectedResponse = new StatisticsResponse(null, null, null);
        LocalDate fromDate = LocalDate.now().minusDays(30);
        LocalDate toDate = LocalDate.now();
        when(productService.getProductsStatistics(fromDate, toDate)).thenReturn(expectedResponse);

        ResponseEntity<StatisticsResponse> response = productController.getProductsStatistics(fromDate, toDate);

        assertThat(response).isNotNull()
                .satisfies(responseEntity -> {
                    assertThat(responseEntity.getStatusCode())
                            .isEqualTo(HttpStatus.OK);

                    assertThat(responseEntity.getBody())
                            .isNotNull()
                            .isEqualTo(expectedResponse);
                });

        verify(productService).getProductsStatistics(fromDate, toDate);
    }
}