<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="includes/header.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String redirect = request.getAttribute("redirect") == null ? "restaurants" : (String) request.getAttribute("redirect");
%>

<main class="container narrow auth-wrap">
    <div class="auth-card card-flat">
    
        <h1>Welcome back 👋</h1>
        <p class="muted">Login to order fresh food from 50+ restaurants.</p>

        <% if (error != null) { %><div class="alert alert-error"><%= error %></div><% } %>

        <form method="post" action="<%= ctx %>/login">
            <input type="hidden" name="redirect" value="<%= redirect %>">
            <label>Email</label>
            <input type="email" name="email" required placeholder="you@example.com">
            <label>Password</label>
            <input type="password" name="password" required placeholder="••••••••">
            <button type="submit" class="btn btn-primary btn-block ripple">Login</button>
        </form>

        <div class="auth-alt">New to Foodwala? <a href="<%= ctx %>/register">Create an account</a></div>
        <div class="demo-box">Demo login → <strong>demo@foodwala.com</strong> / <strong>demo123</strong></div>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>
