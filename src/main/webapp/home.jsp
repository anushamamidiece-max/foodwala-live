<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.foodwala.model.Restaurant" %>
<%@ page import="com.foodwala.model.CityInfo" %>
<%@ include file="includes/header.jsp" %>
<%
    @SuppressWarnings("unchecked")
    List<Restaurant> restaurants = (List<Restaurant>) request.getAttribute("restaurants");
    String q = (String) request.getAttribute("q");
    String cuisine = (String) request.getAttribute("cuisine");
    boolean vegOnly = Boolean.TRUE.equals(request.getAttribute("vegOnly"));
    String sort = (String) request.getAttribute("sort");
    // NOTE: 'city' is already declared by the static include of header.jsp above
    @SuppressWarnings("unchecked")
    List<CityInfo> cities = (List<CityInfo>) request.getAttribute("cities");
    if (cities == null) cities = CityInfo.SUPPORTED;
    String cartNoteCity = (String) request.getAttribute("cartNoteCity");
    String activeCuisine = (cuisine == null || cuisine.isEmpty() || "all".equalsIgnoreCase(cuisine)) ? "" : cuisine;
    String extra = "";
    if (vegOnly) extra += "&veg=1";
    if (sort != null && !sort.isEmpty()) extra += "&sort=" + sort;
    String[] accents = {"#0e9f6e", "#ff6b35", "#0ea5e9", "#a855f7", "#e11d48", "#eab308"};
    String FALLBACK_IMG = "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=900";
%>

<%-- ============ CINEMATIC INTRO SPLASH - plays on the FIRST open of the session.
     Swipe up (or drag/wheel up, tap, click, Enter) slides it away to the home page.
     Refreshes afterwards skip the video and land at the top of the page. ============ --%>
<div id="fwSplash">
    <video id="fwSplashBg" src="<%= ctx %>/assets/intro.mp4" autoplay muted loop playsinline></video>
    <video id="fwSplashVideo" src="<%= ctx %>/assets/intro.mp4" autoplay muted loop playsinline></video>
    <div id="fwBrand">Food<span>wala</span></div>
    <button id="fwSplashSkip" type="button">Skip &rsaquo;</button>
    <div id="fwCap"></div>
    <div id="fwSplashHint">Swipe up or tap to enter</div>
</div>
<style>
    #fwSplash{position:fixed;inset:0;z-index:99999;background:#000;cursor:pointer;touch-action:none;
              -webkit-user-select:none;user-select:none;
              transition:transform .7s cubic-bezier(.55,0,.3,1),opacity .7s ease,visibility .7s ease}
    #fwSplash video{position:absolute;inset:0;width:100%;height:100%;display:block}
    #fwSplashBg{object-fit:cover;filter:blur(30px) brightness(.4) saturate(1.2);transform:scale(1.25)}
    #fwSplashVideo{object-fit:contain}
    #fwBrand{position:absolute;top:20px;left:50%;transform:translateX(-50%);color:#fff;
             font:800 22px/1 system-ui,sans-serif;letter-spacing:.01em;
             text-shadow:0 2px 12px rgba(0,0,0,.8);pointer-events:none}
    #fwBrand span{color:#3ddc97}
    #fwSplash.gone{transform:translateY(-100%);opacity:.5;visibility:hidden;pointer-events:none}
    #fwSplashSkip{position:absolute;top:16px;right:16px;padding:9px 18px;border:1px solid rgba(255,255,255,.45);
                  border-radius:999px;background:rgba(0,0,0,.35);color:#fff;font:600 13px/1 system-ui,sans-serif;
                  letter-spacing:.04em;cursor:pointer;backdrop-filter:blur(4px)}
    #fwSplashSkip:hover{background:rgba(255,255,255,.18)}
    #fwSplashHint{position:absolute;bottom:26px;left:50%;transform:translateX(-50%);color:#fff;
                  font:600 12px/1 system-ui,sans-serif;letter-spacing:.14em;text-transform:uppercase;
                  text-shadow:0 1px 6px rgba(0,0,0,.7);animation:fwPulse 1.6s ease-in-out infinite;pointer-events:none}
    #fwCap{position:absolute;left:50%;bottom:64px;transform:translateX(-50%);max-width:86%;text-align:center;
           color:#fff;font:700 clamp(15px,2.6vmin,26px)/1.35 system-ui,sans-serif;letter-spacing:.02em;
           text-shadow:0 2px 10px rgba(0,0,0,.85);opacity:0;transition:opacity .45s ease;pointer-events:none}
    #fwCap.show{opacity:1}
    @keyframes fwPulse{0%,100%{opacity:.45}50%{opacity:.95}}
</style>
<script>
(function () {
    var s = document.getElementById('fwSplash');
    if (!s) return;
    /* always land at the very top, even on a plain refresh */
    if ('scrollRestoration' in history) history.scrollRestoration = 'manual';
    window.scrollTo(0, 0);

    /* intro plays only on the FIRST open of the session; refreshes skip it */
    var seen = false;
    try { seen = sessionStorage.getItem('fwIntroSeen') === '1'; } catch (e) {}
    if (seen) { s.remove(); return; }

    var v = document.getElementById('fwSplashVideo');
    var b = document.getElementById('fwSplashBg');
    document.body.style.overflow = 'hidden';   /* lock scroll only while the intro plays */

    /* slide captions synced to the video timeline (responsive HTML, not baked in) */
    var cap = document.getElementById('fwCap');
    var CAPS = [[0.30, 'Dum BIRYANI, straight from the handi'],
                [1.65, 'Crispy DOSA, banana-leaf style'],
                [2.97, 'Smash BURGERS, loaded & juicy'],
                [4.29, 'Wood-fired PIZZA, real cheese pull'],
                [5.61, 'Desserts that end the debate']];
    var ci = -1;
    if (v && cap) v.addEventListener('timeupdate', function () {
        var t = v.currentTime, i = -1;
        for (var k = 0; k < CAPS.length; k++) if (t >= CAPS[k][0]) i = k;
        if (t > 6.5) i = -1;                  /* let the logo end-card speak */
        if (i !== ci) {
            ci = i;
            cap.classList.remove('show');
            setTimeout(function () {
                if (ci < 0) { cap.textContent = ''; return; }
                cap.textContent = CAPS[ci][1];
                cap.classList.add('show');
            }, 180);
        }
    });

    var done = false;
    function enter() {
        if (done) return;
        done = true;
        try { sessionStorage.setItem('fwIntroSeen', '1'); } catch (e) {}
        s.classList.add('gone');
        if (v) v.pause();
        if (b) b.pause();
        document.body.style.overflow = '';    /* unlock scrolling */
        window.scrollTo(0, 0);                /* land at the TOP of the home page */
        setTimeout(function () { s.remove(); }, 750);
    }

    /* Pointer events cover touch-swipe AND mouse-drag.
       touch-action:none on #fwSplash stops the browser from hijacking the swipe. */
    var sy = null;
    s.addEventListener('pointerdown', function (e) { sy = e.clientY; });
    s.addEventListener('pointerup', function (e) {
        if (sy === null) return;
        var dy = sy - e.clientY;              /* positive = moved up */
        sy = null;
        if (dy > 24) { enter(); return; }     /* swipe UP opens the home page */
        if (Math.abs(dy) < 10) enter();       /* a plain tap / click also enters */
    });
    s.addEventListener('pointercancel', function () { sy = null; });
    /* extra safety for older mobiles: raw touch swipe-up */
    var ty = null;
    s.addEventListener('touchstart', function (e) { ty = e.touches[0].clientY; }, { passive: true });
    s.addEventListener('touchend', function (e) {
        if (ty === null) return;
        if (ty - e.changedTouches[0].clientY > 24) enter();
        ty = null;
    }, { passive: true });
    /* stop the page underneath from scrolling while swiping on the intro */
    s.addEventListener('touchmove', function (e) { e.preventDefault(); }, { passive: false });

    document.addEventListener('keydown', function (e) {
        if (!document.getElementById('fwSplash')) return;
        if (e.key === 'Enter' || e.key === ' ' || e.key === 'Escape' || e.key === 'ArrowUp') enter();
    });
})();
</script>

<section class="hero">
    <div class="hero-inner">
        <div>
            <h1>Hungry? <span class="grad">Foodwala</span> has you covered.</h1>
            <p>Fresh meals from <strong>500+ restaurants</strong> across <strong><%= city %></strong>, delivered in minutes.</p>
            <form class="hero-search" action="<%= ctx %>/restaurants" method="get">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4"><circle cx="11" cy="11" r="7"/><path d="M21 21l-4.3-4.3"/></svg>
                <input type="text" name="q" value="<%= q == null ? "" : q %>" placeholder="Search for biryani, pizza, momos…">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
            <div class="hero-stats">
                <span>🍽️ 500+ restaurants</span>
                <span>⏱️ ~30 min delivery</span>
                <span>★ 4.6 avg rating</span>
            </div>
        </div>
        <div class="hero-visual" aria-hidden="true">
            <div class="hero-ring"></div>
            <img class="hero-dish"
                 src="https://images.pexels.com/photos/28674660/pexels-photo-28674660.jpeg?auto=compress&cs=tinysrgb&w=700&h=700&fit=crop"
                 alt="Signature dum biryani">
            <div class="float-card fc1"><img src="https://images.pexels.com/photos/20422138/pexels-photo-20422138.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&fit=crop" alt="">Dosa</div>
            <div class="float-card fc2"><img src="https://images.pexels.com/photos/17907810/pexels-photo-17907810.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&fit=crop" alt="">Pizza</div>
            <div class="float-card fc3"><img src="https://images.pexels.com/photos/34407507/pexels-photo-34407507.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&fit=crop" alt="">Burger</div>
            <div class="float-card fc4"><img src="https://images.pexels.com/photos/32318140/pexels-photo-32318140.jpeg?auto=compress&cs=tinysrgb&w=100&h=100&fit=crop" alt="">Dessert</div>
        </div>
    </div>

    <%-- ============ CITY SELECTOR - six supported cities ============ --%>
    <%-- <%-- <div class="city-strip" aria-label="Choose your delivery city">
        <% for (CityInfo ci : cities) { %>
            <a class="city-card <%= ci.getName().equals(city) ? "city-active" : "" %>"
               href="<%= ctx %>/restaurants?city=<%= ci.getName() %>">
                <img src="<%= ci.getImage() %>" alt="<%= ci.getName() %> food" loading="lazy">
                <span class="city-card-name"><%= ci.getName() %></span>
                <span class="city-card-tag"><%= ci.getTagline() %></span>
                <%= ci.getName().equals(city) ? "<b class=\"city-tick\">✓ delivering here</b>" : "" %>
            </a>
        <% } %>
    </div> --%> --%>
</section>

<main class="container">
    <% if (cartNoteCity != null) { %>
        <div class="cart-city-note">🛒 Your cart still holds items from <strong><%= cartNoteCity %></strong>.
            Keep browsing <%= city %> and adding here will offer to replace the cart — or
            <a href="<%= ctx %>/cart/clear">clear the cart</a> /
            <a href="<%= ctx %>/restaurants?city=<%= cartNoteCity %>">go back to <%= cartNoteCity %></a>.</div>
    <% } %>
    <div class="chips-row">
        <a href="<%= ctx %>/restaurants" class="chip ripple <%= activeCuisine.isEmpty() ? "chip-active" : "" %>">🍽️ All</a>
        <a href="<%= ctx %>/restaurants?cuisine=Biryani<%= extra %>" class="chip ripple <%= "Biryani".equals(activeCuisine) ? "chip-active" : "" %>">🍛 Biryani</a>
        <a href="<%= ctx %>/restaurants?cuisine=South+Indian<%= extra %>" class="chip ripple <%= "South Indian".equals(activeCuisine) ? "chip-active" : "" %>">🥞 South Indian</a>
        <a href="<%= ctx %>/restaurants?cuisine=Pizza<%= extra %>" class="chip ripple <%= "Pizza".equals(activeCuisine) ? "chip-active" : "" %>">🍕 Pizza</a>
        <a href="<%= ctx %>/restaurants?cuisine=Burgers<%= extra %>" class="chip ripple <%= "Burgers".equals(activeCuisine) ? "chip-active" : "" %>">🍔 Burgers</a>
        <a href="<%= ctx %>/restaurants?cuisine=Chinese<%= extra %>" class="chip ripple <%= "Chinese".equals(activeCuisine) ? "chip-active" : "" %>">🍜 Chinese</a>
        <a href="<%= ctx %>/restaurants?cuisine=Rolls<%= extra %>" class="chip ripple <%= "Rolls".equals(activeCuisine) ? "chip-active" : "" %>">🌯 Rolls</a>
        <a href="<%= ctx %>/restaurants?cuisine=Kebabs<%= extra %>" class="chip ripple <%= "Kebabs".equals(activeCuisine) ? "chip-active" : "" %>">🍢 Kebabs</a>
        <a href="<%= ctx %>/restaurants?cuisine=Healthy<%= extra %>" class="chip ripple <%= "Healthy".equals(activeCuisine) ? "chip-active" : "" %>">🥗 Healthy</a>
        <a href="<%= ctx %>/restaurants?cuisine=Desserts<%= extra %>" class="chip ripple <%= "Desserts".equals(activeCuisine) ? "chip-active" : "" %>">🍰 Desserts</a>
        <a href="<%= ctx %>/restaurants?cuisine=Cafe<%= extra %>" class="chip ripple <%= "Cafe".equals(activeCuisine) ? "chip-active" : "" %>">☕ Café</a>
    </div>

    <div class="filters-bar">
        <form id="filterForm" action="<%= ctx %>/restaurants" method="get">
            <% if (q != null && !q.isEmpty()) { %><input type="hidden" name="q" value="<%= q %>"><% } %>
            <% if (!activeCuisine.isEmpty()) { %><input type="hidden" name="cuisine" value="<%= activeCuisine %>"><% } %>

            <label class="veg-switch" title="Show only pure-veg restaurants">
                <input type="checkbox" name="veg" value="1" <%= vegOnly ? "checked" : "" %>
                       onchange="document.getElementById('filterForm').submit()">
                <span class="veg-slider"></span>
                <span class="veg-label">🌱 Pure Veg</span>
            </label>

            <select name="sort" class="sort-select" onchange="document.getElementById('filterForm').submit()">
                <option value="rating" <%= (sort == null || sort.isEmpty() || "rating".equals(sort)) ? "selected" : "" %>>⭐ Rating: high to low</option>
                <option value="delivery" <%= "delivery".equals(sort) ? "selected" : "" %>>⏱ Delivery time: fastest first</option>
                <option value="cost_low" <%= "cost_low".equals(sort) ? "selected" : "" %>>₹ Cost: low to high</option>
                <option value="cost_high" <%= "cost_high".equals(sort) ? "selected" : "" %>>₹ Cost: high to low</option>
            </select>
        </form>
    </div>

    <div class="section-head">
        <h2><%= (q != null && !q.isEmpty()) ? "Results for “" + q + "”" : "Delivery Restaurants in " + city %></h2>
        <span class="count"><%= restaurants == null ? 0 : restaurants.size() %> places</span>
    </div>

    <% if (restaurants == null || restaurants.isEmpty()) { %>
        <div class="empty-state">
            <div class="empty-emoji">🍽️</div>
            <h3>No restaurants found</h3>
            <p>Try a different search or clear the filters.</p>
            <a href="<%= ctx %>/restaurants" class="btn btn-primary">Browse all restaurants</a>
        </div>
    <% } else { %>
        <div class="grid">
            <% int i = 0;
               for (Restaurant r : restaurants) { %>
                <a href="<%= ctx %>/restaurant?id=<%= r.getId() %>"
                   class="card ripple" style="--accent:<%= accents[i++ % accents.length] %>">
                    <div class="card-img-wrap">
                        <img src="<%= r.getImage() %>" alt="<%= r.getName() %>" loading="lazy"
                             onerror="this.onerror=null;this.src='<%= FALLBACK_IMG %>';">
                        <div class="card-overlay"></div>
                        <span class="rating-pill">★ <%= String.format("%.1f", r.getRating()) %></span>
                        <span class="time-pill">⏱ <%= r.getDeliveryTime() %> min</span>
                    </div>
                    <div class="card-body">
                        <div class="card-title-row">
                            <h3><%= r.getName() %></h3>
                            <% if (r.isPureVeg()) { %><span class="veg-tag" title="Pure Veg">🌱 Pure Veg</span><% } %>
                        </div>
                        <p class="cuisines"><%= r.getCuisines() %></p>
                        <div class="card-meta">
                            <span>₹<%= r.getPriceForTwo() %> for two</span>
                            <span class="dot">•</span>
                            <span class="addr"><%= r.getAddress() %></span>
                        </div>
                    </div>
                </a>
            <% } %>
        </div>
    <% } %>
</main>

<%@ include file="includes/footer.jsp" %>
