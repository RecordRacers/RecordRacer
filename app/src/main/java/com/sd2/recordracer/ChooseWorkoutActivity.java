package com.sd2.recordracer;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ChooseWorkoutActivity extends Activity implements LocationListener {

    private Spinner spinner_exercise;
    private Spinner spinner_playlist;
    private Spinner spinner_distance_unit;
    private TextView time_label;
    private Button btnSubmit;
    private GoogleMap googleMap;
    private boolean smart_pace;
    private TextView distance_tv;
    private TextView pace_tv;
    private EditText desired_time;
    private EditText desired_distance;
    private CheckBox checkBox;
    private LocationManager locationManager;
    private Criteria criteria;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);
        try {
            if (googleMap == null) {
                Log.d("WTF","MAP IS NULL!");
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            if (googleMap != null) {
                Log.d("WTF","MAP IS NOT NULL");
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(provider, 2000, 0, this);

                Location location = locationManager.getLastKnownLocation(provider);
                Log.d("WTF",location.toString());
                if (location != null) {
                    onLocationChanged(location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
        smartPaceListener();
        addItemsOnPlaylistSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    public void onLocationChanged(Location location) {
        double latitude, longitude;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.d("WTF", "MAP HAS MOVED CAMERA: "+googleMap.getCameraPosition());
    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

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

    public void smartPaceListener() {
        checkBox = (CheckBox) findViewById(R.id.smart_pace_checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    distance_tv.setVisibility(View.GONE);
                    pace_tv.setVisibility(View.GONE);
                    desired_time.setVisibility(View.GONE);
                    desired_distance.setVisibility(View.GONE);
                    spinner_distance_unit.setVisibility(View.GONE);
                    time_label.setVisibility(View.GONE);
                    smart_pace = true;
                } else if (!((CheckBox) v).isChecked()) {
                    distance_tv.setVisibility(View.VISIBLE);
                    pace_tv.setVisibility(View.VISIBLE);
                    desired_time.setVisibility(View.VISIBLE);
                    desired_distance.setVisibility(View.VISIBLE);
                    spinner_distance_unit.setVisibility(View.VISIBLE);
                    time_label.setVisibility(View.VISIBLE);
                    smart_pace = false;
                }
            }
        });
    }

    public void initialize() {
        spinner_exercise = (Spinner) findViewById(R.id.exercise_type_spinner);
        spinner_playlist = (Spinner) findViewById(R.id.spinner_playlist);
        spinner_distance_unit = (Spinner) findViewById(R.id.distance_unit_spinner);
        desired_time = (EditText) findViewById(R.id.target_time);
        desired_distance = (EditText) findViewById(R.id.target_distance);
        time_label = (TextView) findViewById(R.id.time_unit_label);
        distance_tv = (TextView) findViewById(R.id.target_distance_textView);
        pace_tv = (TextView) findViewById(R.id.target_time_textView);
        smart_pace = false;
        user = (User) getIntent().getSerializableExtra("User");
    }

    public void addListenerOnButton() {

        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First convert distance to meters
                String distance_unit = spinner_distance_unit.toString();
                Log.d("DESIRED INPUT",desired_distance.getText().toString());
                Log.d("DESIRED INPUT",desired_time.getText().toString());
                float meters = Float.parseFloat(desired_distance.getText().toString());
                if(distance_unit == "km"){
                    meters = 1000 * meters;
                }else{
                    meters = (float) 1609.34 * meters;
                }
                float seconds = (float) 60 * Float.parseFloat(desired_time.getText().toString());
                Log.d("WTF","Seconds: "+seconds+"\t Meters: "+meters);
                Intent intent = new Intent(ChooseWorkoutActivity.this, MainActivity.class);
                intent.putExtra("Playlist", spinner_playlist.toString());
                intent.putExtra("Exercise", spinner_exercise.getSelectedItem().toString());
                intent.putExtra("Desired Time", seconds);
                intent.putExtra("Desired Distance", meters);
                intent.putExtra("Smart Pace", smart_pace);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}
