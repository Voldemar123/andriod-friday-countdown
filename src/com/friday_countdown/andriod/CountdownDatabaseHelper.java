package com.friday_countdown.andriod;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Work with application database
 * Manage Friday images
 * @author VMaximenko
 *
 */
public class CountdownDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "CountdownDatabaseHelper";
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "data.db";
	
// Table name
	public static final String IMAGES_TABLE = "images";

// Fields	
	public static final String KEY_ROWID = "_id";
	public static final String NAME = "name";
	public static final String RATING = "rating";
	public static final String CAME_FROM_USER = "cameFromUser";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	
	private static final String DATABASE_CREATE = "CREATE TABLE " + IMAGES_TABLE + " (" + 
			KEY_ROWID +" integer primary key autoincrement, " + 
			NAME + " text not null, " + 
			RATING + " float not null, " +
			WIDTH + " int, " +
			HEIGHT + " int, " +
			CAME_FROM_USER + " boolean not null );";

	CountdownDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Create database " + db.getPath());
		
		db.execSQL(DATABASE_CREATE);
		
// fill source images
		fillSourceImages(db);
	}
	
	public void fillSourceImages(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE);
		db.execSQL(DATABASE_CREATE);
		
		ContentValues values = new ContentValues();

		Log.d(TAG, "Found images " + Constants.IMAGE_NAMES.length);
		for (String imageName : Constants.IMAGE_NAMES) {
			Log.d(TAG, "Fill source image " + imageName);
			
			values.put(NAME, imageName);
			values.put(RATING, 0);
			values.put(CAME_FROM_USER, false);

			db.insert(IMAGES_TABLE, null, values);
			values.clear();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Upgrade database from " + oldVersion + " to " + newVersion);
		
		db.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE);
		onCreate(db);
	}
}