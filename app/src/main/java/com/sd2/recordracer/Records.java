package com.sd2.recordracer;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.TabHost;

public class Records extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        User user = getIntent().getSerializableExtra("User");

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Running");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Biking");
        //tabHost.setCurrentTab(1);

        tab1.setIndicator("Running");
        Intent intent1 = new Intent(this, Running.class);
        intent1.putExtra("User", user);
        tab1.setContent(intent1);

        tab2.setIndicator("Biking");
        Intent intent2 = new Intent(this, Biking.class);
        intent2.putExtra("User", user);
        tab2.setContent(new Intent(this, Biking.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }
}
