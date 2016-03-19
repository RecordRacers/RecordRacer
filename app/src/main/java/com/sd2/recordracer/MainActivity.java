package com.sd2.recordracer;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
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
    private List<String> songs = new ArrayList<String>();
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

    }


    // Load all locally saved music to SuperPowered by passing in params
    public void initMusicAndSuperpowered(){

        // Define query that will fetch the music
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC");

        // Store song URIs to our global list
        while (cursor.moveToNext()) {
            String songURI = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA));
            this.songs.add(songURI);
        }

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


        // Finnaly, pass all info to the NDK
        // Arguments: path to the APK file, offset and length of the two resource files, sample rate, audio buffer size.
        SuperpoweredExample(getPackageResourcePath(), params);

    }

    public void SuperpoweredExample_PlayPause(View button) {  // Play/pause.
        playing = !playing;
        onPlayPause(playing);
        Button b = (Button) findViewById(R.id.playPause);
        b.setText(playing ? "Pause" : "Play");
    }

    public void SuperpoweredExample_Previous(View button) {  // go to previous song in queue
        onPreviousSong();
    }

    public void SuperpoweredExample_Next(View button) {  // go to next song in queue
        onNextSong();
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
    private native void onNextSong();
    private native void onPreviousSong();

    static {
        System.loadLibrary("SuperpoweredExample");
    }
}
