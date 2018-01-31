package com.example.android.projectx;


import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    ListView listView;
    ArrayList<MyClass> storeContacts;
    CustomAdapterActivity customAdapterActivity;
    Cursor cursor ;
    String name, phonenumber ;
    Bitmap bitmap;
    String mmContactImage;
    View view;
    ArrayList<String> storeName = new ArrayList<>();
    MyClass myClass;
    Comparator<MyClass> comparator;
    ArrayList<MyClass> mmNewContacList;
    ArrayList<MyClass> a= new ArrayList<>();
    HashSet hs = new HashSet();
    HashMap<Integer, MyClass> foo = new HashMap<Integer, MyClass>();
    ArrayList<MyClass> noDuplicates = new ArrayList<>();
    TextView tv;
    Integer totalContactsCount;

    public static final int RequestPermissionCode = 1;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_people, container, false);
        ViewGroup myHeader = (ViewGroup)inflater.inflate(R.layout.custom_header, listView, false);
        tv = myHeader.findViewById(R.id.totalContacts);
        listView = view.findViewById(R.id.listView);

        listView.addHeaderView(myHeader, null, false);

        EnableRuntimePermission();

        storeContacts = new ArrayList<MyClass>();
        setHasOptionsMenu(true);

        mmNewContacList = GetContactsIntoArrayList();
        a =removeDuplicates(mmNewContacList);
        totalContactsCount =a.size();
        tv.setText("Contacts(" + totalContactsCount+")");
        mmNewContacList.addAll(hs);

        customAdapterActivity = new CustomAdapterActivity(getActivity(), a);
        listView.setTextFilterEnabled(true);

        listView.setAdapter(customAdapterActivity);
//        Collections.sort(contactsList, MyClass.CASE_INSENSITIVE_ORDER);
        Collections.sort(a, new Comparator<MyClass>(){
            public int compare(MyClass m1, MyClass m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });

        EnableRuntimePermission();

        return view;

    }
    public static ArrayList<MyClass> removeDuplicates(ArrayList<MyClass> arraylist) {
        //remove any duplicates
        ArrayList<MyClass> noDuplicates = new ArrayList<>();
        Set<MyClass> setItems = new LinkedHashSet<MyClass>(arraylist);
        noDuplicates.addAll(setItems);

        return noDuplicates;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint("Search by Name or Phone Number...");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    // do your search
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    customAdapterActivity = new CustomAdapterActivity(getContext(), filterArray(s));
                    listView.setAdapter(customAdapterActivity);
                    return false;
                }
            });


        }catch(Exception e){e.printStackTrace();}
    }


    public ArrayList<MyClass> filterArray(String s){
        ArrayList<MyClass> filteredList = new ArrayList<>();
        for (int i = 0 ; i < a.size();i++){
            if(a.get(i).getName().toLowerCase().contains(s.toLowerCase()) || formatNumber(a.get(i).getPhoneNumber()).contains(s)) {
                filteredList.add(a.get(i));
            }
        }
        tv.setText("Contacts(" + filteredList.size()+")");
        return filteredList;
    }

    public String formatNumber(String phonenumber) {
        String formattedNumber = "";
        if (phonenumber.contains("+977")) {
            formattedNumber = phonenumber;
            Log.d("checkn ", "after ignoring +977 " + formattedNumber);
            if (formattedNumber.contains(" ")) {
                formattedNumber = formattedNumber.replaceAll("\\s", "");
                if (formattedNumber.contains("-")) {
                    formattedNumber = formattedNumber.replaceAll("-", "");
                }
            }
            if (formattedNumber.contains("-")) {
                formattedNumber = formattedNumber.replaceAll("-", "");
                if (formattedNumber.contains(" ")){
                    formattedNumber = formattedNumber.replaceAll("\\s", "");

                }
            }
        }

        else{
            if (phonenumber.contains("00 977")) {
                phonenumber.replaceAll("\\s", "");
                formattedNumber = phonenumber.substring(6);
                formattedNumber = "+977" + formattedNumber;
                if (formattedNumber.contains(" ")) {
                    formattedNumber = formattedNumber.replaceAll("\\s", "");
                    if (formattedNumber.contains("-")) {
                        formattedNumber = formattedNumber.replaceAll("-", "");
                    }
                }
                if (formattedNumber.contains("-")) {
                    formattedNumber = formattedNumber.replaceAll("-", "");
                    if (formattedNumber.contains(" ")){
                        formattedNumber = formattedNumber.replaceAll("\\s", "");

                    }
                }
            }
            else
                formattedNumber = "+977" +phonenumber;
            if (formattedNumber.contains(" ")) {
                formattedNumber = formattedNumber.replaceAll("\\s", "");
                if (formattedNumber.contains("-")) {
                    formattedNumber = formattedNumber.replaceAll("-", "");
                }
            }
            if (formattedNumber.contains("-")) {
                formattedNumber = formattedNumber.replaceAll("-", "");
                if (formattedNumber.contains(" ")){
                    formattedNumber = formattedNumber.replaceAll("\\s", "");

                }
            }

        }
        return formattedNumber;
    }


    public ArrayList<MyClass> GetContactsIntoArrayList(){

        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
//        retrieveContactPhoto();

        while (cursor.moveToNext()) {
            bitmap = null;
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            mmContactImage = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            try {
                if (mmContactImage != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(mmContactImage));
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            storeName.add(name);

            String wellFormattedNumber = formatNumber(phonenumber);

            if (wellFormattedNumber.length()==14) {
                storeContacts.add(new MyClass(bitmap, name, wellFormattedNumber));
            }
        }


        return storeContacts;
    }


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(getActivity(),"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




