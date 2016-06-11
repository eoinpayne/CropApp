package com.eoinpayne.crop.cropapp;

import android.app.ListActivity;
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
import android.view.View.OnLongClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccordianActivity extends AppCompatActivity implements OnClickListener
{
    private Context context;
    //retrieve Veg Name from bundle
    private String chosenVeg;
    private Toolbar toolbar;
//    Bundle bundle = getIntent().getExtras();
//    String message = bundle.getString("message");
//    int userID = bundle.getInt("userID");

    public OnLongClickListener longClickListner;
    LinearLayout    soilPanel,  lightPanel,   howToPlantPanel,    carePanel,  pestsPanel,   summaryPanel,   likesPanel, dislikesPanel;
    TextView        soilText,   lightText,    howToPlantText,     careText,   pestsText,    summaryText,    likesText,  dislikesText,
                    soil,       light,        howToPlant,         care,       pests,        summary,        likes,      dislikes;


    //non accordian text views
    TextView name,growth,monthToPlant,avgDaysToGrow,difficulty, avgHeight, avgSpread, perSqFt, waterFrequency;
    View openLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accordian);
        //pull details from intent's extras
        chosenVeg = getIntent().getStringExtra("chosenVeg");

        //set toolbar, and display title with vegetable chosen.
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(chosenVeg + " info");

        //set the Header in toolbar to be clickable and return user to gardens,
        TextView mToolBarHeader = (TextView) toolbar.findViewById(R.id.toolbar_header);
        mToolBarHeader.setOnClickListener(new OnClickListener() {
            @Override  //clicking title brings user back home.
            public void onClick(View v) {
                Intent intent = new Intent(AccordianActivity.this, HomeActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userID", HomeActivity.global_userID);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        context = this;


        //todo Set TextViews based on vegName selected previously.


        //non accordian textViews
        name = (TextView) this.findViewById(R.id.acc_vegName_content);
        growth = (TextView) findViewById(R.id.acc_growthRate_content);
        monthToPlant = (TextView) findViewById(R.id.acc_MonthToPlant_content);
        avgDaysToGrow = (TextView) findViewById(R.id.acc_AvgDaysToGrow_content);
        difficulty = (TextView) findViewById(R.id.acc_Difficulty_content);
        avgHeight = (TextView) findViewById(R.id.acc_AvgHeight_content);
        avgSpread = (TextView) findViewById(R.id.acc_AvgSpread_content);
        perSqFt = (TextView) findViewById(R.id.acc_perSqFt_content);
        waterFrequency = (TextView) findViewById(R.id.acc_WaterFrequency_content);

        //accordian layouts
        soilPanel = (LinearLayout) findViewById(R.id.acc_panel_soilType);
        lightPanel = (LinearLayout) findViewById(R.id.acc_panel_lightReq);
        howToPlantPanel = (LinearLayout) findViewById(R.id.acc_panel_howToPlant);
        carePanel = (LinearLayout) findViewById(R.id.acc_panel_Care);
        pestsPanel = (LinearLayout) findViewById(R.id.acc_panel_Pests);
        summaryPanel = (LinearLayout) findViewById(R.id.acc_panel_Summary);
        likesPanel = (LinearLayout) findViewById(R.id.acc_panel_likes);
        dislikesPanel = (LinearLayout) findViewById(R.id.acc_panel_Dislikes);

        //accordian textViews
        soilText = (TextView) findViewById(R.id.acc_soilType_title);
        lightText = (TextView) findViewById(R.id.acc_lightReq_title);
        howToPlantText = (TextView) findViewById(R.id.acc_howToPlant_title);
        careText = (TextView) findViewById(R.id.acc_Care_title);
        pestsText = (TextView) findViewById(R.id.acc_Pests_title);
        summaryText = (TextView) findViewById(R.id.acc_Summary_title);
        likesText = (TextView) findViewById(R.id.acc_Likes_title);
        dislikesText = (TextView) findViewById(R.id.acc_Dislikes_title);

        soil = (TextView) findViewById(R.id.acc_soilType_content);
        light = (TextView) findViewById(R.id.acc_lightReq_content);
        howToPlant = (TextView) findViewById(R.id.acc_howToPlant_content);
        care = (TextView) findViewById(R.id.acc_Care_content);
        pests = (TextView) findViewById(R.id.acc_Pests_content);
        summary = (TextView) findViewById(R.id.acc_Summary_content);
        likes = (TextView) findViewById(R.id.acc_Likes_content);
        dislikes = (TextView) findViewById(R.id.acc_Dislikes_content);

        //panel1.setVisibility(View.VISIBLE);

        //panel1.setVisibility(View.VISIBLE);

        //Log.v("CZ","height at first ..." + panel1.getMeasuredHeight());
        fillVegFields(chosenVeg, context);

        soilText.setOnClickListener(this);
        soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
//        soilText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                LinearLayout soilpanel = (LinearLayout) findViewById(R.id.acc_panel_soilType);
//                soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
//                if (soilPanel.getVisibility() == View.VISIBLE) {
//                    soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
//                    soilPanel.setVisibility(View.GONE);
//                } else {
//                    soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
//                    soilPanel.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
        lightText.setOnClickListener(this);
        lightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        howToPlantText.setOnClickListener(this);
        howToPlantText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        careText.setOnClickListener(this);
        careText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        pestsText.setOnClickListener(this);
        pestsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        summaryText.setOnClickListener(this);
        summaryText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        likesText.setOnClickListener(this);
        likesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        dislikesText.setOnClickListener(this);
        dislikesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);


    } //close on create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_browse, menu);
        return true;

//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();

        if(res_id == R.id.browse_veg){
            //todo launch veg chooser
            HomeActivity.selectVegToBrowse(context);

        }

        else if(res_id == R.id.show_gardens){
            Intent intent = new Intent(AccordianActivity.this, HomeActivity.class);
            Bundle extras = new Bundle();
//            extras.putString("message", message);
            extras.putInt("userID", HomeActivity.global_userID);
            intent.putExtras(extras);
            //TODO launch background task to scrape DB and build text file of veg info.
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        hideOthers(v);
    }
    private void hideThemAll()
    {
        if(openLayout == null) return;
        if(openLayout == soilPanel) {
            soilPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, soilPanel, true));
            soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == lightPanel) {
            lightPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, lightPanel, true));
            lightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == howToPlantPanel){
            howToPlantPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, howToPlantPanel, true));
            howToPlantText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == carePanel){
            carePanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, carePanel, true));
            careText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == pestsPanel){
            pestsPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, pestsPanel, true));
            pestsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == summaryPanel){
            summaryPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, summaryPanel, true));
            summaryText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == likesPanel){
            likesPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, likesPanel, true));
            likesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }if(openLayout == dislikesPanel){
            dislikesPanel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, dislikesPanel, true));
            dislikesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        }
    }
    private void hideOthers(View layoutView)
    {
        {
            int v;
            if(layoutView.getId() == R.id.acc_soilType_title)
            {
                v = soilPanel.getVisibility();
                if(v != View.VISIBLE)
                {
                    soilPanel.setVisibility(View.VISIBLE);
//                    soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                    Log.v("CZ","height..." + soilPanel.getHeight());
                }
//                else { soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);}

                //panel1.setVisibility(View.GONE);
                //Log.v("CZ","again height..." + panel1.getHeight());
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    soilPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, soilPanel, true));
                    soilText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                }
            }
            else if(layoutView.getId() == R.id.acc_lightReq_title)
            {
                v = lightPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    lightPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, lightPanel, true));
                    lightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }
            else if(layoutView.getId() == R.id.acc_howToPlant_title)
            {
                v = howToPlantPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    howToPlantPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, howToPlantPanel, true));
                    howToPlantText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
                }
            }
            else if(layoutView.getId() == R.id.acc_Care_title)
            {
                v = carePanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    carePanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, carePanel, true));
                    careText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }
            else if(layoutView.getId() == R.id.acc_Pests_title)
            {
                v = pestsPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    pestsPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, pestsPanel, true));
                    pestsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }

            else if(layoutView.getId() == R.id.acc_Summary_title)
            {
                v = summaryPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    summaryPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, summaryPanel, true));
                    summaryText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }
            else if(layoutView.getId() == R.id.acc_Likes_title)
            {
                v = likesPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    likesPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, likesPanel, true));
                    likesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }
            else if(layoutView.getId() == R.id.acc_Dislikes_title)
            {
                v = dislikesPanel.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    dislikesPanel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, dislikesPanel, true));
                    dislikesText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

                }
            }

        }
    }

    public class ScaleAnimToHide extends ScaleAnimation
    {

        private View mView;

        private LayoutParams mLayoutParams;

        private int mMarginBottomFromY, mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view,boolean vanishAfter)
        {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            openLayout = null;
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;

            Log.v("CZ","height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY  + " , mMarginBottomToY.." +mMarginBottomToY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
            else if (mVanishAfter)
            {
                mView.setVisibility(View.GONE);
            }
        }
    }
    public class ScaleAnimToShow extends ScaleAnimation
    {

        private View mView;

        private LayoutParams mLayoutParams;

        private int mMarginBottomFromY, mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view,boolean vanishAfter)
        {
            super(fromX, toX, fromY, toY);
            openLayout = view;
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            mView.setVisibility(View.VISIBLE);
            int height = mView.getHeight();
            //mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
            //mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

            mMarginBottomFromY = 0;
            mMarginBottomToY = height;

            Log.v("CZ",".................height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY  + " , mMarginBottomToY.." +mMarginBottomToY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
        }


    }

    //method that will fill all textView with relevant information
    //information will be taken from a pre loaded file with all veg information stored as Json Strings.
    //the json strings will be returned and converted to JsonObjects with the relevant object being matched with "chosenVeg"
    public void fillVegFields(String chosenVeg, Context ctx){
        JSONObject jsonObject;
        JSONArray daysToGrow_array;
        int daysToGrow = 0;
        try {
            jsonObject = new JSONObject(AddVegActivity.retrieveStringFromFile(DBScraper_vegInfo.VegInfo_file, ctx));
            daysToGrow_array = jsonObject.getJSONArray("vegInfo_collection");
            for (int i=0; i<daysToGrow_array.length(); i++){
                JSONObject veginfo_array = (JSONObject) daysToGrow_array.get(i);
                if(veginfo_array.has(chosenVeg)){
//                    daysToGrow = (int)daysToGrow_pair.get(vegName);
                    JSONObject vegInfo_obj = (JSONObject)veginfo_array.get(chosenVeg);
                    String vegName = vegInfo_obj.get("vegName").toString();


                    //todo fill all text fields with values
                    name.setText(vegName);
                    growth.setText(vegInfo_obj.get("growthRate").toString());
                    monthToPlant.setText(vegInfo_obj.get("pMonthToPlant").toString());
                    avgDaysToGrow.setText(vegInfo_obj.get("avgDaysToGrow").toString());
                    difficulty.setText(vegInfo_obj.get("difficulty").toString());
                    avgHeight.setText(vegInfo_obj.get("height_feet").toString());
                    avgSpread.setText(vegInfo_obj.get("spread_feet").toString());
                    perSqFt.setText(vegInfo_obj.get("noPerSqFt").toString());
                    waterFrequency.setText(vegInfo_obj.get("daysToWater").toString());

                    soil.setText(vegInfo_obj.get("pSoilType").toString());
                    light.setText(vegInfo_obj.get("lightReq").toString());
                    howToPlant.setText(vegInfo_obj.get("howToPlant").toString());
                    care.setText(vegInfo_obj.get("care").toString());
                    pests.setText(vegInfo_obj.get("Pests").toString());
                    summary.setText(vegInfo_obj.get("summary").toString());
                    likes.setText(vegInfo_obj.get("favours").toString());
                    dislikes.setText(vegInfo_obj.get("dislikes").toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

