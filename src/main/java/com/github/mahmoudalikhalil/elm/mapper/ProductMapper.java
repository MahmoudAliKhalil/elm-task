package com.github.mahmoudalikhalil.elm.mapper;

import com.github.mahmoudalikhalil.elm.model.dto.*;
import com.github.mahmoudalikhalil.elm.model.entity.Product;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProductMapper {
    @Mapping(target = "total", source = "total")
    @Mapping(target = "items", source = "products")
    public abstract ProductResponse mapProductsToProductResponse(Iterable<Product> products, Long total);

    @Mapping(target = "total", source = "total")
    @Mapping(target = "items", source = "products", qualifiedByName = "MapDealerProducts")
    public abstract ProductResponse mapProductsToDealerProductResponse(Iterable<Product> products, Long total);

    @Mapping(target = "dealerName", source = "dealer.username")
    protected abstract ProductDTO mapProductToProductDTO(Product product);

    @Named("MapDealerProducts")
    @IterableMapping(qualifiedByName = "MapDealerProduct")
    protected abstract List<ProductDTO> mapDealerProducts(Iterable<Product> products);

    @Named("MapDealerProduct")
    protected abstract ProductDTO mapDealerProduct(Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "dealer", source = "dealer")
    public abstract Product mapProductCreationRequestToProduct(ProductCreationRequest product, User dealer);

    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(target = "products.total", source = "totalProducts")
    @Mapping(target = "products.active", source = "activeProducts")
    @Mapping(target = "products.inactive", source = "inactiveProducts")
    @Mapping(target = "products.totalPrice", source = "totalPrice")
    @Mapping(target = "products.lowest", source = ".", qualifiedByName = "MapLowestProduct")
    @Mapping(target = "products.highest", source = ".", qualifiedByName = "MapHighestProduct")
    @Mapping(target = "dealers.total", source = "totalDealers")
    @Mapping(target = "dealers.hasProducts", source = "hasProducts")
    @Mapping(target = "dealers.hasNoProducts", source = "hasNoProducts")
    @Mapping(target = "clients.total", source = "totalClients")
    @Mapping(target = "clients.active", source = "activeClients")
    @Mapping(target = "clients.inactive", source = "inactiveClients")
    public abstract StatisticsResponse mapStatisticsToStatisticsResponse(StatisticsProjection statistics);

    @Named("MapLowestProduct")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "lowestId")
    @Mapping(target = "dealerName", source = "lowestDealerName")
    @Mapping(target = "name", source = "lowestName")
    @Mapping(target = "price", source = "lowestPrice")
    protected abstract ProductDTO mapLowestProduct(StatisticsProjection statistics);

    @Named("MapHighestProduct")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "highestId")
    @Mapping(target = "dealerName", source = "highestDealerName")
    @Mapping(target = "name", source = "highestName")
    @Mapping(target = "price", source = "highestPrice")
    protected abstract ProductDTO mapHighestProduct(StatisticsProjection statistics);

    @AfterMapping
    protected StatisticsResponse createDefaultStatistics(@MappingTarget StatisticsResponse statisticsResponse) {
        if (statisticsResponse == null || ObjectUtils.allNull(statisticsResponse.products(), statisticsResponse.dealers(), statisticsResponse.dealers())) {
            ProductsStatistics productsStatistics = new ProductsStatistics(0L, 0L, 0L, BigDecimal.ZERO, null, null);
            DealersStatistics dealersStatistics = new DealersStatistics(0L, 0L, 0L);
            ClientsStatistics clientsStatistics = new ClientsStatistics(0L, 0L, 0L);
            return new StatisticsResponse(productsStatistics, dealersStatistics, clientsStatistics);
        }

        return statisticsResponse;
    }
}
