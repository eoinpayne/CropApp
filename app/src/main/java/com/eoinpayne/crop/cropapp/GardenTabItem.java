//package com.eoinpayne.crop.cropapp;
//
//import java.text.ParseException;
//        import java.text.SimpleDateFormat;
//        import java.util.Date;
//        import java.util.Locale;
//
//        import android.content.Intent;
//
//// Do not modify
//
//public class GardenTabItem {
//
//    public static final String ITEM_SEP = System.getProperty("line.separator");
//
//
//    public final static String GARDEN_ID = "gardenID";
//    public final static String USER_ID = "userID";
//    public final static String GARDEN_NAME = "gardenName";
//
//
//    GardenTabItem(String GARDEN_ID, String USER_ID, String GARDEN_NAME) {
//        this.GARDEN_ID = GARDEN_ID;
//        this.USER_ID = USER_ID;
//        this.GARDEN_NAME = GARDEN_NAME;
//
//    }
//
//    // Create a new ToDoItem from data packaged in an Intent
//
//    ToDoItem(Intent intent) {
//
//        GARDEN_ID = intent.getStringExtra(GardenTabItem.GARDEN_ID);
//        mPriority = Priority.valueOf(intent.getStringExtra(ToDoItem.PRIORITY));
//        mStatus = Status.valueOf(intent.getStringExtra(ToDoItem.STATUS));
//
//        try {
//            mDate = ToDoItem.FORMAT.parse(intent.getStringExtra(ToDoItem.DATE));
//        } catch (ParseException e) {
//            mDate = new Date();
//        }
//    }
//
//    public String getTitle() {
//        return mTitle;
//    }
//
//    public void setTitle(String title) {
//        mTitle = title;
//    }
//
//    public Priority getPriority() {
//        return mPriority;
//    }
//
////	public int getPriorityPosition() {
////		return mPriority;
////	}
//
//
//    public void setPriority(Priority priority) {
//        mPriority = priority;
//    }
//
//    public Status getStatus() {
//        return mStatus;
//    }
//
//    public void setStatus(Status status) {
//        mStatus = status;
//    }
//
//    public Date getDate() {
//        return mDate;
//    }
//
//    public void setDate(Date date) {
//        mDate = date;
//    }
//
//    // Take a set of String data values and
//    // package them for transport in an Intent
//
//    public static void packageIntent(Intent intent, String title,
//                                     Priority priority, Status status, String date) {
//
//        intent.putExtra(ToDoItem.TITLE, title);
//        intent.putExtra(ToDoItem.PRIORITY, priority.toString());
//        intent.putExtra(ToDoItem.STATUS, status.toString());
//        intent.putExtra(ToDoItem.DATE, date);
//
//    }
//
//    public String toString() {
//        return mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP
//                + FORMAT.format(mDate);
//    }
//
//    public String toLog() {
//        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
//                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
//                + FORMAT.format(mDate);
//    }
//
//}