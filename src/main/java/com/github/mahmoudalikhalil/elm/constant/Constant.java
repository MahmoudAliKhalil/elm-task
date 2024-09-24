package com.github.mahmoudalikhalil.elm.constant;

public final class Constant {
    private Constant() {
        throw new IllegalCallerException("Constant utility class cannot be instantiated.");
    }

    public static final String PRODUCT_DEALER_GRAPH_NAME  = "product-dealer-graph";
    public static final String ROLE_CLAIM_NAME = "role";

    public static final String PRODUCT_STATISTICS_QUERY = """
            WITH active_products AS (
                SELECT
                    p.id,
                    p.name,
                    p.price,
                    u.username AS dealerName,
                    DENSE_RANK() OVER (ORDER BY p.price DESC) AS ranking
                FROM
                    product p
                JOIN
                    user u ON p.user_id = u.id
                WHERE
                    p.status = 'active'
                    AND p.created_at BETWEEN ?1 AND ?2
            ),
            total_counts AS (
                SELECT
                    COUNT(*) AS total_products,
                    COUNT(CASE WHEN status = 'active' THEN 1 END) AS active_products,
                    COUNT(CASE WHEN status = 'inactive' THEN 1 END) AS inactive_products,
                    SUM(CASE WHEN status = 'active' THEN price END) AS total_price
                FROM
                    product
                WHERE
                    created_at BETWEEN ?1 AND ?2
            ),
            lowest_product AS (
                SELECT
                    id AS lowest_id,
                    dealerName AS lowest_dealer_name,
                    name AS lowest_name,
                    price AS lowest_price
                FROM
                    active_products
                ORDER BY
                    price ASC, id DESC
                LIMIT 1
            ),
            highest_product AS (
                SELECT
                    id AS highest_id,
                    dealerName AS highest_dealer_name,
                    name AS highest_name,
                    price AS highest_price
                FROM
                    active_products
                ORDER BY
                    price DESC, id DESC
                LIMIT 1
            ),
            dealer_counts AS (
                SELECT
                    COUNT(id) AS total_dealers,
                    COUNT(CASE WHEN EXISTS (SELECT 1 FROM product WHERE user_id = id) THEN id END) AS has_products,
                    COUNT(CASE WHEN NOT EXISTS (SELECT 1 FROM product WHERE user_id = id) THEN id END) AS has_no_products
                FROM
                    user
                WHERE
                    role = 'dealer'
                    AND created_at BETWEEN ?1 AND ?2
            ),
            client_counts AS (
                SELECT
                    COUNT(*) AS total_clients,
                    COUNT(CASE WHEN status = 'active' THEN 1 END) AS active_clients,
                    COUNT(CASE WHEN status = 'inactive' THEN 1 END) AS inactive_clients
                FROM
                    user
                WHERE
                    role = 'client'
                    AND created_at BETWEEN ?1 AND ?2
            )
            SELECT
                tc.total_products,
                tc.active_products,
                tc.inactive_products,
                tc.total_price,
                lp.lowest_id,
                lp.lowest_dealer_name,
                lp.lowest_name,
                lp.lowest_price,
                hp.highest_id,
                hp.highest_dealer_name,
                hp.highest_name,
                hp.highest_price,
                dc.total_dealers,
                dc.has_products,
                dc.has_no_products,
                cc.total_clients,
                cc.active_clients,
                cc.inactive_clients
            FROM
                total_counts tc,
                lowest_product lp,
                highest_product hp,
                dealer_counts dc,
                client_counts cc;
            """;

}
