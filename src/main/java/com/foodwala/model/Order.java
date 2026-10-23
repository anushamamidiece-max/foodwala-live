package com.foodwala.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Model for the orders table (with its order_items attached). */
public class Order {

    private int id;
    private int userId;
    private int restaurantId;
    private String restaurantName;
    private int totalAmount;
    private String deliveryAddress;
    private String paymentMode;
    private String status;
    private Timestamp orderedAt;

    private List<OrderItem> items = new ArrayList<>();

    public Order() { }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getOrderedAt() { return orderedAt; }
    public void setOrderedAt(Timestamp orderedAt) { this.orderedAt = orderedAt; }

    /**
     * Demo order tracking: the status advances with real time since the
     * order was placed, so the Orders page shows a live journey.
     *   < 2 min  PLACED
     *   < 5 min  PREPARING
     *   < 10 min OUT FOR DELIVERY
     *   else     DELIVERED
     */
    public String getLiveStatus() {
        if (orderedAt == null) {
            return status;
        }
        long mins = (System.currentTimeMillis() - orderedAt.getTime()) / 60000;
        if (mins < 2) return "PLACED";
        if (mins < 5) return "PREPARING";
        if (mins < 10) return "OUT FOR DELIVERY";
        return "DELIVERED";
    }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
