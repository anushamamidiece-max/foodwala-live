package com.foodwala.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.foodwala.dao.UserDAO;
import com.foodwala.model.User;
import com.foodwala.util.DBUtil;

/** JDBC implementation of UserDAO. */
public class UserDAOImpl implements UserDAO {

	@Override
	public boolean register(User user) {
		if (getByEmail(user.getEmail()) != null) {
			return false; // email already registered
		}
		String sql = "INSERT INTO users (name, email, password, phone, address) VALUES (?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getPhone());
			ps.setString(5, user.getAddress());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.close(con, ps, null);
		}
	}

	@Override
	public User login(String email, String hashedPassword) {
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, hashedPassword);
			rs = ps.executeQuery();
			if (rs.next()) {
				return mapRow(rs);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(con, ps, rs);
		}
	}

	@Override
	public User getById(int id) {
		String sql = "SELECT * FROM users WHERE id = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			return rs.next() ? mapRow(rs) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(con, ps, rs);
		}
	}

	@Override
	public User getByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			return rs.next() ? mapRow(rs) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(con, ps, rs);
		}
	}

	private User mapRow(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setName(rs.getString("name"));
		u.setEmail(rs.getString("email"));
		u.setPassword(rs.getString("password"));
		u.setPhone(rs.getString("phone"));
		u.setAddress(rs.getString("address"));
		return u;
	}
}
