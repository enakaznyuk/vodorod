package service;

import dto.OrderDto;
import entity.Order;
import entity.Product;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private static final String SELECT_ORDER_LINES = """
            SELECT o.id, u.id, u.username, o.order_date, p.id, p.name, oi.quantity, oi.price
            FROM orders o
            JOIN users u ON u.id = o.user_id
            JOIN order_items oi ON oi.order_id = o.id
            JOIN products p ON p.id = oi.product_id
            """;

    public void save(int userId, int[] productIds, int[] quantities) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.find(User.class, userId);
            if (user != null && hasValidItems(productIds, quantities)) {
                Order order = new Order(user);
                session.persist(order);
                session.flush();
                insertItems(session, order.getId(), productIds, quantities);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void update(int orderId, int userId, int[] productIds, int[] quantities) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Order order = session.find(Order.class, orderId);
            User user = session.find(User.class, userId);
            if (order != null && user != null && hasValidItems(productIds, quantities)) {
                order.setUser(user);
                session.createNativeQuery("DELETE FROM order_items WHERE order_id = :orderId")
                        .setParameter("orderId", orderId)
                        .executeUpdate();
                insertItems(session, orderId, productIds, quantities);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public OrderDto findById(int orderId) {
        List<OrderDto> orders = findOrdersByOrderId(orderId);
        return orders.isEmpty() ? null : orders.get(0);
    }

    public List<OrderDto> findAll() {
        return findOrders(SELECT_ORDER_LINES + " ORDER BY o.id DESC");
    }

    public List<OrderDto> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = session.createNativeQuery(
                            SELECT_ORDER_LINES + " WHERE u.id = :userId ORDER BY o.id DESC")
                    .setParameter("userId", userId)
                    .list();
            return aggregateRows(rows);
        }
    }

    private List<OrderDto> findOrdersByOrderId(int orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = session.createNativeQuery(
                            SELECT_ORDER_LINES + " WHERE o.id = :orderId ORDER BY p.id")
                    .setParameter("orderId", orderId)
                    .list();
            return aggregateRows(rows);
        }
    }

    private List<OrderDto> findOrders(String sql) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = session.createNativeQuery(sql).list();
            return aggregateRows(rows);
        }
    }

    private List<OrderDto> aggregateRows(List<Object[]> rows) {
        Map<Integer, OrderDto> orders = new LinkedHashMap<>();

        for (Object[] row : rows) {
            int orderId = ((Number) row[0]).intValue();
            OrderDto dto = orders.computeIfAbsent(orderId, id -> {
                OrderDto order = new OrderDto();
                order.setId(id);
                order.setUserId(((Number) row[1]).intValue());
                order.setUsername((String) row[2]);
                order.setOrderDate(toLocalDateTime(row[3]));
                order.setTotalPrice(BigDecimal.ZERO);
                return order;
            });

            int productId = ((Number) row[4]).intValue();
            String productName = (String) row[5];
            int quantity = ((Number) row[6]).intValue();
            BigDecimal price = toBigDecimal(row[7]);
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(quantity));

            dto.getProductIds().add(productId);
            dto.getQuantities().add(quantity);
            dto.setTotalPrice(dto.getTotalPrice().add(lineTotal));

            appendText(dto, productName, productId, quantity);
        }

        return new ArrayList<>(orders.values());
    }

    private void appendText(OrderDto dto, String productName, int productId, int quantity) {
        String productPart = productName + " (id:" + productId + ")";
        String quantityPart = String.valueOf(quantity);

        if (dto.getProductsText() == null || dto.getProductsText().isEmpty()) {
            dto.setProductsText(productPart);
            dto.setQuantitiesText(quantityPart);
        } else {
            dto.setProductsText(dto.getProductsText() + ", " + productPart);
            dto.setQuantitiesText(dto.getQuantitiesText() + ", " + quantityPart);
        }
    }

    private void insertItems(Session session, int orderId, int[] productIds, int[] quantities) {
        for (int i = 0; i < productIds.length; i++) {
            if (quantities[i] <= 0) {
                continue;
            }
            Product product = session.find(Product.class, productIds[i]);
            if (product == null) {
                continue;
            }
            session.createNativeQuery(
                            """
                            INSERT INTO order_items (order_id, product_id, quantity, price)
                            VALUES (:orderId, :productId, :quantity, :price)
                            """)
                    .setParameter("orderId", orderId)
                    .setParameter("productId", productIds[i])
                    .setParameter("quantity", quantities[i])
                    .setParameter("price", product.getPrice())
                    .executeUpdate();
        }
    }

    private boolean hasValidItems(int[] productIds, int[] quantities) {
        if (productIds == null || quantities == null || productIds.length == 0) {
            return false;
        }
        if (productIds.length != quantities.length) {
            return false;
        }
        for (int quantity : quantities) {
            if (quantity > 0) {
                return true;
            }
        }
        return false;
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        throw new IllegalArgumentException("Unsupported date type: " + value.getClass());
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        throw new IllegalArgumentException("Unsupported number type: " + value.getClass());
    }
}
