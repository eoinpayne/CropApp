package com.eoinpayne.crop.cropapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends  AppCompatActivity {          //ListActivity {

    public static int global_userID =  10;
    int userID = 10;  ///////****/////
    String UserID_String = Integer.toString(userID); ///////****/////
    TextView textView;
    Button updateButton;
    Button showUpdateButton;
    Button createGardenButton;

    String JSON_STRING;
    ListView gardenListView;
    ArrayAdapter<String> adapter;  //could use cursosr adaptor for db?
//    AlertDialog.Builder builder;  //we can initialise in onPreExecute method
    String tempGN;
    EditText input;

    public static ArrayList<String> json_gardens= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
//        backgroundTask.execute("display_gardens", UserID_String);
        tempGN = "";
        input = new EditText(HomeActivity.this);

        gardenListView = (ListView) findViewById(R.id.garden_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, json_gardens);
//        adapter.notifyDataSetChanged();
        gardenListView.setAdapter(adapter);

//         Put divider between ToDoItems and FooterView
//        getListView().setFooterDividersEnabled(true);
//        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, getListView(),false );
//        getListView().addFooterView(footerView);
//
//        footerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                log("Entered footerView.OnClickListener.onClick()");
//                BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
//                backgroundTask.execute("create_garden", UserID_String);
//                //TODO - Attach Listener to FooterView. Implement onClick(). XX
////                Intent addToDo_intent = new Intent(getApplicationContext(), AddGardenActivity.class);
////                startActivityForResult(addToDo_intent, ADD_TODO_ITEM_REQUEST);
//            }
//        });

        //displaying message we passed through with intent "message" in post activity of background task
        textView = (TextView) findViewById(R.id.welcome_txt);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        int userID = bundle.getInt("userID");
        global_userID = userID;
        textView.setText(message);

//        //move to on create and have this button to refresh?
        updateButton = (Button)findViewById(R.id.updateGarden_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if JSON return is empty - display alert builder "no gardens")

                //else - display box (showing x gardens)
                //show important garden updates (x amount of days since watered etc)
                HomeActivity.json_gardens.clear();
                BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
                backgroundTask.execute("display_gardens", UserID_String);
//                notifyAdapter();
                adapter.notifyDataSetChanged();
            }
        });


        showUpdateButton = (Button)findViewById(R.id.showUpdateGarden_btn);
        showUpdateButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });

        //create garden
        createGardenButton = (Button)findViewById(R.id.createGarden_btn);
        createGardenButton .setOnClickListener(new View.OnClickListener() {
            String gardenName = "";
            @Override
            public void onClick(View v) {

                String myGardenName = getGardenName(); ///**
                BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
                backgroundTask.execute("create_garden", UserID_String, myGardenName);
            }
        });


        gardenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) +" is selected", Toast.LENGTH_LONG).show();
            }
        });


//        gardenListView = (ListView) findViewById(R.id.garden_list);
//        String message = getIntent().getStringExtra("message");
//        gardenListView.setText(message);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    } //close onCreate

    public void getJSON(View view)
    {

    }

    public String getGardenName()
    {
        tempGN = "gardenz";
//        final String tempGN = "";
        //call pop up window to take garden name
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Input garden name");

//        final EditText input = new EditText(HomeActivity.this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        input.setInputType();
//        builder.setView(input);

//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tempGN = input.getText().toString();
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();

        return tempGN;

    }





}
