package com.friday_countdown.andriod;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class CountdownService extends Service {
	public static final String UPDATE = "update";
	public static final String CLICK = "click";
	public static final String PLUS = "plus";
	public static final long MODIFY = 86400000;

	private Context mContext;
	
	private static final String TAG = "CountdownService";
	
	@Override
	public void onStart(Intent intent, int startId) {
//		String command = intent.getAction();
		mContext = getApplicationContext();
				
		int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		
		RemoteViews remoteView = new RemoteViews(
				mContext.getPackageName(), 
				R.layout.friday_countdown_widget);
		
		AppWidgetManager appWidgetManager = 
				AppWidgetManager.getInstance(mContext);
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.APP_PREFS_NAME, 0);

//		if (command.equals(PLUS)) {
//			SharedPreferences.Editor edit = prefs.edit();
//			edit.putLong("goal" + appWidgetId,
//					prefs.getLong("goal" + appWidgetId, 0) + MODIFY);
//			edit.commit();
//		}

		int goalHour = prefs.getInt(Constants.PREF_GOAL_HOUR + appWidgetId, 0);
		int goalMinute = prefs.getInt(Constants.PREF_GOAL_MINUTE + appWidgetId, 0);
		boolean notifyMe = prefs.getBoolean(Constants.PREF_NOTIFY_ME + appWidgetId, true);
		int widgetType = prefs.getInt( Constants.PREF_WIDGET_TYPE + appWidgetId, Constants.WIDGET_TYPE_DARK );

		changeWidgetStyle( remoteView, widgetType );
		
		// compute the time left
		FridayTimeLeft timeLeft = new FridayTimeLeft();
		timeLeft.setContext(mContext);
		timeLeft.calc(goalHour, goalMinute);
		
		if ( timeLeft.isFridayStart && notifyMe )
			generateNotification();
		
		int messagePlace;
		if ( timeLeft.isFridayHasCome ||
				timeLeft.isSaturdayHasCome ||
				timeLeft.isSundayHasCome ) {
			remoteView.setViewVisibility(R.id.title, View.GONE);
			remoteView.setViewVisibility(R.id.time_left, View.GONE);
			remoteView.setViewVisibility(R.id.title_has_come, View.VISIBLE);

			messagePlace = R.id.title_has_come;
		}
		else {
			remoteView.setViewVisibility(R.id.title, View.VISIBLE);
			remoteView.setViewVisibility(R.id.time_left, View.VISIBLE);
			remoteView.setViewVisibility(R.id.title_has_come, View.GONE);

// check language spelling			
			remoteView.setTextViewText( R.id.title, timeLeft.getLeftMessage() );
			messagePlace = R.id.time_left;
		}
		
		remoteView.setTextViewText( messagePlace, timeLeft.getMessage() );

		
//		remoteView.setOnClickPendingIntent(R.id.plusbutton, CountdownWidget
//				.makeControlPendingIntent(getApplicationContext(), PLUS,
//						appWidgetId));

		// Create an Intent to launch Activity
        Intent activityIntent = new Intent(mContext, ShowImageActivity.class);
        activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, appWidgetId, activityIntent, 0);

        // Get the layout for the App Widget and attach an on-click listener to the button
        remoteView.setOnClickPendingIntent(R.id.countdown_layout, pendingIntent);

//		remoteView.setOnClickPendingIntent(R.id.countdown_layout,
//				CountdownWidget.makeControlPendingIntent( getApplicationContext(), CLICK, appWidgetId) );
		
		
// apply changes to widget
		appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		
		super.onStart(intent, startId);
	}

	private void changeWidgetStyle(RemoteViews remoteView, int widgetType) {
		int widgetBackground, textColor; 
		
// select widget theme		
		switch (widgetType) {
			case Constants.WIDGET_TYPE_DARK:
				widgetBackground = R.drawable.appwidget_dark_bg_clickable;
				textColor = Color.LTGRAY;
			
				break;

			case Constants.WIDGET_TYPE_DARK_TRANSPARENT:
				widgetBackground = R.drawable.appwidget_dark_transparent_bg_clickable;
				textColor = Color.WHITE;
			
				break;

			case Constants.WIDGET_TYPE_BRIGHT:
				widgetBackground = R.drawable.appwidget_bg_clickable;
				textColor = Color.DKGRAY;
			
				break;
				
			case Constants.WIDGET_TYPE_BRIGHT_TRANSPARENT:
				widgetBackground = R.drawable.appwidget_transparent_bg_clickable;
				textColor = Color.BLACK;
			
				break;

			default:
				widgetBackground = R.drawable.appwidget_dark_bg_clickable;
				textColor = Color.WHITE;
				
				break;
		}
		
		remoteView.setInt( R.id.countdown_layout, "setBackgroundResource", widgetBackground );

		remoteView.setTextColor( R.id.title, textColor );
		remoteView.setTextColor( R.id.time_left, textColor );
		remoteView.setTextColor( R.id.title_has_come, textColor );
	}

// notify user - Friday is begin	
	private void generateNotification() {
        Log.d(TAG, "Notify user - Friday has come");
        
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        CharSequence contentTitle = mContext.getString(R.string.friday_has_come);
        Notification notification = new Notification( R.drawable.ic_launcher, contentTitle, System.currentTimeMillis() );

        CharSequence contentText;
        Intent notificationIntent;

		if (new Random().nextBoolean()) {
// show picture
			Log.d(TAG, "Notify action: enjoy picture");

			contentText = mContext.getString(R.string.action_enjoy_picture);
			
			notificationIntent = new Intent(this, ShowImageActivity.class);
		} else {
// visit Facebook page
			Log.d(TAG, "Notify action: visit Facebook page");

			contentText = mContext.getString(R.string.action_visit_project_page);

			String uri;
// check if Facebook is installed
			try {
				getPackageManager().getApplicationInfo("com.facebook.katana", 0);

				uri = Constants.FACEBOOK_GROUP_LINK_NATIVE;
			} catch (PackageManager.NameNotFoundException e) {

				uri = Constants.FACEBOOK_GROUP_LINK_HTTP;
			}

			notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		}
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);
        notification.defaults = Notification.DEFAULT_ALL;
        
        mNotificationManager.notify( R.string.friday_has_come, notification );
	}

	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
