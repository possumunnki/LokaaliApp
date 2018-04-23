package com.example.possumunnki.locaaliapp;

import com.google.android.gms.maps.model.Marker;

/**
 * This class holds dates of the product posts.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class ProductPost {
    /**id of the post*/
    private int id;
    /**title of the post*/
    private String title;
    /**description of the post*/
    private String description;
    /**latitude of the post*/
    private double latitude;
    /**longitude of the post*/
    private double longitude;
    /**time when the post has been posted*/
    private long postedTime;
    /**price of the post*/
    private double price;
    /**amount of the product that user wants to sell*/
    private int amount;
    /**Marker of the post*/
    private Marker marker;

    /**
     * Creates instance of the product post.
     * @param id id of the post
     * @param title title of the post
     * @param description description of the post
     * @param latitude latitude of the post
     * @param longitude longitude of the post
     */
    public ProductPost(int id, String title, String description, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates instance of the product post.
     * @param id id of the post
     * @param title title of the post
     * @param description description of the post
     * @param latitude latitude of the post
     * @param longitude longitude of the post
     * @param postedTime the time when product has been posted
     * @param price price of the product
     * @param amount amount that user is selling
     */
    public ProductPost(int id, String title, String description, double latitude, double longitude, long postedTime, double price, int amount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postedTime = postedTime;
        this.price = price;
        this.amount = amount;
    }

    /**
     * Gets value of id.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     * @param title title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description
     * @param description description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets latitude.
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     * @param latitude latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     * @param longitude longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets posted date and time.
     * @return posted time
     */
    public long getPostedTime() {
        return postedTime;
    }

    /**
     * Sets posted time and date.
     * @param postedTime posted time to set.
     */
    public void setPostedTime(long postedTime) {
        this.postedTime = postedTime;
    }

    /**
     * gets price of the product.
     * @return price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price of the product.
     * @param price price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets amount of the product.
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets amount of the product.
     * @param amount amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets marker.
     * @return marker
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * Sets marker.
     * @param marker marker to set
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
