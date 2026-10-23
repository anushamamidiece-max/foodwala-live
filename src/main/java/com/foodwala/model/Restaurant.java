package com.foodwala.model;

/** Model for the restaurants table. */
public class Restaurant {

    private int id;
    private String name;
    private String cuisines;
    private String city;       // delivery city (Bengaluru = live MySQL city)
    private String address;
    private String image;      // full URL (Unsplash / Pexels)
    private double rating;
    private int deliveryTime;  // minutes
    private int priceForTwo;   // rupees
    private boolean pureVeg;

    public Restaurant() { }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisines() { return cuisines; }
    public void setCuisines(String cuisines) { this.cuisines = cuisines; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(int deliveryTime) { this.deliveryTime = deliveryTime; }

    public int getPriceForTwo() { return priceForTwo; }
    public void setPriceForTwo(int priceForTwo) { this.priceForTwo = priceForTwo; }

    public boolean isPureVeg() { return pureVeg; }
    public void setPureVeg(boolean pureVeg) { this.pureVeg = pureVeg; }
}
