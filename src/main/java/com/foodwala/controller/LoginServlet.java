package com.foodwala.controller;

import java.io.IOException;

import com.foodwala.daoimpl.UserDAOImpl;
import com.foodwala.model.User;
import com.foodwala.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Login form (GET) + credential check (POST). */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("redirect", safeRedirect(request.getParameter("redirect")));
		request.setAttribute("pageTitle", "Login");
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email") == null ? "" : request.getParameter("email").trim();
		String password = request.getParameter("password") == null ? "" : request.getParameter("password");

		User user = new UserDAOImpl().login(email, PasswordUtil.hash(password));
		if (user == null) {
			request.setAttribute("error", "Invalid email or password.");
			request.setAttribute("redirect", safeRedirect(request.getParameter("redirect")));
			request.setAttribute("pageTitle", "Login");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}

		request.getSession().setAttribute("user", user);
		String redirect = safeRedirect(request.getParameter("redirect"));
		response.sendRedirect(request.getContextPath() + "/" + redirect);
	}

	/** Only allow simple internal targets like "checkout" or "orders". */
	private String safeRedirect(String redirect) {
		if (redirect != null && redirect.matches("[a-zA-Z-]{1,30}")) {
			return redirect;
		}
		return "restaurants";
	}
}
