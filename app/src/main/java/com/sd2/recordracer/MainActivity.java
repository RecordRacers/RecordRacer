package com.sd2.recordracer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

// this activity is the main activity
public class MainActivity extends AppCompatActivity {
    private List<AssetFileDescriptor> songs = new ArrayList<AssetFileDescriptor>();
    private List<String> songsURI = new ArrayList<String>();
    private int currentSongIndex = 1;
    private String samplerateString = null, buffersizeString = null;
    boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch all locally stored music and initialize Superpowered app with it
        initMusicAndSuperpowered();


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

    public void previousSong(View button){
        Log.d("DEBUG","PREVIOUS!");
        if(this.currentSongIndex == 1){
            this.currentSongIndex = songs.size()-1;
        }else {
            this.currentSongIndex--;
        }
        SuperpoweredExample_NewSong();
    }

    public void SuperpoweredExample_PlayPause(View button) {  // Play/pause.
        playing = !playing;
        onPlayPause(playing);
        Button b = (Button) findViewById(R.id.playPause);
        b.setText(playing ? "Pause" : "Play");
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
