package com.github.mahmoudalikhalil.elm.model.dto;

import java.math.BigDecimal;

public interface StatisticsProjection {
    Long getTotalProducts();
    Long getActiveProducts();
    Long getInactiveProducts();
    BigDecimal getTotalPrice();

    Long getLowestId();
    String getLowestDealerName();
    String getLowestName();
    BigDecimal getLowestPrice();

    Long getHighestId();
    String getHighestDealerName();
    String getHighestName();
    BigDecimal getHighestPrice();

    Long getTotalDealers();
    Long getHasProducts();
    Long getHasNoProducts();

    Long getTotalClients();
    Long getActiveClients();
    Long getInactiveClients();
}
