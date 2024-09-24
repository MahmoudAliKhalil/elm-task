package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.model.dto.ProductCreationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.ProductResponse;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsResponse;
import com.github.mahmoudalikhalil.elm.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> retrieveDealerProducts(Pageable pageable, Principal principal) {
        ProductResponse response = productService.retrieveDealerProducts(pageable, principal);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> retrieveAllProducts(Pageable pageable) {
        ProductResponse response = productService.retrieveAllProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> retrieveActiveProducts(Pageable pageable) {
        ProductResponse response = productService.retrieveActiveProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductCreationRequest request, Principal principal) {
        productService.createProduct(request, principal);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/change-status")
    public ResponseEntity<Void> changeProductStatus(@Validated @PathVariable @Positive Long id, Principal principal) {
        productService.changeProductStatus(id, principal);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatisticsResponse> getProductsStatistics(@RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to) {
        StatisticsResponse response = productService.getProductsStatistics(from, to);
        return ResponseEntity.ok(response);
    }
}
