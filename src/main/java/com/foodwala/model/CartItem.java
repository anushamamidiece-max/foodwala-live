package com.foodwala.model;

/** A line of the session cart (cart lives in the HttpSession, like Zomato). */
public class CartItem {

    private int menuItemId;
    private int restaurantId;
    private String restaurantName;
    private String city;           // city the line belongs to (no cross-city carts)
    private String name;
    private int price;
    private boolean veg;
    private String image;
    private int quantity;

    public CartItem() { }

    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getCity() { return city == null ? "Bengaluru" : city; }
    public void setCity(String city) { this.city = city; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getLineTotal() { return price * quantity; }
}
