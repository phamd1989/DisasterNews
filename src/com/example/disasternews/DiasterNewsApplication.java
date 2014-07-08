package com.example.disasternews;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.codepath.oauth.OAuthBaseClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DiasterNewsApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
		
		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
												 .cacheInMemory()
												 .cacheOnDisc()
												 .build();
		ImageLoaderConfiguration config = new 
				ImageLoaderConfiguration.Builder(getApplicationContext())
										.defaultDisplayImageOptions(defaultOptions)
										.build();
		ImageLoader.getInstance().init(config);
	}

	public static DisasterNewsClient getRestClient() {
		return (DisasterNewsClient) OAuthBaseClient.getInstance(DisasterNewsClient.class,
																DiasterNewsApplication.context);
	}
	
	@Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
