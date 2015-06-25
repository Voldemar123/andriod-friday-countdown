package com.friday_countdown.andriod;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Show information about countdown widget
 * @author VMaximenko
 *
 */
public class ShowInfoActivity extends Activity {

	Button mCloseButton;
	
	private static final String TAG = "ShowInfoActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Create sctivity");
		
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        setContentView(R.layout.show_info);
        
        getWindow().setFeatureDrawableResource(
        		Window.FEATURE_LEFT_ICON,
        		android.R.drawable.ic_dialog_info);
        
     // close button
     		Button close = (Button) findViewById(R.id.about_close_button);
     		close.setOnClickListener(new OnClickListener() {
     			@Override
     			public void onClick(View arg0) {
     				finish();
     			}
     		});
	}

}
