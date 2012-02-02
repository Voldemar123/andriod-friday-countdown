package com.friday_countdown.andriod;

import android.os.Environment;

public class Constants {

	public static final String APP_PREFS_NAME = "com.friday_countdown.andriod";
	public static final String APP_CACHE_PATH = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + 
		"/Android/data/" + APP_PREFS_NAME + "/cache/";
	
// localhost 	
//	public static final String APP_SOURCE_IMAGES_URL = "http://10.0.2.2/friday_images/";
// GitHub project storage 	
	public static final String APP_SOURCE_IMAGES_URL = "https://github.com/Voldemar123/andriod-friday-countdown/raw/master/pictures/";
	
	protected static final String PREF_GOAL_HOUR = "goal_hour";
	protected static final String PREF_GOAL_MINUTE = "goal_minute";
	
// Friday pictures names list	
	protected static final String IMAGE_NAMES[] = {
			"f0001.jpg",
			"f0002.jpg",
			"f0003.jpg",
			"f0004.jpg",
			"f0005.jpg",
			"f0006.jpg",
			"f0007.jpg",
			"f0008.jpg",
			"f0009.jpg",
			"f0010.jpg",
			"f0011.jpg",
			"f0012.jpg",
			"f0013.jpg",
			"f0014.jpg",
			"f0015.jpg",
			"f0016.jpg",
			"f0017.jpg",
			"f0018.jpg",
			"f0019.jpg",
			"f0020.jpg",
			"f0021.jpg",
			"f0022.jpg",
			"f0023.jpg",
			"f0024.jpg",
			"f0025.jpg",
			"f0026.jpg",
			"f0027.jpg",
			"f0028.jpg",
			"f0029.jpg",
			"f0030.jpg",
			"f0031.jpg",
			"f0032.jpg",
			"f0033.jpg",
			"f0034.jpg",
			"f0035.jpg",
			"f0036.jpg",
			"f0037.jpg",
			"f0038.jpg",
			"f0039.jpg",
			"f0040.jpg",
			"f0041.jpg",
			"f0042.jpg",
			"f0043.jpg",
			"f0044.jpg",
			"f0045.jpg",
			"f0046.jpg",
			"f0047.jpg",
			"f0048.jpg",
			"f0049.jpg",
			"f0050.jpg"
			
	}; 
	
}
