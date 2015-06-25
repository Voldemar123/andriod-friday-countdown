package com.friday_countdown.andriod;

import java.io.File;

import android.os.Environment;


/**
 * Base application storage working
 * @author VMaximenko
 *
 */
public class BaseStore {
	protected String mObjectPathName = Constants.APP_CACHE_PATH;
	
	private boolean mExternalStorageAvailable, mExternalStorageWriteable;

	protected void checkExternalStorage() throws LocalizedException {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if ( !mExternalStorageAvailable || !mExternalStorageWriteable )
			throw new LocalizedException( R.string.error_external_storage );
	}

	/** 
	 * Check exist path to stored object	
	 */
	protected void checkObjectPath() {
		File file = new File(mObjectPathName);
		file.mkdirs();
	}
	
}
