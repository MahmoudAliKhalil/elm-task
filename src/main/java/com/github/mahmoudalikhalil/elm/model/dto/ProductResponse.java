package com.github.mahmoudalikhalil.elm.model.dto;

import java.util.List;

public record ProductResponse(Long total, List<ProductDTO> items) {
}
