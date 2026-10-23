package com.foodwala.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Empties the cart. When called with ?add=<menuItemId> it clears the old
 * cart and immediately adds the pending item - this is how the
 * "Your cart contains items from another restaurant - replace" flow works.
 */
@WebServlet("/cart/clear")
public class CartClearServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CartHelper.clear(request.getSession());

        String add = request.getParameter("add");
        if (add != null && add.matches("\\d{1,10}")) {
            response.sendRedirect(request.getContextPath() + "/cart/add?menuItemId=" + add);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
