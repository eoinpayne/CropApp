package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.eoinpayne.crop.cropapp.VegItem.Affected;


public class AddVegActivity extends AppCompatActivity {  //appcompat  Activity?

	private Toolbar toolbar;
	//private static final String RESTART_KEY = "restart"; etc.. not needed?

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
//	private static final int SEVEN_DAYS = 604800000;
	private static final String TAG = "CropZ_app";
	private static String dateString;
	private static String timeString;
	private static TextView dateView;
	private Date mDate;
	private static Date mDatePlanted;

//	public Date getDatePlanted() {
//		return mDatePlanted;
//	}
//	public static void setDatePlanted(Date datePlanted) {
//		mDatePlanted = datePlanted;
//	}


//	private int mCount;
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
	Context context;
	Date fullDate_parsed;
	String fullDate_parsedThenFormatted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TypefaceProvider.registerDefaultIconSets();
		setContentView(R.layout.add_veg);
		context = this;

		//Retrieve bundle information passed from previous activity.
		Bundle bundle = getIntent().getExtras();
		mGardenName = bundle.getString("gardenName");
		mGardenID = bundle.getString("gardenID");
//		mUserID = bundle.getString("userID");

		//add toolbar to activity
		toolbar = (Toolbar)findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		mToolbarTitle.setText("Plant veg in " + mGardenName);

		//set the Header in toolbar to be clickable and return user to gardens,
		TextView mToolBarHeader = (TextView) toolbar.findViewById(R.id.toolbar_header);
		mToolBarHeader.setOnClickListener(new OnClickListener() {
			@Override  //clicking title brings user back home.
			public void onClick(View v) {
				Intent intent = new Intent(AddVegActivity.this, HomeActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("userID", HomeActivity.global_userID);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});




		vegSpinner = (Spinner) findViewById(R.id.chooseVegSpinner);
		ArrayList<String> vegItems = retrieveStringArrayFromFile(DBScraper_vegInfo.vegNames_file, context);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt,vegItems);
		vegSpinner.setAdapter(adapter);
		countSpinner = (Spinner) findViewById(R.id.QuantitySpinner);
		mDefaultAffectedButton = (RadioButton) findViewById(R.id.rainYes); // takes default radio button from addVeg xml
		mAffectedRadioGroup = (RadioGroup) findViewById(R.id.rainGroup);
		dateView = (TextView) findViewById(R.id.date);

		setDefaultDateTime();	// Set the default date and time

		// OnClickListener for the Date button, calls showDatePickerDialog() to show
		// the Date dialog
		final BootstrapButton datePickerButton = (BootstrapButton) findViewById(R.id.date_picker_button);
		datePickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		// OnClickListener for the Cancel Button,
		final BootstrapButton cancelButton = (BootstrapButton) findViewById(R.id.cancelButton);
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
		final BootstrapButton resetButton = (BootstrapButton) findViewById(R.id.resetButton);
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

		final BootstrapButton submitButton = (BootstrapButton) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered submitButton.OnClickListener.onClick()");

				//TODO ---------------update DB, delete file, recreate
				// Gather vegItem data
//
//				//TODO -  Get if Affected by Rain XX
//				affected = getAffected();

				//TODO -  Get veg Count
//				vegCount = countSpinner.getSelectedItem().toString();

				// Date
//				fullDate = dateString + " " + timeString;


				try {
//					fullDate_parsed = VegItem.FORMAT.parse(fullDate); //Date
					fullDate_parsed = VegItem.FORMAT.parse(dateString + " " + timeString); //Date
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}



//				Date Datedate = mDate;
//				String workingDate = mDate.toString();
//
//				lastWatered = (String) fullDate_parsed.;

				// Package vegItem data into an Intent
//				Intent data = new Intent();
//				stringed_up_date = fullDate_parsed.toString();
				if ((vegSpinner.getSelectedItem().toString()) == null)
				{
					Toast.makeText(getBaseContext(),"Please choose Vegetable", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					chosenVeg = vegSpinner.getSelectedItem().toString(); //vegSpinner
//					VegItem.packageIntent(data, chosenVeg, affected, fullDate, vegCount, lastWatered);
//					setResult(Activity.RESULT_OK, data);
					//ToDo get expectedHarvest date
//					Date expectedHarvestDate = calcHarvestDate(chosenVeg, fullDate_parsed); //date
//					Date expectedHarvestDate = fullDate_parsed; //date

					//todo persists data to the database
					try {
						AddVeg_BackgroundTask addVeg_backgroundTask = new AddVeg_BackgroundTask(AddVegActivity.this);
//						BackgroundTask backgroundTask = new BackgroundTask(AddVegActivity.this);
						addVeg_backgroundTask.execute("addVegItem", mGardenID, chosenVeg,
								getAffected().toString(), fullDate_parsed.toString(), countSpinner.getSelectedItem().toString(),
								HomeActivity.global_UserID_String, fullDate_parsed.toString(), calcHarvestDate(chosenVeg, fullDate_parsed).toString());

					}catch (Exception e){
						e.printStackTrace();
					}

					//ToDO: Rebuild vegItem list with newly persisted item
//					new Thread(new DBScraper_userVeg(context)).start();

					try {
						Thread.sleep(1100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					finish();
				}
			}
		});
	} //CLOSE ONCREATE

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);
		return true;
//        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int res_id = item.getItemId();

		if(res_id == R.id.browse_veg){
			//todo display the browse catalogue activity
			HomeActivity.selectVegToBrowse(context);
		}
//		else if(res_id == R.id.plant_veg){
//			//toDo launch plant veg
//		}
		return super.onOptionsItemSelected(item);
	}




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

	//Methods for neatly displaying Date & Time
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

		@Override	// Use the current date as the default date in the picker
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override  //when date is picked from pop up window/fragment
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			setDateString(year, monthOfYear, dayOfMonth);
			dateView.setText(dateString);
		}
	}

	//displays the fragment for picking dates when called
	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	//to aid debugging by logging error messages.
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

	// ToDo Display expected days left until harvest XX.
	//Calculates expectedHarvestDate using the vegName to find how many days to grow (int) stored on DB.
	//adds that amount of days to the date the item was planted using Calender.
	public Date calcHarvestDate(String vegName, Date date){
		Date expectedHarvestDate;
		int daysToGrow = findDaysToGrow(vegName);

		Calendar c=new GregorianCalendar();
		c.add(Calendar.DATE, daysToGrow);
		expectedHarvestDate=c.getTime();

		return expectedHarvestDate;
	}

	//Returns number of days to grow to the calcHarvestDate function.
	//Will call method to opens file. The file contains a string of jsonObjects of each vegetables typical grow time.
	// The String and converts to JsonObjects, The vegName passed in will match the correct key:value pair and assign int daysToGrow.
	public int findDaysToGrow(String vegName){
		JSONObject jsonObject;
		JSONArray daysToGrow_array;
		int daysToGrow = 0;
		try {
			jsonObject = new JSONObject(retrieveStringFromFile(DBScraper_vegInfo.daysToGrow_file, context));
			daysToGrow_array = jsonObject.getJSONArray("daysToGrow_collection");
			for (int i=0; i<daysToGrow_array.length(); i++){
				JSONObject daysToGrow_pair = (JSONObject) daysToGrow_array.get(i);
				if(daysToGrow_pair.has(vegName)){
				daysToGrow = (int)daysToGrow_pair.get(vegName);
					return daysToGrow;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return daysToGrow;

	}

	//Opens file and returns it's content as a string.
	public static final String retrieveStringFromFile(String filename, Context ctx) {
		String tempString = "";
		try {
			FileInputStream fileInputStream = ctx.openFileInput(filename);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			tempString = bufferedReader.readLine();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return tempString;
	}

	//opens file and build an array list for each new line in the file.
	public static final ArrayList<String> retrieveStringArrayFromFile(String filename, Context ctx) {
		ArrayList<String> tempList = new ArrayList<>();
		try {
			FileInputStream fileInputStream = ctx.openFileInput(filename);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				tempList.add(line);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return tempList;
	}

}
