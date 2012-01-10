package com.friday_countdown.andriod;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class CountdownService extends Service {
	public static final String UPDATE = "update";
	public static final String CLICK = "click";
	public static final String PLUS = "plus";
	public static final long MODIFY = 86400000;

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("CountdownService", "onStart "+startId);
		Log.i("CountdownService", "command "+intent.getAction());
		
//		String command = intent.getAction();
		Context self = getApplicationContext();
				
		int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		
		RemoteViews remoteView = new RemoteViews(
				self.getPackageName(), 
				R.layout.friday_countdown_widget);
		
		AppWidgetManager appWidgetManager = 
				AppWidgetManager.getInstance(self);
		
		SharedPreferences prefs = self.getSharedPreferences(Constants.APP_PREFS_NAME, 0);

//		if (command.equals(PLUS)) {
//			SharedPreferences.Editor edit = prefs.edit();
//			edit.putLong("goal" + appWidgetId,
//					prefs.getLong("goal" + appWidgetId, 0) + MODIFY);
//			edit.commit();
//		}

		int goalHour = prefs.getInt(Constants.PREF_GOAL_HOUR + appWidgetId, 0);
		int goalMinute = prefs.getInt(Constants.PREF_GOAL_MINUTE + appWidgetId, 0);
		
		// compute the time left
		FridayTimeLeft timeLeft = new FridayTimeLeft(goalHour, goalMinute, self);
		
		if ( timeLeft.isFridayHasCome() ) {
			remoteView.setViewVisibility (R.id.title, View.GONE);
			remoteView.setViewVisibility (R.id.time_left, View.GONE);
			remoteView.setViewVisibility (R.id.title_has_come, View.VISIBLE);
		}
		else {
			remoteView.setViewVisibility (R.id.title, View.VISIBLE);
			remoteView.setViewVisibility (R.id.time_left, View.VISIBLE);
			remoteView.setViewVisibility (R.id.title_has_come, View.GONE);

			remoteView.setTextViewText( R.id.time_left, timeLeft.getMessage() );
		}
		
//		remoteView.setOnClickPendingIntent(R.id.plusbutton, CountdownWidget
//				.makeControlPendingIntent(getApplicationContext(), PLUS,
//						appWidgetId));

		// Create an Intent to launch Activity
        Intent activityIntent = new Intent(self, ShowImageActivity.class);
        activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(self, appWidgetId, activityIntent, 0);

        // Get the layout for the App Widget and attach an on-click listener to the button
        remoteView.setOnClickPendingIntent(R.id.countdown_layout, pendingIntent);

//		remoteView.setOnClickPendingIntent(R.id.countdown_layout,
//				CountdownWidget.makeControlPendingIntent( getApplicationContext(), CLICK, appWidgetId) );
		
		
// apply changes to widget
		appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
