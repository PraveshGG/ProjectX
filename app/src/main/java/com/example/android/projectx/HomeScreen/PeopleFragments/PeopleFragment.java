package com.example.android.projectx.HomeScreen.PeopleFragments;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.android.projectx.HomeScreen.HomeActivity;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.CheckedListModel;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.MyClass;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.MyClass1;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.UserContactModel;
import com.example.android.projectx.R;
import com.example.android.projectx.Reminder.SetReminderActivity;
import com.example.android.projectx.Retrofit.ApiBase;
import com.example.android.projectx.Retrofit.ApiService;
import com.example.android.projectx.ViewSpecificContactActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment implements AdapterView.OnItemClickListener {

    SwipeMenuListView listView;
    Boolean isVisible = false;
    ArrayList<MyClass> storeContacts;
    CustomAdapterActivity customAdapterActivity;
    Cursor cursor;
    String name, phonenumber;
    Bitmap bitmap;
    String mmContactImage;
    View view;
    ArrayList<ColorModel> mColor =new ArrayList<>();
    ArrayList<String> storeName = new ArrayList<>();
    ArrayList<MyClass> mmNewContacList, checkList;
    ArrayList<MyClass> a = new ArrayList<>();
    ArrayList<MyClass1> aNull = new ArrayList<>();
    ArrayList<Boolean> checkedStates = new ArrayList<Boolean>();
    HashSet hs = new HashSet();
    TextView tv;
    Integer totalContactsCount;
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences, settings;
    private String checkModel;
    List<UserContactModel> userContactModel;
    ArrayList<MyClass> addedList, deletedList, modifiedList;
    FloatingActionButton floatingActionButton;
    String yes = "";
    Boolean isFromFragment = false;
    ProgressDialog dialog;
    ArrayList<CheckedListModel> nameNumberList = new ArrayList<CheckedListModel>();


    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_people, container, false);
        ViewGroup myHeader = (ViewGroup) inflater.inflate(R.layout.custom_header, listView, false);
        tv = myHeader.findViewById(R.id.totalContacts);
        listView = view.findViewById(R.id.listView);
        floatingActionButton = view.findViewById(R.id.fab);


        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading.....");
        dialog.show();


        listView.addHeaderView(myHeader, null, false);

        if (getArguments() != null) {
            yes = getArguments().getString("yes");
        }


//        EnableRuntimePermission();
        settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        preferences = getContext().getSharedPreferences("PS", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = preferences.edit();

        storeContacts = new ArrayList<MyClass>();
        setHasOptionsMenu(true);

        mmNewContacList = GetContactsIntoArrayList();
        a = removeDuplicates(mmNewContacList);
        totalContactsCount = a.size();
        tv.setText("Contacts(" + totalContactsCount + ")");
        mmNewContacList.addAll(hs);

        customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "yes");

        listView.setTextFilterEnabled(true);
        listView.setFastScrollEnabled(true);
        listView.setAdapter(customAdapterActivity);
        listView.setOnItemClickListener(this);

        // set creator


//        Collections.sort(contactsList, MyClass.CASE_INSENSITIVE_ORDER);
        Collections.sort(a, new Comparator<MyClass>() {
            public int compare(MyClass m1, MyClass m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });

        listView.setMenuCreator(swipeCreator());
        swipeMenuItemClick(a);

        dialog.dismiss();

        for (int i = 0; i < a.size(); i++) {

            aNull.add(new MyClass1(a.get(i).getName(), a.get(i).getPhoneNumber().toString()));
        }

        // saving images in directory

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < a.size(); i++) {
//                    createDirectoryAndSaveFile(a.get(i).getmImageResourceID(),a.get(i).getName());
//                }
//            }
//        }).start();


        new Thread(new Runnable() {
            public void run() {
                if (settings.getBoolean("first_app_launch", true)) {

                    userContactModel = new ArrayList<>();
                    for (int i = 0; i < a.size(); i++) {
                        userContactModel.add(new UserContactModel(3, a.get(i).getName().toString(), a.get(i).getPhoneNumber().toString(), 0));
                    }
                    addContactsToServer(userContactModel);

                    String modelString = new Gson().toJson(aNull);
                    editor.putString("ms", modelString);
                    editor.commit();

                    settings.edit().putBoolean("first_app_launch", false).commit();
                } else {

                    //check for changes
                    checkModel = preferences.getString("ms", "");
                    Type type = new TypeToken<List<MyClass>>() {
                    }.getType();
                    checkList = new Gson().fromJson(checkModel, type);

                    addedList = new ArrayList<>();
                    deletedList = new ArrayList<>();
                    modifiedList = new ArrayList<>();

                    addedList = findAdded(checkList, a);
                    deletedList = findDeleted(checkList, a);
                    modifiedList = findModified(checkList, a);

                    String modelString2 = new Gson().toJson(aNull);
                    editor.putString("ms", modelString2);
                    editor.commit();

                    userContactModel = new ArrayList<>();

                    for (int i = 0; i < addedList.size(); i++) {

                        userContactModel.add(new UserContactModel(3, addedList.get(i).getName().toString(),
                                addedList.get(i).getPhoneNumber().toString(), 0));
                    }

                    addContactsToServer(userContactModel);
                }
            }
        }).start();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < a.size(); i++) {
                    if (a.get(i).isSelected()) {
                        nameNumberList.add(new CheckedListModel(a.get(i).getName(), a.get(i).getPhoneNumber()));
                    }
                }

                ArrayList<CheckedListModel> newuniquelist = checkduplicate(nameNumberList);
                Log.d("newchecklist", "onClick: "+new Gson().toJson(newuniquelist));

                if (newuniquelist.size() == 0) {

                    Toast.makeText(getContext(), "Select Users to set Reminder.", Toast.LENGTH_SHORT).show();

                } else {
                    HomeActivity hm = new HomeActivity();
//                    sendCheckedBundle.putString("ss",new Gson().toJson(checkedList));
                    String a = new Gson().toJson(newuniquelist);
                    Intent toReminderActivity = new Intent(getContext(), SetReminderActivity.class);
                    toReminderActivity.putExtra("editornew", "float");
                    toReminderActivity.putExtra("cl", a);
                    startActivity(toReminderActivity);
                }

            }
        });

        if (getArguments() != null) {
            if (yes.equalsIgnoreCase("yes")) {
                customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "no");
                listView.setAdapter(customAdapterActivity);
                listView.setMenuCreator(swipeCreator());
                swipeMenuItemClick(a);

                floatingActionButton.setVisibility(View.VISIBLE);
                isVisible = true;
                if ((getContext() instanceof HomeActivity)) {
                    ((HomeActivity) getContext()).onclearbundle();
                }
            } else if (yes == null && yes == "") {
            } else if (yes.equalsIgnoreCase("no")) {
                customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "yes");
                listView.setAdapter(customAdapterActivity);
                listView.setMenuCreator(swipeCreator());
                swipeMenuItemClick(a);
                floatingActionButton.setVisibility(View.GONE);
                isVisible = false;
            }
        } else {

        }

        return view;

    }

    private ArrayList<CheckedListModel> checkduplicate(ArrayList<CheckedListModel> nameNumberList) {
      ArrayList<CheckedListModel> newlist = new ArrayList<>();
      for(CheckedListModel ch:nameNumberList){
          if(!newlist.contains(ch)){
              newlist.add(ch);
          }
      }

      return newlist;
    }

    private ArrayList<MyClass> findModified(ArrayList<MyClass> savedPrefsList, ArrayList<MyClass> newList) {

        ArrayList<MyClass> result = new ArrayList<>();

        for (MyClass save : newList) {
            boolean ch = false;
            for (MyClass newc : savedPrefsList) {
                if (save.getName().equals(newc.getName()) && save.getPhoneNumber().equals(newc.getPhoneNumber())) {
                    ch = true;
                    Log.d("asssss", "findModified: correct " + save.getPhoneNumber());
                    break;
                } else {
                    if (!save.getPhoneNumber().equals(newc.getPhoneNumber())) {
                        ch = true;
                    } else {
                        ch = false;
                        break;
                    }
                }
            }
            if (ch) {

            } else {
                result.add(new MyClass(save.getmImageResourceID(), save.getName(), save.getPhoneNumber()));
            }
        }

        Log.d("asdffda", "Modified: " + new Gson().toJson(result));

        return result;
    }

    private ArrayList<MyClass> findAdded(ArrayList<MyClass> savedPrefsList, ArrayList<MyClass> newList) {

        ArrayList<MyClass> result = new ArrayList<>();
        for (MyClass el : newList) {
            if (!savedPrefsList.contains(el)) {
                result.add(el);
            }
        }

        Log.d("asdffda", "Added: " + new Gson().toJson(result));
        return result;
    }

    private ArrayList<MyClass> findDeleted(ArrayList<MyClass> savedPrefsList, ArrayList<MyClass> newList) {
        ArrayList<MyClass> result = new ArrayList<>();

        for (MyClass el : savedPrefsList) {
            if (!newList.contains(el)) {
                result.add(el);
            }
        }
        Log.d("asdffda", "deleted COntacts: " + new Gson().toJson(result));
        return result;
    }

    private void addContactsToServer(final List<UserContactModel> userContactModel) {

        ApiService api = ApiBase.getApiService();
        Call<Object> call = api.sendContact(userContactModel);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    String success = new Gson().toJson(response.body());
                    if (success.contains("Success")) {

                        List<UserContactModel> editlist = new ArrayList<>();
                        if (modifiedList == null) {

                        } else {
                            if (modifiedList.size() == 0) {

                            } else {
                                for (int i = 0; i < modifiedList.size(); i++) {

                                    editlist.add(new UserContactModel(3, modifiedList.get(i).getName()
                                            , modifiedList.get(i).getPhoneNumber(), 0));
                                }


                            }
                            getEditedToServer(editlist);
                        }
//                       /
                    } else {

                        Log.d("asddd", "onResponse: error " + new Gson().toJson(response.body()));

                    }
                } else {

                    Log.d("asddd", "onResponse: success na huda" + new Gson().toJson(response.body()));


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Log.d("asddd", "failed: " + t);

            }
        });
    }

    private void getEditedToServer(List<UserContactModel> editlist) {

        ApiService api = ApiBase.getApiService();
        Call<Object> call = api.editContact(editlist);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    String success = new Gson().toJson(response.body());
                    if (success.contains("Success")) {


                        List<UserContactModel> deleteList = new ArrayList<>();
                        if (deletedList == null) {

                        } else {
                            for (int i = 0; i < deletedList.size(); i++) {

                                deleteList.add(new UserContactModel(3, deletedList.get(i).getName(), deletedList.get(i).getPhoneNumber(), 6));

                            }
                        }
                        sendDeletedToServer(deleteList);

                        Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();
                        Log.d("asddd", "onResponse: edit " + new Gson().toJson(response.body()));
                    } else {

                        Log.d("asddd", " edit onResponse: error " + new Gson().toJson(response.body()));

                    }
                } else {

                    Log.d("asddd", "edit onResponse: success na huda" + new Gson().toJson(response.body()));


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Log.d("asddd", " edit failed: " + t);

            }
        });


    }

    public void sendDeletedToServer(List<UserContactModel> deletedList) {

        ApiService api = ApiBase.getApiService();
        Call<Object> call = api.deleteContact(deletedList);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    String success = new Gson().toJson(response.body());
                    if (success.contains("Success")) {
                        Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();
                        Log.d("asddd", "onResponse: delete " + new Gson().toJson(response.body()));
                    } else {

                        Log.d("asddd", " edit onResponse: delete " + new Gson().toJson(response.body()));

                    }
                } else {

                    Log.d("asddd", "delete onResponse: success na huda" + new Gson().toJson(response.body()));


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Log.d("asddd", " edit failed: " + t);

            }
        });
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
                    if (!isVisible) {
                        if(getActivity()!=null){
                            customAdapterActivity = new CustomAdapterActivity(getActivity(), filterArray(s), "yes");
                            listView.setAdapter(customAdapterActivity);
                            listView.setMenuCreator(swipeCreator());
                            swipeMenuItemClick(filterArray(s));
                        }



                    } else if (isVisible) {
                        if (getActivity() != null) {
                            customAdapterActivity = new CustomAdapterActivity(getActivity(), filterArray(s), "no");
                            listView.setAdapter(customAdapterActivity);
                            listView.setMenuCreator(swipeCreator());
                            swipeMenuItemClick(filterArray(s));
                        }
                    }
                    return false;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<MyClass> filterArray(String s) {
        ArrayList<MyClass> filteredList = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getName().toLowerCase().contains(s.toLowerCase()) || formatNumber(a.get(i).getPhoneNumber()).contains(s)) {
                filteredList.add(a.get(i));
            }
        }
        tv.setText("Contacts(" + filteredList.size() + ")");
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
                if (formattedNumber.contains(" ")) {
                    formattedNumber = formattedNumber.replaceAll("\\s", "");

                }
            }
        } else {
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
                    if (formattedNumber.contains(" ")) {
                        formattedNumber = formattedNumber.replaceAll("\\s", "");

                    }
                }
            } else
                formattedNumber = "+977" + phonenumber;
            if (formattedNumber.contains(" ")) {
                formattedNumber = formattedNumber.replaceAll("\\s", "");
                if (formattedNumber.contains("-")) {
                    formattedNumber = formattedNumber.replaceAll("-", "");
                }
            }
            if (formattedNumber.contains("-")) {
                formattedNumber = formattedNumber.replaceAll("-", "");
                if (formattedNumber.contains(" ")) {
                    formattedNumber = formattedNumber.replaceAll("\\s", "");

                }
            }

        }
        return formattedNumber;
    }


     public  ArrayList<MyClass> GetContactsIntoArrayList() {

        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        retrieveContactPhoto();


         //

        while (cursor.moveToNext()) {
            bitmap = null;
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            mmContactImage = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            try {
                if (mmContactImage != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(mmContactImage));

                } else {
                    String name1 =name.substring(0, 1);
                    Log.d("asjdfa", "getView: "+name1);
                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT


                    int color = generator.getColor(name1);
                    mColor.add(new ColorModel(name1,color));
                    TextDrawable drawable1 = TextDrawable.builder()
                            .beginConfig()
                            .fontSize(100)
                            .height(222)
                            .width(148)
                            .endConfig()
                            .buildRound(name1, color);
                    bitmap = drawableToBitmap(drawable1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            storeName.add(name);

            String wellFormattedNumber = formatNumber(phonenumber);

            if (wellFormattedNumber.length() == 14) {
                storeContacts.add(new MyClass(bitmap, name, wellFormattedNumber));

            }
        }

        return storeContacts;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
//
//            case R.id.action_set_reminder:
//                getClick();
//                return true;
//
//            case R.id.action_view_reminder:
//                Intent toViewReminderIntent = new Intent(getContext(), ViewReminderActivity.class);
//                startActivity(toViewReminderIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getClick() {
        if (!isVisible) {
//                    customAdapterWithCheckBox = new CustomAdapterWithCheckBox(getActivity(), a);
            customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "no");
            listView.setAdapter(customAdapterActivity);
            listView.setMenuCreator(swipeCreator());
            swipeMenuItemClick(a);
            floatingActionButton.setVisibility(View.VISIBLE);
            isFromFragment = true;
            isVisible = true;


        } else if (isVisible) {
//            Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "yes");
            listView.setAdapter(customAdapterActivity);
            listView.setMenuCreator(swipeCreator());
            swipeMenuItemClick(a);
            floatingActionButton.setVisibility(View.GONE);
            isVisible = false;
            isFromFragment = false;
            listView.setClickable(false);


        }
    }

    public Boolean backPressedFragment() {

        customAdapterActivity = new CustomAdapterActivity(getActivity(), a, "yes");
        listView.setAdapter(customAdapterActivity);
        floatingActionButton.setVisibility(View.GONE);
        isVisible = false;

        return isFromFragment;
    }

    public void clearList() {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).isSelected()) {
                a.get(i).setSelected(false);
            }

        }
        isFromFragment = false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    //    private void SaveImage(Bitmap finalBitmap, String name) {
    //
    //        String root = Environment.getExternalStorageDirectory().toString();
    //        File myDir = new File(root + "/saved_images");
    //
    //        n++;
    //        String fname = name + n + ".jpg";
    //        File file = new File(myDir, fname);
    //        if (file.exists())
    //            file.delete();
    //        try {
    //            FileOutputStream out = new FileOutputStream(file);
    //            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
    //            out.flush();
    //            out.close();
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

    private void createDirectoryAndSaveFile(Bitmap imgSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/.IMG");

        if (!direct.exists()) {
            File imageDirectory = new File("/sdcard/.IMG/");
            imageDirectory.mkdirs();
        }

        fileName = fileName + ".jpg";
        File file = new File(new File("/sdcard/.IMG/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imgSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public SwipeMenuCreator swipeCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem callItem = new SwipeMenuItem(getContext());
                // set item background
                callItem.setBackground(new ColorDrawable(Color.rgb(50, 205,
                        50))); //rgb(50,205,50)
                // set item width
                callItem.setWidth(dp2px(90));
                // set item title
                callItem.setIcon(R.drawable.ic_call_24dp);
                callItem.setTitle("Call");
                // set item title fontsize
                callItem.setTitleSize(16);
                // set item title font color
                callItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(callItem);

                // create "delete" item
                SwipeMenuItem smsItem = new SwipeMenuItem(
                        getContext());
                // set item background
                smsItem.setBackground(new ColorDrawable(Color.rgb(255,
                        165, 0)));//rgb(255,165,0)
                // set item width
                smsItem.setWidth(dp2px(90));

                // set a icon
                smsItem.setIcon(R.drawable.ic_sms_24dp);
                smsItem.setTitle("SMS");
                smsItem.setTitleSize(16);
                smsItem.setTitleColor(Color.WHITE);


                // add to menu
                menu.addMenuItem(smsItem);
            }
        };
        return creator;
    }

    public void swipeMenuItemClick(final ArrayList<MyClass> list) {
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + list.get(position).getPhoneNumber()));
                        startActivity(intent);

                        break;
                    case 1:
                        Uri uri = Uri.parse("smsto:" + list.get(position).getPhoneNumber());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "Hey! There");
                        startActivity(it);
                        // delete
//					delete(item);

                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position= position -1;
        startActivity(new Intent(getContext(), ViewSpecificContactActivity.class)
                .putExtra("phoneNumber",a.get(position).getPhoneNumber())
                .putExtra("name",a.get(position).getName())
                .putExtra("colorList",new Gson().toJson(mColor)));
    }

//    public Bitmap openDisplayPhoto(long contactId) {
//        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
//        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
//
//
//        Bitmap photo = null;
//        try {
//            AssetFileDescriptor fd =
//                    getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
//            if(fd.createInputStream()==null){
//                photo= BitmapFactory.decodeResource(getResources(),
//                        R.drawable.colorful_pencils);
//            }else {
//                photo=BitmapFactory.decodeStream(fd.createInputStream());
//            }
//            return photo;
//        } catch (IOException e) {
//            return null;
//        }
//    }
}




