package com.sd2.recordracer;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainMenuActivity extends Activity {

    private Spinner spinner_exercise;
    private Spinner spinner_playlist;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addItemsOnPlaylistSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    //fill spinner with playlist music
    public void addItemsOnPlaylistSpinner() {
        spinner_playlist = (Spinner) findViewById(R.id.spinner_playlist);
        List<String> list = new ArrayList<String>();
        // add music code here to fill list
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_playlist.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner_exercise = (Spinner) findViewById(R.id.exercise_type_spinner);
    }

    public void addListenerOnButton() {
        spinner_exercise = (Spinner) findViewById(R.id.exercise_type_spinner);
        spinner_playlist = (Spinner) findViewById(R.id.spinner_playlist);
        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
