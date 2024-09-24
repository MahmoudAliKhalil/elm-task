package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.exception.ProductNotFoundException;
import com.github.mahmoudalikhalil.elm.mapper.ProductMapper;
import com.github.mahmoudalikhalil.elm.mapper.UserMapper;
import com.github.mahmoudalikhalil.elm.model.dto.ProductCreationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.ProductResponse;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsResponse;
import com.github.mahmoudalikhalil.elm.model.entity.Product;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsProjection;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.mahmoudalikhalil.elm.data.ProductSpecifications.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public ProductResponse retrieveAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return productMapper.mapProductsToProductResponse(products, products.getTotalElements());
    }

    public ProductResponse retrieveActiveProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(hasActiveStatus(), pageable);

        return productMapper.mapProductsToProductResponse(products, products.getTotalElements());
    }

    public ProductResponse retrieveDealerProducts(Pageable pageable, Principal dealer) {
        User dealerUser = userMapper.mapPrinicepalToUser(dealer);

        Page<Product> products = productRepository.findAll(hasDealer(dealerUser), pageable);

        return productMapper.mapProductsToDealerProductResponse(products, products.getTotalElements());
    }

    @Transactional
    public void createProduct(ProductCreationRequest request, Principal dealer) {
        User dealerUser = userMapper.mapPrinicepalToUser(dealer);

        Product product = productMapper.mapProductCreationRequestToProduct(request, dealerUser);

        productRepository.save(product);
    }

    @Transactional
    public void changeProductStatus(Long productId, Principal dealer) {
        User dealerUser = userMapper.mapPrinicepalToUser(dealer);

        Product product = productRepository.findOne(hasId(productId).and(hasDealer(dealerUser)))
                .orElseThrow(ProductNotFoundException::new);

        product.setStatus(product.getStatus().flipStatus());
    }

    public StatisticsResponse getProductsStatistics(LocalDate from, LocalDate to) {
        LocalDateTime start = from != null ? LocalDateTime.of(from, LocalTime.MIDNIGHT) : LocalDateTime.MIN;
        LocalDateTime end = to != null ? LocalDateTime.of(to, LocalTime.MAX) : LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        StatisticsProjection statistics = productRepository.getStatistics(start, end);
        return productMapper.mapStatisticsToStatisticsResponse(statistics);
    }
}
