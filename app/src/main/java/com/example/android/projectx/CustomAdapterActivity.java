package com.example.android.projectx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by Pravesh on 1/20/2018.
 */

public class CustomAdapterActivity extends ArrayAdapter<MyClass> {
    Context cont;
    ArrayList<MyClass> myList;
    String firstLetter;

    public CustomAdapterActivity(Context cont, ArrayList<MyClass> myList) {
        super(cont, 0, myList);
        this.cont = cont;
        this.myList = myList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {

            v = LayoutInflater.from(cont).inflate(R.layout.activity_custom_contacts_view, parent, false);

        }

        CircleImageView contactImage = v.findViewById(R.id.contactImage);
        TextView contactName = v.findViewById(R.id.contactName);
        TextView contactNumber = v.findViewById(R.id.contactNumber);


//        contactImage.setImageResource(Integer.parseInt(String.valueOf(myList.get(position).getmImageResourceID())));
        MyClass myClass = getItem(position);
        contactName.setText(myClass.getName());
        contactNumber.setText(myClass.getPhoneNumber());
        // Set image if exists
        try {

            if (myClass.getmImageResourceID() != null) {
                contactImage.setImageBitmap(myClass.getmImageResourceID());
            } else {

                firstLetter = myClass.getName().substring(0, 1);
                Log.d("mytag", "getView: " + firstLetter);


                contactImage = v.findViewById(R.id.contactImage);
                contactImage.setImageBitmap(textAsBitmap(firstLetter,200, Color.WHITE));


            }

        } catch (OutOfMemoryError e) {
            // Add default picture
            contactImage.setImageDrawable(cont.getResources().getDrawable(R.drawable.bottombar_people));
            e.printStackTrace();
        }

//

        return v;
    }
    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        String mText = "       " + text + "       ";
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(mText) + 0.4f); // round
        int height = (int) (baseline + paint.descent() + 0.4f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(mText, 0, baseline, paint);
//        canvas.drawARGB(2, 225, 225, 255);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(firstLetter);
        canvas.drawColor(color2 , PorterDuff.Mode.LIGHTEN);

        return image;
    }
}