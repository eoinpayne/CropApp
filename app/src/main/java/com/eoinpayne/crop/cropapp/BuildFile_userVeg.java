package com.eoinpayne.crop.cropapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Hefty Balls on 29/05/2016.
 */
public class BuildFile_userVeg extends AsyncTask<String,Void,String> {

    //TODO make runnable?

    boolean deleted1;
    final public static String userVeg_file = "user_veg.txt";

    Context ctx;
    Activity activity;

    public BuildFile_userVeg(Context ctx){
        //initialise variables
        this.ctx = ctx;
        activity = (Activity)ctx; //cast conext to activity

    }

    @Override
    protected void onPreExecute() {
        //todo delete file
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
    }


    protected String doInBackground(String... params) {
        String json = params[0];
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
//                    bw.append(thisJO.toString() + "~");
//                    bw.append("?");
//                    bw.append(System.getProperty("line.separator"));
//                    bw.newLine();
                bw.close();
//                    OutputStreamWriter osw = new OutputStreamWriter(fos);
//                    osw.append(thisJO.toString());
//                    osw.append("\r\n");

//                    fos.write(thisJO.getBytes());
//                    fos.write(jsonString.getBytes());
//                    fos.write((thisJO.toString()+"\r\n").getBytes());
//                    fos.write("\n".getBytes());
//                    osw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject thisJO = jsonArray.getJSONObject(i);
////                String jsonString = thisJO.toString();
////                String userVeg = "VegID: " + thisJO.getString("vID") + ".   Veg Name: " + thisJO.getString("vegName")
////                        + ".   Preferred Soil: " + thisJO.getString("pSoilType") + thisJO.getString("pSoilType");
//
//
//                //write name to one file
//                //write all to rest
//            } //for int i =1


//                String code = JO.getString("code");  //from server
//                String message = JO.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute(String json) {}




























    //TODO takes data that comes from the server DB, deletes existing file and rebuilds a fresh upto date list.
    public void BuildUserVegFile(String json, Context ctx){





        //////////// Decode JSON response ////////////////

    } //close build user veg file


    public void UpdateUserVegFile(){

    }
}
