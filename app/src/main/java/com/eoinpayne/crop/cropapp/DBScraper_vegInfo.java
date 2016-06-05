package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;

/**
 * Created by Hefty Balls on 21/05/2016.
 */
public class DBScraper_vegInfo implements Runnable {

//    String scrapeVegName_url = "http://192.168.0.34/scrapeVegName.php";
//    String scrapeAllVegInfo_url = "http://192.168.0.34/scrapeAllVegInfo.php";
//
    String scrapeVegName_url = "http://10.0.2.2/scrapeVegName.php";
    String scrapeAllVegInfo_url = "http://10.0.2.2/scrapeAllVegInfo.php";
    String json;
    final public static String allVegInfo_file = "veg_info.txt";
    final public static String vegNames_file = "veg_names.txt";
    final public static String daysToGrow_file = "days_to_grow.txt";

    Context ctx;
    boolean deleted1;
    boolean deleted2;
    Hashtable daysToGrow_hash;
    JSONObject myObject = new JSONObject();
    JSONArray myArray = new JSONArray();

    public DBScraper_vegInfo(Context current){
        ctx = current;
    }

    @Override
    public void run() {
        // Move current thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);


        try {
//            File dir = ctx.getFilesDir();
//            File allVegInfo_file_delete = new File(dir, userVeg_file);
//            File vegNames_file_delete = new File(dir, userVeg_file);
//            deleted1 = allVegInfo_file_delete.delete();
//            deleted2 = vegNames_file_delete.delete();
            deleted1 = ctx.deleteFile(allVegInfo_file);
            deleted2 = ctx.deleteFile(vegNames_file);
            deleted2 = ctx.deleteFile(daysToGrow_file);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (deleted1 && deleted2) {
            try {  //to pass data to server
                URL url = new URL(scrapeAllVegInfo_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder(); //to read JSON response from bufferedReader
                String line = "";  //take each line from reader and append to String.
                while ((bufferedReader.readLine()) != null)///
                {
                    line = bufferedReader.readLine();
                    stringBuilder.append(line + "\n");
                }
                //create a pause between background methods
                httpURLConnection.disconnect(); //close conenction
                Thread.sleep(1500);
//                Log.i();
                json = stringBuilder.toString().trim(); //return string builder in normal string format

            } catch (MalformedURLException e) {
                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //////////// Decode JSON response ////////////////
            try {
//                daysToGrow_hash = new Hashtable();
                JSONObject jsonObject = new JSONObject(json);  //get Json object
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");  //get objects' array
                JSONObject JO = jsonArray.getJSONObject(0); //get inner object from array at index 0
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject thisJO = jsonArray.getJSONObject(i);
                    String allVegInfo = "VegID: " + thisJO.getString("vID") + ".   Veg Name: " + thisJO.getString("vegName")
                            + ".   Preferred Soil: " + thisJO.getString("pSoilType") + thisJO.getString("pSoilType");
                    String vegName = thisJO.getString("vegName") + "\n";

                    String vegName1 = thisJO.getString("vegName");
                    int averageDaysToGrow = (thisJO.getInt("avgDaysToGrow"));

////                    String averageDaysToGrow = null;
//                    try {
////                        averageDaysToGrow = Integer.parseInt(thisJO.getInt("avgDaysToGrow"));
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }

                    JSONObject jsonDaysToGrow = new JSONObject();
                    jsonDaysToGrow.put(vegName1, averageDaysToGrow);
                    myArray.put(jsonDaysToGrow);
//                    myArray.add(vegName1, jsonDaysToGrow);



                    try {
                        //todo write in full json object of everything. then parse out after to allow choosing of "carrot"

                        FileOutputStream fos_vegInfo = ctx.openFileOutput(allVegInfo_file, ctx.MODE_APPEND);
//                        fos_vegInfo.write(allVegInfo.getBytes());
                        fos_vegInfo.write(thisJO.toString().getBytes());
                        fos_vegInfo.close();
                        FileOutputStream fos_vegNames = ctx.openFileOutput(vegNames_file, ctx.MODE_APPEND);
                        fos_vegNames.write(vegName.getBytes());
                        fos_vegNames.close();

                        //ToDo add each key/value pair to hash table

//                        daysToGrow_hash.put(vegName1, averageDaysToGrow);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } //for int i =1

                //toDo put the JsonArray of JsonObjects into a JsonObject
                myObject.put("daysToGrow_collection", myArray);
                String daysToGrow_collection = myObject.toString();

                //toDo write daysToGrow_collection to a file, like the vegName file.

                FileOutputStream fos_daysToGrow = null;
                try {
                    fos_daysToGrow = ctx.openFileOutput(daysToGrow_file, ctx.MODE_APPEND);
                    fos_daysToGrow.write(daysToGrow_collection.getBytes());
                    fos_daysToGrow.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //ToDo add completed hash table to it's file
//                daysToGrow_file
//                FileOutputStream fos_daysToGrow = null;
//                try {
//                    fos_daysToGrow = ctx.openFileOutput(daysToGrow_file, ctx.MODE_APPEND);
////                        fos_vegInfo.write(allVegInfo.getBytes());
//                    fos_daysToGrow.write(daysToGrow_hash.toString().getBytes());
//                    fos_daysToGrow.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



//                saveDaysToGrow(daysToGrow_hash, daysToGrow_file);



            } catch (JSONException e) {
                e.printStackTrace();
            }
//                String code = JO.getString("code");  //from server
//                String message = JO.getString("message");

            //ToDo get all veg names write to file, if old list is same as last list then replace and do nothing?
            //ToDo if lists don't match, scrape for All info and rebuild that list. (set a bool?)

//        } //close if deleted 1 && 2
    }

    public void saveDaysToGrow(Serializable object, String file){
        try {
            FileOutputStream saveFile = ctx.openFileOutput(file, ctx.MODE_APPEND);

//            FileOutputStream saveFile = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(object);
//            out.flush();
            out.close();
//            saveFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage (){

        try {
            String Message;
            FileInputStream fileInputStream = ctx.openFileInput("hello_file");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while((Message = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(Message + "\n");
            }
//            textView.setText(stringBuffer.toString());
//            textView.setVisibility(View.VISIBLE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


