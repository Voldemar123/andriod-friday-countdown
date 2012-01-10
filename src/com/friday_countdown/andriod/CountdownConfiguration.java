package com.friday_countdown.andriod;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class CountdownConfiguration extends Activity {

	private Context self = this;
	private int mAppWidgetId;
	private TimePicker mTimePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get the appWidgetId of the appWidget being configured
		Intent launchIntent = getIntent();
		Bundle extras = launchIntent.getExtras();
		if (extras != null)
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
			finish();

		// set the result for cancel first
		Intent cancelResultValue = new Intent();
		cancelResultValue.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_CANCELED, cancelResultValue);

		setContentView(R.layout.configuration);

		mTimePicker = (TimePicker) findViewById(R.id.friday_start_time);
		mTimePicker.setCurrentHour(19);
		mTimePicker.setCurrentMinute(0);
		
		// the OK button
		Button ok = (Button) findViewById(R.id.ok_button);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = self.getSharedPreferences(Constants.APP_PREFS_NAME, 0);
				SharedPreferences.Editor edit = prefs.edit();
				
				edit.putInt(Constants.PREF_GOAL_HOUR + mAppWidgetId, mTimePicker.getCurrentHour());
				edit.putInt(Constants.PREF_GOAL_MINUTE + mAppWidgetId, mTimePicker.getCurrentMinute());
				
				edit.commit();

				// fire an update to display initial state of the widget
				PendingIntent updatepending = 
						CountdownWidget.makeControlPendingIntent(self, CountdownService.UPDATE, mAppWidgetId);
				try {
					updatepending.send();
				} catch (CanceledException e) {
					e.printStackTrace();
				}
				// change the result to OK
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});

		// cancel button
		Button cancel = (Button) findViewById(R.id.cancel_button);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
