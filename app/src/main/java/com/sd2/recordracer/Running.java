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
public class Running extends Activity {
    private List run_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_record);

        //User user = getIntent().getSerializableExtra("User");
        User user = new User("test", "dummy", "dummy@dummy.com");
        user.setLongestRun(12.2f);
        user.setFastestMileRun(8);
        user.setFastest5kRun(5);
        user.setFastestKilometerRun(100);
        user.setTotalMilesRun(312.3f);
        user.setTotalRuns(40);

        float longestRun = user.getLongestRun();
        float fastestMile = user.getFastestMileRun();
        float fastestKM = user.getFastestKilometerRun();
        float fastest5K = user.getFastest5kRun();
        float totalMiles = user.getTotalMilesRun();
        float totalRuns = user.getTotalRuns();

        TextView longestRunTV = (TextView) findViewById(R.id.longestRun);
        TextView fastestMileTV = (TextView) findViewById(R.id.fastestMile);
        TextView fastestKMTV = (TextView) findViewById(R.id.fastestKM);
        TextView fastest5KTV = (TextView) findViewById(R.id.fastest5K);
        TextView totalMilesTV = (TextView) findViewById(R.id.totalMiles);
        TextView totalRunsTV = (TextView) findViewById(R.id.totalRuns);

        setVals(longestRun,longestRunTV);
        setVals(fastestMile, fastestMileTV);
        setVals(fastestKM, fastestKMTV);
        setVals(fastest5K, fastest5KTV);
        setVals(totalMiles, totalMilesTV);
        setVals(totalRuns, totalRunsTV);
    }

    public void setVals(float val, TextView textView) {
        textView.setText(Float.toString(val));
    }
}
