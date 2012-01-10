package com.friday_countdown.andriod;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;

public class FridayTimeLeft {

	private Context mContext;
	
	private Date mCurDate, mStartFriday, mEndFriday;
	
	public FridayTimeLeft(int goalHour, int goalMinute, Context context) {
		mContext = context;
		
		Calendar cal = GregorianCalendar.getInstance();

		mCurDate = cal.getTime();
		
		cal.set(GregorianCalendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.set(GregorianCalendar.HOUR_OF_DAY, goalHour);
		cal.set(GregorianCalendar.MINUTE, goalMinute);
		cal.set(GregorianCalendar.SECOND, 0);

		mStartFriday = cal.getTime();
		
		cal.set(GregorianCalendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		
		mEndFriday = cal.getTime();
	}

//	private void setCurDate(Date date) {
//		mCurDate = date;
//	}
	
	public boolean isFridayHasCome() {
		return mCurDate.after(mStartFriday) && mCurDate.before(mEndFriday); 
	}
	
	public String getMessage() {
		String msg = "";
		
		if ( isFridayHasCome() ) {
			msg = mContext.getString(R.string.friday_has_come);
//			msg = "пятница пришла!";
		}
		else {
//			msg = context.getString(R.string.friday_time_left);

			long left = mStartFriday.getTime() - mCurDate.getTime();

			int days = (int) Math.floor(left / (long) (60 * 60 * 24 * 1000));
            left = left - days * (long) (60 * 60 * 24 * 1000);
            int hours = (int) Math.floor(left / (60 * 60 * 1000));
            left = left - hours * (long) (60 * 60 * 1000);
            int mins = (int) Math.floor(left / (60 * 1000));
            
// left days
			if ( days > 0 )
				msg = getPluralDays(days);
// left hours
			else if ( hours > 0 )
				msg = getPluralHours(hours);
// left minutes
			else if ( mins >= 0 )
				msg = getPluralMinutes(mins);
		}
		
		return msg;
	}
	
	private String getPluralDays(int count) {
		return getPluralNumber(count, 
				mContext.getString(R.string.plural_day0), 
				mContext.getString(R.string.plural_day1), 
				mContext.getString(R.string.plural_day2), 
				mContext.getString(R.string.plural_day3));
		
//		return getPluralNumber(count, "д", "ень", "ня", "ней");		
	}

	private String getPluralHours(int count) {
		return getPluralNumber(count, 
				mContext.getString(R.string.plural_hour0), 
				mContext.getString(R.string.plural_hour1), 
				mContext.getString(R.string.plural_hour2), 
				mContext.getString(R.string.plural_hour3));
		
//		return getPluralNumber(count, "час", "", "а", "ов");		
	}

	private String getPluralMinutes(int count) {
		return getPluralNumber(count, 
				mContext.getString(R.string.plural_min0), 
				mContext.getString(R.string.plural_min1), 
				mContext.getString(R.string.plural_min2), 
				mContext.getString(R.string.plural_min3));
		
//		return getPluralNumber(count, "минут", "а", "ы", "");		
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
//		final DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//
//		FridayTimeLeft fr = new FridayTimeLeft(19, 0);
//		
//		Date checkDate = dfDate.parse("2011-12-30 18:59");
//		fr.setCurDate(checkDate);
//		
//		System.out.println(fr.isFridayHasCome());
//		System.out.println(fr.getMessage());
		
	}

}
