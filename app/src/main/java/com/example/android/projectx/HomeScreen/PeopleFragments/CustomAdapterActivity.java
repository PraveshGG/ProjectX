package com.example.android.projectx.HomeScreen.PeopleFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.MyClass;
import com.example.android.projectx.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pravesh on 1/20/2018.
 */

public class CustomAdapterActivity extends ArrayAdapter<MyClass> implements SectionIndexer {
    Context cont;
    String[] sections;
    List<String> names;

    HashMap<String, Integer> mapIndex;

    private final Activity context;
    ArrayList<MyClass> myList, sendCheckedToReminder;
    String firstLetter;
    String check;


    public CustomAdapterActivity(Activity context, ArrayList<MyClass> myList, String check) {
        super(context, R.layout.activity_custom_contacts_view, myList);
        this.context = context;
        this.myList = myList;
        this.check = check;

        mapIndex = new LinkedHashMap<String, Integer>();

        Collections.sort(myList, new Comparator<MyClass>() {
            public int compare(MyClass m1, MyClass m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });

        for (int i = 0; i < myList.size(); i++) {
            String name1 = myList.get(i).getName().toString();
            Log.d("lpop", "CustomAdapterActivity: " + name1);
            String ch = name1.substring(0, 1);
            ch = ch.toUpperCase(Locale.US);

            mapIndex.put(ch, i);
            Set<String> sectionLetters = mapIndex.keySet();

            ArrayList<String> sectionList = new ArrayList<>(sectionLetters);

            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);


        }

    }


    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mapIndex.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }


    static class ViewHolder {
        public LinearLayout lg;
        public CircleImageView contactsImage;
        public CheckBox checkBox;
        public TextView contactsName;
        public TextView phoneNumber;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder = null;


        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.activity_custom_contacts_view, null);
            viewHolder = new ViewHolder();

            viewHolder.contactsName = convertView.findViewById(R.id.contactName);
            viewHolder.contactsImage = convertView.findViewById(R.id.contactImage);
            viewHolder.phoneNumber = convertView.findViewById(R.id.contactNumber);
            viewHolder.checkBox = convertView.findViewById(R.id.contactCheck);


            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int getPosition = (Integer) compoundButton.getTag();
                    myList.get(getPosition).setSelected(compoundButton.isChecked());
                    sendCheckedToReminder = new ArrayList<>();
                }
            });


            if (check.equalsIgnoreCase("no")) {
                final ViewHolder finalViewHolder = viewHolder;
                final View finalConvertView = convertView;
                finalConvertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                       finalConvertView.setFocusable(true);
                        finalConvertView.setFocusableInTouchMode(true);

                        if (finalViewHolder.checkBox.isChecked()) {
                            finalViewHolder.checkBox.setChecked(false);
                        } else {
                            finalViewHolder.checkBox.setChecked(true);
                        }
                    }
                });
            }


            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (check.equalsIgnoreCase("yes")) {

            viewHolder.contactsName.setText(myList.get(position).getName());
            viewHolder.phoneNumber.setText(myList.get(position).getPhoneNumber());
            try {

                if (myList.get(position).getmImageResourceID() != null) {
                    Drawable d = new BitmapDrawable(context.getResources(), myList.get(position).getmImageResourceID());
                    viewHolder.contactsImage.setImageDrawable(d);
                } else {
                    firstLetter = myList.get(position).getName().substring(0, 1);
                    Log.d("asjdfa", "getView: " + firstLetter);
                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

                    int color = generator.getColor(firstLetter);
                    TextDrawable drawable1 = TextDrawable.builder()
                            .beginConfig()
                            .fontSize(100)
                            .height(222)
                            .width(148)
                            .endConfig()
                            .buildRound(firstLetter, color);

                    viewHolder.contactsImage.setImageDrawable(drawable1);

                }

            } catch (OutOfMemoryError e) {
                // Add default picture
                viewHolder.contactsImage.setImageDrawable(cont.getResources().getDrawable(R.drawable.bottombar_people));
                e.printStackTrace();
            }

        } else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setTag(position);
            viewHolder.checkBox.setChecked(myList.get(position).isSelected());
            viewHolder.contactsName.setText(myList.get(position).getName());
            viewHolder.phoneNumber.setText(myList.get(position).getPhoneNumber());
        }


        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}