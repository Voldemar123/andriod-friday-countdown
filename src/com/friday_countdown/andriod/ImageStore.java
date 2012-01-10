package com.friday_countdown.andriod;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Work with widget images
 * @author VMaximenko
 *
 */
public class ImageStore extends BaseStore {
	
	private static final String TAG = "ImageStore";
	
//	private Context mContext;
	private CountdownDatabaseHelper imagesData;
	
	protected ArrayList<String> missedImages;
	
	public FridayImage fridayImage;
	
	
	public ImageStore(Context context) {
		super();
		
//		mContext = context;
		imagesData = new CountdownDatabaseHelper(context);
		
//		fillDatabase();
	}

	private void fillDatabase() {
		Log.i(TAG, "Fill database");
		
		SQLiteDatabase db = imagesData.getWritableDatabase();
		imagesData.fillSourceImages(db);		
	}
	
	
// Check for existence of source images, download the missing, if necessary  	
	public boolean checkIsContentExists() throws LocalizedException {
		Log.i(TAG, "Check source images existence");

		checkExternalStorage();
		checkObjectPath();
		
		missedImages = new ArrayList<String>();
		ArrayList<String> images = getSourceImages();
		Log.d(TAG, "Found images " + images.size());
		
		for (String imageName : images) {
			if ( !isImageExists(imageName) )
				missedImages.add(imageName);
		}
		
		return missedImages.size() == 0;
	}
	
// Check existence of image in application store	
	private boolean isImageExists(String imageName) {
		Log.d(TAG, "Check image " + imageName );
		
		File file = new File( Constants.APP_CACHE_PATH + imageName );
	    if (!file.exists()) 
	    	Log.w(TAG, "Image " + imageName + " not exists");

		return file.exists();
	}

	// get list of sources image names 	
	private ArrayList<String> getSourceImages() {
		ArrayList<String> imageNames = new ArrayList<String>();
		
		SQLiteDatabase db = imagesData.getReadableDatabase();
		
	    Cursor cursor = db.query(CountdownDatabaseHelper.IMAGES_TABLE, 
	    		new String[] { CountdownDatabaseHelper.NAME }, 
	    		CountdownDatabaseHelper.CAME_FROM_USER + " = 0", 
	    		null, null, null, null);
	    
	    cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
        	imageNames.add( cursor.getString(0) );
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        
	    return imageNames;
	}

// get all Friday images	
	private ArrayList<FridayImage> getImages() {
		ArrayList<FridayImage> images = new ArrayList<FridayImage>();
		
		SQLiteDatabase db = imagesData.getReadableDatabase();
		
	    Cursor cursor = db.query(CountdownDatabaseHelper.IMAGES_TABLE, 
	    		new String[] { CountdownDatabaseHelper.KEY_ROWID, CountdownDatabaseHelper.NAME, CountdownDatabaseHelper.RATING }, 
	    		null, null, null, null, null);
	    
	    cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
        	FridayImage image = new FridayImage();
        	
        	image.id = cursor.getInt(0);
        	image.name = cursor.getString(1);
        	image.rating = cursor.getFloat(2);
        	
        	images.add(image);
        	
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        
	    return images;
	}
	
// download source image and save it to the application resources	
	public void downloadImage(String imageName) throws LocalizedException {
		Log.d(TAG, "Download image " + imageName);

		File file = new File( Constants.APP_CACHE_PATH + imageName );
		
		HttpURLConnection conn = null;
		
		try {
			URL url = new URL( Constants.APP_SOURCE_IMAGES_URL + imageName);
			Log.d(TAG, "Url " + url.toString());
			
			conn = (HttpURLConnection) url.openConnection();
			
			if ( conn.getResponseCode() != HttpURLConnection.HTTP_OK)
				throw new LocalizedException(R.string.error_connection_status);
			
	        InputStream input = new BufferedInputStream( conn.getInputStream() );
	        OutputStream output = new FileOutputStream(file);
			
	        byte data[] = new byte[1024];
	        int count = 0;
	        
	        while (( count = input.read(data) ) != -1)
	            output.write( data, 0, count );

	        output.flush();
	        output.close();
	        
	        input.close();
			
		} catch (IOException e) {
			Log.e(TAG, "Connection error : " + e.getMessage(), e);
			throw new LocalizedException( R.string.error_connection, e.getMessage() );
		}
        finally {
        	if ( conn != null )
        		conn.disconnect();
        }
	}
	
// select random image based on user rating	
	public void selectRandomImage() {
		Log.d(TAG, "Get random image");
		
		ArrayList<FridayImage> pool = new ArrayList<FridayImage>();
		
		for (FridayImage image : getImages() ) {
			Log.d(TAG, "Fill image pool " + image.name + " ( " + image.rating + " )");
			
			pool.add(image);
			
			if ( image.rating > 0 ) {
				for (int i = 0; i < image.rating; i++) {
					Log.d(TAG, "Increase image chances " + image.name);
					pool.add(image);
				}
			}
		}
		
		Collections.shuffle(pool);
		
		fridayImage = pool.get( new Random().nextInt( pool.size() ) );
	}

// update user rating for image	
	public boolean setRating(float rating) {
		SQLiteDatabase db = imagesData.getWritableDatabase();

		ContentValues args = new ContentValues();
		
		args.put(CountdownDatabaseHelper.RATING, rating);

		return db.update(CountdownDatabaseHelper.IMAGES_TABLE, 
				args,
				CountdownDatabaseHelper.KEY_ROWID + "=" + fridayImage.id, 
				null) > 0;
	}

// get image as picture object	
	public Bitmap getPicture() {
		Log.d(TAG, "Get picture object " + fridayImage.name);
		
		try {
			FileInputStream fis = new FileInputStream( Constants.APP_CACHE_PATH + fridayImage.name );
			BufferedInputStream bis = new BufferedInputStream(fis);
            
			return BitmapFactory.decodeStream(bis);
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found : " + e.getMessage(), e);
		}

		return null;
	}
}
