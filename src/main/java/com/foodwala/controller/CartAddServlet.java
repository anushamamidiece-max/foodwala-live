package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.daoimpl.MenuItemDAOImpl;
import com.foodwala.daoimpl.RestaurantDAOImpl;
import com.foodwala.model.CartItem;
import com.foodwala.model.CityInfo;
import com.foodwala.model.MenuItem;
import com.foodwala.model.Restaurant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Adds an item to the session cart (Zomato style: one restaurant per cart).
 * If the cart already has items from a different restaurant the user is
 * redirected back with conflict=1 so the page can offer "Replace cart".
 */
@WebServlet("/cart/add")
public class CartAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int menuItemId = 0;
        int qty = 1;
        try { menuItemId = Integer.parseInt(request.getParameter("menuItemId")); } catch (Exception ignored) { }
        try { qty = Integer.parseInt(request.getParameter("qty")); } catch (Exception ignored) { }
        if (qty < 1) qty = 1;
        if (qty > 10) qty = 10;

        if (menuItemId <= 0) {
            response.sendRedirect(request.getContextPath() + "/restaurants");
            return;
        }

        MenuItem item = new MenuItemDAOImpl().getById(menuItemId);
        if (item == null) {
            response.sendRedirect(request.getContextPath() + "/restaurants");
            return;
        }

        HttpSession session = request.getSession();
        List<CartItem> cart = CartHelper.getCart(session);

        // one-restaurant-per-cart rule (also guarantees one-city-per-cart)
        if (!cart.isEmpty() && cart.get(0).getRestaurantId() != item.getRestaurantId()) {
            response.sendRedirect(request.getContextPath()
                    + "/restaurant?id=" + item.getRestaurantId()
                    + "&conflict=1&pendingItem=" + menuItemId);
            return;
        }

        CartItem existing = CartHelper.findItem(cart, menuItemId);
        if (existing != null) {
            existing.setQuantity(Math.min(10, existing.getQuantity() + qty));
        } else {
            Restaurant restaurant = new RestaurantDAOImpl().getById(item.getRestaurantId());
            CartItem ci = new CartItem();
            ci.setMenuItemId(item.getId());
            ci.setRestaurantId(item.getRestaurantId());
            ci.setRestaurantName(restaurant != null ? restaurant.getName() : "Restaurant");
            ci.setCity(restaurant != null && restaurant.getCity() != null
                    ? restaurant.getCity() : CityInfo.DEFAULT_CITY);
            ci.setName(item.getName());
            ci.setPrice(item.getPrice());
            ci.setVeg(item.isVeg());
            ci.setImage(item.getImage());
            ci.setQuantity(qty);
            cart.add(ci);
        }

        response.sendRedirect(request.getContextPath()
                + "/restaurant?id=" + item.getRestaurantId() + "&added=1");
    }
}
