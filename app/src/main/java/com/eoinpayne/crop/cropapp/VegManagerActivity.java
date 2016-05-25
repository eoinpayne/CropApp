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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import com.eoinpayne.crop.cropapp.VegItem.Priority;
import com.eoinpayne.crop.cropapp.VegItem.Status;


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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a new VegListAdapter for this ListActivity's ListView
		mAdapter = new VegListAdapter(getApplicationContext()); //mContext instead of getApplicationContect()?

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		log("Entered onActivityResult()");

		// TODO - Check result code and  code. XX ? cancel? / reuest code?
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
	private void loadItems() {
		BufferedReader reader = null;
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
//			String priority = null;
//			String status = null;
			Date date = null;
			String affected = null;
			String vegCount = null;

			while (null != (title = reader.readLine())) {
//				priority = reader.readLine();
//				status = reader.readLine();
				affected = reader.readLine();
				date = VegItem.FORMAT.parse(reader.readLine());
				vegCount = reader.readLine();
//				mAdapter.add(new VegItem(title, Priority.valueOf(priority),
//						Status.valueOf(status), date));
				mAdapter.add(new VegItem(title, VegItem.Affected.valueOf(affected), date, vegCount));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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