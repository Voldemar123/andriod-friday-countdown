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

    public static final int PICTURES_NUM = 93;
    public static final int DEFAULT_IMAGE = drawable.f0001;
}
