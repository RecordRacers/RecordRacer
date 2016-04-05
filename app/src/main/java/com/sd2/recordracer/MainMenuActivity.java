package com.sd2.recordracer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;


public class MainMenuActivity extends TabActivity {

    private String TAG = "Main Menu Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        User user = (User) getIntent().getSerializableExtra("User");

        if (user==null) {
            Log.d(TAG, "No user was logged in");
        } else {
            Log.d(TAG, "Currently logged in user is " + user.getUsername() + " with email: " + user.getEmail());
        }


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Workout");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("History");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Records");

        tab1.setIndicator("Workout");
        Intent intent1 = new Intent(this, setUpWorkout.class);
        intent1.putExtra("User", user);
        tab1.setContent(intent1);

        tab2.setIndicator("Records");
        Intent intent2 = new Intent(this, Records.class);
        intent2.putExtra("User", user);
        tab2.setContent(intent2);

        tab3.setIndicator("History");
        Intent intent3 = new Intent(this, History.class);
        intent3.putExtra("User", user);
        tab3.setContent(intent3);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }
}
