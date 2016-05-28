package com.eoinpayne.crop.cropapp;

import java.util.Date;

/**
 * Created by Hefty Balls on 25/05/2016.
 */
public class Garden {

    private String mGardenName;
    private String mGardenID;
    private String nUserID;

    Garden(String gardenName, String gardenID, String userID) {
        this.mGardenName = gardenName;
        this.mGardenID = gardenID;
        this.nUserID = userID;
    }

    @Override
    public String toString()
    {
        return "Garden: " + mGardenName + "ID: " + mGardenID ;
    }

    public String getGardenName() {
        return mGardenName;
    }
    public void setGardenName(String gardenName) {
        mGardenName = gardenName;
    }

    public String getGardenID() {
        return mGardenID;
    }
    public void setGardenID(String gardenID) {
        mGardenID = gardenID;
    }

    public String getUserID() {
        return nUserID;
    }
    public void setUserID(String userID) {
        nUserID = userID;
    }


}
