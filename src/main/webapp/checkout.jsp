<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.foodwala.model.CartItem" %>
<%@ include file="includes/header.jsp" %>
<%
    @SuppressWarnings("unchecked")
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    int itemTotal = (Integer) request.getAttribute("itemTotal");
    int deliveryFee = (Integer) request.getAttribute("deliveryFee");
    int grandTotal = (Integer) request.getAttribute("grandTotal");
    String savedAddress = loginUser.getAddress() == null ? "" : loginUser.getAddress();
%>

<main class="container narrow">
    <h1 class="page-title">Checkout</h1>

    <div class="cart-layout">
        <form class="checkout-form card-flat" method="post" action="<%= ctx %>/place-order">
            <h3>1 · Delivery address</h3>
            <textarea name="address" rows="3" required placeholder="Flat no, street, landmark…"><%= savedAddress %></textarea>

            <h3>2 · Payment method</h3>
            <label class="pay-option">
                <input type="radio" name="payment" value="COD" checked>
                <span>💵 Cash on Delivery</span>
            </label>
            <label class="pay-option">
                <input type="radio" name="payment" value="UPI">
                <span>📱 UPI (pay on delivery)</span>
            </label>

            <h3>3 · Order summary</h3>
            <div class="summary-items">
                <% for (CartItem ci : cart) { %>
                    <div class="summary-row">
                        <span class="food-icon <%= ci.isVeg() ? "veg" : "nonveg" %>"></span>
                        <span class="s-name"><%= ci.getName() %> × <%= ci.getQuantity() %></span>
                        <span>₹<%= ci.getLineTotal() %></span>
                    </div>
                <% } %>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg ripple">Place Order · ₹<%= grandTotal %></button>
        </form>

        <aside class="bill-card card-flat">
            <h3>Bill Details</h3>
            <div class="bill-row"><span>Item total</span><span>₹<%= itemTotal %></span></div>
            <div class="bill-row">
                <span>Delivery fee</span>
                <% if (deliveryFee == 0) { %><span class="free">FREE</span><% } else { %><span>₹<%= deliveryFee %></span><% } %>
            </div>
            <div class="bill-row total"><span>To Pay</span><span>₹<%= grandTotal %></span></div>
            <p class="bill-hint">Delivered by <strong><%= cart.get(0).getRestaurantName() %></strong></p>
        </aside>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>
