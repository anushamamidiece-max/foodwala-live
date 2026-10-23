package com.foodwala.dao;

import java.util.List;

import com.foodwala.model.CartItem;
import com.foodwala.model.Order;

/** DAO contract for order operations. */
public interface OrderDAO {

    /**
     * Persists an order together with its line items in one transaction.
     * Returns the generated order id, or -1 on failure.
     */
    int placeOrder(Order order, List<CartItem> cartItems);

    /** All orders of one user, newest first (items included). */
    List<Order> getByUserId(int userId);

    /** One order (with items) - only if it belongs to the given user. */
    Order getById(int orderId, int userId);
}
