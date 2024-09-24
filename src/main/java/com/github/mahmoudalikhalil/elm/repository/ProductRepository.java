package com.github.mahmoudalikhalil.elm.repository;

import com.github.mahmoudalikhalil.elm.constant.Constant;
import com.github.mahmoudalikhalil.elm.model.entity.Product;
import com.github.mahmoudalikhalil.elm.model.dto.StatisticsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(value = Constant.PRODUCT_DEALER_GRAPH_NAME, type = EntityGraph.EntityGraphType.LOAD)
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Override
    @EntityGraph(value = Constant.PRODUCT_DEALER_GRAPH_NAME, type = EntityGraph.EntityGraphType.LOAD)
    Page<Product> findAll(Pageable pageable);

    @Query(value = Constant.PRODUCT_STATISTICS_QUERY, nativeQuery = true)
    StatisticsProjection getStatistics(LocalDateTime start, LocalDateTime end);
}
