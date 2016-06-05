package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class HomeActivity extends AppCompatActivity {          //ListActivity {

    private Toolbar toolbar;
    public static int global_userID;
    public static String global_UserID_String;
    public String UserID_String = Integer.toString(global_userID); ///////****/////
    TextView textView;
//    Button updateButton;
//    Button createGardenButton;
    ListView gardenListView;
    public ArrayList<Garden> mGardens = new ArrayList<>();
    public ArrayAdapter<Garden> adapter;  //could use cursosr adaptor for db?

//    AlertDialog.Builder builder;  //we can initialise in onPreExecute method
    public EditText input;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        context = this;

        //displaying message we passed through with intent "message" in post activity of background task
        textView = (TextView) findViewById(R.id.welcome_txt);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        int userID = bundle.getInt("userID");
        global_userID = userID;
        global_UserID_String = Integer.toString(userID);
//        textView.setText(message);

        input = new EditText(HomeActivity.this);
        gardenListView = (ListView) findViewById(R.id.garden_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mGardens);
        gardenListView.setAdapter(adapter);
        new HomeBackgroundTask().execute("display_gardens", UserID_String);


//        //move to on create and have this button to refresh?
//        updateButton = (Button)findViewById(R.id.updateGarden_btn);
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.clear();
//                new HomeBackgroundTask().execute("display_gardens", UserID_String);
//            }
//        });

        ////////////create garden
//        createGardenButton = (Button)findViewById(R.id.createGarden_btn);
//        createGardenButton .setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getGardenName();
//            }
//        });

        //// LOAD GARDEN
        gardenListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                final Garden selected_garden = (Garden)parent.getItemAtPosition(position);
                //TODO delete garden item and remove from DB on long click
//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) +" is to be deleted", Toast.LENGTH_LONG).show();


                new AlertDialog.Builder(parent.getContext())
                        .setTitle("Delete Garden") // set as strings item Delete
                        .setMessage("Are you sure?")
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    AddVeg_BackgroundTask addVeg_backgroundTask = new AddVeg_BackgroundTask(HomeActivity.this);
                                    addVeg_backgroundTask.execute("deleteGarden", selected_garden.getGardenID());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                mGardens.remove(selected_garden);
                                adapter.notifyDataSetChanged();
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




//        item_layout.setOnLongClickListener(new View.OnLongClickListener() {  //creating an annoymous inner class
//            @Override
//            public boolean onLongClick(View v) {
//                new AlertDialog.Builder(parent.getContext())
//                        .setTitle("Delete Item") // set as strings item Delete
//                        .setMessage("Are you sure?")
//                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                try {
//                                    AddVeg_BackgroundTask addVeg_backgroundTask = new AddVeg_BackgroundTask(mContext);
//                                    addVeg_backgroundTask.execute("deleteVegItem", vegItem.getGardenVegID());
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                                mItems.remove(vegItem);
//                                notifyDataSetChanged();
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//                return true;
//            }
//
//        });










        //// LOAD GARDEN
        gardenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) +" is selected", Toast.LENGTH_LONG).show();

                Garden selected_garden = (Garden)parent.getItemAtPosition(position);
                Intent intent = new Intent(HomeActivity.this, VegManagerActivity.class );
                Bundle extras = new Bundle();
                extras.putString("gardenName", selected_garden.getGardenName());
                extras.putString("gardenID", selected_garden.getGardenID());
                extras.putString("userID", selected_garden.getUserID());
                intent.putExtras(extras);
                startActivity(intent); //starts VegManagerActivity

            }
        });
    } //close onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return true;

//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();

        if(res_id == R.id.browse_veg){
            //todo display the browse catalogue activity?
        }
        else if(res_id == R.id.create_garden){
            getGardenName();
        }
        else if(res_id == R.id.refresh_garden){
            adapter.clear();
            new HomeBackgroundTask().execute("display_gardens", UserID_String);
        }
        return super.onOptionsItemSelected(item);
    }






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
                final String gardenName;
                Editable gardenName_input = input.getText();
                gardenName = gardenName_input.toString();
                try {
                    new HomeBackgroundTask().execute("create_garden", UserID_String, gardenName);
                        }   catch (Exception e){  e.printStackTrace();   }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    } //close getGardenName

//---------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------

    public class HomeBackgroundTask extends AsyncTask<String,Void,String> {
//        String createGarden_URL = "http://192.168.0.34/createGarden.php";
//        String displayGarden_URL = "http://192.168.0.34/displayGardens.php";

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
        }

        @Override
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
                            URLEncoder.encode("gardenName", "UTF-8")+ "=" + URLEncoder.encode(gardenName, "UTF-8");
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
                progressDialog.dismiss(); //to close progressDialog

                /////////////    Display Gardens     ///////////////////
                //if user has gardens in the db to display
                if (code.equals("gardens_exist")){
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject thisJO = jsonArray.getJSONObject(i);
                        String gardenName = thisJO.getString("gardenName");
                        String gardenID = thisJO.getString("gardenID");
                        String userID = UserID_String;
                        Garden tempGarden = new Garden(gardenName,gardenID,userID );
//                        String tempGarden = "GardenID: " + thisJO.getString("gardenID") +  ".   Garden Name: " + thisJO.getString("gardenName");
                        mGardens.add(tempGarden);
                        adapter.notifyDataSetChanged();
                    } //for int i =1
                }
                else if (code.equals("no_gardens")){
                    showDialog("Login Error", message, code);
                }
                /////////////    CREATE GARDEN     ///////////////////
                else if (code.equals("garden_created")){
                    showDialog("Garden Created!", message, code);
                    String gardenName = JO.getString("gardenName");
                    String gardenID = JO.getString("gardenID");
                    String userID = UserID_String;
                    Garden newGarden = new Garden(gardenName,gardenID,userID );
//                        String newGarden = "GardenID: " + JO.getString("gardenID") +  ".   Garden Name: " + JO.getString("gardenName");
                    mGardens.add(newGarden);
                    adapter.notifyDataSetChanged();
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
            if (code.equals("createGarden_fail") || code.equals("garden_exists")
                    || code.equals("garden_created") || code.equals("no_gardens")) {
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


//    public class ValContainer{
//        private String val;
//        public ValContainer(){}
//        public ValContainer(String v){
//            this.val = v; }
//        public String getVal(){ return val;}
//        public  void setVal(String val){
//            this.val = val;
//        }
//    }





}
