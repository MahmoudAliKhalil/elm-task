package com.github.mahmoudalikhalil.elm.model.dto;

import java.math.BigDecimal;

public record ProductsStatistics(Long total, Long active, Long inactive, BigDecimal totalPrice, ProductDTO lowest, ProductDTO highest) {
}
