package com.foodwala.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * The six supported delivery cities. All six are real MySQL-backed cities:
 * Bengaluru (500 restaurants) plus Hyderabad, Chennai, Mumbai, Delhi and
 * Pune (50 each), all stored in the same restaurants table via the city
 * column and ordered from exactly like Bengaluru.
 */
public final class CityInfo {

    public static final String DEFAULT_CITY = "Bengaluru";
    public static final String COOKIE_NAME = "fwCity";

    private final String name;
    private final String tagline;
    private final String image;   // food-only hotlink used on the hero city cards

    private CityInfo(String name, String tagline, String image) {
        this.name = name;
        this.tagline = tagline;
        this.image = image;
    }

    public String getName() { return name; }
    public String getTagline() { return tagline; }
    public String getImage() { return image; }

    public static final List<CityInfo> SUPPORTED = Arrays.asList(
        new CityInfo("Bengaluru", "Garden City - dosas, biryani & filter coffee",
            "https://images.pexels.com/photos/28674660/pexels-photo-28674660.jpeg?auto=compress&cs=tinysrgb&w=600&h=400&fit=crop"),
        new CityInfo("Hyderabad", "City of Nizams - dum biryani & irani chai",
            "https://images.pexels.com/photos/9738983/pexels-photo-9738983.jpeg?auto=compress&cs=tinysrgb&w=600&h=400&fit=crop"),
        new CityInfo("Chennai", "Gateway of the South - dosa & Chettinad",
            "https://images.pexels.com/photos/20422133/pexels-photo-20422133.jpeg?auto=compress&cs=tinysrgb&w=600&h=400&fit=crop"),
        new CityInfo("Mumbai", "City of Dreams - vada pav & pav bhaji",
            "https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=600&h=400&q=60"),
        new CityInfo("Delhi", "Capital flavours - chaat, chole & kebabs",
            "https://images.pexels.com/photos/37153389/pexels-photo-37153389.jpeg?auto=compress&cs=tinysrgb&w=600&h=400&fit=crop"),
        new CityInfo("Pune", "Culture capital - misal pav & mastani",
            "https://images.unsplash.com/photo-1565557623262-b51c2513a641?auto=format&fit=crop&w=600&h=400&q=60"));

    /** True when the given name (any case/spacing) is one of the six cities. */
    public static boolean isSupported(String city) {
        return normalize(city) != null;
    }

    /** Canonical city name for loose input ("bengaluru ", "BENGALURU"), else null. */
    public static String normalize(String city) {
        if (city == null) return null;
        String c = city.trim();
        for (CityInfo ci : SUPPORTED) {
            if (ci.name.equalsIgnoreCase(c)) return ci.name;
        }
        return null;
    }

    public static CityInfo get(String city) {
        String n = normalize(city);
        if (n == null) return null;
        for (CityInfo ci : SUPPORTED) {
            if (ci.name.equals(n)) return ci;
        }
        return null;
    }

    /** URL-encoded cookie value for a city name. */
    public static String encode(String city) {
        try {
            return URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return city;
        }
    }

    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
