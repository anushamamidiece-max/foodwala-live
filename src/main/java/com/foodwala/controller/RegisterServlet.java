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

/** Registration form (GET) + account creation (POST). */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "Create account");
		request.getRequestDispatcher("/register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String name = trim(request.getParameter("name"));
		String email = trim(request.getParameter("email"));
		String phone = trim(request.getParameter("phone"));
		String password = request.getParameter("password");
		String address = trim(request.getParameter("address"));

		// basic validation
		if (name.isEmpty() || email.isEmpty() || password == null || password.length() < 4) {
			request.setAttribute("error", "Please fill all required fields (password must be at least 4 characters).");
			request.setAttribute("pageTitle", "Create account");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		//user.setPassword(password);
		user.setPassword(PasswordUtil.hash(password));
		user.setAddress(address);

		boolean ok = new UserDAOImpl().register(user);
		if (!ok) {
			request.setAttribute("error", "This email is already registered. Try logging in instead.");
			request.setAttribute("pageTitle", "Create account");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}

		// auto-login after successful registration
		User saved = new UserDAOImpl().getByEmail(email);
		request.getSession().setAttribute("user", saved);
		response.sendRedirect(request.getContextPath() + "/restaurants?welcome=1");
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
