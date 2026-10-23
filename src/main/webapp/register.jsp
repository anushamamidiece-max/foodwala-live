<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="includes/header.jsp" %>
<%
    String error = (String) request.getAttribute("error");
%>

<main class="container narrow auth-wrap">
    <div class="auth-card card-flat">
        <h1>Create your account 🍽️</h1>
        <p class="muted">One account for all your cravings.</p>

        <% if (error != null) { %><div class="alert alert-error"><%= error %></div><% } %>

        <form method="post" action="<%= ctx %>/register">
            <label>Full name *</label>
            <input type="text" name="name" required placeholder="Ananya Sharma">
            <label>Email *</label>
            <input type="email" name="email" required placeholder="you@example.com">
            <label>Phone</label>
            <input type="tel" name="phone" placeholder="98765 43210">
            <label>Password * <small>(min 4 characters)</small></label>
            <input type="password" name="password" required minlength="4" placeholder="••••••••">
            <label>Delivery address</label>
            <input type="text" name="address" placeholder="Flat, street, area…">
            <button type="submit" class="btn btn-primary btn-block ripple">Create account</button>
        </form>

        <div class="auth-alt">Already have an account? <a href="<%= ctx %>/login">Login</a></div>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>
