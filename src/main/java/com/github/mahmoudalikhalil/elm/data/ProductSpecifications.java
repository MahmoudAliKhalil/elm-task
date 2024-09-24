package com.github.mahmoudalikhalil.elm.data;

import com.github.mahmoudalikhalil.elm.model.entity.Product;
import com.github.mahmoudalikhalil.elm.model.entity.Product_;
import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {
    private ProductSpecifications() {
        throw new IllegalCallerException("Utility class cannot be instantiated.");
    }

    public static Specification<Product> hasId(Long id) {
        return (root, query, builder) -> builder.equal(root.get(Product_.id), id);
    }

    public static Specification<Product> hasActiveStatus() {
        return (root, query, builder) -> builder.equal(root.get(Product_.status), Status.ACTIVE);
    }

    public static Specification<Product> hasDealer(User dealer) {
        return (root, query, builder) -> builder.equal(root.get(Product_.dealer), dealer);
    }
}
