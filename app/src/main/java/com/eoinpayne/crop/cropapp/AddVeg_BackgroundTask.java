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


/**
 * Created by Hefty Balls on 24/04/2016.
 */

public class AddVeg_BackgroundTask extends AsyncTask<String,Void,String> {
    //to execute background operation, go to registrationActivity.java in the else condition where
    //user enetered correct details

    //define variables
//    String addVegItem_url = "http://192.168.0.34/addVeg.php";

//    String login_url = "http://147.252.139.83/login.php";

    String addVegItem_url = "http://10.0.2.2/addVeg.php";
    String deleteVegItem_url = "http://10.0.2.2/deleteVeg.php";
    String deleteGarden_url = "http://10.0.2.2/deleteGarden.php";
    String waterSingle_url = "http://10.0.2.2/waterSingle.php";
    String waterAll_url = "http://10.0.2.2/waterAll.php";


    Context ctx;
//    Activity activity;
    AlertDialog.Builder builder;  //we can initialise in onPreExecute method
    ProgressDialog progressDialog;
//    ArrayAdapter<String> adapter;
//    public ArrayList<String> mGardens= new ArrayList<>();
//    ListView gardenListView;

//    HomeActivity.HomeBackgroundTask homeBackgroundTask;

    //define contructor for class ... we will pass context in from RegisterActivity
    public AddVeg_BackgroundTask(Context ctx){
        //initialise variables
        this.ctx = ctx;
//        activity = (Activity)ctx; //cast conext to activity

    }
    //implement life cycle methods
    @Override
    protected void onPreExecute() {
//        builder = new AlertDialog.Builder(activity); //ctx
//        progressDialog = new ProgressDialog(ctx);
//        progressDialog.setTitle("Please Wait");
//        progressDialog.setMessage("Connecting to Server");
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, mGardens);
    }

    @Override
    //change return type & public class BackgroundTask 3rd param to String
    protected String doInBackground(String... params) {
        String method = params[0];
        //////////////////////////// ADD VEG ITEM  ////////////////////////////////////////
        if (method.equals("addVegItem"))
        {
            try {
                URL url = new URL(addVegItem_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenID, userID, vegName, affectedByRain, datePlanted, vegCount, lastWatered, eta;
//                userID = params[1];
                gardenID = params[1];
                vegName = params[2];
                affectedByRain = params[3];
                datePlanted = params[4];
                vegCount = params[5];
                userID = params[6];
                lastWatered = params[7];
                eta = params[7]; //expectedHarvestDate
                //ToDo pass through the estaimted harvest date
//                int eta =
                String data = URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(gardenID,"UTF-8" )
                        + "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID,"UTF-8" )
                        + "&" + URLEncoder.encode("vegName", "UTF-8") + "=" + URLEncoder.encode(vegName,"UTF-8" )
                        + "&" + URLEncoder.encode("affectedByRain", "UTF-8") + "=" + URLEncoder.encode(affectedByRain,"UTF-8" )
                        + "&" + URLEncoder.encode("datePlanted", "UTF-8") + "=" + URLEncoder.encode(datePlanted,"UTF-8")
                        + "&" + URLEncoder.encode("vegCount", "UTF-8") + "=" + URLEncoder.encode(vegCount,"UTF-8")
                        + "&" + URLEncoder.encode("lastWatered", "UTF-8") + "=" + URLEncoder.encode(lastWatered,"UTF-8")
                        + "&" + URLEncoder.encode("eta", "UTF-8") + "=" + URLEncoder.encode(eta,"UTF-8") ; //expectedHarvestDate
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
//                Thread.sleep(1200);
//                Log.i();
                //ToDo: finish?
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();       }
        }   //close login else if


                ///////////// Delete Veg Item /////////////
        else if (method.equals("deleteVegItem"))
        {
            try {
                URL url = new URL(deleteVegItem_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenVegID;
                gardenVegID = params[1];

                //ToDo pass through the estaimted harvest date
                String data = URLEncoder.encode("gardenVegID", "UTF-8") + "=" + URLEncoder.encode(gardenVegID,"UTF-8");
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
                Thread.sleep(800);
//                Log.i();
                //ToDo: finish?
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();       }
        }   //close login else if

            ////////////////// Delete Garden //////////////////////
        else if (method.equals("deleteGarden"))
        {
            try {
                URL url = new URL(deleteGarden_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenID;
                gardenID = params[1];

                //ToDo pass through the estaimted harvest date
                String data = URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(gardenID,"UTF-8");
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
                Thread.sleep(800);
//                Log.i();
                //ToDo: finish?
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();       }
        }   //close login else if


        ////////////////// Water Single Veg //////////////////////
        else if (method.equals("waterSingle"))
        {
            try {
                URL url = new URL(waterSingle_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenVegID, currentDate;
                gardenVegID = params[1];
                currentDate = params[2];

                //ToDo pass through the estaimted harvest date
                String data = URLEncoder.encode("gardenVegID", "UTF-8") + "=" + URLEncoder.encode(gardenVegID,"UTF-8")
                        + "&" + URLEncoder.encode("currentDate", "UTF-8") + "=" + URLEncoder.encode(currentDate,"UTF-8");
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
                Thread.sleep(800);
//                Log.i();
                //ToDo: finish?
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();       }
        }   //close login else if

        ////////////////// Water All  Veg //////////////////////
        else if (method.equals("waterAll"))
        {
            try {
                URL url = new URL(waterAll_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST"); //?
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String gardenID;
                gardenID = params[1];

                //ToDo pass through the estaimted harvest date
                String data = URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(gardenID,"UTF-8");
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
                Thread.sleep(800);
//                Log.i();
                //ToDo: finish?
                return stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();       }
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
        //todo drill into file and add to it?

        //we decode json data
        try {
            JSONObject jsonObject = new JSONObject(json);  //get Json object
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");  //get objects' array
            JSONObject JO = jsonArray.getJSONObject(0); //get inner object from array at index 0
            String code = JO.getString("code");  //from server
//            String message = JO.getString("message");
//            progressDialog.dismiss(); //to close progressDialog


            /////////////    ADD VEG     ///////////////////
            if (code.equals("vegItem_deleted") || code.equals("garden_deleted")){
//                showDialog("Planted Successfully!", message, code);
                Toast.makeText(ctx, "Successfully deleted", Toast.LENGTH_LONG).show();
            }
            else if (code.equals("vegItem_not_deleted")|| code.equals("garden_not_deleted")){
                Toast.makeText(ctx, "Delete fail.. Try Again", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    } //close on post execute


    //to analyse message and dipaly alert
//    public void showDialog(String title, String message, String code)
//    {
//        builder.setTitle(title);
//        if (code.equals("reg_true")||code.equals("reg_false")){ //if so we will show message
//            builder.setMessage(message);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //when user clicks ok, we dismiss and finish activity
//                    dialog.dismiss();
//                    activity.finish();
//                }
//            });
//        } //close if
//        else if (code.equals("login_false")) { //if so we will show message
//
//            builder.setMessage(message);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    EditText email, mpassword;
//                    email = (EditText) activity.findViewById(R.id.email);
//                    mpassword = (EditText) activity.findViewById(R.id.password);
//                    email.setText("");
//                    mpassword.setText("");
//                    dialog.dismiss();
////                    activity.finish();
//                }
//            });
//        } //close else if
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }

}
