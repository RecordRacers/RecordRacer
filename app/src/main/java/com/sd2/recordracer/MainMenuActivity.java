package com.sd2.recordracer;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMenuActivity extends Activity implements LocationListener {

    private Spinner spinner_exercise;
    private Spinner spinner_playlist;
    private Button btnSubmit;
    private GoogleMap googleMap;
    private boolean smart_pace;
    private TextView distance_tv;
    private TextView pace_tv;
    private EditText desired_pace;
    private EditText desired_distance;
    private CheckBox checkBox;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        try{
            if(googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            if(googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                /*googleMap.setOnMapClickListener(new OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Run Location");
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.addMarker(markerOptions);
                    }
                });*/
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria,true);
                Location location = locationManager.getLastKnownLocation(provider);
                if(location != null) {
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider,20000,0,this);
            }
        } catch(Exception e) {
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
        longitude = location.getLatitude();
        LatLng latLng = new LatLng(latitude,longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
                if(((CheckBox) v).isChecked()) {
                    distance_tv.setVisibility(View.GONE);
                    pace_tv.setVisibility(View.GONE);
                    desired_pace.setVisibility(View.GONE);
                    desired_distance.setVisibility(View.GONE);
                    smart_pace = true;
                }
                else if(!((CheckBox) v).isChecked()) {
                    distance_tv.setVisibility(View.VISIBLE);
                    pace_tv.setVisibility(View.VISIBLE);
                    desired_pace.setVisibility(View.VISIBLE);
                    desired_distance.setVisibility(View.VISIBLE);
                    smart_pace = false;
                }
            }
        });
    }

    public void initialize() {
        spinner_exercise = (Spinner) findViewById(R.id.exercise_type_spinner);
        spinner_playlist = (Spinner) findViewById(R.id.spinner_playlist);
        desired_pace = (EditText) findViewById(R.id.target_pace);
        desired_distance = (EditText) findViewById(R.id.distance);
        distance_tv = (TextView) findViewById(R.id.distance_textView);
        pace_tv = (TextView) findViewById(R.id.target_pace_textView);
        smart_pace = false;
    }

    public void addListenerOnButton() {

        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                intent.putExtra("Playlist", spinner_playlist.toString());
                intent.putExtra("Exercise", spinner_exercise.toString());
                intent.putExtra("Desired Pace", desired_pace.toString());
                intent.putExtra("Desired Distance", desired_distance.toString());
                intent.putExtra("Smart Pace", smart_pace);
                startActivity(intent);
            }
        });
    }
}
