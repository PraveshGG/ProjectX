package com.example.android.projectx.HomeScreen;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.projectx.EditDescriptionActivity;
import com.example.android.projectx.HomeScreen.NotificationsFragments.NotificationsFragment;
import com.example.android.projectx.HomeScreen.ProfileFragment.ProfileFragment;
import com.example.android.projectx.ModelUser;
import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleFragment;
import com.example.android.projectx.R;
import com.example.android.projectx.SettingsActivity;
import com.example.android.projectx.WelcomeRegister.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.Profile {

    BottomBar bottomBar;
    NavigationView navigationView;
    BottomBarTab notif;
    LinearLayout linearLayout;
    LinearLayout container;
    PeopleFragment peopleFragment;
    MessagesFragment messagesFragment;
    NotificationsFragment notificationsFragment;
    ProfileFragment profileFragment;
    String model, path;
    private Boolean exit = false;
    CircleImageView navHeaderProfileImage;
    ImageView editImage;
    String paths, imgPath;
    Bitmap headerImageProfile;
    TextView navHeaderPhoneNumber, navHeaderFirstName;
    SharedPreferences preferences, preferences1,checkFirstAppLaunch;
    DrawerLayout drawer;
    ModelUser nameUser;
    String yess;
    Boolean backPressed,whichTabSelected;
    Boolean otherTabSelected =false;
    TextView view;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("iop", "onCreate: ");
        yess = getIntent().getStringExtra("yes");

        bottomBar = findViewById(R.id.bottomBar);
        container = findViewById(R.id.fragmentContainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("SP", MODE_PRIVATE);




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
         view = (TextView) navigationView.getMenu().findItem(R.id.nav_view_reminders).getActionView();

        checkFirstAppLaunch = getSharedPreferences("RSSP",MODE_PRIVATE);
        int size = checkFirstAppLaunch.getInt("size",0);
        if(size==0){
            view.setText(String.valueOf(0));
        }else {
            view.setText(String.valueOf(size));
        }

//        if (checkFirstAppLaunch.getBoolean("first_app_launch", true)) {
//
//            checkFirstAppLaunch.edit().putBoolean("first_app_launch", false).commit();
//
//        } else {
//            int a = checkFirstAppLaunch.getInt("size",0);
//            view.setText(String.valueOf(a));
//        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


//        if(checkFirstAppLaunch==null){}
//        else{
//            int a = checkFirstAppLaunch.getInt("size",0);
//            view.setText(a);
//        }

//        navigationView.inflateHeaderView(R.layout.nav_header_home);

        View header = navigationView.getHeaderView(0);
        editImage = header.findViewById(R.id.edit);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(HomeActivity.this, EditDescriptionActivity.class);
                startActivity(newIntent);

            }
        });

        notif = findViewById(R.id.tab_notifications);
        notif.setBadgeCount(5);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navHeaderProfileImage = header.findViewById(R.id.nav_header_profile_image);
        navHeaderPhoneNumber = header.findViewById(R.id.nav_header_user_phone_number);
        navHeaderFirstName = header.findViewById(R.id.nav_header_first_name);

        preferences1 = getSharedPreferences("sp", MODE_PRIVATE);
        String phoneNumber = preferences1.getString("phoneNumber", "");
        navHeaderPhoneNumber.setText(phoneNumber);

//        String nameFromEditDescriptionActivity = getIntent().getStringExtra("firstName");
//
//        navHeaderFirstName.setText(nameFromEditDescriptionActivity);

        File imgFile = new  File("/storage/emulated/0/profilePic.png");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            navHeaderProfileImage.setImageBitmap(myBitmap);

        }else{
            navHeaderProfileImage.setImageResource(R.drawable.man);

        }
//        if (imgPath == null || imgPath == "") {
//            navHeaderProfileImage.setImageResource(R.drawable.man);
//        } else {
//            navHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/profilePic.png"));
//        }

        navHeaderProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("model", model);
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(b);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer, profileFragment);
                bottomBar.selectTabAtPosition(3);
                ft.commit();
//                drawer.closeDrawer(GravityCompat.START);
                onBackPressed();

            }
        });
        Bundle imagePathBndl = getIntent().getExtras();
        if (imagePathBndl != null) {
            String imgpth1 = imagePathBndl.getString("imgpth");
            Log.d("fisss", "onCreate: " + String.valueOf(imgpth1));
        }


        model = preferences.getString("models", "");

        path = preferences.getString("path", "");
        Log.d("fis", "onCreate: " + path);

        if (model == null || model == "") {

        } else {
            nameUser = new Gson().fromJson(model, ModelUser.class);
            if (nameUser.getfName() == null || nameUser.getfName() == "") {
                navHeaderFirstName.setText("User");

            } else
                navHeaderFirstName.setText(nameUser.getfName().toString());
        }

        peopleFragment = new PeopleFragment();
        messagesFragment = new MessagesFragment();
        notificationsFragment = new NotificationsFragment();
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, peopleFragment).commit();

        Integer submitFromEditDescriptionToFragment4 = getIntent().getIntExtra("submitFromEditDescriptionToFragment4", 0);

        Integer backPressedFromEditDescriptionToFragment4 = getIntent().getIntExtra("backPressedFromEditDescriptionToFragment4", 0);

        int backPressedFromViewReminderToFragment2 = getIntent().getIntExtra("backFromToolbar",0);


        if(backPressedFromViewReminderToFragment2==15){
            MessagesFragment msg = new MessagesFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer, msg);
            bottomBar.selectTabAtPosition(1);
            ft.commit();
        }

        if (submitFromEditDescriptionToFragment4.equals(5)) {
            Bundle b = new Bundle();
            b.putString("model", model);
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer, profileFragment);
            bottomBar.selectTabAtPosition(3);
            ft.commit();
        }
        if (backPressedFromEditDescriptionToFragment4.equals(5)) {
            Bundle b = new Bundle();
            b.putString("model", model);
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer, profileFragment);
            bottomBar.selectTabAtPosition(3);
            ft.commit();
            whichTabSelected =true;
        }


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                                             @Override
                                             public void onTabSelected(@IdRes int tabId) {
                                                 if (tabId == R.id.tab_people) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, peopleFragment, "FRIST").commit();
                                                     whichTabSelected=true;


                                                 }
                                                 if (tabId == R.id.tab_reminders) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, messagesFragment, "SECOND").commit();
                                                     whichTabSelected=false;

                                                 }
                                                 if (tabId == R.id.tab_notifications) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, notificationsFragment, "THIRD").commit();
                                                     whichTabSelected=false;


                                                 }
                                                 if (tabId == R.id.tab_profile) {
                                                     whichTabSelected=false;
                                                     Bundle b = new Bundle();
                                                     b.putString("model", model);
                                                     ProfileFragment profileFragment = new ProfileFragment();
                                                     profileFragment.setArguments(b);
                                                     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                                     ft.replace(R.id.fragmentContainer, profileFragment, "FOURTH");
                                                     ft.commit();

                                                 }
                                             }
                                         }
        );


//        if (getIntent().getStringExtra("yes") == null) {
//
//        } else {
//            Bundle b = new Bundle();
//            b.putString("yes", yess);
//            peopleFragment.setArguments(b);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragmentContainer, peopleFragment);
//            ft.commit();
//        }




    }

    public void setImage() {

        navHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/profilePic.png"));
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getFragmentManager();
        backPresed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_view_reminders) {



//             Handler mHandler = new Handler();
//            mHandler.postDelayed(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         startActivity(new Intent(HomeActivity.this, ViewReminderActivity.class));
//                                     }
//                                 }, 250);

             Handler handler = new Handler();
             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     MessagesFragment msg = new MessagesFragment();
                     Bundle intentBundel = new Bundle();
                     intentBundel.putInt("val",10);
                     msg.setArguments(intentBundel);
                     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                     ft.replace(R.id.fragmentContainer, msg);
                     bottomBar.selectTabAtPosition(1);
                     ft.commit();
                 }
             },250);





         } else if(id==R.id.nav_settings){
             startActivity(new Intent(this, SettingsActivity.class));

         }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
//            ProfileFragment.stored=false;
            preferences.edit().clear().commit();
            File file = new File("/storage/emulated/0/", "profilePic.png");
            file.delete();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }


    @Override
    public void sendimage(String imagepath) {
        Log.d("hello", "sendimage: " + imagepath);
        paths = imagepath;

//            navHeaderProfileImage.setBackgroundColor(Color.GREEN);
//            navHeaderProfileImage.setImageResource(R.drawable.man);
//            navHeaderProfileImage.setBackgroundColor(Color.GREEN);
        Log.d("asdf", "onCreate: " + paths);
        navHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile(paths));

    }

    public void backPresed() {

        if(whichTabSelected){
            backPressed=peopleFragment.backPressedFragment();

            if (backPressed) {
                peopleFragment.backPressedFragment();
                peopleFragment.clearList();

//            peopleFragment.clearList();
            } else {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (exit) {
                    finishAffinity(); // finish activity
                } else {
                    Toast.makeText(this, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 2 * 1000);

                }
            }

        }else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (exit) {
                finishAffinity(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2 * 1000);

            }
        }


    }

    public void onMethodCallback(){
        b = new Bundle();
        yess= "yes";
        b.putString("yes", yess);
        peopleFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, peopleFragment);
        ft.commit();
        bottomBar.selectTabAtPosition(0);

    }

    public void onclearbundle(){
        b = new Bundle();
        yess= "no";
        b.putString("yes", yess);
        peopleFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, peopleFragment);
        ft.commit();
        bottomBar.selectTabAtPosition(0);
    }

    public void sendSize(int size){
        view.setText(String.valueOf(size));
    }
}
