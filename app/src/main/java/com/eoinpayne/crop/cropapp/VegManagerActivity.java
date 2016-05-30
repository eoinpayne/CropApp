package com.eoinpayne.crop.cropapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eoinpayne.crop.cropapp.VegItem.Priority;
import com.eoinpayne.crop.cropapp.VegItem.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VegManagerActivity extends ListActivity {
	//extend one of the built-in layouts and inflate custom layout during initialization:

	Button plantVeg;
	Button waterAll;
	// Add a vegItem Request Code
	private static final int ADD_VEG_ITEM_REQUEST = 0;  //request code? is this always 0?
	private static final String FILE_NAME = "VegManagerActivityData1.txt";
	private static final String TAG = "Lab-UserInterface";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	VegListAdapter mAdapter;
	private String gardenName;
	private String gardenID;
	private String userID;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a new VegListAdapter for this ListActivity's ListView
		mAdapter = new VegListAdapter(getApplicationContext()); //mContext instead of getApplicationContect()?

		Bundle bundle = getIntent().getExtras();
		gardenName = bundle.getString("gardenName");
		gardenID = bundle.getString("gardenID");
		userID = bundle.getString("userID");


		//Veg EXTRA 5 - TABS
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		ActionBar.TabListener tabListener = new TabListener(mAdapter);
//		actionBar.addTab(actionBar.newTab().setText("Priority").setTabListener(tabListener));
//		actionBar.addTab(actionBar.newTab().setText("Deadline").setTabListener(tabListener));

		// Put divider between VegItems and headerView
//		getListView().setFooterDividersEnabled(true);
		getListView().setHeaderDividersEnabled(true);

		//TODO - Inflate headerView for header_view.xml file XX
		LinearLayout headerView = (LinearLayout) getLayoutInflater().inflate(R.layout.header_view, getListView(),false );

		//TODO - Add HeaderView to ListView XX
		getListView().addHeaderView(headerView);


//		headerView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				log("Entered headerView.OnClickListener.onClick()");
//				//TODO - Attach Listener to headerView. Implement onClick(). XX
//				Intent addVeg_intent = new Intent(getApplicationContext(), AddVegActivity.class);
//				startActivityForResult(addVeg_intent, ADD_VEG_ITEM_REQUEST);
//			}
//		});
		//TODO - Attach Listener to headerView buttons. Implement onClick(). XX

		plantVeg = (Button) findViewById(R.id.plantVeg_btn);
		plantVeg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addVeg_intent = new Intent(getApplicationContext(), AddVegActivity.class);
				Bundle extras = new Bundle();
				extras.putString("gardenName", gardenName);
				extras.putString("gardenID", gardenID);
				extras.putString("userID", userID);
				addVeg_intent.putExtras(extras);
				//todo: get rid of double way of displaying to mAdapter? Just regular start activity?
				//todo persist data and finish?
				startActivityForResult(addVeg_intent, ADD_VEG_ITEM_REQUEST);
			}
		});

		waterAll = (Button) findViewById(R.id.waterAll_btn);
		waterAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(),"WATERED ALL!!", Toast.LENGTH_LONG).show();

			}
		});


		//TODO - Attach the adapter to this ListActivity's ListView XX
        getListView().setAdapter(mAdapter);

	} //closes the onCreate


//	private int resetWatered(){
//		int daysSinceWatered;
//		return daysSinceWatered;
//	}

	//todo: get rid of double way of displaying to mAdapter?
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		log("Entered onActivityResult()");

		// TODO - Check result code and  code. XX
		//switch
		switch(resultCode){
			case RESULT_OK:
				VegItem my_veg = new VegItem(data);
				mAdapter.add(my_veg);
				break;
		}
		dump();
		}


	@Override
	public void onResume() {
		super.onResume();

		// Load saved VegItems, if necessary
		if (mAdapter.getCount() == 0)
			loadItems();
	}

	@Override
	protected void onPause() {
		super.onPause();

		//TODO: save items more than saveItems()?
		// Save VegItems
		saveItems();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			mAdapter.clear();
			return true;
		case MENU_DUMP:
			dump();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dump() {

		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((VegItem) mAdapter.getItem(i)).toLog();
			log("Item " + i + ": " + data.replace(VegItem.ITEM_SEP, ","));
		}

	}

	// Load stored vegItems
	//TODO when user logs in, start background task that collects all vegItems belonging to his userID
	//TODO write all these items to a file stored locally
	//TODO when a garden is selected, scan the file and build vegItems for all lines that have a userId matching
	//TODO add these vegItems to the adapter's list.
	//TODO any time a new item is added/removed to garden, rebuild the file. always build list from file?
	//TODO Call this background task anytime user logs in and any time new item is added?
	private void loadItems() {
		BufferedReader reader = null;
//todo: send to a background task with a load bar?


		try {
			FileInputStream fis = openFileInput(BuildFile_userVeg.userVeg_file);
			reader = new BufferedReader(new InputStreamReader(fis));
//			reader = new BufferedReader(new FileReader(DBScraper_userVeg.userVeg_file));

			String _vegName = null;		//vegName
			String _affected = null;  	//affected by rain
			Date _date = null; 			 //date planted
			String _vegCount = null;		//quantity of veg
			Date _lastWatered = null;	//date last watered
			String _gardenID = null;

			//ToDo pull json from file convert to build new veg item

			//TODO: read json file and convert to string
			String jsonString = reader.readLine();

			//ToDo convert string to json array.
			JSONArray jsonArray = new JSONArray(jsonString);

			//ToDo: iterate each object in the array, make a json object, make a veg item and add to the adaptor to display as list.

			for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJO = jsonArray.getJSONObject(i);
				_vegName = tempJO.get("vegName").toString();
				_affected = tempJO.get("affectedByRain").toString();

				_date = VegItem.FORMAT.parse(tempJO.get("timePlanted").toString());
				_vegCount = tempJO.get("quantity").toString();

				_lastWatered = VegItem.FORMAT.parse(tempJO.get("lastWatered").toString());
				_gardenID = tempJO.get("gardenID").toString();
				if (_gardenID.equals(gardenID)){
					mAdapter.add(new VegItem(_vegName, VegItem.Affected.valueOf(_affected), _date, _vegCount, _lastWatered));
				}
			}

////			JSONArray jsonArray = new JSONArray(reader.readLine());
//			for (int i = 1; i < jsonObject.length(); i++) {
//				JSONObject thisJO = jsonObject.getJSONObject(i);
//				affected = thisJO.getString("affectedByRain");
////				String gardenID = thisJO.getString("gardenID");
//
//			}
//			JSONArray jsonArray = new JSONArray();
//
//			String incoming = reader.readLine();
//
////			incoming.split("}");
//			for (String retval: incoming.split("[~]")){
////				for (String retval: incoming.split("(?<=,)")){
//
////				System.out.println(retval);
//				if (!(retval.equals(""))) {
//					JSONObject tempJO = new JSONObject(retval);
//					affected = tempJO.get("affectedByRain").toString();
//					jsonArray.put(tempJO);
//				}
//			}



//			jsonArray.put(incoming);
//			for (int i = 1; i < jsonArray.length(); i++) {
//				JSONObject thisJO = jsonArray.getJSONObject(i);
//				affected = thisJO.getString("affectedByRain");
////				String gardenID = thisJO.getString("gardenID");
//
//			}
/////////////////
//			String fileContent = "";
//			try {
//				String currentLine;
//				File cacheFile = new File(getFilesDir(), DBScraper_userVeg.userVeg_file);
////				FileInputStream fis = openFileInput(DBScraper_userVeg.userVeg_file);
//				BufferedReader br = new BufferedReader(new FileReader(cacheFile));
//
//				while ((currentLine = br.readLine()) != null) {
//					fileContent += currentLine + '\n';
//				}
//
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//
//				// on exception null will be returned
//				fileContent = null;
//			}
/////////////////////

//			while (null != (title = reader.readLine())) {
////				while (reader.readLine() != null) {
//				JSONObject jsonObject = new JSONObject(reader.readLine());
////				priority = reader.readLine();
////				status = reader.readLine();
//				affected = jsonObject.get("affectedByRain").toString();
////				date = VegItem.FORMAT.parse(reader.readLine());
//				vegCount = reader.readLine();
////				mAdapter.add(new VegItem(title, Priority.valueOf(priority),
////						Status.valueOf(status), date));
//				mAdapter.add(new VegItem(title, VegItem.Affected.valueOf(affected), date, vegCount));
//			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		catch (ParseException e) {
//			e.printStackTrace();
//		}
		catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Save vegItems to file
	private void saveItems() {
		PrintWriter writer = null;
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {

				writer.println(mAdapter.getItem(idx));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

	//TODO EXTRA 5 - TABSs Listener
	private class TabListener implements ActionBar.TabListener{
		private final VegListAdapter adapter;
		public TabListener(VegListAdapter adapter){
		this.adapter = adapter;
		}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				//to order by date on click
			}

			@Override
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				//to
			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

			}
	}

	public static int getDaysDifference(Date fromDate,Date toDate)
	{
		if(fromDate==null||toDate==null)
			return 0;

		return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
	}

}