/**
 * 
 */
package com.example.disasternews;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.disasternews.models.Disaster;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *
 */
public class DisasterArrayAdapter extends ArrayAdapter<Disaster> {

    private Map<String, Integer> nameIconMap;
    
    public DisasterArrayAdapter( Context context, List<Disaster> disasters) {
        super( context, 0, disasters );
        
        nameIconMap = new TreeMap<String, Integer>();
        nameIconMap.put("Cold Wave", R.drawable.disaster_cold_wave_100px_bluebox);
        nameIconMap.put("Cyclone", R.drawable.disaster_cyclone_100px_bluebox );
        nameIconMap.put("Tropical Cyclone", R.drawable.disaster_cyclone_100px_bluebox );
        nameIconMap.put("Drought", R.drawable.disaster_drought_100px_bluebox);
        nameIconMap.put("Earthquake", R.drawable.disaster_earthquake_100px_bluebox);
        nameIconMap.put("Epidemic", R.drawable.disaster_epidemic_100px_bluebox);
        nameIconMap.put("Wild Fire", R.drawable.disaster_fire_100px_bluebox);
        nameIconMap.put("Fire", R.drawable.disaster_fire_100px_bluebox);
        nameIconMap.put("Flash Flood", R.drawable.disaster_flash_flood_100px_bluebox);
        nameIconMap.put("Flood", R.drawable.disaster_flood_100px_bluebox);
        nameIconMap.put("Heatwave", R.drawable.disaster_heatwave_100px_bluebox);
        nameIconMap.put("Heavy Rain", R.drawable.disaster_heavy_rain_100px_bluebox);
        nameIconMap.put("Insect Infestation", R.drawable.disaster_insect_infestation_100px_bluebox);
        nameIconMap.put("Landslide", R.drawable.disaster_landslide_100px_bluebox);
        nameIconMap.put("Land Slide", R.drawable.disaster_landslide_100px_bluebox);
        nameIconMap.put("Locust Infestation", R.drawable.disaster_locust_infestation_100px_bluebox);
        nameIconMap.put("Snow Avalanche", R.drawable.disaster_snow_avalanche_100px_bluebox);
        nameIconMap.put("Snowfall", R.drawable.disaster_snowfall_100px_bluebox);
        nameIconMap.put("Storm", R.drawable.disaster_storm_100px_bluebox);
        nameIconMap.put("Storm Surge", R.drawable.disaster_storm_surge_100px_bluebox);
        nameIconMap.put("Technological", R.drawable.disaster_technological_100px_bluebox);
        nameIconMap.put("Tornado", R.drawable.disaster_tornado_100px_bluebox);
        nameIconMap.put("Tsunami", R.drawable.disaster_tsunami_100px_bluebox);
        nameIconMap.put("Violent Wind", R.drawable.disaster_violent_wind_100px_bluebox);
        nameIconMap.put("Volcano", R.drawable.disaster_volcano_100px_bluebox);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        final Disaster disaster = getItem(position);
        
        // Find or inflate the template
        View v;
        if ( convertView == null ) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.disaster_item, parent, false);
        }
        else {
            v = convertView;
        }
        
        TextView tvType = (TextView) v.findViewById(R.id.tvStreamDisasterType);
        TextView tvCountry = (TextView) v.findViewById(R.id.tvStreamDisasterCountry);
        TextView tvDescription = (TextView) v.findViewById(R.id.tvStreamDisasterName);
        String type = disaster.getType();
        tvType.setText(type);
        tvCountry.setText(disaster.getCountry());
        
        String originalName = disaster.getName();
        String[] originalNameArray = originalName.split("-");
        tvDescription.setText( originalNameArray[0] );
        
        // Find the views within template
        ImageView ivDisasterIcon = (ImageView) v.findViewById(R.id.ivDisasterIcon);
        ivDisasterIcon.setImageResource(android.R.color.transparent);
        
        
        if ( nameIconMap.containsKey(type) ) {
            int resId = nameIconMap.get(type);
            ivDisasterIcon.setImageResource(resId);
        }
        else {
            Log.d("DEBUG", "unknown type: " + type);
            ImageLoader imageLoader = ImageLoader.getInstance();
            // load the icon based on type
            imageLoader.displayImage(disaster.getImageUrl(), ivDisasterIcon);
        }
        
        // Set image listener
        ivDisasterIcon.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // set Intents here
                
                // start activities if necessary
            }
            
        });
        
        return v;
    }
    
    
}

