package com.foodwala.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodwala.dao.MenuItemDAO;
import com.foodwala.model.MenuItem;
import com.foodwala.util.DBUtil;

/** JDBC implementation of MenuItemDAO. */
public class MenuItemDAOImpl implements MenuItemDAO {

    @Override
    public List<MenuItem> getByRestaurantId(int restaurantId) {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ? ORDER BY category, id";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }

    @Override
    public MenuItem getById(int id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
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

    private MenuItem mapRow(ResultSet rs) throws SQLException {
        MenuItem m = new MenuItem();
        m.setId(rs.getInt("id"));
        m.setRestaurantId(rs.getInt("restaurant_id"));
        m.setName(rs.getString("name"));
        m.setDescription(rs.getString("description"));
        m.setPrice(rs.getInt("price"));
        m.setVeg(rs.getInt("is_veg") == 1);
        m.setCategory(rs.getString("category"));
        m.setImage(rs.getString("image"));
        return m;
    }
}
