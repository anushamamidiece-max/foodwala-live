<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="includes/header.jsp" %>
<%
    String dbError = (String) request.getAttribute("dbError");
%>

<main class="container narrow auth-wrap">
    <div class="auth-card card-flat">
        <div class="empty-emoji">⚠️</div>
        <h1>Something went wrong</h1>
        <% if (dbError != null) { %><p class="muted"><%= dbError %></p><% } %>

        <div class="demo-box" style="text-align:left;">
            <strong>Quick checklist:</strong><br>
            1. MySQL server is running.<br>
            2. You ran <code>sql/database.sql</code> in MySQL Workbench (creates DB <code>foodwala</code>).<br>
            3. <code>mysql-connector-j-9.2.0.jar</code> is in <code>src/main/webapp/WEB-INF/lib</code>.<br>
            4. DB username/password match <code>com.foodwala.util.DBUtil</code>.
        </div>

        <a href="<%= ctx %>/restaurants" class="btn btn-primary btn-block">← Back to restaurants</a>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>
