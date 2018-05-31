package com.jarcor.www.jarcor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jarcor.www.jarcor.customfonts.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private TextView textChatGroup;
    private TextView textJobs;
    private TextView textProfile;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdater;
    private TabLayout mTabLayout;
    private DatabaseReference mUserRef;
    private Toolbar mToolbar;


    final int[] ICONS = new int[]{
            R.drawable.ic_notifications_white_24dp,
            R.mipmap.ic_chat_white_24dp,
            R.drawable.ic_people_outline_white_24dp
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.mainToolBar);
        setSupportActionBar(mToolbar);

        textChatGroup = (TextView) findViewById(R.id.text_chatgroup);
        textJobs = (TextView) findViewById(R.id.text_jobs);
        textProfile = (TextView) findViewById(R.id.text_profile);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }


        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mSectionsPagerAdater = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdater);
        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //TabBarLayout on selected ICONS
        mTabLayout.getTabAt(0).setIcon(ICONS[0]);
        mTabLayout.getTabAt(1).setIcon(ICONS[1]);
        mTabLayout.getTabAt(2).setIcon(ICONS[2]);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_groupchat:
                                textChatGroup.setVisibility(View.VISIBLE);
                                textJobs.setVisibility(View.GONE);
                                textProfile.setVisibility(View.GONE);
                                Intent i = new Intent(MainActivity.this, GroupChat.class);
                                startActivity(i);
                                break;
                            case R.id.action_jobs:
                                textChatGroup.setVisibility(View.GONE);
                                textJobs.setVisibility(View.VISIBLE);
                                textProfile.setVisibility(View.GONE);
                                Intent jobIntent = new Intent(MainActivity.this, UsersActivity.class);
                                startActivity(jobIntent);
                                break;
                            case R.id.action_profile:
                                textChatGroup.setVisibility(View.GONE);
                                textJobs.setVisibility(View.GONE);
                                textProfile.setVisibility(View.VISIBLE);
                                Intent iProfile = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(iProfile);
                                break;
                        }
                        return false;
                    }
                });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){

            sendToStart();
        }
        else{

            mUserRef.child("online").setValue(true);
            mUserRef.child("verified").setValue(true);
        }
    }
    private void sendToStart() {

        Intent setupIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(setupIntent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!= null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            mUserRef.child("verified").setValue(true);

        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (item.getItemId() == R.id.main_settings_btn){

            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if (item.getItemId() == R.id.allUsers){
            Intent settingsIntent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(settingsIntent);
        }
        if (item.getItemId() == R.id.allpost){
            Intent settingsIntent = new Intent(MainActivity.this, PostLive.class);
            startActivity(settingsIntent);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_group) {

            Intent intent = new Intent(MainActivity.this, GroupChat.class);
            startActivity(intent);
        }  else if (id == R.id.nav_livestream) {

            Intent intent = new Intent(MainActivity.this, LiveStream.class);
            startActivity(intent);

        } else if (id == R.id.nav_notification) {

            Intent intent = new Intent(MainActivity.this, Notification.class);
            startActivity(intent);

        }else if (id == R.id.nav_file_report) {

            Intent intent = new Intent(MainActivity.this, ReportIssue.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_about) {

            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
