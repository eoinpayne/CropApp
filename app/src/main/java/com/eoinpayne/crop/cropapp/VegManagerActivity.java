package com.eoinpayne.crop.cropapp;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VegManagerActivity extends ListActivity {
	//extend one of the built-in layouts and inflate custom layout during initialization:
	private Toolbar toolbar;
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

//		toolbar = (Toolbar)findViewById(R.id.my_toolbar);
//		setSupportActionBar(toolbar);

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
		getListView().setHeaderDividersEnabled(true);
		//TODO - Inflate headerView for header_view.xml file XX
		LinearLayout headerView = (LinearLayout) getLayoutInflater().inflate(R.layout.header_view, getListView(),false );
		//TODO - Add HeaderView to ListView XX
		getListView().addHeaderView(headerView);
//		new VegManagerBackgroundTask().execute("display_veg", userID, gardenID);

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
				startActivity(addVeg_intent);
			}
		});
		//TODO waterall resets all the dates that plant last watered. persist these dates into db for when item loads out
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



//	//todo: get rid of double way of displaying to mAdapter?
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		log("Entered onActivityResult()");
//
//		// TODO - Check result code and  code. XX
//		//switch
//		switch(resultCode){
//			case RESULT_OK:
//				VegItem my_veg = new VegItem(data);
//				mAdapter.add(my_veg);
//				break;
//		}
//		dump();
//		}


	@Override
	public void onResume() {
		super.onResume();

		// Load saved VegItems, if necessary
//		if (mAdapter.getCount() == 0)
		new VegManagerBackgroundTask().execute("display_veg", userID, gardenID);
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		//TODO: save items more than saveItems()?
//		// Save VegItems
//		saveItems();
//
//	}

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
//	private void loadItems() {
////		BufferedReader reader = null;
//////todo: send to a background task with a load bar?
////		try {
//			FileInputStream fis = openFileInput(BuildFile_userVeg.userVeg_file);
////			reader = new BufferedReader(new InputStreamReader(fis));
//////			reader = new BufferedReader(new FileReader(DBScraper_userVeg.userVeg_file));
////			String _vegName = null;		//vegName
////			String _affected = null;  	//affected by rain
////			Date _date = null; 			 //date planted
////			String _vegCount = null;		//quantity of veg
////			Date _lastWatered = null;	//date last watered
////			String _gardenID = null;
////			//ToDo pull json from file convert to build new veg item
////			//TODO: read json file and convert to string
////			String jsonString = reader.readLine();
////			//ToDo convert string to json array.
////			JSONArray jsonArray = new JSONArray(jsonString);
////			//ToDo: iterate each object in the array, make a json object, make a veg item and add to the adaptor to display as list.
////			for (int i = 0; i < jsonArray.length(); i++) {
////                JSONObject tempJO = jsonArray.getJSONObject(i);
////				_vegName = tempJO.get("vegName").toString();
////				_affected = tempJO.get("affectedByRain").toString();
////				_date = VegItem.FORMAT.parse(tempJO.get("timePlanted").toString());
////				_vegCount = tempJO.get("quantity").toString();
////				_lastWatered = VegItem.FORMAT.parse(tempJO.get("lastWatered").toString());
////				_gardenID = tempJO.get("gardenID").toString();
////				if (_gardenID.equals(gardenID)){
////					mAdapter.add(new VegItem(_vegName, VegItem.Affected.valueOf(_affected), _date, _vegCount, _lastWatered));
////				}
////			}
////		} catch (FileNotFoundException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//////		catch (ParseException e) {
//////			e.printStackTrace();
//////		}
////		catch (JSONException e) {
////			e.printStackTrace();
////		} catch (ParseException e) {
////			e.printStackTrace();
////		} finally {
////			if (null != reader) {
////				try {
////					reader.close();
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
////			}
////		}
//
//
//
//		//TODO call vegManager background task to return vegItems and add to mAdapter.
//		new VegManagerBackgroundTask().execute("display_veg", userID, gardenID);
//
//	} //close loadItems

	// Save vegItems to file
//	private void saveItems() {
//		PrintWriter writer = null;
//		try {
//			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//					fos)));
//
//			for (int idx = 0; idx < mAdapter.getCount(); idx++) {
//
//				writer.println(mAdapter.getItem(idx));
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != writer) {
//				writer.close();
//			}
//		}
//	}




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

	////////////////////////////////////////////////////////////////////////////////////
	public class VegManagerBackgroundTask extends AsyncTask<String,Void,String> {
//        String createGarden_URL = "http://192.168.0.34/createGarden.php";
//        String displayGarden_URL = "http://192.168.0.34/displayGardens.php";

		String displayVeg_URL = "http://10.0.2.2/displayVeg.php";
		AlertDialog.Builder builder;  //we can initialise in onPreExecute method
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			mAdapter.clear();
			builder = new AlertDialog.Builder(VegManagerActivity.this); //ctx
			progressDialog = new ProgressDialog(VegManagerActivity.this);
			progressDialog.setTitle("Please Wait");
			progressDialog.setMessage("Connecting to Server");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
//			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String method = params[0];

			//////////////    Show Garden     //////////////////////////////
			if (method.equals("display_veg"))
			{
				try {
					URL url = new URL(displayVeg_URL);
					HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
					httpURLConnection.setRequestMethod("POST"); //?
					httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
					OutputStream outputStream = httpURLConnection.getOutputStream();
					BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
					String userID,gardenID;
					userID = params[1];
					gardenID = params[2];
					String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID,"UTF-8" ) + "&" +
							URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(gardenID,"UTF-8" );
					bufferedWriter.write(data);  //pass data string
					bufferedWriter.flush();
					bufferedWriter.close();
					outputStream.close();
					InputStream inputStream = httpURLConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder stringBuilder = new StringBuilder(); //to read JSON response from bufferedReader
					String line = "";  //take each line from reader and append to String.
					while ((bufferedReader.readLine()) != null)///
					{ line = bufferedReader.readLine();
						stringBuilder.append(line+"\n");
					}
					//create a pause between background methods
					httpURLConnection.disconnect(); //close connection
					Thread.sleep(1200);
					return stringBuilder.toString().trim(); //return string builder in normal string format

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}   //close show garden else if

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		//change argument to a String Json
		protected void onPostExecute(String json) {
			//we decode json data
			try {
				JSONObject jsonObject = new JSONObject(json);  //get Json object
				JSONArray jsonArray = jsonObject.getJSONArray("server_response");  //get objects' array
				JSONObject JO = jsonArray.getJSONObject(0); //get inner object from array at index 0
				String code = JO.getString("code");  //from server
				String message = JO.getString("message");
//				progressDialog.dismiss(); //to close progressDialog

				/////////////    Display Gardens     ///////////////////
				//if user has veg in gardens persisted in the db to display
				if (code.equals("veg_exist")){
					for (int i = 1; i < jsonArray.length(); i++) {
						JSONObject thisJO = jsonArray.getJSONObject(i);
//						String _gardenName = thisJO.getString("gardenName");
//						String _gardenID = thisJO.getString("gardenID");
						String _vegName = thisJO.get("vegName").toString();
						String _affected = thisJO.get("affectedByRain").toString();
						//todo format date coming in?
						Date _date = VegItem.FORMATLONG.parse(thisJO.get("timePlanted").toString());
						Date _lastWatered = VegItem.FORMATLONG.parse(thisJO.get("lastWatered").toString());
//						Date _date = null;
//						Date _lastWatered = null;
////						try {
//							Date in_date = (Date) thisJO.get("timePlanted");
//						} catch (Exception e){
//							e.printStackTrace();
//						}
//						try {
////							_lastWatered = VegItem.FORMAT.format(thisJO.get("lastWatered"));
//							String string_date = thisJO.get("timePlanted").toString();
//							_date = VegItem.FORMATLONG.parse(string_date); //crashes?
//						} catch (Exception e){
//							e.printStackTrace();
//						}
//						try {
//							_lastWatered = VegItem.FORMATLONG.parse(thisJO.get("lastWatered").toString());
//						} catch (Exception e){
//							e.printStackTrace();
//						}

//						String string_date = thisJO.get("timePlanted").toString();
//						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
//						LocalDate date = LocalDate.parse(string, formatter);
//						System.out.println(date); // 2010-01-02

//						String string_date = thisJO.get("timePlanted").toString();
//						DateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss z", Locale.ENGLISH);
//						Date date = null;
//						try {
//							date = format.parse(string_date);
//						} catch (ParseException e) {
//							e.printStackTrace();
//						}
//						System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010

						String _vegCount = thisJO.get("quantity").toString();

//						Date _lastWatered = (Date)thisJO.get("lastWatered");
						String _garden_veg_id = thisJO.get("garden_veg_ID").toString();
						mAdapter.add(new VegItem(_vegName, VegItem.Affected.valueOf(_affected), _date, _vegCount, _lastWatered, _garden_veg_id));

						mAdapter.notifyDataSetChanged();
					} //for int i =1
				}
				else if (code.equals("no_veg")){
					showDialog("Login Error", message, code);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		} //close on post execute

		public void showDialog(String title, String message, String code)
		{
			builder.setTitle(title);
			if (code.equals("veg_exist") || code.equals("no_veg")) {
				builder.setMessage(message);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
	}

}