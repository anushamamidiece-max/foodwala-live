package com.foodwala.controller;

import java.io.IOException;
import java.util.List;

import com.foodwala.daoimpl.RestaurantDAOImpl;
import com.foodwala.model.CityInfo;
import com.foodwala.model.CartItem;
import com.foodwala.model.Restaurant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Landing page - lists the selected city's restaurants (top 50 on home)
 *  with search, cuisine, pure-veg filter and sorting. */
@WebServlet("/restaurants")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ---- resolve the city: ?city= param wins, then the fwCity cookie ----
        String paramCity = request.getParameter("city");
        String city = CityInfo.DEFAULT_CITY;
        if (paramCity != null && !paramCity.trim().isEmpty()) {
            city = CityInfo.decode(paramCity);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (CityInfo.COOKIE_NAME.equals(c.getName())) {
                        city = CityInfo.decode(c.getValue());
                    }
                }
            }
        }

        // ---- unsupported / misspelt city: never fall back to Bengaluru ----
        if (!CityInfo.isSupported(city)) {
            request.setAttribute("attemptedCity", city);
            request.setAttribute("cities", CityInfo.SUPPORTED);
            request.setAttribute("pageTitle", "City not available yet");
            request.getRequestDispatcher("/city-unavailable.jsp").forward(request, response);
            return;
        }
        city = CityInfo.normalize(city);

        // remember the choice (30 days) when it came from a link/param
        if (paramCity != null && !paramCity.trim().isEmpty()) {
            Cookie ck = new Cookie(CityInfo.COOKIE_NAME, CityInfo.encode(city));
            ck.setPath("/");
            ck.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(ck);
        }

        String q = request.getParameter("q");
        String cuisine = request.getParameter("cuisine");
        boolean vegOnly = "1".equals(request.getParameter("veg"));
        String sort = request.getParameter("sort");

        try {
            List<Restaurant> restaurants =
                    new RestaurantDAOImpl().getAll(q, cuisine, vegOnly, sort, city);
            request.setAttribute("restaurants", restaurants);
            request.setAttribute("q", q == null ? "" : q);
            request.setAttribute("cuisine", cuisine == null ? "" : cuisine);
            request.setAttribute("vegOnly", vegOnly);
            request.setAttribute("sort", sort == null ? "" : sort);
            request.setAttribute("city", city);
            request.setAttribute("cities", CityInfo.SUPPORTED);

            // switching city while the cart holds another city's items?
            @SuppressWarnings("unchecked")
            List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
            if (cart != null && !cart.isEmpty() && !city.equals(cart.get(0).getCity())) {
                request.setAttribute("cartNoteCity", cart.get(0).getCity());
            }

            request.setAttribute("pageTitle", "Order food online in " + city);
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("dbError", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
