<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% 
    // Ensure the context path variable is safely initialized
    if (request.getAttribute("ctx") == null && pageContext.getAttribute("ctx") == null) { 
        pageContext.setAttribute("ctx", request.getContextPath()); 
    } 
%> 

<footer class="footer"> 
    <div class="footer-inner"> 
        <div class="footer-brand"> 
            <!-- Fixed corrupted emoji string -->
            <div class="logo">🍔 Food<span>wala</span></div> 
            <p>Fresh food from 50+ restaurants in Bengaluru, delivered to your door.</p> 
        </div> 
        
        <div class="footer-col"> 
            <h4>Explore</h4> 
            <a href="${ctx}/restaurants">Restaurants</a> 
            <a href="${ctx}/restaurants?cuisine=Biryani">Biryani</a> 
            <a href="${ctx}/restaurants?cuisine=Pizza">Pizza</a> 
            <a href="${ctx}/restaurants?cuisine=Desserts">Desserts</a> 
        </div> 
        
        <div class="footer-col"> 
            <h4>Account</h4> 
            <a href="${ctx}/cart">Cart</a> 
            <a href="${ctx}/orders">My Orders</a> 
            <a href="${ctx}/login">Login</a> 
            <a href="${ctx}/register">Sign up</a> 
        </div> 
        
        <div class="footer-col"> 
            <h4>Tech stack</h4> 
            <!-- Cleaned up character encoding artifacts (Â· changed to &middot;) -->
            <p class="footer-note">Servlets &middot; JSP &middot; JDBC &middot; MySQL &middot; Tomcat 10.1</p> 
        </div> 
    </div> 
    
    <div class="footer-bottom">
        &copy; 2026 Foodwala &middot; Built with plain Java EE on Tomcat 10.1 &middot; Images: Unsplash &amp; Pexels
    </div> 
</footer> 

<div id="toast" class="toast"></div> 
<script src="${ctx}/js/app.js"></script> 
</body> 
</html>
