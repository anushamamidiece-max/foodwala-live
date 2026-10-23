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

/** Removes one line from the cart. */
@WebServlet("/cart/remove")
public class CartRemoveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int menuItemId = 0;
        try { menuItemId = Integer.parseInt(request.getParameter("menuItemId")); } catch (Exception ignored) { }

        HttpSession session = request.getSession();
        List<CartItem> cart = CartHelper.getCart(session);
        CartItem item = CartHelper.findItem(cart, menuItemId);
        if (item != null) {
            cart.remove(item);
        }
        if (cart.isEmpty()) {
            CartHelper.clear(session);
        }

        response.sendRedirect(request.getContextPath() + "/cart?removed=1");
    }
}
