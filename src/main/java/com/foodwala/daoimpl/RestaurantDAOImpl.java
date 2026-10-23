package com.foodwala.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodwala.dao.RestaurantDAO;
import com.foodwala.model.CityInfo;
import com.foodwala.model.Restaurant;
import com.foodwala.util.DBUtil;

/** JDBC implementation of RestaurantDAO. */
public class RestaurantDAOImpl implements RestaurantDAO {

    @Override
    public List<Restaurant> getAll(String search, String cuisine, boolean vegOnly, String sort, String city) {
        List<Restaurant> list = new ArrayList<>();
        String theCity = CityInfo.normalize(city);
        if (theCity == null) theCity = CityInfo.DEFAULT_CITY;

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM restaurants WHERE city = ?");
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasCuisine = cuisine != null && !cuisine.trim().isEmpty()
                && !"all".equalsIgnoreCase(cuisine.trim());

        if (hasSearch) {
            sql.append(" AND (name LIKE ? OR cuisines LIKE ? OR address LIKE ?)");
        }
        if (hasCuisine) {
            sql.append(" AND cuisines LIKE ?");
        }
        if (vegOnly) {
            sql.append(" AND is_pure_veg = 1");
        }

        // whitelist-based sort (no user text goes into the ORDER BY)
        String order;
        if ("delivery".equals(sort)) {
            order = "delivery_time ASC, id ASC";
        } else if ("cost_low".equals(sort)) {
            order = "price_for_two ASC, id ASC";
        } else if ("cost_high".equals(sort)) {
            order = "price_for_two DESC, id ASC";
        } else {
            order = "rating DESC, id ASC";
        }
        sql.append(" ORDER BY ").append(order);

        // Home page (no search, no cuisine pill) shows only the top 50 so the
        // page stays short; pills / search / veg-filter always show ALL matches.
        if (!hasSearch && !hasCuisine) {
            sql.append(" LIMIT 50");
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql.toString());
            int idx = 1;
            ps.setString(idx++, theCity);
            if (hasSearch) {
                String like = "%" + search.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (hasCuisine) {
                ps.setString(idx++, "%" + cuisine.trim() + "%");
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("MySQL error while loading restaurants: " + e.getMessage()
                    + " - make sure MySQL is running, the 'foodwala' database was created by "
                    + "sql/database.sql in MySQL Workbench, and DBUtil.java has the right "
                    + "username/password.", e);
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }

    @Override
    public List<Restaurant> getAll(String search, String cuisine, String city) {
        return getAll(search, cuisine, false, null, city);
    }

    @Override
    public Restaurant getById(int id) {
        String sql = "SELECT * FROM restaurants WHERE id = ?";
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

    private Restaurant mapRow(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setId(rs.getInt("id"));
        r.setName(rs.getString("name"));
        r.setCuisines(rs.getString("cuisines"));
        r.setCity(rs.getString("city"));
        r.setAddress(rs.getString("address"));
        r.setImage(rs.getString("image"));
        r.setRating(rs.getDouble("rating"));
        r.setDeliveryTime(rs.getInt("delivery_time"));
        r.setPriceForTwo(rs.getInt("price_for_two"));
        r.setPureVeg(rs.getInt("is_pure_veg") == 1);
        return r;
    }
}
