
package com.foodwala.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple SHA-256 password hashing (JDK only - no external libraries).
 */
public class PasswordUtil {

	public static String hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not available", e);
		}
	}
}

/*
 * package com.foodwala.util;
 * 
 * import org.mindrot.jbcrypt.BCrypt;
 * 
 *//**
	 * Secure BCrypt password hashing for a Dynamic Web Project. (Ensure
	 * jbcrypt-0.4.jar is placed inside your WEB-INF/lib folder)
	 */
/*
 * public class PasswordUtil {
 * 
 *//**
	 * Call this when registering a user or when they UPDATE their password.
	 * Generates a secure, 60-character string to save to the database.
	 */
/*
 * public static String hash(String password) { // Automatically adds a random
 * salt and securely hashes the text return BCrypt.hashpw(password,
 * BCrypt.gensalt()); }
 * 
 *//**
	 * Call this during login to check if the input password matches the database
	 * hash.
	 *//*
		 * public static boolean verify(String plainPassword, String hashedPassword) {
		 * try { return BCrypt.checkpw(plainPassword, hashedPassword); } catch
		 * (IllegalArgumentException e) { // Returns false if the database string is
		 * empty, null, or not a BCrypt hash return false; } } }
		 */
