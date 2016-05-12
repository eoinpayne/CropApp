package com.eoinpayne.crop.cropapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HomeActivity extends  AppCompatActivity {          //ListActivity {

    public static int global_userID =  0;
//    int userID = 10;  ///////****/////
    String UserID_String = Integer.toString(global_userID); ///////****/////
    TextView textView;
    Button updateButton;
    Button showUpdateButton;
    Button createGardenButton;

    String JSON_STRING;
    ListView gardenListView;

    public ArrayList<String> json_gardens= new ArrayList<>();
    public ArrayAdapter<String> adapter;  //could use cursosr adaptor for db?

    AlertDialog.Builder builder;  //we can initialise in onPreExecute method
    public EditText input;

    public ValContainer gardenValueContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        input = new EditText(HomeActivity.this);
        gardenListView = (ListView) findViewById(R.id.garden_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, json_gardens);
        adapter.add("test");
        gardenListView.setAdapter(adapter);
        new HomeBackgroundTask().execute("display_gardens", UserID_String);


        //displaying message we passed through with intent "message" in post activity of background task
        textView = (TextView) findViewById(R.id.welcome_txt);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        int userID = bundle.getInt("userID");
        global_userID = userID;
        textView.setText(message);


        gardenValueContainer = new ValContainer();
        gardenValueContainer.setVal("abc");

//        //move to on create and have this button to refresh?
        updateButton = (Button)findViewById(R.id.updateGarden_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                new HomeBackgroundTask().execute("display_gardens", UserID_String);
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
            @Override
            public void onClick(View v) {
//                final String myGardenName;
//                String s = getGardenName();
                getGardenName();

//                try {
//                    new HomeBackgroundTask().execute("create_garden", UserID_String, gardenValueContainer.getVal());
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

//                String myGN = gardenValueContainer.getVal();
//                HomeBackgroundTask mhomebackgroundtask1 = new HomeBackgroundTask();
//                mhomebackgroundtask1.execute("create_garden", UserID_String, myGN);

//                String myGardenName = getGardenName(); ///**
//                BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
//                backgroundTask.execute("create_garden", UserID_String, myGN);
            }
        });


        gardenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) +" is selected", Toast.LENGTH_LONG).show();
                //start intent activy
            }
        });

    } //close onCreate


    public void getGardenName()
    {
        //call pop up window to take garden name
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Input garden name");

        final EditText input = new EditText(HomeActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                input.getText().toString();

                final String s;
                Editable myS = input.getText();
                s = myS.toString();
                gardenValueContainer.setVal(s);

                try {
                    new HomeBackgroundTask().execute("create_garden", UserID_String, s);

                }catch (Exception e){
                    e.printStackTrace();
                }

//                new HomeBackgroundTask().execute("create_garden", UserID_String,s);
//                HomeBackgroundTask mhomebackgroundtask11 = new HomeBackgroundTask();
//                mhomebackgroundtask11.execute("create_garden", UserID_String,s);

//                BackgroundTask backgroundTask = new BackgroundTask(HomeActivity.this);
//                backgroundTask.execute("create_garden", UserID_String, myGardenName);
            }



        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

//        return gardenValueContainer.getVal();

    } //close getGardenName

//---------------------------------------------------------------------------------------------
//
//
// ---------------------------------------------------------------------------------------------

    public class HomeBackgroundTask extends AsyncTask<String,Void,String> {
        String createGarden_URL = "http://10.0.2.2/createGarden.php";
        String displayGarden_URL = "http://10.0.2.2/displayGardens.php";
        AlertDialog.Builder builder;  //we can initialise in onPreExecute method
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            builder = new AlertDialog.Builder(HomeActivity.this); //ctx
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Connecting to Server");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

//            adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, json_gardens);

        }

        @Override
        //change return type & public class BackgroundTask 3rd param to String
        protected String doInBackground(String... params) {
            String method = params[0];

            //////////////    Show Garden     //////////////////////////////
            if (method.equals("display_gardens"))
            {
                try {
                    URL url = new URL(displayGarden_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST"); //?
                    httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String userID;
                    userID = params[1];
                    String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID,"UTF-8" );
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
                    Thread.sleep(5500);
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

            //////////////    Create  Garden         ///////////////////////////////////////////////////////////////////////////////
            else if (method.equals("create_garden"))
            {
                try {
                    URL url = new URL(createGarden_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST"); //?
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String userID, gardenName;
                    userID = params[1];
                    gardenName = params[2];
                    String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID,"UTF-8" ) + "&" +
                            URLEncoder.encode("gardenName", "UTF-8")+ URLEncoder.encode(gardenName, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //now to get the response FROM the server. (in Json, needs to be decoded)
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder(); //to read JSON response from bufferedReader
                    String line = "";  //take each line from reader and append to String.
                    while ((bufferedReader.readLine()) != null)///
                    { line = bufferedReader.readLine();
                        stringBuilder.append(line+"\n");
                    }
                    httpURLConnection.disconnect(); //close connection
                    Thread.sleep(2500);
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
//            JSONObject JO2 = JO.getJSONArray(); //get inner object from array at index 0
                String code = JO.getString("code");  //from server
                String message = JO.getString("message");
//            Toast.makeText(this.activity,message, Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //to close progressDialog

                /////////////    Display Gardens     ///////////////////
                if (code.equals("gardens_exist")){
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject thisJO = jsonArray.getJSONObject(i);
                        String tempGarden = "GardenID: " + thisJO.getString("gardenID") +  ".   Garden Name: " + thisJO.getString("gardenName");
//                    json_gardens.add(tempGarden);  ///////*************
                        json_gardens.add(tempGarden);
                        adapter.notifyDataSetChanged();
                    } //for int i =1
                }
                else if (code.equals("no_gardens")){
                    showDialog("Login Error", message, code);
                }
                /////////////    CREATE GARDEN     ///////////////////
                else if (code.equals("garden_created")){
                    showDialog("Garden Created!", message, code);
                }
                else if (code.equals("garden_exists")){
                    showDialog("Garden exists already", message, code);
                }
                else if (code.equals("createGarden_fail")){
                    showDialog("Error, create fail", message, code);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } //close on post execute

        public void showDialog(String title, String message, String code)
        {
            builder.setTitle(title);
            if (code.equals("no_gardens")) {
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when user clicks ok, we dismiss and finish activity
                        dialog.dismiss();
//                    activity.finish();
                    }
                });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            }

            else if (code.equals("createGarden_fail") || code.equals("garden_exists")) {
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when user clicks ok, we dismiss and finish activity
                        dialog.dismiss();
//                    activity.finish();
                    }
                });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            }

            else if (code.equals("garden_created")) {
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when user clicks ok, we dismiss and finish activity
                        dialog.dismiss();
//                    activity.finish();
                        //launch activity here for entering in details etc
                    }
                });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            }

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public class ValContainer{
        private String val;
        public ValContainer(){}
        public ValContainer(String v){
            this.val = v; }
        public String getVal(){ return val;}
        public  void setVal(String val){
            this.val = val;
        }

    }





}
