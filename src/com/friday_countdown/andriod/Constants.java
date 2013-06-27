package com.friday_countdown.andriod;

import com.friday_countdown.andriod.R.drawable;

import android.os.Environment;

public class Constants {

	public static final String APP_RESOURCE_PATH = "android_";
	public static final String APP_PREFS_NAME = "com.friday_countdown.andriod";
	public static final String APP_CACHE_PATH = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + 
		"/Android/data/" + APP_PREFS_NAME + "/cache/";
	
	public static final String APP_FACEBOOK_GROUP = "346930055333462";
	public static final String FACEBOOK_GROUP_LINK_NATIVE = "fb://group/" + Constants.APP_FACEBOOK_GROUP;
	public static final String FACEBOOK_GROUP_LINK_HTTP = "http://m.facebook.com/groups/" + Constants.APP_FACEBOOK_GROUP;
	
// localhost 	
//	public static final String APP_SOURCE_IMAGES_URL = "http://10.0.2.2/friday_images/";
// GitHub project storage 	
	public static final String APP_SOURCE_IMAGES_URL = "https://github.com/Voldemar123/andriod-friday-countdown/raw/master/pictures/";
	
	protected static final String PREF_GOAL_HOUR = "goal_hour";
	protected static final String PREF_GOAL_MINUTE = "goal_minute";
	protected static final String PREF_NOTIFY_ME = "notify_me";
	protected static final String PREF_WIDGET_TYPE = "widget_type";

// widget type (background color)	
	public static final int WIDGET_TYPE_DARK = 1;
	public static final int WIDGET_TYPE_DARK_TRANSPARENT = 2;
	public static final int WIDGET_TYPE_BRIGHT = 3;
	public static final int WIDGET_TYPE_BRIGHT_TRANSPARENT = 4;
	
	public static final int DEFAULT_IMAGE = drawable.f0001;
	
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
			"f0050.jpg",
			"f0051.jpg",
			"f0052.jpg",
			"f0053.jpg",
			"f0054.jpg",
			"f0055.jpg",
			"f0056.jpg",
			"f0057.jpg",
			"f0058.jpg",
			"f0059.jpg",
			"f0060.jpg",
			"f0061.jpg",
			"f0062.jpg",
			"f0063.jpg",
			"f0064.jpg",
			"f0065.jpg",
			"f0066.jpg",
			"f0067.jpg",
			"f0068.jpg",
			"f0069.jpg",
			"f0070.jpg",
			"f0071.jpg",
			"f0072.jpg",
			"f0073.jpg",
			"f0074.jpg",
			"f0075.jpg",
			"f0076.jpg",
			"f0077.jpg",
			"f0078.jpg",
			"f0079.jpg",
			"f0080.jpg",
			"f0081.jpg",
			"f0082.jpg",
			"f0083.jpg"
	}; 
	
}
