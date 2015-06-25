package com.friday_countdown.andriod;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Work with widget images
 * @author VMaximenko
 *
 */
public class ImageStore extends BaseStore {
	
	private static final String TAG = "ImageStore";
	
	private CountdownDatabaseHelper imagesData;
	protected ArrayList<String> missedImages;
	public FridayImage fridayImage;
	private String sImageHtml;
	private Context mContext;
	
	public ImageStore(Context context) {
		super();
		
		mContext = context;
		imagesData = new CountdownDatabaseHelper(context);
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
        while (!cursor.isAfterLast()) {
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
	    		new String[] { CountdownDatabaseHelper.KEY_ROWID, 
	    						CountdownDatabaseHelper.NAME,
	    						CountdownDatabaseHelper.WIDTH,
	    						CountdownDatabaseHelper.HEIGHT,
	    						CountdownDatabaseHelper.RATING }, 
	    		null, null, null, null, null);
	    
	    cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        	FridayImage image = new FridayImage();
        	
        	image.id = cursor.getInt(0);
        	image.name = cursor.getString(1);
        	image.width = cursor.getInt(2);
        	image.height = cursor.getInt(3);
        	image.rating = cursor.getFloat(4);
        	
        	images.add(image);
        	
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        
	    return images;
	}
	
	private boolean checkInternetConnection() {
		Log.d(TAG, "Test for connection ");
		
	    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected());
	}
	
// download source image and save it to the application resources	
	public void downloadImage(String imageName) throws LocalizedException {
		Log.d(TAG, "Download image " + imageName);
		
		if ( !checkInternetConnection() ) {
	        Log.e(TAG, "Internet connection not present");
	        throw new LocalizedException( R.string.error_connection_not_present );
		}

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
	        int count;
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

// select default image if resources aren't load 	
	public void loadDefaultImage() {
		Log.d(TAG, "Get default image");
		
		fridayImage = new FridayImage();
		fridayImage.isDefault = true;
		
		checkDefaultImageProperties();
	}
		
// select random image based on user rating	
	public void loadRandomImage() {
		Log.d(TAG, "Get random image");
		
		ArrayList<FridayImage> pool = new ArrayList<FridayImage>();
		
// fill images pool		
		for (FridayImage image : getImages() ) {
			
			pool.add(image);
			
// increase image chances			
			if ( image.rating > 0 ) {
				for (int i = 0; i < image.rating; i++) {
					pool.add(image);
				}
			}
		}
		
		Collections.shuffle(pool);
		
		fridayImage = pool.get( new Random().nextInt( pool.size() ) );
		
		checkImageProperties();
	}

// update user rating for image	
	public void setRating(float rating) {
		Log.d(TAG, "Update picture rating #" + fridayImage.id + " : " + rating);
		
		SQLiteDatabase db = imagesData.getWritableDatabase();

		ContentValues args = new ContentValues();
		
		args.put(CountdownDatabaseHelper.RATING, rating);

		db.update(CountdownDatabaseHelper.IMAGES_TABLE, 
				args,
				CountdownDatabaseHelper.KEY_ROWID + "=" + fridayImage.id, 
				null);
		
		db.close();
	}

// update image dimension
	public void setPictureDimensions(int width, int height) {
		Log.d(TAG, "Update picture width = " + width + ", height = " + height);
		
		SQLiteDatabase db = imagesData.getWritableDatabase();

		ContentValues args = new ContentValues();

		args.put(CountdownDatabaseHelper.WIDTH, width);
		args.put(CountdownDatabaseHelper.HEIGHT, height);

		db.update(CountdownDatabaseHelper.IMAGES_TABLE, args,
				CountdownDatabaseHelper.KEY_ROWID + "=" + fridayImage.id, null);

		db.close();
	}
	
// check resource picture path and dimensions	
	private void checkDefaultImageProperties() {
		if ( fridayImage.width != 0 && fridayImage.height != 0 )
			return;
		
		Resources res = mContext.getResources();
		Bitmap img = BitmapFactory.decodeResource( res, Constants.DEFAULT_IMAGE );
		
// get image file path from resources 
		fridayImage.path = "file:///" + Constants.APP_RESOURCE_PATH + 
				res.getString(Constants.DEFAULT_IMAGE);

		fridayImage.width = img.getWidth();
		fridayImage.height = img.getHeight();
	}
	
// check picture path and dimensions	
	private void checkImageProperties() {
		Log.d(TAG, "Get picture object " + fridayImage.name);
		
		fridayImage.path = Constants.APP_CACHE_PATH + fridayImage.name;

		try {
			if ( fridayImage.width == 0 || fridayImage.height == 0 ) {
				FileInputStream fis = new FileInputStream( fridayImage.path );
				BufferedInputStream bis = new BufferedInputStream(fis);
	            
				Bitmap img = BitmapFactory.decodeStream(bis);
				
				bis.close();
				fis.close();

				if ( img != null ) {
					fridayImage.width = img.getWidth();
					fridayImage.height = img.getHeight();
				}
				
	// only for DB stored images			
				setPictureDimensions(fridayImage.width, fridayImage.height);
			}
			
		} catch (IOException e) {
			Log.e(TAG, "Picture load problem : " + e.getMessage(), e);
		}
		
		fridayImage.path = "file://" + fridayImage.path;
	}

// return HTML document contains Friday picture	
	public String getHtml() {
		if ( sImageHtml != null )
			return sImageHtml;
		
		StringBuffer html = new StringBuffer();
		
		html.append("<html><body style='margin:0;padding:0;'>");
		html.append("<img src='");
		html.append(fridayImage.path);
		html.append("' width='");
		html.append(fridayImage.width);
		html.append("' height='");
		html.append(fridayImage.height);
		html.append("'></body></html>");
		
		return sImageHtml = html.toString();
	}
}
