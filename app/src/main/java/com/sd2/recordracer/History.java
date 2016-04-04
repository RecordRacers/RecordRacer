package com.sd2.recordracer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class History extends Activity {

    private ListView listview;
    private ListAdapter listAdapter;
    private List exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listview = (ListView) findViewById(R.id.listView);
        User user = getIntent().getSerializableExtra("User");
        ArrayList list = new ArrayList();
        Date date = new Date();

        TextView empty = (TextView) findViewById(R.id.empty);
        empty.setVisibility(View.GONE);

        exercises = user.getExercises();

        if (exercises.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }

        else {
            listAdapter = new customAdapter(this, exercises);
            listview.setAdapter(listAdapter);
        }
    }
}
