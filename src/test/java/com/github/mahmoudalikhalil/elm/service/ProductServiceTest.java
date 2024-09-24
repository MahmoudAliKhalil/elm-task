package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.mapper.ProductMapper;
import com.github.mahmoudalikhalil.elm.mapper.UserMapper;
import com.github.mahmoudalikhalil.elm.model.dto.*;
import com.github.mahmoudalikhalil.elm.model.entity.Product;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsProjection;
import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;

    private Principal dealer;
    private User dealerUser;
    private Product product;

    @BeforeEach
    void setUp() {
        dealer = mock(Principal.class);
        dealerUser = new User();
        product = new Product();
    }

    @Test
    void shouldReturnAllProducts() {
        Pageable pageable = mock(Pageable.class);
        Page<Product> productPage = mock(Page.class);

        Long totalProducts = 5L;
        ProductDTO firstProduct = new ProductDTO(1L, "dealer", "first product", BigDecimal.ONE, Status.ACTIVE);
        ProductDTO secondProduct = new ProductDTO(2L, "dealer", "second product", BigDecimal.TWO, Status.ACTIVE);

        when(productPage.getTotalElements()).thenReturn(totalProducts);
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.mapProductsToProductResponse(productPage, totalProducts)).thenReturn(new ProductResponse(totalProducts, List.of(firstProduct, secondProduct)));

        ProductResponse response = productService.retrieveAllProducts(pageable);

        assertThat(response).isNotNull()
                .satisfies(products -> assertThat(products)
                                .extracting(ProductResponse::total)
                                .isEqualTo(totalProducts),
                        products -> assertThat(products.items())
                                .hasSize(2)
                                .containsExactly(firstProduct, secondProduct));


        verify(productRepository).findAll(pageable);
        verify(productMapper).mapProductsToProductResponse(productPage, totalProducts);
    }

    @Test
    void shouldReturnActiveProducts() {
        Pageable pageable = mock(Pageable.class);
        Page<Product> productPage = mock(Page.class);

        Long totalProducts = 5L;
        ProductDTO firstProduct = new ProductDTO(1L, "dealer", "first product", BigDecimal.ONE, Status.ACTIVE);
        ProductDTO secondProduct = new ProductDTO(2L, "dealer", "second product", BigDecimal.TWO, Status.ACTIVE);

        when(productPage.getTotalElements()).thenReturn(totalProducts);
        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productMapper.mapProductsToProductResponse(productPage, totalProducts)).thenReturn(new ProductResponse(totalProducts, List.of(firstProduct, secondProduct)));

        ProductResponse response = productService.retrieveActiveProducts(pageable);

        assertThat(response).isNotNull()
                .satisfies(products -> assertThat(products)
                                .extracting(ProductResponse::total)
                                .isEqualTo(totalProducts),
                        products -> assertThat(products.items())
                                .hasSize(2)
                                .containsExactly(firstProduct, secondProduct));

        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productMapper).mapProductsToProductResponse(productPage, totalProducts);
    }

    @Test
    void shouldReturnAllProductsOfSpecificDealer() {
        Pageable pageable = mock(Pageable.class);
        when(userMapper.mapPrinicepalToUser(dealer)).thenReturn(dealerUser);
        Page<Product> productPage = mock(Page.class);

        Long totalProducts = 5L;
        ProductDTO firstProduct = new ProductDTO(1L, null, "first product", BigDecimal.ONE, Status.ACTIVE);
        ProductDTO secondProduct = new ProductDTO(2L, null, "second product", BigDecimal.TWO, Status.ACTIVE);

        when(productPage.getTotalElements()).thenReturn(totalProducts);
        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productMapper.mapProductsToDealerProductResponse(productPage, totalProducts)).thenReturn(new ProductResponse(totalProducts, List.of(firstProduct, secondProduct)));

        ProductResponse response = productService.retrieveDealerProducts(pageable, dealer);

        assertThat(response).isNotNull()
                .satisfies(products -> assertThat(products)
                                .extracting(ProductResponse::total)
                                .isEqualTo(totalProducts),
                        products -> assertThat(products.items())
                                .hasSize(2)
                                .containsExactly(firstProduct, secondProduct));

        verify(userMapper).mapPrinicepalToUser(dealer);
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
        verify(productMapper).mapProductsToDealerProductResponse(productPage, productPage.getTotalElements());
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductCreationRequest productCreationRequest = new ProductCreationRequest("test product", BigDecimal.ONE);

        when(userMapper.mapPrinicepalToUser(dealer)).thenReturn(dealerUser);
        when(productMapper.mapProductCreationRequestToProduct(productCreationRequest, dealerUser)).thenReturn(product);

        assertThatCode(() -> productService.createProduct(productCreationRequest, dealer))
                        .doesNotThrowAnyException();

        verify(userMapper).mapPrinicepalToUser(dealer);
        verify(productMapper).mapProductCreationRequestToProduct(productCreationRequest, dealerUser);
        verify(productRepository).save(product);
    }

    @Test
    void shouldChangeProductStatusSuccessfully() {
        Long productId = 1L;
        product.setStatus(Status.ACTIVE);

        when(userMapper.mapPrinicepalToUser(dealer)).thenReturn(dealerUser);
        when(productRepository.findOne(any(Specification.class))).thenReturn(Optional.of(product));

        productService.changeProductStatus(productId, dealer);

        assertThat(product)
                .extracting(Product::getStatus)
                        .isSameAs(Status.INACTIVE);

        verify(userMapper).mapPrinicepalToUser(dealer);
        verify(productRepository).findOne(any(Specification.class));
    }

    @Test
    void shouldReturnStatisticsSuccessfully() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);
        StatisticsProjection statistics = mock(StatisticsProjection.class);

        ProductsStatistics productsStatistics = new ProductsStatistics(0L, 0L, 0L, BigDecimal.ZERO, null, null);
        DealersStatistics dealersStatistics = new DealersStatistics(0L, 0L, 0L);
        ClientsStatistics clientsStatistics = new ClientsStatistics(0L, 0L, 0L);
        StatisticsResponse statisticsResponse = new StatisticsResponse(productsStatistics, dealersStatistics, clientsStatistics);

        when(productRepository.getStatistics(LocalDateTime.of(from, LocalTime.MIDNIGHT), LocalDateTime.of(to, LocalTime.MAX)))
                .thenReturn(statistics);
        when(productMapper.mapStatisticsToStatisticsResponse(statistics))
                .thenReturn(statisticsResponse);

        StatisticsResponse response = productService.getProductsStatistics(from, to);

        assertThat(response).isNotNull()
                .isEqualTo(statisticsResponse);

        verify(productRepository).getStatistics(LocalDateTime.of(from, LocalTime.MIDNIGHT), LocalDateTime.of(to, LocalTime.MAX));
        verify(productMapper).mapStatisticsToStatisticsResponse(statistics);
    }

}