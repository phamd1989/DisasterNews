package com.example.disasternews.models;

import java.util.List;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Disaster")
public class Disaster extends Model {


    @Column(name = "disasterId")
    public int disasterId;
    @Column(name = "url")
    public String url;
    @Column(name = "disasterName")
    public String disasterName;
    @Column(name = "description")
    public String description;
    @Column(name = "date")
    public String date;
    @Column(name = "country")
    public String country;
    @Column(name = "type")
    public String type;
    @Column(name = "imageUrl")
    public String imageUrl;
    @Column(name = "title")
    public String title;
    @Column(name = "favorite")
    public boolean favorite;


    /**
     * Default constructor for every ActiveAndroid model
     */
    public Disaster() {
        super();
    }
    
    /**
     * @param id
     * @param url
     * @param name
     * @param description
     * @param date
     * @param country
     * @param type
     */
    public Disaster(int disasterId, String url, String name, String description,
            String date, String country, String type, String imageUrl, String title) {
        super();
        this.disasterId = disasterId;
        this.url = url;
        this.disasterName = name;
        this.description = description;
        this.date = date;
        this.country = country;
        this.type = type;
        this.imageUrl = imageUrl;
        this.title = title;
        this.favorite = false;
    }
    
    
    /**
     * @return the disasterId
     */
    public int getDisasterId() {
        return disasterId;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return disasterName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }
    

    /**
     * @return the favorite
     */
    public boolean isFavorite() {
        return favorite;
    }
    
    /**
     * @return the favorite
     */
    public boolean getFavorite() {
        return favorite;
    }

    /**
     * @param favorite the favorite to set
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Method to return all the favorite disasters
     * @return
     */
    public static List<Disaster> getFavoriteDisasters() {
        List<Disaster> result = new Select()
        .from(Disaster.class)
        .where("favorite = 1")
        .execute();
        
        Log.d("DEBUG", "Size of favorites: " + result.size());
        
        return result;
    }
    
}
