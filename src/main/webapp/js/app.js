/* ============================================================
   FOODWALA - interactions
   1. Ripple effect on cards & buttons (color comes from element)
   2. Toast notifications driven by URL params (?added, ?removed, ?welcome)
   ============================================================ */
(function () {
    'use strict';

    /* ---------- ripple ---------- */
    document.addEventListener('click', function (e) {
        var el = e.target.closest('.ripple');
        if (!el) return;
        var rect = el.getBoundingClientRect();
        var size = Math.max(rect.width, rect.height);
        var span = document.createElement('span');
        span.className = 'ripple-effect';
        span.style.width = span.style.height = size + 'px';
        span.style.left = (e.clientX - rect.left - size / 2) + 'px';
        span.style.top = (e.clientY - rect.top - size / 2) + 'px';
        el.appendChild(span);
        setTimeout(function () { span.remove(); }, 650);
    });

    /* ---------- toast ---------- */
    var toastEl = document.getElementById('toast');
    var toastTimer = null;

    function showToast(msg) {
        if (!toastEl) return;
        toastEl.textContent = msg;
        toastEl.classList.add('show');
        clearTimeout(toastTimer);
        toastTimer = setTimeout(function () { toastEl.classList.remove('show'); }, 3200);
    }

    var params = new URLSearchParams(window.location.search);
    if (params.get('added') === '1') {
        showToast('🛒 Item added to your cart!');
    } else if (params.get('removed') === '1') {
        showToast('Item removed from cart.');
    } else if (params.get('welcome') === '1') {
        showToast('🎉 Welcome to Foodwala! Start ordering.');
    }

    // Clean the URL so the toast doesn't re-appear on refresh
    if (params.has('added') || params.has('removed') || params.has('welcome')) {
        if (window.history && window.history.replaceState) {
            window.history.replaceState({}, '', window.location.pathname);
        }
    }
})();
