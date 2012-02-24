package com.friday_countdown.andriod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;

public class FridayTimeLeft {

	private final int MINUTE = 60 * 1000;
	private final int HOUR = 60 * MINUTE;
	private final int DAY = 24 * HOUR;

	private Date mCurDate, mStartNextFriday;
	private Calendar mCal;

	public boolean isFridayHasCome; 
	public boolean isFridayStart; 

	public FridayTimeLeft() {
		mCal = GregorianCalendar.getInstance();
	}

	public FridayTimeLeft(Date today) {
		mCal = GregorianCalendar.getInstance();
		mCal.setTime(today);
	}
		
	public void calc(int goalHour, int goalMinute) {
		mCurDate = mCal.getTime();
		
		mCal.set(GregorianCalendar.DAY_OF_WEEK, Calendar.FRIDAY);
		mCal.set(GregorianCalendar.HOUR_OF_DAY, goalHour);
		mCal.set(GregorianCalendar.MINUTE, goalMinute);
		mCal.set(GregorianCalendar.SECOND, 0);

		Date startFriday = mCal.getTime();

// time to start Friday		
		if ( mCurDate.getDay() == startFriday.getDay() && 
				mCurDate.getHours() == startFriday.getHours() &&
				mCurDate.getMinutes() == startFriday.getMinutes() )
			isFridayStart = true;
		
		mCal.set(GregorianCalendar.DAY_OF_WEEK, Calendar.SATURDAY);
		mCal.set(GregorianCalendar.HOUR_OF_DAY, 0);
		mCal.set(GregorianCalendar.MINUTE, 0);
		
		Date endFriday = mCal.getTime();
		
		isFridayHasCome = mCurDate.after(startFriday) && mCurDate.before(endFriday);
		
// count next Friday		
		mCal.setTime(startFriday);
		if ( mCurDate.after( mCal.getTime() ) )
			mCal.add( GregorianCalendar.WEEK_OF_YEAR, 1 );
		
		mStartNextFriday = mCal.getTime();
	}

	public String getMessage(Context context) {
		String msg = "";
		
		if ( isFridayHasCome ) {
			if ( context == null )
				return "пятница пришла!";
			else
				return context.getString(R.string.friday_has_come);
		}
		else {
			long left = mStartNextFriday.getTime() - mCurDate.getTime();

			int days = (int) Math.ceil(left / DAY);
            left = left - days * DAY;
            int hours = (int) Math.ceil(left / HOUR);
            left = left - hours * HOUR;
            int mins = (int) Math.ceil(left / MINUTE) + 1;
            
// left days
			if ( days > 0 )
				return getPluralDays(days, context);
// left hours
			else if ( hours > 0 )
				msg = getPluralHours(hours, context);
// left minutes
			else if ( mins >= 0 )
				msg = getPluralMinutes(mins, context);
		}
		
		return msg;
	}
	
	private String getPluralDays(int count, Context context) {
		if ( context == null )
			return getPluralNumber(count, "д", "ень", "ня", "ней");
		else
			return getPluralNumber(count, 
					context.getString(R.string.plural_day0), 
					context.getString(R.string.plural_day1), 
					context.getString(R.string.plural_day2), 
					context.getString(R.string.plural_day3));
	}

	private String getPluralHours(int count, Context context) {
		if ( context == null )
			return getPluralNumber(count, "час", "", "а", "ов");		
		else
			return getPluralNumber(count, 
					context.getString(R.string.plural_hour0), 
					context.getString(R.string.plural_hour1), 
					context.getString(R.string.plural_hour2), 
					context.getString(R.string.plural_hour3));
	}

	private String getPluralMinutes(int count, Context context) {
		if ( context == null )
			return getPluralNumber(count, "минут", "а", "ы", "");		
		else
			return getPluralNumber(count, 
					context.getString(R.string.plural_min0), 
					context.getString(R.string.plural_min1), 
					context.getString(R.string.plural_min2), 
					context.getString(R.string.plural_min3));
	}
	
	private static String getPluralNumber(int count, String arg0, String arg1, String arg2, String arg3) {
		StringBuffer result = new StringBuffer();

		result.append(count);
		result.append(" ");
		result.append(arg0);
		
		int last_digit = count % 10;
		int last_two_digits = count % 100;

		if (last_digit == 1 && last_two_digits != 11)
			result.append(arg1);
		else if ((last_digit == 2 && last_two_digits != 12) || 
				(last_digit == 3 && last_two_digits != 13) || 
				(last_digit == 4 && last_two_digits != 14))
			result.append(arg2);
		else
			result.append(arg3);

		return result.toString();
	}	
	
	public static void main(String[] args) throws ParseException {
		final DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date curDate = dfDate.parse("2012-02-24 19:01");
		
		FridayTimeLeft fr = new FridayTimeLeft(curDate);
		fr.calc(19, 0);
		
		System.out.println(fr.mCurDate);
		System.out.println(fr.isFridayStart);
		System.out.println(fr.isFridayHasCome);
		System.out.println(fr.getMessage(null));
		
	}

}
