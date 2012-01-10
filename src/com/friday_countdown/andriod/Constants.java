package com.friday_countdown.andriod;

import android.os.Environment;

public class Constants {

	public static final String APP_PREFS_NAME = "com.friday_countdown.andriod";
	public static final String APP_CACHE_PATH = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + 
		"/Android/data/" + APP_PREFS_NAME + "/cache/";
	
// localhost	
	public static final String APP_SOURCE_IMAGES_URL = "http://10.0.2.2/friday_images/";
	
	protected static final String PREF_GOAL_HOUR = "goal_hour";
	protected static final String PREF_GOAL_MINUTE = "goal_minute";
	
}
