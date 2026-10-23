package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Shows the cart page with bill details. */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CartItem> cart = CartHelper.getCart(request.getSession());
        request.setAttribute("itemTotal", CartHelper.itemTotal(cart));
        request.setAttribute("deliveryFee", CartHelper.deliveryFee(cart));
        request.setAttribute("grandTotal", CartHelper.grandTotal(cart));
        request.setAttribute("pageTitle", "Your cart");
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
