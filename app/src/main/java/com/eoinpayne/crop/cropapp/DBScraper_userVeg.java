package com.eoinpayne.crop.cropapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

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

//TODO Upon logging in, finds all vegetables planted by a specified user, writes to a file.
public class DBScraper_userVeg implements Runnable {
//    public static String scrapeuserVeg_url = "http://192.168.0.34/scrapeUserVeg.php";
    public static String scrapeuserVeg_url = "http://eoinpayne.dx.am/php/scrapeUserVeg.php";
    String json;
    Context ctx;
    boolean deleted1;
    final public String userVeg_file = "user_veg.txt";

    public DBScraper_userVeg(Context current) {
        ctx = current;
    }

    @Override
    public void run() {

        //todo delete file
        try {
            deleted1 = ctx.deleteFile(userVeg_file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Move current thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {  //to pass data to server
            URL url = new URL(scrapeuserVeg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(HomeActivity.global_UserID_String,"UTF-8" );
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
                stringBuilder.append(line); // + "\n"
            }
            //create a pause between background methods
            httpURLConnection.disconnect(); //close conenction
            Thread.sleep(1400);
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

//       //TODO call background task to rebuild the userVeg file
//        try {
//            BuildFile_userVeg buildFile = new BuildFile_userVeg(ctx);
//            buildFile.execute(json);
//        } catch (Exception e){
//            e.printStackTrace();
//        }


        try {
//            JSONObject jsonArray = new JSONObject(json);  //get Json object

            JSONArray jsonArray = new JSONArray(json);  //get Json object
//            JSONArray jsonArray = jsonObject.getJSONArray("server_response");  //get objects' array
//            JSONObject JO = jsonArray.getJSONObject(0); //get inner object from array at index 0
            //todo write in full json array object of everything. then parse out after to allow choosing of "carrot"
            try {
                FileOutputStream fos = ctx.openFileOutput(userVeg_file, ctx.MODE_APPEND);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.append(jsonArray.toString());
                bw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //ToDo get all veg names write to file, if old list is same as last list then replace and do nothing?


        //ToDo if lists don't match, scrape for All info and rebuild that list. (set a bool?)

//        } //close if deleted 1 && 2


    } //close run
} //close class
