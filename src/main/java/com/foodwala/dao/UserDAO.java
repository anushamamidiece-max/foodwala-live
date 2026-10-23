package com.foodwala.dao;

import com.foodwala.model.User;

/** DAO contract for user operations. */
public interface UserDAO {

	/** Registers a new user. Returns false if the email already exists. */
	boolean register(User user);

	/** Returns the user when email + (hashed) password match, else null. */
	User login(String email, String hashedPassword);

	/** Fetches a user by id. */
	User getById(int id);

	/** Fetches a user by email (used to detect duplicates). */
	User getByEmail(String email);
}
