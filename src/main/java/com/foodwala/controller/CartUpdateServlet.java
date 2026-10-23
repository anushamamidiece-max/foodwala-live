package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Changes the quantity of a cart line (quantity 0 removes it). */
@WebServlet("/cart/update")
public class CartUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int menuItemId = 0;
        int qty = 0;
        try { menuItemId = Integer.parseInt(request.getParameter("menuItemId")); } catch (Exception ignored) { }
        try { qty = Integer.parseInt(request.getParameter("qty")); } catch (Exception ignored) { }

        HttpSession session = request.getSession();
        List<CartItem> cart = CartHelper.getCart(session);
        CartItem item = CartHelper.findItem(cart, menuItemId);
        int restaurantId = cart.isEmpty() ? 0 : cart.get(0).getRestaurantId();

        if (item != null) {
            if (qty <= 0) {
                cart.remove(item);
            } else {
                item.setQuantity(Math.min(10, qty));
            }
        }
        if (cart.isEmpty()) {
            CartHelper.clear(session);
        }

        String back = request.getParameter("back");
        if ("menu".equals(back) && restaurantId > 0) {
            response.sendRedirect(request.getContextPath() + "/restaurant?id=" + restaurantId);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
