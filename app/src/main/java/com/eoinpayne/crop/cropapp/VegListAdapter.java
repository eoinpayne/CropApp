package com.eoinpayne.crop.cropapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.eoinpayne.crop.cropapp.VegItem.Status;

public class VegListAdapter extends BaseAdapter {
	ArrayAdapter myAdapter;



	// List of vegItems
	private final ArrayList<VegItem> mItems = new ArrayList<VegItem>();
	private final Context mContext;
	private static final String TAG = "Lab-UserInterface";

	private View mItemLayout;

	public View getmItemLayout() {
		return this.mItemLayout;
	}


	public VegListAdapter(Context context) {
		mContext = context;
	}

	// Add a vegItem to the adapter
	// Notify observers that the data set has changed
	public void add(VegItem item) {
		mItems.add(item);
		notifyDataSetChanged(); //notifys the view .. which triggers the getView method od adaptyer
	}

	// Clears the list adapter of all items.
	public void clear(){
		mItems.clear();
		notifyDataSetChanged();
	}

	public ArrayList<VegItem> getmItems() {
		return this.mItems;
	}
	// Returns the number of vegItems
	@Override
	public int getCount() {
		return mItems.size();
	}

	// Retrieve the number of vegItems
	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}

	// Get the ID for the vegItem
	// In this case it's just the position
	@Override
	public long getItemId(int pos) {
		return pos;
	}

	//Create a View to display the vegItem
	// at specified position in mItems
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {


		//TODO - Get the current vegItem XX
		final VegItem vegItem = (VegItem) getItem(position);

		//TODO - Inflate the View for this vegItem  XX
		// from veg_item.xml.
		LayoutInflater inflater1 = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		final View item_layout = inflater1.inflate(R.layout.veg_item, parent, false); //null true? -- parent, false

		//TODO - Fill in specific vegItem data xx
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined in the layout file

		//TODO - Display Title in TextView xx
		final TextView titleView = (TextView)item_layout.findViewById(R.id.titleView);
		titleView.setText(vegItem.getTitle());

		//TODO - Display count prefix and count
		final TextView vegCountPrefix = (TextView)item_layout.findViewById(R.id.vegCountPrefix);
		final TextView vegCount = (TextView)item_layout.findViewById(R.id.vegCount);
		vegCount.setText(vegItem.getVegCount());

//		// TODO - Set up Status CheckBox xx
		final CheckBox statusView = (CheckBox)item_layout.findViewById(R.id.statusCheckBox);
		statusView.setChecked(vegItem.getStatus() == Status.DONE);

		//TODO Display days since watered
		final TextView waterCountView = (TextView)item_layout.findViewById(R.id.waterCount);
		final ImageButton waterBTN = (ImageButton)item_layout.findViewById(R.id.water_btn);
		waterBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext,"WATERED SINGLE!!", Toast.LENGTH_LONG).show();

				vegItem.setLastWatered(GetCurrentDate());

				//ToDo persist to DB
				try {
					AddVeg_BackgroundTask waterSingle_backgroundTask = new AddVeg_BackgroundTask(mContext);
					waterSingle_backgroundTask.execute("waterSingle", vegItem.getGardenVegID(), GetCurrentDate().toString());
				}catch (Exception e){
					e.printStackTrace();
				}
				checkWater(vegItem, waterCountView);
			}
		});

		final TextView daysToHarvestView = (TextView)item_layout.findViewById(R.id.etaView);

		checkColour(vegItem, item_layout);
		checkWater(vegItem, waterCountView);
		calcDaysToHarvest(vegItem, daysToHarvestView);
		//todo: pass in daystowater view. repeat daystoharevest.. (if statement in method?)
//		changeTextColour(vegItem, titleView);



		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				log("Entered onCheckedChanged()");

				// TODO - Set up and implement an OnCheckedChangeListener
				//ToDO: EXTRA WORK 1 - cyan/magenta
				if (isChecked) {
					vegItem.setStatus(Status.DONE);
					item_layout.setBackgroundResource(R.color.harvested);
				} else {
					vegItem.setStatus(Status.NOTDONE);
					item_layout.setBackgroundResource(R.color.vegItem);
				}
			}
		});

//		//TODO - Display Priority in a TextView xx
//		// TODO: EXTRA FUNCTIONALITY 3: MAKE A SPINNER XX
//		final Spinner priorityView = (Spinner) item_layout.findViewById(R.id.priorityView);
//		priorityView.setSelection(vegItem.getPriority().getPosition());
//		priorityView.setOnItemSelectedListener(new PrioritySelectedListener(vegItem)) ;


		// TODO - Display Time and Date.xx
		final TextView dateView = (TextView)item_layout.findViewById(R.id.dateView);
		dateView.setText(vegItem.FORMAT.format((vegItem.getDate())));


		//TODO: ONLONGLCIKCLISTENER - alert Dialogue
		//TODO EXTRA  - 4 - long press to do
		item_layout.setOnLongClickListener(new View.OnLongClickListener() {  //creating an annoymous inner class
			@Override
			public boolean onLongClick(View v) {
				new AlertDialog.Builder(parent.getContext())
						.setTitle("Delete Item") // set as strings item Delete
						.setMessage("Are you sure?")
						.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								try {
									AddVeg_BackgroundTask addVeg_backgroundTask = new AddVeg_BackgroundTask(mContext);
									addVeg_backgroundTask.execute("deleteVegItem", vegItem.getGardenVegID());
								}catch (Exception e){
									e.printStackTrace();
								}
								mItems.remove(vegItem);
								notifyDataSetChanged();
							}
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.setIcon(android.R.drawable.ic_dialog_alert)
						.show();

				return true;
			}

		});

		// TODO -  Return the View you created XX
		mItemLayout = item_layout;
		return item_layout;

	} ////////////////////////////////////closes getView()

//	private class PrioritySelectedListener implements AdapterView.OnItemSelectedListener {
//		private final VegItem vegItem;
//
//		public PrioritySelectedListener(VegItem vegItem){
//			this.vegItem = vegItem;
//		}
//
//		@Override
//		public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
//			vegItem.setPriority(VegItem.Priority.valueFromPosition(position));
//		}
//		@Override
//		public void onNothingSelected (AdapterView<?> parent){
//
//		}
//	}



//	private int waterAll(ArrayList<VegItem> mItems){
//		for (VegItem item:mItems) {
//			item.
//		}
//		return daysSinceWatered;
//	}

	public void calcDaysToHarvest(VegItem vegItem, TextView daysToHarvestView){
		int daysToHarvest = 0;
//		daysToHarvest = getDaysDifference(VegListAdapter.GetCurrentDate(), vegItem.getExpectedHarvestDate());
//		daysToHarvestView.setText(String.valueOf(daysToHarvest +1));
		daysToHarvestView.setText(String.valueOf(getDaysDifference(VegListAdapter.GetCurrentDate(), vegItem.getExpectedHarvestDate())+1));
	}


	public void checkWater(VegItem vegItem, TextView waterCountView){
//		waterCountView.setText(String.valueOf(vegItem.getLastWatered()));
		waterCountView.setText(String.valueOf(getDaysWatered(vegItem)));

	}

	public int getDaysWatered(VegItem vegItem) {
//		Calendar c = Calendar.getInstance();
		Date now = GetCurrentDate();

		int daysSinceWatered = getDaysDifference(vegItem.getLastWatered(), now);

		return daysSinceWatered;
	}

	public static int getDaysDifference(Date fromDate, Date toDate)
	{
		if(fromDate==null||toDate==null)
			return 0;


		return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
	}

	//ToDO: EXTRA WORK 1 - cyan/magenta
	public void checkColour(VegItem vegItem, View item_layout ){
		if (vegItem.getStatus().equals(Status.DONE)){
			item_layout.setBackgroundResource(R.color.harvested);
		} else {
			item_layout.setBackgroundResource(R.color.vegItem);
		}
	} //closes check colour




//	//ToDo: EXTRA WORK 2 - Modify textcolor
	//ToDo set each veg item with corresponding "daysToWater"
	//ToDo: set a warning boolean to bepicked up on entry
//	public void changeTextColour(VegItem vegItem, TextView titleView ){ //View item_layout
//		Date now = new Date(System.currentTimeMillis());
//		Date dateOfItem = vegItem.getDate();
//
//		int dateDiff = VegListAdapter.getDaysDifference(now, dateOfItem);
//		String dateDiff_string = String.valueOf(dateDiff);
//
//		if (dateDiff == 0){
//			titleView.setTextColor(Color.RED);
//		} else if (dateDiff == 1){
//			titleView.setTextColor(Color.YELLOW);
//		}
//	}

	public static Date GetCurrentDate(){
		Calendar c = Calendar.getInstance();
		Date currentDate = null;
		try {
			//todo  format?
			Date now = c.getTime();
			String formattedDate =  VegItem.FORMAT.format(now);
			Date parsedDate =  VegItem.FORMAT.parse(formattedDate);
			String test_date = "2016-05-23 14:56:00";
			Date test_parse =  VegItem.FORMAT.parse(test_date);


//			currentDate = VegItem.FORMAT.parse(now.toString());
			return now;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String GetCurrentDate_String(){
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		String formattedDate =  VegItem.FORMAT.format(now);
		try {
			String currentDate_string = VegItem.FORMAT.parse(c.getTime().toString()).toString();
			return currentDate_string;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
