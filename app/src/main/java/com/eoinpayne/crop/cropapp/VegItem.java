package com.eoinpayne.crop.cropapp;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Do not modify 

public class VegItem {

	public static final String ITEM_SEP = System.getProperty("line.separator");

	public enum Priority {
		LOW(2), MED(1), HIGH(0);
		private final int position;
		Priority(int position){
			this.position= position;
		}
		public static Priority valueFromPosition(int position){
			for (Priority priority: values()){
				if (priority.position == position){
					return priority;
				}
			}
			return LOW;
		}
		public int getPosition(){
			return position;
		}
	};

	public enum Status {
		NOTDONE, DONE
	};
	public enum Affected {
		YES, NO
	};
	public final static String TITLE = "title";
	public final static String PRIORITY = "priority";
	public final static String STATUS = "status";
	public final static String AFFECTED = "affected";
	public final static String VEGCOUNT = "vegCount";
	public final static String DATE = "date";
	public final static String LASTWATERED = "lastWatered";

	public final static String FILENAME = "filename";
	public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private String mTitle = new String();
	private Affected mAffected = Affected.YES;
	private Status mStatus = Status.NOTDONE;
	private Date mDate;
	private Date mLastWatered;
	private String mVegCount;


	VegItem(String title, Affected affected, Date date, String vegCount) {
		this.mTitle = title;
//		this.mStatus = status;
		this.mAffected = affected;
		this.mDate = date;
		this.mLastWatered = date;
		this.mVegCount = vegCount;

	}

	VegItem(String title, Affected affected, Date date, String vegCount, Date lastWatered) {
		this.mTitle = title;
//		this.mStatus = status;
		this.mAffected = affected;
		this.mDate = date;
		this.mLastWatered = lastWatered;
		this.mVegCount = vegCount;

	}

	// Create a new vegItem from data packaged in an Intent
	VegItem(Intent intent) {
		mTitle = intent.getStringExtra(VegItem.TITLE);
		mAffected = Affected.valueOf(intent.getStringExtra(VegItem.AFFECTED));
		mVegCount =  intent.getStringExtra(VegItem.VEGCOUNT);
//		mStatus = Status.valueOf(intent.getStringExtra(VegItem.STATUS));

		try {
			mDate = VegItem.FORMAT.parse(intent.getStringExtra(VegItem.DATE));

		} catch (ParseException e) {
			mDate = new Date();
		}
		try {
			mLastWatered = VegItem.FORMAT.parse(intent.getStringExtra(VegItem.DATE));
		} catch (ParseException e) {
			mLastWatered = new Date();
		}

	} // Close VegItem from intent

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

//	public Priority getPriority() {
//		return mPriority;
//	}

//	public int getPriorityPosition() {
//		return mPriority;
//	}
//	public void setPriority(Priority priority) {
//		mPriority = priority;
//	}
//
	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status status) {
		mStatus = status;
	}

	public Affected getAffected() {
		return mAffected;
	}

	public void setAffected(Affected affected) {
		mAffected = affected;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public String getVegCount() {
		return mVegCount;
	}

	public void setVegCount(String vegCount) {
		mVegCount = vegCount;
	}


public Date getLastWatered() {return mLastWatered;}

	public void setLastWatered() {
		Calendar c = Calendar.getInstance();
		mLastWatered = c.getTime();
	}

	// Take a set of String data values and 
	// package them for transport in an Intent

//	public static void packageIntent(Intent intent, String title,
//									 Priority priority, Status status, String date) {
public static void packageIntent(Intent intent, String title, Affected affected, String date, String vegCount, String lastWatered) {

		intent.putExtra(VegItem.TITLE, title);
		intent.putExtra(VegItem.AFFECTED, affected.toString());
		intent.putExtra(VegItem.DATE, date);
		intent.putExtra(VegItem.VEGCOUNT, vegCount);
		intent.putExtra(VegItem.LASTWATERED, lastWatered);

	}

	public String toString() {
		return mTitle + ITEM_SEP + mAffected + ITEM_SEP + mStatus + ITEM_SEP
				+ FORMAT.format(mDate);
	}
//public String toString() {
//	return mTitle + ITEM_SEP + FORMAT.format(mDate);
//}
//	public String toLog() {
//		return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
//				+ ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
//				+ FORMAT.format(mDate);
//	}
public String toLog() {
	return "Title:" + mTitle + ITEM_SEP + "affected:" + mAffected + ITEM_SEP
			+"status:" + mStatus + ITEM_SEP + "Date:" + FORMAT.format(mDate);
}
}
