package fi.lokaali.LokaaliBackend;

import javax.persistence.*;

/**
 * Created by possumunnki on 1.4.2018.
 */
@Entity
@Table(name = "products")
public class ProductPost {
    @Id @GeneratedValue
    private long id;

    private double latitude;

    private double longitude;

    private String title;
    private String description;

    public ProductPost() {
    }


    public ProductPost(double latitude, double longitude, String title, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
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
