
//
package com.foodwala.controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Protects checkout / orders - users must be logged in.
 * After login they are sent back to the page they were trying to open.
 */
@WebFilter(urlPatterns = { "/checkout", "/place-order", "/orders" })
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            // e.g. /Foodwala/checkout -> redirect=checkout
            String uri = request.getRequestURI();
            String ctx = request.getContextPath();
            String target = uri.substring(ctx.length());
            if (target.startsWith("/")) {
                target = target.substring(1);
            }
            response.sendRedirect(ctx + "/login?redirect=" + target.split("[/?]")[0]);
            return;
        }
        chain.doFilter(req, res);
    }
}
