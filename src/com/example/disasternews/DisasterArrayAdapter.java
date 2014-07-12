/**
 * 
 */
package com.example.disasternews;

import java.util.List;

import com.example.disasternews.models.Disaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *
 */
public class DisasterArrayAdapter extends ArrayAdapter<Disaster> {

    public DisasterArrayAdapter( Context context, List<Disaster> disasters) {
        super( context, 0, disasters );
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
        
        // Find the views within template
        ImageView ivDisasterIcon = (ImageView) v.findViewById(R.id.ivDisasterIcon);
        ivDisasterIcon.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        
        // TODO
        // load the icon based on type
        // imageLoader.displayImage(uri, ivDisasterIcon);
        
        TextView tvType = (TextView) v.findViewById(R.id.tvStreamDisasterType);
        TextView tvCountry = (TextView) v.findViewById(R.id.tvStreamDisasterCountry);
        tvType.setText(disaster.getType());
        tvCountry.setText(disaster.getCountry());
        
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

