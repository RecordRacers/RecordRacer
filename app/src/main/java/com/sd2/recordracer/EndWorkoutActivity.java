package com.sd2.recordracer;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class EndWorkoutActivity extends Activity implements LocationListener {
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private float distance_covered;
    private float time_elapsed;
    private float average_pace;
    private User user;
    private String activityType;
    private int targetPace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        Intent intent = getIntent();

        distance_covered =  intent.getFloatExtra("Distance Covered",1600.0f);
        time_elapsed =  intent.getFloatExtra("Time Elapsed",300.0f);
        user = (User) getIntent().getSerializableExtra("User");
        activityType = intent.getStringExtra("ActivityType");
        targetPace = (int) intent.getFloatExtra("ExpectedRate", 0f);
        Log.d("GET_INPUT", "DISTANCE COVERED: " + distance_covered);
        Log.d("GET_INPUT", "TIME ELAPSED:" + time_elapsed);

        updateDatabase();




        try {
            if (googleMap == null) {
                Log.d("WTF", "MAP IS NULL!");
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
    }

    public void onLocationChanged(Location location) {
        double latitude, longitude;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.d("WTF", "MAP HAS MOVED CAMERA: " + googleMap.getCameraPosition());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void toMainMenu(View view){
        Intent intent = new Intent(this, ChooseWorkoutActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDatabase() {
        int weightInPounds = user.getWeight();
        int caloriesBurned = 0;
        double miles = distance_covered * 0.000621371;
        double distance = miles;
        if (user.isUseMetricSystem()) {
            //was actually in kilograms - must adjust
            weightInPounds*=2.20462;
            distance = distance_covered/1000;
        }
        if ("Running".compareTo(activityType)==0) {
            caloriesBurned = caloriesBurnedRunning(weightInPounds, miles);
        } else {
            caloriesBurned = caloriesBurnedBiking(weightInPounds, miles);
        }
        int seconds = (int) time_elapsed;
        /**
         * activityType is either "Running" or "Biking"
         * distance is in either km or miles depending on the user's preference
         *
         */
        Exercise exercise = new Exercise(activityType, (float) distance, seconds, targetPace ,caloriesBurned, new Date());
        List<Exercise> exercises = user.getExercises();
        exercises.add(exercise);
        user.setExercises(exercises);
        Dao dao = new CouchDao(this);
        dao.updateUser(user);

    }

    private int caloriesBurnedRunning(int pounds, double milesRun) {
        double cal = pounds*milesRun*.63;
        return (int) cal;
    }
    private int caloriesBurnedBiking(int pounds, double milesBiked) {
        double cal = pounds*milesBiked*4.54/15;
        return (int) cal;
    }
}
