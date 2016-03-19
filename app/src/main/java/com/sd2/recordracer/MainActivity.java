package com.sd2.recordracer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.content.IntentSender;
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

        prevLocation = mCurrentLocation;
        if(prevLocation != null) {
            mCurrentLocation = location;
            tempDistance = (long) prevLocation.distanceTo(mCurrentLocation);
            currDistanceCovered += tempDistance;

            Log.d("currDistanceCovered", "currDistanceCovered = " + String.format("%.2f", currDistanceCovered));

            if(currDistanceCovered >= totalDistance) {
                stopLocationUpdates();
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
                newSampleRate = optimalSampleRate - ((currPercent - expectedPercent) * optimalSampleRate * 10);
            } else {
                newSampleRate = optimalSampleRate + ((expectedPercent - currPercent) * optimalSampleRate * 10);
            }
            Log.d("newSample", "newSample = " + newSampleRate);

            onResamplerValue((int) newSampleRate);
        }


    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        createLocationRequest();
        if (mCurrentLocation != null) {
            mLatitudeText = String.valueOf(mCurrentLocation.getLatitude());
            mLongitudeText = String.valueOf(mCurrentLocation.getLongitude());
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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
        setContentView(R.layout.activity_main);

        // Fetch all locally stored music and initialize Superpowered app with it
        initMusicAndSuperpowered();


        // resampler fader events
        final SeekBar rsfader = (SeekBar)findViewById(R.id.rsFader);
        rsfader.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
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


    public void setMockLocation() {

    }

    public void SuperpoweredExample_PlayPause(View button) {  // Play/pause.
        playing = !playing;
        onPlayPause(playing);
        Button b = (Button) findViewById(R.id.playPause);
        b.setText(playing ? "Pause" : "Play");
        if(playing) {
            //set prev to null
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            //set start time, distance, and time goals
            startTime = System.nanoTime();
            totalDistance = 1538.0f; //meters
            timeGoal = 400.0f; //1 sec
            expectedRate =  totalDistance / timeGoal;
            currDistanceCovered = 0;

            startLocationUpdates();
        } else { //false
            stopLocationUpdates();
        }
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
