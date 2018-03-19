package com.example.android.projectx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

/**
 * Created by Pravesh on 1/20/2018.
 */

public class MyClass implements ChipInterface {
    Bitmap mImageResourceID;
    private Uri avatarUri;
    String name;
    String phoneNumber;
    boolean selected;

    public MyClass(Uri avatarUri, String name, String phoneNumber) {
        this.avatarUri = avatarUri;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    public MyClass(Bitmap mImageResourceID, String name, String phoneNumber) {
        this.mImageResourceID = mImageResourceID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public boolean equals(Object o) {
        if(!(o instanceof MyClass)){
            return false;
        }else{
            MyClass r = (MyClass) o;

            String phnname1 = this.getPhoneNumber();
            String phnname2 = r.getPhoneNumber();
            String name1 = this.getName();
            String name2 = r.getName();

            if(phnname1.equals(phnname2)) {
                return true;
            } else return false;
        }


    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        return this.phoneNumber.hashCode();
    }


    public Bitmap getmImageResourceID() {
        return mImageResourceID;
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

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {

        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return phoneNumber;
    }
}
