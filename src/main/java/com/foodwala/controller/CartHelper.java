package com.foodwala.controller;

import java.util.ArrayList;
import java.util.List;

import com.foodwala.model.CartItem;
import jakarta.servlet.http.HttpSession;

/** Small helper that keeps the session-cart logic in one place. */
public final class CartHelper {

    public static final String CART_ATTRIBUTE = "cart";
    public static final int FREE_DELIVERY_ABOVE = 499;
    public static final int DELIVERY_FEE = 29;

    private CartHelper() { }

    @SuppressWarnings("unchecked")
    public static List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }
        return cart;
    }

    public static CartItem findItem(List<CartItem> cart, int menuItemId) {
        for (CartItem ci : cart) {
            if (ci.getMenuItemId() == menuItemId) {
                return ci;
            }
        }
        return null;
    }

    public static int itemTotal(List<CartItem> cart) {
        int total = 0;
        for (CartItem ci : cart) {
            total += ci.getLineTotal();
        }
        return total;
    }

    public static int deliveryFee(List<CartItem> cart) {
        if (cart.isEmpty()) {
            return 0;
        }
        return itemTotal(cart) >= FREE_DELIVERY_ABOVE ? 0 : DELIVERY_FEE;
    }

    public static int grandTotal(List<CartItem> cart) {
        return itemTotal(cart) + deliveryFee(cart);
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
    }
}
