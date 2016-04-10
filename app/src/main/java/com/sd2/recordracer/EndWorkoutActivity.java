package com.sd2.recordracer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;



public class EndWorkoutActivity extends Activity  {
    private GoogleMap googleMap;
    private LocationManager locationManager;

    private Criteria criteria;
    private float distance_covered;
    private float time_elapsed;
    private float average_pace;
    private User user;
    private String activityType;
    private int targetPace;
    private float calories_burned;
    private ArrayList<LatLng> route;
    private TextView distance_covered_text;
    private TextView time_elapsed_text;
    private TextView average_pace_text;
    private TextView calories_burned_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        Intent intent = getIntent();
        double latitude = 29.6510435;
        double longitude = -82.3265487;
        route = new ArrayList<LatLng>(); // TODO: fill LatLng objects from previous Intent here
        //route = (ArrayList<LatLng>) intent.getSerializableExtra("Locations");

        // tests
        for(int i = 0; i < 50; i++) {
            LatLng latLng = new LatLng(latitude, longitude);
            route.add(latLng);
            latitude = latitude - 0.00001361;
        }

        latitude = latitude - 0.00001361;
        for(int i = 0; i < 50; i++) {
            LatLng latLng = new LatLng(latitude, longitude);
            route.add(latLng);
            longitude = longitude + 0.0004174;
        }
        distance_covered =  intent.getFloatExtra("Distance Covered", 1600.0f);
        time_elapsed =  intent.getFloatExtra("Time Elapsed",300.0f);
        user = (User) getIntent().getSerializableExtra("User");
        activityType = intent.getStringExtra("ActivityType");
        targetPace = (int) intent.getFloatExtra("ExpectedRate", 0f);
        Log.d("GET_INPUT", "DISTANCE COVERED: " + distance_covered);
        Log.d("GET_INPUT", "TIME ELAPSED:" + time_elapsed);

        updateDatabase();

        Log.d("GET_INPUT", "DISTANCE COVERED: " + distance_covered);
        Log.d("GET_INPUT","TIME ELAPSED:"+time_elapsed);

        distance_covered_text = (TextView) findViewById(R.id.distance_covered_text);
        time_elapsed_text = (TextView) findViewById(R.id.time_elapsed_text);

/*      average_pace_text = (TextView) findViewById(R.id.average_pace_text);
        calories_burned_text = (TextView) findViewById(R.id.calories_burned_text); */

        distance_covered_text.setText(Float.toString(((distance_covered / 100) * 100)));
        time_elapsed_text.setText(Float.toString(((time_elapsed / 100) * 100)));
/*      average_pace_text.setText(Float.toString(((average_pace / 100) * 100)));
        calories_burned_text.setText(Float.toString(((calories_burned / 100) * 100)));*/

        try {
            if (googleMap == null) {
                Log.d("WTF", "MAP IS NULL!");
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            if (googleMap != null) {
                Log.d("WTF", "MAP IS NOT NULL");
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                final LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                MarkerOptions options = new MarkerOptions();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(3);

                for(int i = 0; i < route.size(); i++) {
                    options.position(route.get(i));
                    bounds.include(route.get(i));
                    if(i == 0 || i == route.size() - 1) {
                        googleMap.addMarker(options);
                    }
                }
                polylineOptions.addAll(route);
                googleMap.addPolyline(polylineOptions);
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),30));
                        googleMap.setOnCameraChangeListener(null);
                    }
                });

/*                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(provider, 2000, 0, this);

                Location location = locationManager.getLastKnownLocation(provider);
                Log.d("WTF",location.toString());
                if (location != null) {
                    onLocationChanged(location);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void onLocationChanged(Location location) {
        double latitude, longitude;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.d("WTF", "MAP HAS MOVED CAMERA: " + googleMap.getCameraPosition());
    }*/

/*    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

    public void toMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
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
            if (distance_covered>=3000 && time_elapsed< user.getFastest5kRun()) {
                user.setFastest5kRun((int) time_elapsed);
            }
            if (distance_covered>=1000 && time_elapsed < user.getFastestKilometerRun()) {
                user.setFastestKilometerRun((int) time_elapsed);
            }
            if (distance_covered >=1609 && time_elapsed < user.getFastestMileRun()) {
                user.setFastestMileRun((int) time_elapsed);
            }
            if (distance_covered > user.getLongestRun()) {
                user.setLongestRun(distance_covered);
            }
            user.setTotalRuns(user.getTotalRuns()+1);
            user.setTotalMilesRun(user.getTotalMilesRun()+(distance_covered/1609.34f));
        } else {
            caloriesBurned = caloriesBurnedBiking(weightInPounds, miles);
            if (distance_covered>=3000 && time_elapsed< user.getFastest5kBiked()) {
                user.setFastest5kBiked((int)time_elapsed);
            }
            if (distance_covered>=1000 && time_elapsed < user.getFastestKilometerBiked()) {
                user.setFastestKilometerBiked((int)time_elapsed);
            }
            if (distance_covered >=1609 && time_elapsed < user.getFastestMileBiked()) {
                user.setFastestMileBiked((int)time_elapsed);
            }
            if (distance_covered > user.getLongestRide()) {
                user.setLongestRide(distance_covered);
            }
            user.setTotalRides(user.getTotalRides()+1);
            user.setTotalMilesBiked(user.getTotalMilesBiked()+(distance_covered/1609.34f));
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
