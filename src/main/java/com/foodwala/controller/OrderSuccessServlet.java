package com.foodwala.controller;

import java.io.IOException;

import com.foodwala.daoimpl.OrderDAOImpl;
import com.foodwala.model.Order;
import com.foodwala.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** "Order placed" confirmation page. */
@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        int id = 0;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (Exception ignored) { }

        Order order = null;
        if (user != null && id > 0) {
            try {
                order = new OrderDAOImpl().getById(id, user.getId());
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("dbError", e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
        }
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        request.setAttribute("order", order);
        request.setAttribute("pageTitle", "Order placed!");
        request.getRequestDispatcher("/order-success.jsp").forward(request, response);
    }
}