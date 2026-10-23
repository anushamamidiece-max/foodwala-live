package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Checkout page - address + payment method + order summary. */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CartItem> cart = CartHelper.getCart(request.getSession());
        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("itemTotal", CartHelper.itemTotal(cart));
        request.setAttribute("deliveryFee", CartHelper.deliveryFee(cart));
        request.setAttribute("grandTotal", CartHelper.grandTotal(cart));
        request.setAttribute("pageTitle", "Checkout");
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}
