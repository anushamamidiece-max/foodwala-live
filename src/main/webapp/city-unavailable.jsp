<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.foodwala.model.CityInfo" %>
<%@ include file="includes/header.jsp" %>
<%
    String attempted = (String) request.getAttribute("attemptedCity");
    if (attempted == null || attempted.trim().isEmpty()) attempted = "This city";
    @SuppressWarnings("unchecked")
    List<CityInfo> cities = (List<CityInfo>) request.getAttribute("cities");
    if (cities == null) cities = CityInfo.SUPPORTED;
%>

<main class="container narrow">
    <div class="unavailable-card card-flat">
        <div class="unavailable-emoji">🛵💨</div>
        <h1>Sorry! Foodwala is not available in this city yet.</h1>
        <p class="muted">We are coming soon. You asked for
            <strong>“<%= attempted %></strong>” — the moment our riders land there,
            this page will turn into a menu.</p>
        <h3 class="unavailable-sub">Meanwhile, order from one of our supported cities:</h3>
        <ul class="unavailable-list">
            <% for (CityInfo ci : cities) { %>
                <li><a href="<%= ctx %>/restaurants?city=<%= ci.getName() %>">📍 <%= ci.getName() %></a></li>
            <% } %>
        </ul>
    </div>
</main>

<%@ include file="includes/footer.jsp" %>