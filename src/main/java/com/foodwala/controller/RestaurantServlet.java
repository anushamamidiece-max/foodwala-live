package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.daoimpl.MenuItemDAOImpl;
import com.foodwala.daoimpl.RestaurantDAOImpl;
import com.foodwala.model.MenuItem;
import com.foodwala.model.Restaurant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Shows one restaurant's full menu. */
@WebServlet("/restaurant")
public class RestaurantServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception ignored) { }

        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/restaurants");
            return;
        }

        try {
            Restaurant restaurant = new RestaurantDAOImpl().getById(id);
            if (restaurant == null) {
                response.sendRedirect(request.getContextPath() + "/restaurants");
                return;
            }
            List<MenuItem> menuItems = new MenuItemDAOImpl().getByRestaurantId(id);
            request.setAttribute("restaurant", restaurant);
            request.setAttribute("menuItems", menuItems);
            request.setAttribute("pageTitle", restaurant.getName());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("dbError", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
