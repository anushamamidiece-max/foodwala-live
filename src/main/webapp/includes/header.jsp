<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.foodwala.model.User" %>
<%@ page import="com.foodwala.model.CartItem" %>
<%@ page import="com.foodwala.model.CityInfo" %>
<%
    String ctx = request.getContextPath();
    User loginUser = (User) session.getAttribute("user");
    @SuppressWarnings("unchecked")
    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
    int cartCount = (cartItems == null) ? 0 : cartItems.size();
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) pageTitle = "Fresh food, delivered fast";

    // active delivery city: request attr (set by servlets) else fwCity cookie
    String city = (String) request.getAttribute("city");
    if (city == null) {
        city = CityInfo.DEFAULT_CITY;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie ck : request.getCookies()) {
                if (CityInfo.COOKIE_NAME.equals(ck.getName())) {
                    String v = CityInfo.normalize(CityInfo.decode(ck.getValue()));
                    if (v != null) city = v;
                }
            }
        }
    }
    List<CityInfo> cityList = CityInfo.SUPPORTED;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script>
/* apply saved theme before first paint (no flash) */
try { document.documentElement.setAttribute('data-theme', localStorage.getItem('fwTheme') || 'light'); } catch (e) {}
</script>
<title><%= pageTitle %> · Foodwala</title>
<link rel="icon" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E<text y='.9em' font-size='90'>%F0%9F%8D%94</text>%3C/svg%3E">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
<link rel="stylesheet" href="<%= ctx %>/css/style.css">
<style>
/* ==== MULTI-CITY COMPONENT STYLES (inside the JSP so they always load;
       the location picker itself uses NATIVE input+datalist widgets, so it
       works even if this CSS never loads - no images, nothing to break) ==== */
.location{display:flex;align-items:center;gap:6px;border:1px solid var(--line,#e6e4dd);
    background:var(--chip,#f4f2ec);color:var(--ink,#1c1c1c);
    font:600 13.5px/1 Poppins,system-ui,sans-serif;padding:8px 12px;border-radius:999px;
    box-shadow:var(--shadow,0 4px 14px rgba(0,0,0,.06));margin:0}
.location svg{flex:none;color:var(--brand,#0e9f6e)}
.location input{border:0;outline:0;background:transparent;color:var(--ink,#1c1c1c);
    font:600 13.5px/1 Poppins,system-ui,sans-serif;width:104px;padding:2px 0;cursor:pointer}
.location .chev{font-size:10px;opacity:.7}
/* city tag pill + demo notices */
.city-tag{display:inline-block;background:var(--chip,#f4f2ec);color:var(--ink,#1c1c1c);
    border:1px solid var(--line,#e6e4dd);font:600 11.5px/1 Poppins,system-ui,sans-serif;
    padding:5px 10px;border-radius:999px;margin-left:4px;vertical-align:middle}
.demo-note{margin:10px 14px 0;padding:9px 12px;border-radius:12px;
    font:500 12.5px/1.55 Poppins,system-ui,sans-serif;
    background:rgba(234,179,8,.12);color:#8a6100;border:1px dashed rgba(234,179,8,.55)}
[data-theme="dark"] .demo-note{background:rgba(234,179,8,.10);color:#f5d478}
.demo-banner{margin:0 0 18px;padding:13px 16px;border-radius:14px;
    font:500 13.5px/1.6 Poppins,system-ui,sans-serif;
    background:linear-gradient(90deg,rgba(234,179,8,.16),rgba(255,107,53,.14));
    border:1px solid rgba(234,179,8,.5);color:#7a5200}
[data-theme="dark"] .demo-banner{color:#f5d478}
.cart-city-note{margin:16px auto 0;max-width:1180px;padding:11px 16px;border-radius:14px;
    background:var(--card,#fff);border:1px solid var(--line,#e6e4dd);
    box-shadow:var(--shadow,0 6px 18px rgba(0,0,0,.08));
    font:500 13px/1.6 Poppins,system-ui,sans-serif;color:var(--ink,#1c1c1c)}
.cart-city-note a{color:var(--brand,#0e9f6e);font-weight:600}
/* unsupported city page */
.unavailable-card{max-width:720px;margin:34px auto;padding:38px 30px;text-align:center}
.unavailable-emoji{font-size:52px;margin-bottom:10px}
.unavailable-card h1{font:800 clamp(20px,3.4vw,30px)/1.3 Poppins,system-ui,sans-serif;color:var(--ink,#1c1c1c)}
.unavailable-sub{margin:26px 0 10px;font:700 15px/1.4 Poppins,system-ui,sans-serif;color:var(--ink,#1c1c1c)}
.unavailable-list{display:flex;flex-wrap:wrap;justify-content:center;gap:8px;margin:0;padding:0;list-style:none}
.unavailable-list a{color:var(--brand,#0e9f6e);font:600 13.5px/1 Poppins,system-ui,sans-serif;
    text-decoration:none;border:1.5px solid var(--line,#e6e4dd);border-radius:999px;padding:9px 14px;
    background:var(--card,#fff)}
.unavailable-list a:hover{border-color:var(--brand,#0e9f6e)}
@media (max-width:600px){ .location input{width:78px} }
</style>
</head>
<body>

<nav class="navbar">
    <div class="nav-inner">
        <a href="<%= ctx %>/restaurants" class="logo ripple">🍔 Food<span>wala</span></a>

        <%-- Zomato-style location picker: ONE pill with a native type-ahead
             search box (input + datalist). Shows the six cities as suggestions;
             Enter/pick a supported city switches it, anything else lands on
             the "not available yet" page. No images, no custom popup. --%>
        <form class="location" action="<%= ctx %>/restaurants" method="get" id="locForm" title="Change delivery city">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4"><path d="M12 21s-7-5.1-7-11a7 7 0 1 1 14 0c0 5.9-7 11-7 11z"/><circle cx="12" cy="10" r="2.6"/></svg>
            <input type="text" id="locInput" name="city" list="fwCities" value="<%= city %>"
                   autocomplete="off" aria-label="Delivery city" placeholder="City…">
            <span class="chev">▾</span>
            <datalist id="fwCities">
                <% for (CityInfo ci : cityList) { %><option value="<%= ci.getName() %>"><% } %>
            </datalist>
        </form>

        <form class="nav-search" action="<%= ctx %>/restaurants" method="get">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4"><circle cx="11" cy="11" r="7"/><path d="M21 21l-4.3-4.3"/></svg>
            <input type="text" name="q" value="<%= request.getAttribute("q") == null ? "" : request.getAttribute("q") %>"
                   placeholder="Search for restaurants, cuisines or dishes…">
        </form>

        <div class="nav-links">
            <button id="themeToggle" class="theme-toggle" type="button" title="Toggle dark / light mode" aria-label="Toggle dark / light mode">
                <span>☀️</span><span>🌙</span><span class="tt-knob"></span>
            </button>
            <a href="<%= ctx %>/restaurants" class="nav-link ripple">Restaurants</a>
            <% if (loginUser != null) { %>
                <a href="<%= ctx %>/orders" class="nav-link ripple">Orders</a>
            <% } %>
            <a href="<%= ctx %>/cart" class="nav-link ripple cart-link">
                <svg width="19" height="19" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2"><circle cx="9" cy="20" r="1.6"/><circle cx="17" cy="20" r="1.6"/><path d="M3 4h2l2.4 12.2A2 2 0 0 0 9.4 18h7.7a2 2 0 0 0 2-1.6L21 8H6"/></svg>
                Cart
                <% if (cartCount > 0) { %><span class="cart-badge"><%= cartCount %></span><% } %>
            </a>
            <% if (loginUser != null) { %>
                <span class="nav-user">Hi, <%= loginUser.getName().split(" ")[0] %></span>
                <a href="<%= ctx %>/logout" class="nav-link ripple logout">Logout</a>
            <% } else { %>
                <a href="<%= ctx %>/login" class="btn btn-outline btn-sm ripple">Login</a>
            <% } %>
        </div>
    </div>
</nav>

<script>
/* dark / light toggle - remembered in localStorage */
(function () {
    var b = document.getElementById('themeToggle');
    if (!b) return;
    b.addEventListener('click', function () {
        var next = document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        try { localStorage.setItem('fwTheme', next); } catch (e) {}
    });
})();

/* location picker: picking a suggestion or pressing Enter submits the city;
   clicking the pill arrow opens the native suggestion list where possible */
(function () {
    var form = document.getElementById('locForm');
    var input = document.getElementById('locInput');
    if (!form || !input) return;
    /* Zomato-style: a unique prefix ("Hyd") resolves to that city */
    function resolvePrefix() {
        var v = input.value.trim().toLowerCase();
        if (!v) { input.value = '<%= city %>'; return; }
        var opts = document.getElementById('fwCities').options;
        var match = null, count = 0;
        for (var i = 0; i < opts.length; i++) {
            if (opts[i].value.toLowerCase().indexOf(v) === 0) { count++; match = opts[i].value; }
        }
        if (count === 1) input.value = match;
    }
    function go() { resolvePrefix(); form.submit(); }
    input.addEventListener('change', go);                    /* picked from list */
    input.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') { e.preventDefault(); go(); } /* typed + Enter */
    });
    form.addEventListener('submit', resolvePrefix);          /* native submits */
    form.querySelector('.chev').addEventListener('click', function () {
        input.focus();
        if (typeof input.showPicker === 'function') { try { input.showPicker(); } catch (e) {} }
    });
})();
</script>