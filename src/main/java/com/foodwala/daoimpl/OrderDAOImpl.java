package com.foodwala.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foodwala.dao.OrderDAO;
import com.foodwala.model.CartItem;
import com.foodwala.model.Order;
import com.foodwala.model.OrderItem;
import com.foodwala.util.DBUtil;

/** JDBC implementation of OrderDAO. */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public int placeOrder(Order order, List<CartItem> cartItems) {
        Connection con = null;
        PreparedStatement orderPs = null;
        PreparedStatement itemPs = null;
        ResultSet keys = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            orderPs = con.prepareStatement(
                    "INSERT INTO orders (user_id, restaurant_id, restaurant_name, total_amount, "
                            + "delivery_address, payment_mode, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, order.getUserId());
            orderPs.setInt(2, order.getRestaurantId());
            orderPs.setString(3, order.getRestaurantName());
            orderPs.setInt(4, order.getTotalAmount());
            orderPs.setString(5, order.getDeliveryAddress());
            orderPs.setString(6, order.getPaymentMode());
            orderPs.setString(7, "PLACED");
            orderPs.executeUpdate();

            keys = orderPs.getGeneratedKeys();
            if (!keys.next()) {
                con.rollback();
                return -1;
            }
            int orderId = keys.getInt(1);

            itemPs = con.prepareStatement(
                    "INSERT INTO order_items (order_id, menu_item_id, item_name, price, quantity) "
                            + "VALUES (?, ?, ?, ?, ?)");
            for (CartItem ci : cartItems) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, ci.getMenuItemId());
                itemPs.setString(3, ci.getName());
                itemPs.setInt(4, ci.getPrice());
                itemPs.setInt(5, ci.getQuantity());
                itemPs.addBatch();
            }
            itemPs.executeBatch();

            con.commit();
            return orderId;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignored) { }
            }
            return -1;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ignored) { }
            }
            DBUtil.close(null, orderPs, keys);
            DBUtil.close(con, itemPs, null);
        }
    }

    @Override
    public List<Order> getByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(
                    "SELECT * FROM orders WHERE user_id = ? ORDER BY ordered_at DESC");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(mapRow(rs));
            }
            for (Order o : orders) {
                loadItems(con, o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return orders;
    }

    @Override
    public Order getById(int orderId, int userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement("SELECT * FROM orders WHERE id = ? AND user_id = ?");
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Order o = mapRow(rs);
                loadItems(con, o);
                return o;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(con, ps, rs);
        }
    }

    private void loadItems(Connection con, Order order) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(
                    "SELECT * FROM order_items WHERE order_id = ? ORDER BY id");
            ps.setInt(1, order.getId());
            rs = ps.executeQuery();
            List<OrderItem> items = new ArrayList<>();
            while (rs.next()) {
                OrderItem oi = new OrderItem();
                oi.setId(rs.getInt("id"));
                oi.setOrderId(rs.getInt("order_id"));
                oi.setMenuItemId(rs.getInt("menu_item_id"));
                oi.setItemName(rs.getString("item_name"));
                oi.setPrice(rs.getInt("price"));
                oi.setQuantity(rs.getInt("quantity"));
                items.add(oi);
            }
            order.setItems(items);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException ignored) { } }
            if (ps != null) { try { ps.close(); } catch (SQLException ignored) { } }
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setRestaurantId(rs.getInt("restaurant_id"));
        o.setRestaurantName(rs.getString("restaurant_name"));
        o.setTotalAmount(rs.getInt("total_amount"));
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setPaymentMode(rs.getString("payment_mode"));
        o.setStatus(rs.getString("status"));
        o.setOrderedAt(rs.getTimestamp("ordered_at"));
        return o;
    }
}
