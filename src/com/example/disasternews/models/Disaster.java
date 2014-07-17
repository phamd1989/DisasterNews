package com.example.disasternews.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Disaster")
public class Disaster extends Model {

    // public static final String DISASTER_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    public static final String DISASTER_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    
    @Column(name = "disasterId", unique = true )
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
    @Column(name = "relativeTime")
    public String relativeTime;
    @Column(name = "epochTime")
    public Long epochTime;


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
        this.relativeTime = this.getRelativeTimeAgo(date);
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
     * @return the relativeTime
     */
    public String getRelativeTime() {
        return relativeTime;
    }

    /**
     * @return the epochTime
     */
    public Long getEpochTime() {
        return epochTime;
    }

    /**
     * @param epochTime the epochTime to set
     */
    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
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
    
    /**
     * Method to return results
     * @param countries 
     * @return
     */
    public static List<Disaster> getOrderedDisasters( List<String> countries) {
        Log.d("DEBUG", "countries " + countries.toString() );
        
        List<Disaster> result = null;
        
        String countryQuery = "";
        if ( countries.size() > 0 ) {
            countryQuery = String.format("country = %s", countries.get(0));
        }
        
        for ( int i=1; i<countries.size(); i++ ) {
            countryQuery += String.format("OR country = '%s'", countries.get(i));
        }
        
        Log.d( "DEBUG", "countryQuery: " + countryQuery );
        
        result = new Select()
        .from(Disaster.class)
        //.where( countryQuery )
        .orderBy("epochTime")
        .execute();
        
        Collections.reverse(result);
        
        return result;
    }
    
    public static Disaster getDisasterInfo(int dId) {
		return (Disaster) new Select().from(Disaster.class).where("disasterId = ?", dId).executeSingle();
	}
    
    /**
     * Method to parse the created_at field from twitter.
     * 
     * getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
     * 
     * @param rawJsonDate
     * @return relativeDate of the tweet
     */
    public String getRelativeTimeAgo(String rawJsonDate) {
        SimpleDateFormat sf = new SimpleDateFormat(DISASTER_FORMAT, Locale.ENGLISH);
        sf.setLenient(true);
     
        String relativeDate = "";
        try {            
            long dateMillis = sf.parse(rawJsonDate).getTime();
            this.epochTime = dateMillis;
            
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        return relativeDate;
    }
    
}
