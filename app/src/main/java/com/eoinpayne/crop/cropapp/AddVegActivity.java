package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eoinpayne.crop.cropapp.VegItem.Affected;


public class AddVegActivity extends Activity {  //appcompat?

	//private static final String RESTART_KEY = "restart"; etc.. not needed?

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
	private static final int SEVEN_DAYS = 604800000;
	private static final String TAG = "Lab-UserInterface";
	private static String dateString;
	private static String timeString;
	private static TextView dateView;
	private Date mDate;
	private int mCount;
	private RadioGroup mAffectedRadioGroup;
	private Spinner vegSpinner;
	private Spinner countSpinner;
	private RadioButton mDefaultAffectedButton;
	private String mGardenName;
	private String mGardenID;
	private String mUserID;
	String chosenVeg;
	String vegCount;
	String fullDate;
	Affected affected;
	String lastWatered;
	//	public int mCreate = 0;
	//  public int mRestart = 0;
	//	TextView mTvCreate ;
	//  TextView mTvRestart;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_veg);
		context = this;

		Bundle bundle = getIntent().getExtras();
		mGardenName = bundle.getString("gardenName");
		mGardenID = bundle.getString("gardenID");
		mUserID = bundle.getString("userID");

		vegSpinner = (Spinner) findViewById(R.id.chooseVegSpinner);
		ArrayList<String> vegItems = getVegNames(DBScraper_vegInfo.vegNames_file);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt,vegItems);
		vegSpinner.setAdapter(adapter);
		countSpinner = (Spinner) findViewById(R.id.QuantitySpinner);
		mDefaultAffectedButton = (RadioButton) findViewById(R.id.rainYes); // takes default radio button from addVeg xml
		mAffectedRadioGroup = (RadioGroup) findViewById(R.id.rainGroup);
		dateView = (TextView) findViewById(R.id.date);

		setDefaultDateTime();	// Set the default date and time

		// OnClickListener for the Date button, calls showDatePickerDialog() to show
		// the Date dialog
		final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
		datePickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		// OnClickListener for the Cancel Button,
		final Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered cancelButton.OnClickListener.onClick()");
                Intent intent_cancel = null;
                setResult(Activity.RESULT_CANCELED, intent_cancel);  //intent_cancel needed?
                finish();
				//TODO - Implement onClick(). XX
				//returned cancelled intent (return ok or CANCEL to the thing that called. i.e vegmgrActivity foterView
				//finish it  RESULT_CANCELED
				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
			}
		});

		//OnClickListener for the Reset Button
		final Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered resetButton.OnClickListener.onClick()");

				//TODO - Reset data fields to default values XX
//                vegSpinner.setText("");
                mDefaultAffectedButton.setChecked(true);
                setDefaultDateTime();

			}
		});

		// OnClickListener for the Submit Button
		// Implement onClick().

		final Button submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered submitButton.OnClickListener.onClick()");

				//TODO ---------------update DB, delete file, recreate
				// Gather vegItem data
//
//				//TODO -  Get if Affected by Rain XX
				affected = getAffected();

				//TODO -  Get veg Count
				vegCount = countSpinner.getSelectedItem().toString();

				// Date
				fullDate = dateString + " " + timeString;
				lastWatered = fullDate;

				// Package vegItem data into an Intent
				Intent data = new Intent();
//				VegItem.packageIntent(data, titleString, priority, status, fullDate);

				if ((vegSpinner.getSelectedItem().toString()) == null)
				{
					Toast.makeText(getBaseContext(),"Please choose Vegetable", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					chosenVeg = vegSpinner.getSelectedItem().toString(); //vegSpinner
					VegItem.packageIntent(data, chosenVeg, affected, fullDate, vegCount, lastWatered);
					setResult(Activity.RESULT_OK, data);
					//todo persists data to the database
					try {
						BackgroundTask backgroundTask = new BackgroundTask(AddVegActivity.this);
						backgroundTask.execute("addVegItem", mGardenID, chosenVeg, affected.toString(), fullDate, vegCount, HomeActivity.global_UserID_String, lastWatered);
					}catch (Exception e){
						e.printStackTrace();
					}

					//ToDO: Rebuild vegItem list with newly persisted item
					new Thread(new DBScraper_userVeg(context)).start();

					//ToDo add newly planted item to the file created on user login that's filled with all veg planted by user
					//Todo Create a JSON object, similar to how file is already composed.
//
//					JSONObject jsonObject = new JSONObject(); //response
//					try {
//						jsonObject.put("userID", HomeActivity.global_UserID_String);
//						jsonObject.put("gardenID", mGardenID);
//						jsonObject.put("timePlanted", fullDate);
//						jsonObject.put("quantity", vegCount );
//						jsonObject.put("vegName", chosenVeg );
//						jsonObject.put("affectedByRain", affected );
////						jsonObject.put("eta", );
//
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					//write file to existing file
//					//TODO parse file with json, drill into array, add new object, pack back into array and write to file.
//					try {
//					FileOutputStream fos = openFileOutput(DBScraper_userVeg.userVeg_file, MODE_APPEND);
//						fos.write(jsonObject.toString().getBytes());
//						fos.write("~".getBytes());
//						fos.close();
//					} catch (FileNotFoundException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}

					finish();
				}
			}
		});
	} //CLOSE ONCREATE



	private ArrayList<String> getVegNames(String filename) {
		JSONArray jsonArray = null;
		ArrayList<String> vegList = new ArrayList<>();
		try {
//			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//			InputStream inputStream = getResources().getAssets().open(filename);

//			String yourFilePath = context.getFilesDir() + "/" + "filename";
//			File myVegNames_file = new File( yourFilePath );

			FileInputStream fileInputStream = openFileInput(DBScraper_vegInfo.vegNames_file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
//				stringBuilder.append(line);
				vegList.add(line);
			}
			String data = stringBuilder.toString().trim();

//			int size = inputStreamReader.available();
//			byte[] data = new byte[size];
//			inputStream.read(data);
//			inputStream.close();

//			JSONObject json = new JSONObject(data);
//			jsonArray= json.getJSONArray("server_response");
//			if(jsonArray != null){
//				for(int i =0; i <jsonArray.length();i++){
//					vegList.add(jsonArray.getJSONObject(i).getString("vegName"));
//				}
//			}

		}catch (IOException e) {
			e.printStackTrace();
		}

		return vegList;
	}

	// Do not modify below here
	
	// Use this method to set the default date and time
	
	private void setDefaultDateTime() {

		mDate = new Date();
		mDate = new Date(mDate.getTime());

		Calendar c = Calendar.getInstance();
		c.setTime(mDate);

		setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		dateView.setText(dateString);

		setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				c.get(Calendar.MILLISECOND));

//		timeView.setText(timeString);
	}

//	private void setETA() {
//
//		// Default is current time + 7 days
//		mETA = new Date();
//		mETA = new Date(mDate.getTime() + SEVEN_DAYS);
//
//		Calendar c = Calendar.getInstance();
//		c.setTime(mDate);
//
//		setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//				c.get(Calendar.DAY_OF_MONTH));
//
//		dateView.setText(dateString);
//
//		setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
//				c.get(Calendar.MILLISECOND));
//
//		timeView.setText(timeString);
//	}



	private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

		// Increment monthOfYear for Calendar/Date -> Time Format setting
		monthOfYear++;
		String mon = "" + monthOfYear;
		String day = "" + dayOfMonth;

		if (monthOfYear < 10)
			mon = "0" + monthOfYear;
		if (dayOfMonth < 10)
			day = "0" + dayOfMonth;

		dateString = year + "-" + mon + "-" + day;
	}

	private static void setTimeString(int hourOfDay, int minute, int mili) {
		String hour = "" + hourOfDay;
		String min = "" + minute;

		if (hourOfDay < 10)
			hour = "0" + hourOfDay;
		if (minute < 10)
			min = "0" + minute;

		timeString = hour + ":" + min + ":00";
	}

//	private Priority getPriority() {
//
//		switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
//		case R.id.lowPriority: {
//			return Priority.LOW;
//		}
//		case R.id.highPriority: {
//			return Priority.HIGH;
//		}
//		default: {
//			return Priority.MED;
//		}
//		}
//	}

//	private Status getStatus() {
//
//		switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
//		case R.id.statusCheckBox: {
//			return Status.DONE;
//		}
//		default: {
//			return Status.NOTDONE;
//		}
//		}
//	}

	private Affected getAffected() {

		switch (mAffectedRadioGroup.getCheckedRadioButtonId()) {
			case R.id.rainGroup: {
				return Affected.YES;
			}
			default: {
				return Affected.YES;
		}
		}
	}

	// DialogFragment used to pick a vegItem deadline date

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current date as the default date in the picker

			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			setDateString(year, monthOfYear, dayOfMonth);

			dateView.setText(dateString);
		}
	}

	// DialogFragment used to pick a vegItem deadline time

//	public static class TimePickerFragment extends DialogFragment implements
//			TimePickerDialog.OnTimeSetListener {
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//			// Use the current time as the default values for the picker
//			final Calendar c = Calendar.getInstance();
//			int hour = c.get(Calendar.HOUR_OF_DAY);
//			int minute = c.get(Calendar.MINUTE);
//
//			// Create a new instance of TimePickerDialog and return
//			return new TimePickerDialog(getActivity(), this, hour, minute,
//					true);
//		}
//
//		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//			setTimeString(hourOfDay, minute, 0);
//
//			timeView.setText(timeString);
//		}
//	}

	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

//	private void showTimePickerDialog() {
//		DialogFragment newFragment = new TimePickerFragment();
//		newFragment.show(getFragmentManager(), "timePicker");
//	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
