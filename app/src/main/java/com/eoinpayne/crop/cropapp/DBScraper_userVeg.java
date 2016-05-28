package com.eoinpayne.crop.cropapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Hefty Balls on 28/05/2016.
 */
public class DBScraper_userVeg implements Runnable {

    String scrapeuserVeg_url = "http://10.0.2.2/scrapeUserVeg.php";
    String json;
    final public static String userVeg_file = "user_veg.txt";
    Context ctx;
    boolean deleted1;

    public DBScraper_userVeg(Context current) {
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
            deleted1 = ctx.deleteFile(userVeg_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (deleted1 && deleted2) {
        try {  //to pass data to server
            URL url = new URL(scrapeuserVeg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data = URLEncoder.encode("gardenID", "UTF-8") + "=" + URLEncoder.encode(HomeActivity.global_UserID_String,"UTF-8" );
            bufferedWriter.write(data);  //pass data string
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
            Thread.sleep(1200);
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
            JSONObject jsonObject = new JSONObject(json);  //get Json object
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");  //get objects' array
            JSONObject JO = jsonArray.getJSONObject(0); //get inner object from array at index 0
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject thisJO = jsonArray.getJSONObject(i);
//                String jsonString = thisJO.toString();
//                String userVeg = "VegID: " + thisJO.getString("vID") + ".   Veg Name: " + thisJO.getString("vegName")
//                        + ".   Preferred Soil: " + thisJO.getString("pSoilType") + thisJO.getString("pSoilType");
                try {
                    //todo write in full json object of everything. then parse out after to allow choosing of "carrot"

                    FileOutputStream fos_vegInfo = ctx.openFileOutput(userVeg_file, ctx.MODE_APPEND);
//                    fos_vegInfo.write(thisJO.getBytes());
//                    fos_vegInfo.write(jsonString.getBytes());
                    fos_vegInfo.write(thisJO.toString().getBytes());


                    fos_vegInfo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //write name to one file
                //write all to rest
            } //for int i =1


//                String code = JO.getString("code");  //from server
//                String message = JO.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //ToDo get all veg names write to file, if old list is same as last list then replace and do nothing?


        //ToDo if lists don't match, scrape for All info and rebuild that list. (set a bool?)

//        } //close if deleted 1 && 2


    } //close run
} //close class
