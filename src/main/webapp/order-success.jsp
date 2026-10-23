<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.foodwala.model.Order" %>
<%@ page import="com.foodwala.model.OrderItem" %>
<%@ include file="includes/header.jsp" %>
<%
    Order order = (Order) request.getAttribute("order");
    String live = order.getLiveStatus();
    int step = "PLACED".equals(live) ? 0 : "PREPARING".equals(live) ? 1
             : "OUT FOR DELIVERY".equals(live) ? 2 : 3;
%>

<main class="container narrow">
    <div class="success-card card-flat">
        <div class="success-anim">✅</div>
        <h1>Order placed successfully!</h1>
        <p class="muted">Order <strong>#<%= order.getId() %></strong> from <strong><%= order.getRestaurantName() %></strong></p>

        <span class="status-pill status-<%= live.toLowerCase().replace(' ', '-') %>"><%= live %></span>

        <div class="eta-track">
            <div class="eta-step <%= 0 <= step ? "done" : "" %>"><span class="eta-dot"></span>Placed</div>
            <div class="eta-line <%= 0 < step ? "active" : "" %>"></div>
            <div class="eta-step <%= 1 <= step ? "done" : "" %>"><span class="eta-dot"></span>Preparing</div>
            <div class="eta-line <%= 1 < step ? "active" : "" %>"></div>
            <div class="eta-step <%= 2 <= step ? "done" : "" %>"><span class="eta-dot"></span>Out for delivery</div>
            <div class="eta-line <%= 2 < step ? "active" : "" %>"></div>
            <div class="eta-step <%= 3 <= step ? "done" : "" %>"><span class="eta-dot"></span>Delivered</div>
        </div>

        <div class="summary-items">
            <% for (OrderItem oi : order.getItems()) { %>
                <div class="summary-row">
                    <span class="s-name"><%= oi.getItemName() %> × <%= oi.getQuantity() %></span>
                    <span>₹<%= oi.getLineTotal() %></span>
                </div>
            <% } %>
            <div class="summary-row total">
                <span class="s-name">Total (<%= order.getPaymentMode() %>)</span>
                <span>₹<%= order.getTotalAmount() %></span>
            </div>
        </div>

        <p class="muted small">Delivering to: <%= order.getDeliveryAddress() %></p>

        <div class="success-actions">
            <a class="btn btn-primary ripple" href="<%= ctx %>/orders">Track in My Orders</a>
            <a class="btn btn-ghost ripple" href="<%= ctx %>/restaurants">Hungry again?</a>
        </div>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>
