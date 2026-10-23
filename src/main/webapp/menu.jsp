<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.HashMap, java.util.Map" %>
<%@ page import="com.foodwala.model.Restaurant" %>
<%@ page import="com.foodwala.model.MenuItem" %>
<%@ page import="com.foodwala.model.CartItem" %>
<%@ include file="includes/header.jsp" %>
<%
    Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
    @SuppressWarnings("unchecked")
    List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItems");
    @SuppressWarnings("unchecked")
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    Map<Integer, Integer> qtyMap = new HashMap<>();
    int cartRestaurantId = 0;
    if (cart != null && !cart.isEmpty()) {
        cartRestaurantId = cart.get(0).getRestaurantId();
        for (CartItem ci : cart) qtyMap.put(ci.getMenuItemId(), ci.getQuantity());
    }
    String FALLBACK_IMG = "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=900";
%>

<main class="container">
    <section class="rest-hero card ripple-less">
        <div class="rest-hero-img">
            <img src="<%= restaurant.getImage() %>" alt="<%= restaurant.getName() %>"
                 onerror="this.onerror=null;this.src='<%= FALLBACK_IMG %>';">
        </div>
            <div class="rest-hero-info">
                <span class="city-tag">📍 <%= restaurant.getCity() %></span>
            <div class="card-title-row">
                <h1><%= restaurant.getName() %></h1>
                <% if (restaurant.isPureVeg()) { %><span class="veg-tag">🌱 Pure Veg</span><% } %>
            </div>
            <p class="cuisines"><%= restaurant.getCuisines() %></p>
            <p class="addr-line"><%= restaurant.getAddress() %></p>
            <div class="rest-stats">
                <span class="stat"><span class="rating-pill">★ <%= String.format("%.1f", restaurant.getRating()) %></span></span>
                <span class="stat-divider"></span>
                <span class="stat">⏱ <%= restaurant.getDeliveryTime() %> min</span>
                <span class="stat-divider"></span>
                <span class="stat">₹<%= restaurant.getPriceForTwo() %> for two</span>
            </div>
        </div>
    </section>

    <% if (cartCount > 0) { %>
        <a href="<%= ctx %>/cart" class="floating-cart ripple">
            🛒 <%= cartCount %> item<%= cartCount > 1 ? "s" : "" %> in cart · View cart →
        </a>
    <% } %>

    <section class="menu-section">
        <h2>Menu <span class="count"><%= menuItems.size() %> items</span></h2>

        <%
            String lastCategory = null;
            for (MenuItem item : menuItems) {
                if (lastCategory == null || !lastCategory.equals(item.getCategory())) {
                    if (lastCategory != null) { %></div><% }
                    lastCategory = item.getCategory();
        %>
                    <h3 class="menu-cat"><%= lastCategory %></h3>
                    <div class="menu-list">
        <%      }
                Integer qty = qtyMap.get(item.getId());
        %>
                <div class="menu-item">
                    <div class="menu-item-info">
                        <span class="food-icon <%= item.isVeg() ? "veg" : "nonveg" %>" title="<%= item.isVeg() ? "Vegetarian" : "Non-vegetarian" %>"></span>
                        <h4><%= item.getName() %></h4>
                        <div class="price">₹<%= item.getPrice() %></div>
                        <p class="desc"><%= item.getDescription() %></p>
                    </div>
                    <div class="menu-item-side">
                        <div class="item-thumb">
                            <img src="<%= item.getImage() %>" alt="<%= item.getName() %>" loading="lazy"
                                 onerror="this.onerror=null;this.src='<%= FALLBACK_IMG %>';">
                        </div>
                        <% if (qty != null) { %>
                            <div class="stepper">
                                <a class="step-btn ripple" href="<%= ctx %>/cart/update?menuItemId=<%= item.getId() %>&qty=<%= qty - 1 %>&back=menu">−</a>
                                <span class="qty"><%= qty %></span>
                                <a class="step-btn ripple" href="<%= ctx %>/cart/add?menuItemId=<%= item.getId() %>">+</a>
                            </div>
                        <% } else { %>
                            <a class="add-btn ripple" href="<%= ctx %>/cart/add?menuItemId=<%= item.getId() %>">ADD</a>
                        <% } %>
                    </div>
                </div>
        <%  }
            if (lastCategory != null) { %></div><% } %>
    </section>
</main>

<% if ("1".equals(request.getParameter("conflict"))) { %>
    <div class="toast-bar" id="conflictBar">
        <span>🛒 Your cart has items from another restaurant.</span>
        <a class="btn btn-primary btn-sm" href="<%= ctx %>/cart/clear?add=<%= request.getParameter("pendingItem") %>">Replace cart</a>
        <button class="btn btn-ghost btn-sm" onclick="document.getElementById('conflictBar').style.display='none'">Keep</button>
    </div>
<% } %>

<%@ include file="includes/footer.jsp" %>
