package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.daoimpl.OrderDAOImpl;
import com.foodwala.model.Order;
import com.foodwala.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Order history of the logged-in user. */
@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        List<Order> orders = new OrderDAOImpl().getByUserId(user.getId());
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "Your orders");
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
