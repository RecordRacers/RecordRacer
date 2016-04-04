package com.sd2.recordracer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by woodr_000 on 4/3/2016.
 */
public class Biking extends Activity {

    private ListView listview;
    private List exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_record);


       // User user = getIntent().getSerializableExtra("User");
        User user = new User("test", "dummy", "dummy@dummy.com");
        user.setLongestRide(12.2f);
        user.setFastestMileBiked(8);
        user.setFastest5kBiked(5);
        user.setFastestKilometerBiked(100);
        user.setTotalMilesBiked(312.3f);
        user.setTotalRides(40);

        float longestRide = user.getLongestRide();
        float fastestMile = user.getFastestMileBiked();
        float fastestKM = user.getFastestKilometerBiked();
        float fastest5K = user.getFastest5kBiked();
        float totalMiles = user.getTotalMilesBiked();
        float totalRides = user.getTotalRides();

        TextView longestRideTV = (TextView) findViewById(R.id.longestRide);
        TextView fastestMileTV = (TextView) findViewById(R.id.fastestMile);
        TextView fastestKMTV = (TextView) findViewById(R.id.fastestKM);
        TextView fastest5KTV = (TextView) findViewById(R.id.fastest5K);
        TextView totalMilesTV = (TextView) findViewById(R.id.totalMiles);
        TextView totalRidesTV = (TextView) findViewById(R.id.totalRides);

        setVals(longestRide, longestRideTV);
        setVals(fastestMile, fastestMileTV);
        setVals(fastestKM, fastestKMTV);
        setVals(fastest5K, fastest5KTV);
        setVals(totalMiles, totalMilesTV);
        setVals(totalRides, totalRidesTV);
    }

    public void setVals(float val, TextView textView) {
        textView.setText(Float.toString(val));
    }
}