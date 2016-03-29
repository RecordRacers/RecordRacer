package com.sd2.recordracer;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
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

public class MainMenuActivity extends Activity {

    private Spinner spinner_exercise;
    private Spinner spinner_playlist;
    private Button btnSubmit;
    private GoogleMap googleMap;
    private EditText desired_pace;
    private float distance;
    private ArrayList<MarkerOptions> markers;
    private Location start_position; // TODO: set from current location
    private Location end_position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        markers = new ArrayList<MarkerOptions>();
        try{
            if(googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            if(googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMapClickListener(new OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Run Location");
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.addMarker(markerOptions);
                        markers.add(markerOptions);
                        /*end_position = new Location("");
                        end_position.setLatitude(latLng.latitude);
                        end_position.setLongitude(latLng.longitude);*/
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        //distance = calculateDistance(start_position, end_position);
        addItemsOnPlaylistSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    // calculate approximate distance of route
    public float calculateDistance(Location start, Location end) {
        return Math.round(start.distanceTo(end) * 100)/100;
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
        desired_pace = (EditText) findViewById(R.id.target_pace);
        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("Playlist", spinner_playlist.toString());
                intent.putExtra("Exercise", spinner_exercise.toString());
                intent.putExtra("Desired Pace", desired_pace.toString());
                intent.putExtra("Total Distance", distance);
                startActivity(intent);
            }
        });
    }
}
