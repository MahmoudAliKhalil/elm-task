package com.github.mahmoudalikhalil.elm.mapper;

import com.github.mahmoudalikhalil.elm.model.dto.*;
import com.github.mahmoudalikhalil.elm.model.entity.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductMapperTest {
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldMapProductsToProductResponse() {
        Long total = 5L;

        Long firstProductId = 1L;
        String firstProductDealerName = "dealer1";
        String firstProductName = "product1";
        BigDecimal firstProductPrice = BigDecimal.valueOf(10.00);

        Long secondProductId = 2L;
        String secondProductDealerName = "dealer2";
        String secondProductName = "Product2";
        BigDecimal secondProductPrice = BigDecimal.valueOf(20.00);

        Iterable<Product> products = List.of(
                createActiveProduct(firstProductId, firstProductName, firstProductPrice, firstProductDealerName),
                createActiveProduct(secondProductId, secondProductName, secondProductPrice, secondProductDealerName));

        ProductResponse response = productMapper.mapProductsToProductResponse(products, total);

        assertThat(response).isNotNull()
                .satisfies(productResponse -> {
                    assertThat(productResponse.total())
                            .isEqualTo(total);

                    assertThat(productResponse.items())
                            .hasSize(2)
                            .containsExactly(
                                    new ProductDTO(firstProductId, firstProductDealerName, firstProductName, firstProductPrice, Status.ACTIVE),
                                    new ProductDTO(secondProductId, secondProductDealerName, secondProductName, secondProductPrice, Status.ACTIVE)
                            );
                });
    }

    @Test
    void shouldMapProductsToDealerProductResponse() {
        String productDealerName = "dealer1";
        Long total = 5L;

        Long firstProductId = 1L;
        String firstProductName = "product1";
        BigDecimal firstProductPrice = BigDecimal.valueOf(10.00);

        Long secondProductId = 2L;
        String secondProductName = "Product2";
        BigDecimal secondProductPrice = BigDecimal.valueOf(20.00);

        Iterable<Product> products = List.of(
                createActiveProduct(firstProductId, firstProductName, firstProductPrice, productDealerName),
                createActiveProduct(secondProductId, secondProductName, secondProductPrice, productDealerName));

        ProductResponse response = productMapper.mapProductsToDealerProductResponse(products, total);

        assertThat(response).isNotNull()
                .satisfies(productResponse -> {
                    assertThat(productResponse.total())
                            .isEqualTo(total);

                    assertThat(productResponse.items())
                            .hasSize(2)
                            .containsExactly(
                                    new ProductDTO(firstProductId, null, firstProductName, firstProductPrice, Status.ACTIVE),
                                    new ProductDTO(secondProductId, null, secondProductName, secondProductPrice, Status.ACTIVE)
                            );
                });
    }

    @Test
    void shouldMapProductToProductDTO() {
        Long productId = 1L;
        String productDealerName = "dealer1";
        String productName = "product1";
        BigDecimal productPrice = BigDecimal.valueOf(10.00);

        Product product = createActiveProduct(productId, productName, productPrice, productDealerName);

        ProductDTO productDTO = productMapper.mapProductToProductDTO(product);

        assertThat(productDTO).isNotNull()
                .satisfies(mappedProduct -> {
                    assertThat(mappedProduct.id())
                            .isEqualTo(productId);

                    assertThat(mappedProduct.name())
                            .isEqualTo(productName);

                    assertThat(mappedProduct.price())
                            .isEqualTo(productPrice);

                    assertThat(mappedProduct.dealerName())
                            .isEqualTo(productDealerName);
                });
    }

    @Test
    void shouldMapDealerProductsToProductDTOsWithoutDealerName() {
        String productDealerName = "dealer1";

        Long firstProductId = 1L;
        String firstProductName = "product1";
        BigDecimal firstProductPrice = BigDecimal.valueOf(10.00);

        Long secondProductId = 2L;
        String secondProductName = "Product2";
        BigDecimal secondProductPrice = BigDecimal.valueOf(20.00);

        Iterable<Product> products = List.of(
                createActiveProduct(firstProductId, firstProductName, firstProductPrice, productDealerName),
                createActiveProduct(secondProductId, secondProductName, secondProductPrice, productDealerName));

        List<ProductDTO> productDTO = productMapper.mapDealerProducts(products);

        assertThat(productDTO).hasSize(2)
                .containsExactly(
                        new ProductDTO(firstProductId, null, firstProductName, firstProductPrice, Status.ACTIVE),
                        new ProductDTO(secondProductId, null, secondProductName, secondProductPrice, Status.ACTIVE)
                );
    }

    @Test
    void shouldMapDealerProductToProductDTOWithoutDealerName() {
        Long productId = 1L;
        String productDealerName = "dealer1";
        String productName = "product1";
        BigDecimal productPrice = BigDecimal.valueOf(10.00);

        Product product = createActiveProduct(productId, productName, productPrice, productDealerName);

        ProductDTO productDTO = productMapper.mapDealerProduct(product);

        assertThat(productDTO).isNotNull()
                .satisfies(mappedProduct -> {
                    assertThat(mappedProduct.id())
                            .isEqualTo(productId);

                    assertThat(mappedProduct.name())
                            .isEqualTo(productName);

                    assertThat(mappedProduct.price())
                            .isEqualTo(productPrice);

                    assertThat(mappedProduct.dealerName())
                            .isNull();
                });
    }

    @Test
    void shouldMapProductCreationRequestToProduct() {
        String productName = "product1";
        BigDecimal productPrice = BigDecimal.valueOf(10.00);
        ProductCreationRequest request = new ProductCreationRequest(productName, productPrice);
        User dealer = User.builder()
                .id(1L)
                .build();

        Product product = productMapper.mapProductCreationRequestToProduct(request, dealer);

        assertThat(product).isNotNull()
                .hasAllNullFieldsOrPropertiesExcept(Product_.NAME, Product_.PRICE, Product_.STATUS, Product_.DEALER)
                .hasFieldOrPropertyWithValue(Product_.NAME, productName)
                .hasFieldOrPropertyWithValue(Product_.PRICE, productPrice)
                .hasFieldOrPropertyWithValue(Product_.STATUS, Status.ACTIVE)
                .hasFieldOrPropertyWithValue(Product_.DEALER, dealer);
    }

    @Test
    void shouldMapStatisticsQueryResultToStatisticsResponse() {
        StatisticsProjection statisticsProjection = mock(StatisticsProjection.class);
        when(statisticsProjection.getTotalProducts()).thenReturn(10L);
        when(statisticsProjection.getActiveProducts()).thenReturn(5L);
        when(statisticsProjection.getInactiveProducts()).thenReturn(5L);
        when(statisticsProjection.getTotalPrice()).thenReturn(BigDecimal.valueOf(100.00));
        when(statisticsProjection.getTotalDealers()).thenReturn(4L);
        when(statisticsProjection.getHasProducts()).thenReturn(2L);
        when(statisticsProjection.getHasNoProducts()).thenReturn(2L);
        when(statisticsProjection.getTotalClients()).thenReturn(20L);
        when(statisticsProjection.getActiveClients()).thenReturn(15L);
        when(statisticsProjection.getInactiveClients()).thenReturn(5L);

        StatisticsResponse response = productMapper.mapStatisticsToStatisticsResponse(statisticsProjection);

        assertThat(response).isNotNull()
                .satisfies(statistics -> {
                    assertThat(statistics.products())
                            .isNotNull();

                    assertThat(statistics.products().total())
                            .isEqualTo(10L);

                    assertThat(statistics.products().active())
                            .isEqualTo(5L);

                    assertThat(statistics.products().inactive())
                            .isEqualTo(5L);

                    assertThat(statistics.products().totalPrice())
                            .isEqualTo(BigDecimal.valueOf(100.00));

                    assertThat(statistics.dealers())
                            .isNotNull();

                    assertThat(statistics.dealers().total())
                            .isEqualTo(4L);

                    assertThat(statistics.dealers().hasProducts())
                            .isEqualTo(2L);

                    assertThat(statistics.dealers().hasNoProducts())
                            .isEqualTo(2L);

                    assertThat(statistics.clients())
                            .isNotNull();

                    assertThat(statistics.clients().total())
                            .isEqualTo(20L);

                    assertThat(statistics.clients().active())
                            .isEqualTo(15L);

                    assertThat(statistics.clients().inactive())
                            .isEqualTo(5L);
                });
    }

    @Test
    void shouldMapLowestProductSuccessfully() {
        StatisticsProjection statisticsProjection = mock(StatisticsProjection.class);
        when(statisticsProjection.getLowestId()).thenReturn(1L);
        when(statisticsProjection.getLowestDealerName()).thenReturn("Lowest Dealer");
        when(statisticsProjection.getLowestName()).thenReturn("Lowest Product");
        when(statisticsProjection.getLowestPrice()).thenReturn(BigDecimal.valueOf(5.00));

        ProductDTO productDTO = productMapper.mapLowestProduct(statisticsProjection);

        assertThat(productDTO).isNotNull()
                .satisfies(product -> {
                    assertThat(product.id())
                            .isEqualTo(1L);

                    assertThat(product.dealerName())
                            .isEqualTo("Lowest Dealer");

                    assertThat(product.name())
                            .isEqualTo("Lowest Product");

                    assertThat(product.price())
                            .isEqualTo(BigDecimal.valueOf(5.00));
                });
    }

    @Test
    void shouldMapHighestProductSuccessfully() {
        StatisticsProjection statisticsProjection = mock(StatisticsProjection.class);
        when(statisticsProjection.getHighestId()).thenReturn(2L);
        when(statisticsProjection.getHighestDealerName()).thenReturn("Highest Dealer");
        when(statisticsProjection.getHighestName()).thenReturn("Highest Product");
        when(statisticsProjection.getHighestPrice()).thenReturn(BigDecimal.valueOf(50.00));

        ProductDTO productDTO = productMapper.mapHighestProduct(statisticsProjection);

        assertThat(productDTO).isNotNull()
                .satisfies(product -> {
                    assertThat(product.id())
                            .isEqualTo(2L);

                    assertThat(product.dealerName())
                            .isEqualTo("Highest Dealer");

                    assertThat(product.name())
                            .isEqualTo("Highest Product");

                    assertThat(product.price())
                            .isEqualTo(BigDecimal.valueOf(50.00));
                });
    }

    @Test
    void shouldReturnDefaultStatisticsWhenGivenNull() {
        StatisticsResponse defaultStatistics = productMapper.createDefaultStatistics(null);

        assertThat(defaultStatistics).isNotNull()
                .satisfies(statistics -> {
                    assertThat(statistics.products())
                            .isNotNull();

                    assertThat(statistics.products().total())
                            .isZero();

                    assertThat(statistics.products().active())
                            .isZero();

                    assertThat(statistics.products().inactive())
                            .isZero();

                    assertThat(statistics.products().totalPrice())
                            .isZero();

                    assertThat(statistics.products().lowest())
                            .isNull();

                    assertThat(statistics.products().highest())
                            .isNull();

                    assertThat(statistics.dealers())
                            .isNotNull();

                    assertThat(statistics.dealers().total())
                            .isZero();

                    assertThat(statistics.dealers().hasProducts())
                            .isZero();

                    assertThat(statistics.dealers().hasNoProducts())
                            .isZero();

                    assertThat(statistics.clients())
                            .isNotNull();

                    assertThat(statistics.clients().total())
                            .isZero();

                    assertThat(statistics.clients().active())
                            .isZero();

                    assertThat(statistics.clients().inactive())
                            .isZero();
                });
    }

    @Test
    void shouldReturnDefaultStatisticsWhenGivenEmptyStatistics() {
        StatisticsResponse defaultStatistics = productMapper.createDefaultStatistics(new StatisticsResponse(null, null, null));

        assertThat(defaultStatistics).isNotNull()
                .satisfies(statistics -> {
                    assertThat(statistics.products())
                            .isNotNull();

                    assertThat(statistics.products().total())
                            .isZero();

                    assertThat(statistics.products().active())
                            .isZero();

                    assertThat(statistics.products().inactive())
                            .isZero();

                    assertThat(statistics.products().totalPrice())
                            .isZero();

                    assertThat(statistics.products().lowest())
                            .isNull();

                    assertThat(statistics.products().highest())
                            .isNull();

                    assertThat(statistics.dealers())
                            .isNotNull();

                    assertThat(statistics.dealers().total())
                            .isZero();

                    assertThat(statistics.dealers().hasProducts())
                            .isZero();

                    assertThat(statistics.dealers().hasNoProducts())
                            .isZero();

                    assertThat(statistics.clients())
                            .isNotNull();

                    assertThat(statistics.clients().total())
                            .isZero();

                    assertThat(statistics.clients().active())
                            .isZero();

                    assertThat(statistics.clients().inactive())
                            .isZero();
                });
    }

    @Test
    void shouldReturnSameStatisticsWhenGivenStatisticsIsNotNullOrEmpty() {
        ProductDTO lowestProduct = new ProductDTO(1L, "Dealer1", "Lowest Product", BigDecimal.ONE, Status.ACTIVE);
        ProductDTO highestProduct = new ProductDTO(2L, "Dealer2", "Highest Product", BigDecimal.valueOf(9.00), Status.ACTIVE);
        ProductsStatistics productsStatistics = new ProductsStatistics(4L, 2L, 2L, BigDecimal.TEN, lowestProduct, highestProduct);
        DealersStatistics dealersStatistics = new DealersStatistics(5L, 2L, 3L);
        ClientsStatistics clientsStatistics = new ClientsStatistics(10L, 8L, 2L);
        StatisticsResponse statisticsResponse = new StatisticsResponse(productsStatistics, dealersStatistics, clientsStatistics);

        StatisticsResponse defaultStatistics = productMapper.createDefaultStatistics(statisticsResponse);

        assertThat(defaultStatistics).isNotNull()
                .isSameAs(statisticsResponse);
    }

    private Product createActiveProduct(Long productId, String productName, BigDecimal price, String dealerName) {
        return Product.builder()
                .id(productId)
                .name(productName)
                .price(price)
                .status(Status.ACTIVE)
                .dealer(User.builder()
                        .username(dealerName)
                        .build())
                .build();
    }
}