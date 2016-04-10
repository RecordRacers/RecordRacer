package com.sd2.recordracer;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

/**
 * Created by Eric on 4/1/16.
 */
public class LocationService extends IntentService {
    public LocationService() {
        super("LocationService");
    }

    protected void onHandleIntent(Intent intent) {
        Location location = intent.getParcelableExtra(LocationServices.FusedLocationApi.KEY_LOCATION_CHANGED);
        Log.d("RECEIVED", "RECEIVED");
        if(location != null)
        {
            Log.d("RECEIVED LOCATION", "lat = " + String.format("%.8f", location.getLatitude()) + " long = "
                        + String.format("%.8f", location.getLongitude()));

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.ResponseReceiver.LOCAL_ACTION);
            broadcastIntent.putExtra("location", location);
            sendBroadcast(broadcastIntent);
        }
    }
}
