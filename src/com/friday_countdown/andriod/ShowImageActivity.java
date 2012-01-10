package com.friday_countdown.andriod;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

/**
 * Show random Friaday image after click to countdown widget
 * Check and download all missed content 
 * @author VMaximenko
 *
 */
public class ShowImageActivity extends Activity {

	private static final String TAG = "ShowImageActivity";
	
	private WebView mWebView;
	private RatingBar mRatingbar;
	private ImageStore mImageStore;
	private ProgressDialog mProgressDialog;
	private Display mDisplay;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Create sctivity");
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);

        mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        
        mWebView = (WebView) findViewById(R.id.friday_image_view);
        mWebView.setBackgroundColor(color.black);
        
        loadImage();
    }
	
	private void loadImage() {
		Log.d(TAG, "Load random Friday image");
		
		mImageStore = new ImageStore(this);
        try {
        	
			if ( !mImageStore.checkIsContentExists() )
				downloadMissingContent(mImageStore);
			else
				initializeUI();
			
		} catch (LocalizedException e) {
			Toast.makeText( getApplicationContext(), 
					e.getString( getApplicationContext() ),
					Toast.LENGTH_LONG ).show();
		}
		
	}

// try to download not exists images
	private void downloadMissingContent(ImageStore store) {
		mProgressDialog = new ProgressDialog(this);
		
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.missing_content_dialog_title)
				.setMessage(R.string.missing_content_dialog_message)
				.setPositiveButton(R.string.yes_button,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
// start task on download missed images, showing the progress	
								new DownloadImagesTask().execute();
							}

						})
				.setNegativeButton(R.string.no_button,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								finish();
							}
						}).show();
		
	}

	class DownloadImagesTask extends AsyncTask<Object, Integer, Boolean> {
		private LocalizedException e;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mProgressDialog.setTitle(R.string.missing_content_progress);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.show();
		}
		
        @Override
        protected Boolean doInBackground(Object... params) {
			publishProgress(0);

			int total = mImageStore.missedImages.size();
			try {
				int piece = 100 / total;
				for (int i = 0; i < total; i++) {
					mImageStore.downloadImage( mImageStore.missedImages.get(i) );
					publishProgress(piece*(i + 1));
				}
			} catch (LocalizedException e) {
				this.e = e;
				return false;
			}

			return true;
        }
        
        @Override
        protected void onProgressUpdate(Integer... progress) {
             super.onProgressUpdate( progress[0] );
             
             mProgressDialog.setProgress( progress[0] );
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
             super.onPostExecute(result);
             
             mProgressDialog.dismiss();
             
             if (result)             
            	 initializeUI();
             else
            	 Toast.makeText(getApplicationContext(),
            			 e.getString( getApplicationContext() ), 
            			 Toast.LENGTH_LONG)
 						.show();
        }
    }
	
//	@Override
//	public Object onRetainNonConfigurationInstance() {
//		Log.i("ShowImageActivity", "onRetainNonConfigurationInstance");
//		
//		Toast.makeText(this, "onRetainNonConfigurationInstance", Toast.LENGTH_SHORT).show();
//	    return null;
//	}
	
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		Log.i("ShowImageActivity", "onConfigurationChanged");
//		
//	    super.onConfigurationChanged(newConfig);
//
//    	setContentView(R.layout.show_image);
//    	initializeUI();
//	    
//	    // Checks the orientation of the screen
//	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//	    }
//	}	
	
    private void initializeUI() {
    	Log.i(TAG, "initializeUI");
    	
    	mImageStore.selectRandomImage();
    	
    	if (mImageStore.fridayImage == null)
    		return;
    	
    	mWebView.getSettings().setSupportZoom(true);
//    	mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

    	mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    	mWebView.setScrollbarFadingEnabled(true);

//    	mWebView.getSettings().setLoadWithOverviewMode(true);
//    	mWebView.getSettings().setUseWideViewPort(true);

//        String imagePath = "3309579_person.png";
//        String html = "<html><head></head><body><img src='"+ imagePath + "'></body></html>";
        
    	mWebView.loadUrl( "file:///"+ Constants.APP_CACHE_PATH + mImageStore.fridayImage.name );
//        webview.loadData( html, "text/html", "UTF-8" );
//        webview.loadDataWithBaseURL("file:///"+ APP_CACHE_PATH +"/a87115/",
//                html,
//                "text/html",
//                "UTF-8",
//                "");
        
    	mWebView.setPadding(0, 0, 0, 0);
    	
    	setWebViewScale();
    	
//    	mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView webView, String url) {
//                super.onPageFinished(webView, url);
//                
//                mWebView.setInitialScale( calcWebViewScale() );
//            }
//        });
    	
    	
    	mRatingbar = (RatingBar) findViewById(R.id.image_rating);
    	mRatingbar.setRating(mImageStore.fridayImage.rating);
        
    	mRatingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            	mImageStore.setRating(rating);
            	
                Toast.makeText(ShowImageActivity.this, 
                		R.string.image_rating_changed, 
                		Toast.LENGTH_SHORT).show();
            }
        });        
    }
  
    private void setWebViewScale() {
    	Log.d(TAG, "Scale picture to web view");
    	
    	Bitmap picture = mImageStore.getPicture();
    	if ( picture == null )
    		return;
    	
    	int width = mDisplay.getWidth(); 
        int height = mDisplay.getHeight(); 

        Log.i(TAG, "Display width " + width);
        Log.i(TAG, "Display height " + height);

        int picWidth = picture.getWidth();
        int picHeight = picture.getHeight();
        
        Log.i(TAG, "Picture width " + picWidth);
        Log.i(TAG, "Picture height " + picHeight);

// TODO: rewrite for rotate device        
        
        Double val = 1d;
        if ( picHeight > height )
        	val = new Double(height) / new Double(picHeight);

    	val = val * 100d;
    	Log.i(TAG, "Scale to " + val);
    
    	mWebView.setInitialScale( val.intValue() );
    }
	
}