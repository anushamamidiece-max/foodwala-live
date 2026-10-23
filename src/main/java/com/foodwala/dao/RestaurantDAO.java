package com.foodwala.dao;

import java.util.List;

import com.foodwala.model.Restaurant;

/** DAO contract for restaurant operations. */
public interface RestaurantDAO {

    /**
     * Lists restaurants of ONE city, optionally filtered by a free-text
     * search (name/cuisine), a cuisine keyword, a pure-veg toggle and a
     * sort order. Pass null/empty/false to skip a filter.
     */
    List<Restaurant> getAll(String search, String cuisine, boolean vegOnly, String sort, String city);

    /** Convenience overload: no veg toggle, default (rating) sort. */
    List<Restaurant> getAll(String search, String cuisine, String city);

    /** Single restaurant by id, or null. */
    Restaurant getById(int id);
}
