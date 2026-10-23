<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.text.SimpleDateFormat" %>
<%@ page import="com.foodwala.model.Order" %>
<%@ page import="com.foodwala.model.OrderItem" %>
<%@ include file="includes/header.jsp" %>
<%
    @SuppressWarnings("unchecked")
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
%>

<main class="container narrow">
    <h1 class="page-title">Your Orders</h1>

    <% if (orders == null || orders.isEmpty()) { %>
        <div class="empty-state">
            <div class="empty-emoji">📦</div>
            <h3>No orders yet</h3>
            <p>When you place an order it will show up here.</p>
            <a href="<%= ctx %>/restaurants" class="btn btn-primary">Order something tasty</a>
        </div>
    <% } else { %>
        <div class="orders-list">
            <% for (Order o : orders) { %>
                <div class="order-card card-flat">
                    <div class="order-head">
                        <div>
                            <h3><%= o.getRestaurantName() %></h3>
                            <div class="muted small">Order #<%= o.getId() %> · <%= fmt.format(o.getOrderedAt()) %></div>
                        </div>
                        <span class="status-pill status-<%= o.getStatus().toLowerCase() %>"><%= o.getStatus() %></span>
                    </div>
                    <div class="order-items">
                        <% for (OrderItem oi : o.getItems()) { %>
                            <div class="summary-row">
                                <span class="s-name"><%= oi.getItemName() %> × <%= oi.getQuantity() %></span>
                                <span>₹<%= oi.getLineTotal() %></span>
                            </div>
                        <% } %>
                    </div>
                    <div class="order-foot">
                        <span>Paid via <%= o.getPaymentMode() %></span>
                        <span class="order-total">₹<%= o.getTotalAmount() %></span>
                    </div>
                    <a class="btn btn-outline btn-sm" href="<%= ctx %>/restaurant?id=<%= o.getRestaurantId() %>">Reorder →</a>
                </div>
            <% } %>
        </div>
    <% } %>
</main>

<%@ include file="includes/footer.jsp" %>
