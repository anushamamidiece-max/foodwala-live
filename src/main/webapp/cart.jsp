<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.foodwala.model.CartItem" %>
<%@ include file="includes/header.jsp" %>
<%
    @SuppressWarnings("unchecked")
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    int itemTotal = request.getAttribute("itemTotal") == null ? 0 : (Integer) request.getAttribute("itemTotal");
    int deliveryFee = request.getAttribute("deliveryFee") == null ? 0 : (Integer) request.getAttribute("deliveryFee");
    int grandTotal = request.getAttribute("grandTotal") == null ? 0 : (Integer) request.getAttribute("grandTotal");
    String FALLBACK_IMG = "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=900";
%>

<main class="container narrow">
    <h1 class="page-title">Your Cart</h1>

    <% if (cart == null || cart.isEmpty()) { %>
        <div class="empty-state">
            <div class="empty-emoji">🛒</div>
            <h3>Your cart is empty</h3>
            <p>Good food is one click away.</p>
            <a href="<%= ctx %>/restaurants" class="btn btn-primary">Browse restaurants</a>
        </div>
    <% } else { %>
        <div class="cart-layout">
            <div class="cart-items card-flat">
                <div class="cart-restaurant">
                    <span class="cart-rest-name">🏪 <%= cart.get(0).getRestaurantName() %>
                        <span class="city-tag">📍 <%= cart.get(0).getCity() %></span>
                    </span>
                    <a href="<%= ctx %>/restaurant?id=<%= cart.get(0).getRestaurantId() %>">Add more items</a>
                </div>

                <% for (CartItem ci : cart) { %>
                    <div class="cart-row">
                        <span class="food-icon <%= ci.isVeg() ? "veg" : "nonveg" %>"></span>
                        <div class="cart-item-info">
                            <h4><%= ci.getName() %></h4>
                            <div class="price muted">₹<%= ci.getPrice() %> each</div>
                        </div>
                        <div class="stepper">
                            <a class="step-btn ripple" href="<%= ctx %>/cart/update?menuItemId=<%= ci.getMenuItemId() %>&qty=<%= ci.getQuantity() - 1 %>">−</a>
                            <span class="qty"><%= ci.getQuantity() %></span>
                            <a class="step-btn ripple" href="<%= ctx %>/cart/update?menuItemId=<%= ci.getMenuItemId() %>&qty=<%= ci.getQuantity() + 1 %>">+</a>
                        </div>
                        <div class="cart-line-total">₹<%= ci.getLineTotal() %></div>
                        <a class="remove-link" title="Remove" href="<%= ctx %>/cart/remove?menuItemId=<%= ci.getMenuItemId() %>">✕</a>
                    </div>
                <% } %>

                <div class="cart-actions">
                    <a class="btn btn-ghost btn-sm" href="<%= ctx %>/cart/clear">Clear cart</a>
                </div>
            </div>

            <aside class="bill-card card-flat">
                <h3>Bill Details</h3>
                <div class="bill-row"><span>Item total</span><span>₹<%= itemTotal %></span></div>
                <div class="bill-row">
                    <span>Delivery fee</span>
                    <% if (deliveryFee == 0) { %><span class="free">FREE</span><% } else { %><span>₹<%= deliveryFee %></span><% } %>
                </div>
                <% if (deliveryFee > 0) { %>
                    <div class="bill-hint">🎉 Add items worth ₹<%= 499 - itemTotal %> more for FREE delivery</div>
                <% } %>
                <div class="bill-row total"><span>To Pay</span><span>₹<%= grandTotal %></span></div>
                <a href="<%= ctx %>/checkout" class="btn btn-primary btn-block ripple">Proceed to Checkout →</a>
            </aside>
        </div>
    <% } %>
</main>

<%@ include file="includes/footer.jsp" %>
