package com.sd2.recordracer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;

import android.os.Build;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.view.View;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.location.Location;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


// this activity is the main activity
public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;

    ResponseReceiver receiver;

    boolean playing = false;
    float startTime;
    float currTime ;
    float totalDistance;
    float timeGoal;
    float expectedRate;
    float currDistanceCovered;

    float currPercent;
    float expectedPercent;
    float newSampleRate;

    //set prev point
    int DELAY; //ms
    Location mCurrentLocation;
    Location prevLocation;
    String mLatitudeText;
    String mLongitudeText;
    LocationRequest mLocationRequest;
    float tempDistance;
    final int optimalSampleRate = 44100;

    PendingIntent intervalPendingIntent;
    Handler handler;

    public void onConnectionSuspended (int cause) {

    }

    public void onConnectionFailed (ConnectionResult result) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //sample current location in android, have distance goal variable
        //keep track of time, have time goal variable
        //get expected rate of distance per ms to meet goal
        //-- totalDistance(meters) / goalTime(s) / 1000 (so meters/ms)
        //first, set prev point to curr location, then
        //at each sample:
        //break loop when currDistanceCovered >= distanceGoal
        //0. --delay a certain amount of time to allow for new location, updating currTime
        //1. get distance from curr point to prev point: tempDistance
        //2. add that tempDistance to currDistanceCovered var
        //3. update sample frequency based on currTime:
        //   expectedPercent = expectedRate * currTime(ms) / totalDistance(meters)
        //   currPercent = currDistanceCovered / totalDistance(meters)
        //   newSampleRate = (currPercent - expectedPercent) * optimalSampleRate
        //4. update prev point to curr point

        //update everything,

        //set mock location

//        prevLocation = mCurrentLocation;
//        if(prevLocation != null) {
//            mCurrentLocation = location;
//            tempDistance = (long) prevLocation.distanceTo(mCurrentLocation);
//            currDistanceCovered += tempDistance;
//
//            Log.d("currDistanceCovered", "currDistanceCovered = " + String.format("%.2f", currDistanceCovered));
//
//            if(currDistanceCovered >= totalDistance) {
//                stopLocationUpdates();
//                //stop song
//                SuperpoweredExample_PlayPause((Button) findViewById(R.id.playPause));
//                return;
//            }
//            currTime = (System.nanoTime() - startTime) / 1000000000.0f; //s
//            Log.d("Time", "Current time is " + currTime);
//
//            Log.d("expectedRate", "expectedRate = " + String.format("%.5f", expectedRate));
//            Log.d("totalDistance", "totalDistance = " + String.format("%.5f", totalDistance));
//
//            expectedPercent = (expectedRate * currTime / totalDistance);
//            Log.d("expectedPercent", "expectedPercent = " + String.format("%.5f", expectedPercent));
//
//            currPercent = (currDistanceCovered / totalDistance);
//            Log.d("currPercent", "currPercent = " + String.format("%.5f", currPercent));
//
//            if(currPercent > expectedPercent) { //lower freq, speed up
//                newSampleRate = optimalSampleRate - ((currPercent - expectedPercent) * optimalSampleRate);
//            } else {
//                newSampleRate = optimalSampleRate + ((expectedPercent - currPercent) * optimalSampleRate);
//            }
//            Log.d("newSample", "newSample = " + newSampleRate);
//
//            onResamplerValue((int) newSampleRate);
//        }


    }

    @Override
    public void onConnected(Bundle connectionHint) {
        createLocationRequest();
//        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);

//        if (mCurrentLocation != null) {
//            mLatitudeText = String.valueOf(mCurrentLocation.getLatitude());
//            mLongitudeText = String.valueOf(mCurrentLocation.getLongitude());
//        }
    }

    protected void startLocationUpdates() {

        receiver = new ResponseReceiver();

        Intent intervalIntent = new Intent(this, LocationService.class);
        intervalPendingIntent = PendingIntent.getService(this, 0, intervalIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        IntentFilter broadcastFilter = new IntentFilter(ResponseReceiver.LOCAL_ACTION);

        getApplicationContext().registerReceiver(receiver, broadcastFilter, null, handler); // Will not run on main thread

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mCurrentLocation != null) {
            Log.d("RECEIVED IN PLAY", "lat = " + String.format("%.8f", mCurrentLocation.getLatitude()) + " long = "
                    + String.format("%.8f", mCurrentLocation.getLongitude()));
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, intervalPendingIntent);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void stopLocationUpdates() {
        intervalPendingIntent.cancel();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(250);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d("created request", "created location request");
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new ResponseReceiver();

        HandlerThread handlerThread = new HandlerThread("ht");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        handler = new Handler(looper);

        setContentView(R.layout.activity_main);

        // Get the device's sample rate and buffer size to enable low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        }
        if (samplerateString == null) samplerateString = "44100";
        if (buffersizeString == null) buffersizeString = "512";

        // Files under res/raw are not compressed, just copied into the APK. Get the offset and length to know where our files are located.
        AssetFileDescriptor fd0 = getResources().openRawResourceFd(R.raw.lycka), fd1 = getResources().openRawResourceFd(R.raw.nuyorica);
        long[] params = {
                fd0.getStartOffset(),
                fd0.getLength(),
                fd1.getStartOffset(),
                fd1.getLength(),
                Integer.parseInt(samplerateString),
                Integer.parseInt(buffersizeString)
        };
        try {
            fd0.getParcelFileDescriptor().close();
            fd1.getParcelFileDescriptor().close();
        } catch (IOException e) {
            android.util.Log.d("", "Close error.");
        }

        // Arguments: path to the APK file, offset and length of the two resource files, sample rate, audio buffer size.
        SuperpoweredExample(getPackageResourcePath(), params);

        // crossfader events
        final SeekBar crossfader = (SeekBar)findViewById(R.id.crossFader);
        crossfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onCrossfader(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // fx fader events
        final SeekBar fxfader = (SeekBar)findViewById(R.id.fxFader);
        fxfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onFxValue(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                onFxValue(seekBar.getProgress());
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                onFxOff();
            }
        });

        // resampler fader events
        final SeekBar rsfader = (SeekBar)findViewById(R.id.rsFader);
        rsfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onResamplerValue(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                onResamplerValue(seekBar.getProgress());
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        // fx select event
        final RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup1);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                onFxSelect(radioGroup.indexOfChild(checkedRadioButton));
            }
        });

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setMockLocation();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize
                        // location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    0x1);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the erroßr.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public void setMockLocation() {

    }

    public void SuperpoweredExample_PlayPause(View button) {  // Play/pause.
        playing = !playing;
        onPlayPause(playing);
        Button b = (Button) findViewById(R.id.playPause);
        b.setText(playing ? "Pause" : "Play");
        if(playing) {
            //set prev to null

            //set start time, distance, and time goals
            startTime = System.nanoTime();
            totalDistance = 200.0f; //meters 1538 to test deceleration slowly
            timeGoal = 400.0f; //1 sec
            expectedRate =  totalDistance / timeGoal;
            currDistanceCovered = 0;



            startLocationUpdates();
        } else { //false
            stopLocationUpdates();
//            try {
//                unregisterReceiver(receiver);
//            } catch(Exception e) {
//                Log.d("receiver null already", "receiver null already");
//            }
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

            //1539 covered total distance
            //takes 297.9
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String LOCAL_ACTION = "com.sd2.recordracer.ALL_DONE";

        @Override
        public void onReceive(Context context, Intent intent) {
            //get location from broadcast
            Location location = (Location) intent.getExtras().get("location");

            prevLocation = mCurrentLocation;
            if(prevLocation != null) {
                mCurrentLocation = location;
                tempDistance = (long) prevLocation.distanceTo(mCurrentLocation);
                currDistanceCovered += tempDistance;

                Log.d("currDistanceCovered", "currDistanceCovered = " + String.format("%.2f", currDistanceCovered));

                if(currDistanceCovered >= totalDistance) {
                    stopLocationUpdates();
                    //stop song
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SuperpoweredExample_PlayPause((Button) findViewById(R.id.playPause));
                        }
                    });

                    return;
                }
                currTime = (System.nanoTime() - startTime) / 1000000000.0f; //s
                Log.d("Time", "Current time is " + currTime);

                Log.d("expectedRate", "expectedRate = " + String.format("%.5f", expectedRate));
                Log.d("totalDistance", "totalDistance = " + String.format("%.5f", totalDistance));

                expectedPercent = (expectedRate * currTime / totalDistance);
                Log.d("expectedPercent", "expectedPercent = " + String.format("%.5f", expectedPercent));

                currPercent = (currDistanceCovered / totalDistance);
                Log.d("currPercent", "currPercent = " + String.format("%.5f", currPercent));

                if(currPercent > expectedPercent) { //lower freq, speed up
                    newSampleRate = optimalSampleRate - ((currPercent - expectedPercent) * optimalSampleRate);
                } else {
                    newSampleRate = optimalSampleRate + ((expectedPercent - currPercent) * optimalSampleRate);
                }
                Log.d("newSample", "newSample = " + newSampleRate);

                onResamplerValue((int) newSampleRate);
            }


        }
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

    private native void SuperpoweredExample(String apkPath, long[] offsetAndLength);
    private native void onPlayPause(boolean play);
    private native void onCrossfader(int value);
    private native void onFxSelect(int value);
    private native void onFxOff();
    private native void onFxValue(int value);
    private native void onResamplerValue(int value);

    static {
        System.loadLibrary("SuperpoweredExample");
    }
}
