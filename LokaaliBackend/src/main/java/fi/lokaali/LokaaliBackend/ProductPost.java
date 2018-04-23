package fi.lokaali.LokaaliBackend;

import javax.persistence.*;

/**
 * Created by possumunnki on 1.4.2018.
 */
@Entity
@Table(name = "products")
public class ProductPost {
    @Id @GeneratedValue
    @Column(name = "id")
    private long id;
    private double latitude;
    private double longitude;
    private String title;
    private String description;

    private double price;
    private int maxAmount;
    private long timePosted;

    public ProductPost() {
    }


    public ProductPost(double latitude, double longitude, String title, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
    }

    public ProductPost(double latitude, double longitude, String title, String description, long timePosted) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.timePosted = timePosted;
    }

    public ProductPost(double latitude, double longitude, String title, String description, double price, int maxAmount, long timePosted) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.price = price;
        this.maxAmount = maxAmount;
        this.timePosted = timePosted;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(long timePosted) {
        this.timePosted = timePosted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
