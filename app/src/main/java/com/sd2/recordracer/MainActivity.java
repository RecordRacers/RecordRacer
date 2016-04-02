package com.sd2.recordracer;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    private List<AssetFileDescriptor> songs = new ArrayList<AssetFileDescriptor>();
    private List<String> songsURI = new ArrayList<String>();
    private int currentSongIndex = 1;
    private String samplerateString = null, buffersizeString = null;

    private GoogleApiClient mGoogleApiClient;

    ResponseReceiver receiver;

    boolean playing = false;
    boolean smartPacing = true;
    float startTime;
    float currTime ;
    float totalDistance;
    float startupDistance;
    float pacingDistance = 5.0f;       // 1 mile for optimal smart pacing
    float pacingWindowCount = 0;
    float timeGoal;
    float expectedRate;
    float currDistanceCovered;
    float prevTime = 0.0f;
    float sampleTime;

    float currPercent;
    float expectedPercent;
    float expectedDistance;
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


    /*
    LOCATION-BASED ALGORITHM
        on location changes
    */
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
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
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


    public boolean nextPacingWindow(){
        //
        int currWindow = (int)(currDistanceCovered / pacingDistance);
        pacingDistance = pacingDistance * (1.005f);
        if( currWindow > pacingWindowCount ) {
            Log.d("currWindow", " currWindow = " + currWindow);
            pacingWindowCount = currWindow;
            return true;
        }else{
            return false;
        }
    }

    protected void onPause() {
        super.onPause();
    }

    protected void stopLocationUpdates() {

        intervalPendingIntent.cancel();

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

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

        // Fetch all locally stored music and initialize Superpowered app with it
        //initMusicAndSuperpowered();
        initEmulatorMusicAndSuperpowered();

        // resampler fader events
//        final SeekBar rsfader = (SeekBar)findViewById(R.id.rsFader);
//        rsfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                onResamplerValue(progress);
//            }
//
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                onResamplerValue(seekBar.getProgress());
//            }
//
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                //
//            }
//        });

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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


    // Load all locally saved music to SuperPowered by passing in params
    public void initMusicAndSuperpowered(){
        // Define query that will fetch the music
        Cursor cursor = getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                null,
                MediaStore.Audio.Media.DATA + " like ? ",
                new String[] {"%RecordRacer%"},
                null);

        // Store song URIs and File Descriptors to our global lists
        while (cursor.moveToNext()) {
            String songURI = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA));
            try{
                this.songsURI.add(songURI);
                this.songs.add(getContentResolver().openAssetFileDescriptor(Uri.parse("file://" + songURI), "r"));
                Log.d("DEBUG",">>>>>SONGURI>>>>>"+songURI);
            }catch(FileNotFoundException e){
                Log.d("DEBUG",e.getMessage());
            }
        }


        // Get the device's sample rate and buffer size to enable low-latency Android audio output, if available.

        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            this.samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            this.buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        }
        if (this.samplerateString == null) this.samplerateString = "44100";
        if (this.buffersizeString == null) this.buffersizeString = "512";

        // Set up the first audio file as a parameter we can pass to NDK
        long[] params = {
                songs.get(this.currentSongIndex).getStartOffset(),
                songs.get(this.currentSongIndex).getLength(),
                Integer.parseInt(this.samplerateString),
                Integer.parseInt(this.buffersizeString)
        };
        try {
            // Close pipe after reading
            songs.get(this.currentSongIndex).getParcelFileDescriptor().close();
        } catch (IOException e) {
            android.util.Log.d("", "Close error.");
        }

        // Finally, pass the first song to the AdvancedAudioPlayer as you initialize it
        SuperpoweredExample(songsURI.get(this.currentSongIndex), params);

    }

    // Load all locally saved music to SuperPowered by passing in params
    public void initEmulatorMusicAndSuperpowered(){
        // Get the device's sample rate and buffer size to enable low-latency Android audio output, if available.

        if (Build.VERSION.SDK_INT >= 17) {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            this.samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            this.buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        }
        if (this.samplerateString == null) this.samplerateString = "44100";
        if (this.buffersizeString == null) this.buffersizeString = "512";


        AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.nuyorica);
        // Set up test audio file as a parameter we can pass to NDK
        long[] params = {
                fd.getStartOffset(),
                fd.getLength(),
                Integer.parseInt(this.samplerateString),
                Integer.parseInt(this.buffersizeString)
        };

        // Finally, pass the first song to the AdvancedAudioPlayer as you initialize it
        SuperpoweredExample(getPackageResourcePath(), params);

    }


    public void nextSong(View button){
        Log.d("DEBUG","NEXT!");
        if(this.currentSongIndex == songs.size()-1){
            this.currentSongIndex = 1;
        }else {
            this.currentSongIndex++;
        }
        SuperpoweredExample_NewSong();
    }

    public void previousSong(View button) {
        Log.d("DEBUG", "PREVIOUS!");
        if (this.currentSongIndex == 1) {
            this.currentSongIndex = songs.size() - 1;
        } else {
            this.currentSongIndex--;
        }
        SuperpoweredExample_NewSong();
    }


    public void SuperpoweredExample_PlayPause(View button) {  // Play/pause.
        playing = !playing;

        Button b = (Button) findViewById(R.id.playPause);
        b.setText(playing ? "Pause" : "Play");
        if(playing) {
            //set prev to null

            //set start time, distance, and time goals
            startTime = System.nanoTime();

            totalDistance = 200.0f; //meters 1538 to test deceleration slowly
            timeGoal = 400.0f; //1 sec
            expectedRate =  totalDistance / timeGoal;


            // Depending on workout mode, set starting variables
            if(!smartPacing) {
                // calculate expected rate
                totalDistance = 1538.0f; //meters
                timeGoal = 400.0f; //1 sec
                expectedRate = totalDistance / timeGoal;
             }else{
                // we nee d to wait before setting rate
                expectedRate = 0.0f;
                pacingWindowCount = 0;

            }

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


        onPlayPause(playing);
    }

    @Override
    protected void onDestroy() {
            unregisterReceiver(receiver);
            super.onDestroy();
    }

    public void SuperpoweredExample_NewSong() {  // go to next song in queue
        // Set up the new audio file as a parameter we can pass to NDK
        long[] params = {
                songs.get(this.currentSongIndex).getStartOffset(),
                songs.get(this.currentSongIndex).getLength(),
                Integer.parseInt(this.samplerateString),
                Integer.parseInt(this.buffersizeString)
        };
        try {
            // Close pipe after reading
            songs.get(this.currentSongIndex).getParcelFileDescriptor().close();
        } catch (IOException e) {
            android.util.Log.d("", "Close error.");
        }

        // Finally, pass the first song to the AdvancedAudioPlayer as you initialize it
        onNewSong(songsURI.get(this.currentSongIndex), params);

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

            if (prevLocation != null) {
                mCurrentLocation = location;

                currTime = (System.nanoTime() - startTime) / 1000000000.0f; //s
                tempDistance = (long) prevLocation.distanceTo(mCurrentLocation);

                if(tempDistance > 5.0f) {
                    tempDistance = 0.0f;
                    mCurrentLocation = prevLocation;
                }

                currDistanceCovered += tempDistance;

                Log.d("currDistanceCovered", "currDistanceCovered = " + String.format("%.2f", currDistanceCovered));

                if (!smartPacing) {
                    if (currDistanceCovered >= totalDistance) {
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

                    Log.d("Time", "Current time is " + currTime);

                    Log.d("expectedRate", "expectedRate = " + String.format("%.5f", expectedRate));
                    Log.d("totalDistance", "totalDistance = " + String.format("%.5f", totalDistance));

                    expectedPercent = (expectedRate * currTime / totalDistance);
                    Log.d("expectedPercent", "expectedPercent = " + String.format("%.5f", expectedPercent));

                    currPercent = (currDistanceCovered / totalDistance);
                    Log.d("currPercent", "currPercent = " + String.format("%.5f", currPercent));

                    if (currPercent > expectedPercent) { //lower freq, speed up
                        newSampleRate = optimalSampleRate - ((currPercent - expectedPercent) * optimalSampleRate);
                    } else {
                        newSampleRate = optimalSampleRate + ((expectedPercent - currPercent) * optimalSampleRate);
                    }
                    Log.d("newSample", "newSample = " + newSampleRate);

                    onResamplerValue((int) newSampleRate);
                } else {
                    // In this case, the user doesn't care about total distance or time but on general pacing
                    // within a window of time.

                    if (currTime < 15.0f) {
                        // Avoid setting expected rate for now
                        if (currTime < 5.0f) {
                            // During the first 5 seconds, ramp up sampling rate to 44.1kHz
                            float percentOfSpeedupComplete = currTime / 5.0f;

                            newSampleRate = 94100.0f - (50000.0f * percentOfSpeedupComplete);
                            onResamplerValue((int) newSampleRate);
                            Log.d("DEBUG", "SAMPLE RATE RAMPING DOWN: " + newSampleRate);
                        } else {
                            Log.d("DEBUG", "SAMPLING RATE SHOULD NOW BE CONSTANT");
                        }
                    } else {
                        // If goal pace has not yet been established
                        // it must be calculated now
                        if (expectedRate == 0.0f) {
                            startupDistance = currDistanceCovered;
                            expectedRate = currDistanceCovered / currTime;      // average pace thus far
                            Log.d("DEBUG", "INITIAL RATE SET TO: " + expectedRate);
                        } else {
                            // Only update goal pace upon reaching new pacing window
                            if (nextPacingWindow()) {
                                // Running average as a goal
                                if (currDistanceCovered - startupDistance < 0.0f) {
                                    expectedRate = 0.0f;
                                } else {
                                    expectedRate = (currDistanceCovered) / (currTime);
                                }
                                Log.d("DEBUG", "NEXT WINDOW, UPDATED GOAL RATE: " + expectedRate);
                                // Expand window (the initial acceleration will have least weight)
                                // pacingDistance = 1.02f * pacingDistance;
                            }
                        }
                        // Adjust / report workout after smart-pacing calculations
                        Log.d("expectedRate", "expectedRate = " + String.format("%.5f", expectedRate));
                        Log.d("Distance Covered", "Distance Covered = " + String.format("%.5f", currDistanceCovered));

                        expectedDistance = expectedRate * currTime;
                        Log.d("expectedDistance", "expectedDistance = " + String.format("%.5f", expectedDistance));

                        if (currDistanceCovered > expectedDistance) { //lower freq, speed up
                            newSampleRate = optimalSampleRate - (((currDistanceCovered - expectedDistance) / currDistanceCovered) * optimalSampleRate);
                        } else {
                            newSampleRate = optimalSampleRate + (((expectedDistance - currDistanceCovered) / expectedDistance) * optimalSampleRate);
                        }
                        Log.d("newSample", "newSample = " + newSampleRate);
                        prevTime = currTime;
                        onResamplerValue((int) newSampleRate);

                    }
                }

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
    private native void onNewSong(String apkPath, long[] offsetAndLength);

    static {
        System.loadLibrary("SuperpoweredExample");
    }
}
