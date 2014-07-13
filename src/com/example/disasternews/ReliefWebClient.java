/**
 * 
 */
package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 *
 */
public class ReliefWebClient {
    private String DETAILS_URL =
            "http://api.rwlabs.org/v1/reports?filter[operator]=AND"
            + "&filter[conditions][0][field]=disaster.id"
            + "&filter[conditions][0][value]=%s"
            + "&filter[conditions][1][field]=format.name"
            + "&filter[conditions][1][value]=Maps";
    
    private String BASE_URL = "http://api.rwlabs.org/v1/disasters?filter[operator]=OR";
    private String CONDITION_COUNTRY = 
            "&filter[conditions][%d][field]=country&filter[conditions][%d][value]=%s";
    private String CONDITION_TYPE = 
            "&filter[conditions][%d][field]=type&filter[conditions][%d][value]=%s";
    private String LIMIT_FIELD =
            "&limit=%d";
    private String SORT_FIELD =
            "&sort[]=date:desc";
    
    private Long limit = (long) 20;
    private Long count = (long) 20;
    private Long offset = (long) 0;
    
    private static ReliefWebClient instance = null;
    
    AsyncHttpClient client = new AsyncHttpClient();
    
    /**
     * 
     */
    private ReliefWebClient() {
    }

    public static ReliefWebClient getInstance() {
        if (instance == null) {
            try {
                instance = new ReliefWebClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    
    /**
     * API call to ReliefWeb to get disaster reports
     * @param countries 
     * 
     * @param handler
     */
    public void getDisasters( ArrayList<String> countries, AsyncHttpResponseHandler handler ) {
        String apiURL = BASE_URL;
        Integer count = 0;
        String param;
        
        for ( String country : countries ) {
            param = String.format(CONDITION_COUNTRY, count, count, Uri.encode(country) );
            apiURL += param;
            count++;
        }
        
        param = String.format(LIMIT_FIELD, limit);
        apiURL += param;
        apiURL += SORT_FIELD;
        
        Log.d("DEBUG", "Making first call to " + apiURL );
        
        client.get(apiURL, handler);
    }
    
    /**
     * API call to ReliefWeb to get list of reports
     * 
     * @param handler
     */
    public void getDisasterMaps( List<String> idList, AsyncHttpResponseHandler handler ) {
        String apiURL = "";
        
        for ( String id : idList ) {
            apiURL = String.format(DETAILS_URL, id);
            Log.d("DEBUG", "Making 2nd call to " + apiURL );
            client.get(apiURL, handler);
        }
        
    }
    
    
    /**
     * API call to get details such as maps and descriptions.
     */
    public void getDisasterDetails( String url, AsyncHttpResponseHandler handler ) {
        // Log.d("DEBUG", "Making 3rd call to " + url );
        client.get(url, handler);
    }
    
    
}
