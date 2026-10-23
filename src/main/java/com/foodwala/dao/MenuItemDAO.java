package com.foodwala.dao;

import java.util.List;

import com.foodwala.model.MenuItem;

/** DAO contract for menu item operations. */
public interface MenuItemDAO {

    /** All menu items of one restaurant, ordered by category. */
    List<MenuItem> getByRestaurantId(int restaurantId);

    /** Single menu item by id, or null. */
    MenuItem getById(int id);
}
