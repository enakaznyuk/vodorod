package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private int id;
    private int userId;
    private String username;
    private String productsText;
    private String quantitiesText;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private List<Integer> productIds = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();

    public OrderDto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductsText() {
        return productsText;
    }

    public void setProductsText(String productsText) {
        this.productsText = productsText;
    }

    public String getQuantitiesText() {
        return quantitiesText;
    }

    public void setQuantitiesText(String quantitiesText) {
        this.quantitiesText = quantitiesText;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }
}
