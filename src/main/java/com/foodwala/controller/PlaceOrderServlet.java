package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.daoimpl.OrderDAOImpl;
import com.foodwala.model.CartItem;
import com.foodwala.model.Order;
import com.foodwala.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Persists the cart as a real order in MySQL and clears the cart.
 *  Works identically for every supported city (Bengaluru, Hyderabad,
 *  Chennai, Mumbai, Delhi, Pune) - all of them live in the same
 *  restaurants / menu_items tables, so all orders are real orders. */
@WebServlet("/place-order")
public class PlaceOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = CartHelper.getCart(session);

        if (user == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String address = request.getParameter("address");
        if (address == null || address.trim().isEmpty()) {
            address = user.getAddress() == null ? "" : user.getAddress();
        }
        String payment = request.getParameter("payment");
        if (!"UPI".equals(payment)) {
            payment = "Cash on Delivery";
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setRestaurantId(cart.get(0).getRestaurantId());
        order.setRestaurantName(cart.get(0).getRestaurantName());
        order.setTotalAmount(CartHelper.grandTotal(cart));
        order.setDeliveryAddress(address.trim());
        order.setPaymentMode(payment);

        int orderId = new OrderDAOImpl().placeOrder(order, cart);
        if (orderId <= 0) {
            request.setAttribute("dbError", "Could not save the order. Please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        CartHelper.clear(session); // cart is now an order
        response.sendRedirect(request.getContextPath() + "/order-success?id=" + orderId);
    }
}