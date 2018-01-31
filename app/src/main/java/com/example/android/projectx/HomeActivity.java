package com.example.android.projectx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BottomBar bottomBar;
    LinearLayout linearLayout;
    LinearLayout container;
    PeopleFragment peopleFragment;
    MessagesFragment messagesFragment;
    NotificationsFragment notificationsFragment;
    ProfileFragment profileFragment;
    String model;
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences=getSharedPreferences("SP",MODE_PRIVATE);

        model = preferences.getString("models","");

        if(model==null||model==""){

        }else {
            ModelUser user = new Gson().fromJson(model, ModelUser.class);
        }


        bottomBar= findViewById(R.id.bottomBar);

        container = findViewById(R.id.fragmentContainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        peopleFragment = new PeopleFragment();
        messagesFragment = new MessagesFragment();
        notificationsFragment = new NotificationsFragment();
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, peopleFragment).commit();

        int fragmentid = getIntent().getIntExtra("fragmentid",0);

        Log.d("asdf", "onCreate assss: "+fragmentid);
        if(fragmentid==3){
            Bundle b = new Bundle();
            b.putString("model",model);
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer, profileFragment);
            bottomBar.selectTabAtPosition(3);
            ft.commit();
        }

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                                             @Override
                                             public void onTabSelected(@IdRes int tabId) {
                                                 if (tabId == R.id.tab_people) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, peopleFragment).commit();

                                                 }
                                                 if (tabId == R.id.tab_messages) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, messagesFragment).commit();

                                                 }
                                                 if (tabId == R.id.tab_notifications) {
                                                     getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, notificationsFragment).commit();

                                                 }
                                                 if (tabId == R.id.tab_profile) {
                                                     Bundle b = new Bundle();
                                                     b.putString("model",model);
                                                     ProfileFragment profileFragment = new ProfileFragment();
                                                     profileFragment.setArguments(b);
                                                     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                                     ft.replace(R.id.fragmentContainer, profileFragment);
                                                     ft.commit();
                                                 }
                                             }
                                         }
        );


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
