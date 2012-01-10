package com.friday_countdown.andriod;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

public class CountdownWidget extends AppWidgetProvider {
	// update rate in milliseconds
	public static final int UPDATE_RATE = 60000;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds)
			setAlarm(context, appWidgetId, -1);

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Log.i("CountdownWidget", "onDisabled");
		
		context.stopService(new Intent(context, CountdownService.class));
		super.onDisabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i("CountdownWidget", "onUpdate");
		
		for (int appWidgetId : appWidgetIds)
			setAlarm(context, appWidgetId, UPDATE_RATE);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private static void setAlarm(Context context, int appWidgetId, int updateRate) {
		Log.i("CountdownWidget", "setAlarm "+appWidgetId);

		PendingIntent newPending = makeControlPendingIntent(context, CountdownService.UPDATE, appWidgetId);
		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		// on a negative updateRate stop the refreshing
		if (updateRate >= 0)
			alarms.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime(), 
					updateRate, 
					newPending);
		else
			alarms.cancel(newPending);
	}

	public static PendingIntent makeControlPendingIntent(Context context, String command, int appWidgetId) {
		Intent active = new Intent(context, CountdownService.class);

		active.setAction(command);
		active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// this Uri data is to make the PendingIntent unique, so it wont be updated by FLAG_UPDATE_CURRENT
		// so if there are multiple widget instances they wont override each other
		Uri data = Uri.withAppendedPath(
				Uri.parse("countdown_widget://widget/id/#" + command + appWidgetId), 
				String.valueOf(appWidgetId));
		active.setData(data);

		return (PendingIntent.getService(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT));
	}
}
