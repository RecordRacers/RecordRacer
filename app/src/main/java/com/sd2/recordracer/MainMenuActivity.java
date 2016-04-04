package com.sd2.recordracer;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;


public class MainMenuActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        User user = getIntent().getSerializableExtra("User");

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Workout");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("History");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Records");

        tab1.setIndicator("Workout");
        tab1.setContent(new Intent(this, setUpWorkout.class));

        tab2.setIndicator("Records");
        tab2.setContent(new Intent(this, Records.class));

        tab3.setIndicator("History");
        tab3.setContent(new Intent(this, History.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }
}
