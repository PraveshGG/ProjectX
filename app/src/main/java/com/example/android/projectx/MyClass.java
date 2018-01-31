package com.example.android.projectx;

import android.graphics.Bitmap;

/**
 * Created by Pravesh on 1/20/2018.
 */

public class MyClass {
    Bitmap mImageResourceID;
    String name;
    String phoneNumber;

    public MyClass(String name) {
        this.name = name;
    }

    public MyClass(Bitmap mImageResourceID, String name, String phoneNumber) {
        this.mImageResourceID = mImageResourceID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public boolean equals(Object o) {
        MyClass r = (MyClass) o;
        String phnname1 = this.getPhoneNumber();
        String phnname2 = r.getPhoneNumber();
        String name1 = this.getName();
        String name2 = r.getName();

        if(phnname1.equals(phnname2)) {
            return true;
        } else return false;

    }
    @Override
    public int hashCode() {
        return this.phoneNumber.hashCode();
    }


    public Bitmap getmImageResourceID() {
        return mImageResourceID;
    }

    public void setmImageResourceID(Bitmap mImageResourceID) {
        this.mImageResourceID = mImageResourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
