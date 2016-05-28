package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

/**
 * Created by Hefty Balls on 24/04/2016.
 */

public class BackgroundTask extends AsyncTask<String,Void,String>{
    //to execute background operation, go to registrationActivity.java in the else condition where
    //user enetered correct details

    //define variables
//    String register_url = "http://192.168.0.34/register.php";
//    String login_url = "http://192.168.0.34/login.php";
//    String register_url = "http://147.252.139.83/register.php";
//    String login_url = "http://147.252.139.83/login.php";
    String register_url = "http://10.0.2.2/register.php"; //make global
    String login_url = "http://10.0.2.2/login.php";
    String addVegItem_url = "http://10.0.2.2/addVeg.php";


    Context ctx;
    Activity activity;
    AlertDialog.Builder builder;  //we can initialise in onPreExecute method
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    public ArrayList<String> json_gardens= new ArrayList<>();
    ListView gardenListView;

    HomeActivity.HomeBackgroundTask homeBackgroundTask;

    //define contructor for class ... we will pass context in from RegisterActivity
    public BackgroundTask(Context ctx){
        //initialise variables
        this.ctx = ctx;
        activity = (Activity)ctx; //cast conext to activity

    }
    //implement life cycle methods
    @Override
    protected void onPreExecute() {
        builder = new AlertDialog.Builder(activity); //ctx
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting to Server");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, json_gardens);
    }

    @Override
    //change return type & public class BackgroundTask 3rd param to String
    protected String doInBackground(String... params) {
        String method = params[0];
        if(method.equals("register")){
            try {  //to pass data to server
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String name = params[1]; //pull variables from the parameters
                String email = params[2];
                String mpassword = params[3];
                //create data string to pass through
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name,"UTF-8" ) + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email,"UTF-8" ) + "&" +
                        URLEncoder.encode("mpassword", "UTF-8") + "=" + URLEncoder.encode(mpassword,"UTF-8" );
                bufferedWriter.write(data);  //pass data string
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //now to get the response FROM the server.
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder(); //to read JSON response from bufferedReader
                String line = "";  //take each line from reader and append to String.
                while ((bufferedReader.readLine()) != null)///
                { line = bufferedReader.readLine();
                    stringBuilder.append(line+"\n");
                }
                //create a pause between background methods
                httpURLConnection.disconnect(); //close conenction
                Thread.sleep(800);
//                Log.i();
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    ////////////////////////////LOGIN////////////////////////////////////////
        //pass email and password to server, get a response. if positibe display ome acticvity, toherwise alert dialoge
        else if (method.equals("login"))
        {
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String email, mpassword;
                email = params[1];
                mpassword = params[2];
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email,"UTF-8" ) + "&" +
                        URLEncoder.encode("mpassword", "UTF-8") + "=" + URLEncoder.encode(mpassword,"UTF-8" );
                bufferedWriter.write(data);  //pass data string
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
                //create a pause between background methods
                httpURLConnection.disconnect(); //close conenction
                Thread.sleep(1200);
//                Log.i();
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
        }   //close login else if

        //////////////////////////// ADD VEG ITEM  ////////////////////////////////////////
        else if (method.equals("addVegItem"))
        {
            try {
                URL url = new URL(addVegItem_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenID, userID, vegName, affectedByRain, datePlanted, vegCount;
//                userID = params[1];
                gardenID = params[1];
                vegName = params[2];
                affectedByRain = params[3];
                datePlanted = params[4];
                vegCount = params[5];
                userID = params[6];
                //ToDo pass through the estaimted harvest date
//                int eta =
                String data = URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(gardenID,"UTF-8" )
                        + "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID,"UTF-8" )
                        + "&" + URLEncoder.encode("vegName", "UTF-8") + "=" + URLEncoder.encode(vegName,"UTF-8" )
                        + "&" + URLEncoder.encode("affectedByRain", "UTF-8") + "=" + URLEncoder.encode(affectedByRain,"UTF-8" )
                        + "&" + URLEncoder.encode("datePlanted", "UTF-8") + "=" + URLEncoder.encode(datePlanted,"UTF-8")
                        + "&" + URLEncoder.encode("vegCount", "UTF-8") + "=" + URLEncoder.encode(vegCount,"UTF-8");
//                        + "&" + URLEncoder.encode("eta", "UTF-8") + "=" + URLEncoder.encode(eta,"UTF-8") ;
                bufferedWriter.write(data);  //pass data string
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
                //create a pause between background methods
                httpURLConnection.disconnect(); //close conenction
                Thread.sleep(1200);
//                Log.i();
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
        }   //close login else if

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

            /////////////    REGISTER     ///////////////////
            // call helper method which will display JSON messages in alert
            if (code.equals("reg_true")){
                showDialog("Registration Success", message, code);
            }
            else if (code.equals("reg_false")){
                showDialog("Registration Failed", message, code);
            }
            /////////////    LOGIN     ///////////////////
            else if (code.equals("login_true")){
                int userID = JO.getInt("userID");
                String userID_STR = JO.getString("userID");
                HomeActivity.global_userID = userID;
                String str_userID = JO.getString("userID");
                //ToDo launch background task to scrape DB and build text file of items belonging to user.

                Intent intent = new Intent(activity, HomeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("message", message);
                extras.putInt("userID", userID);
                intent.putExtras(extras);
                activity.startActivity(intent);

                /////////////    ADD VEG     ///////////////////
                if (code.equals("addItem_success")){
                    showDialog("Planted Successfully!", message, code);
                }

                //TODO launch background task to scrape DB and build text file of veg info.
                new Thread(new DBScraper_vegInfo(ctx)).start();
            }
            else if (code.equals("login_false")){
                showDialog("Login Error", message, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } //close on post execute

    //to analyse message and dipaly alert
    public void showDialog(String title, String message, String code)
    {
        builder.setTitle(title);
        if (code.equals("reg_true")||code.equals("reg_false")){ //if so we will show message
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //when user clicks ok, we dismiss and finish activity
                    dialog.dismiss();
                    activity.finish();
                }
            });
        } //close if
        else if (code.equals("login_false")) { //if so we will show message

            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    EditText email, mpassword;
                    email = (EditText) activity.findViewById(R.id.email);
                    mpassword = (EditText) activity.findViewById(R.id.password);
                    email.setText("");
                    mpassword.setText("");
                    dialog.dismiss();
//                    activity.finish();
                }
            });
        } //close else if
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
