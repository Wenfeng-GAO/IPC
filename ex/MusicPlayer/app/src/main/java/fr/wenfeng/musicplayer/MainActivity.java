package fr.wenfeng.musicplayer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This MainActivity is a simple front-end to the MusicService, which
 * plays a song in the background.
 */
public class MainActivity extends ActionBarActivity {
    /**
     * Debug Tag for logging debug output to LogCat.
     */
    private final String TAG = getClass().getSimpleName().toString();

    /**
     * URL to the default song to play if the user doesn't enter a URL.
     */
    private final String DEFAULT_SONG = "http://www.dre.vanderbilt.edu/~schmidt/braincandy.m4a";

    /**
     * User inputs an URL to play a song.
     */
    private EditText mUrlEditText;

    /**
     * User clicks mPlayButton or mStopButton to play or stop the requested song.
     */
    private Button mPlayButton, mStopButton;

    /**
     * Intent that's used to start and stop the MusicService.
     */
    private Intent mMusicServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store the mUrlEditText, mPlayButton and mStopButton resource
        mUrlEditText = (EditText) findViewById(R.id.mUrlEditText);
        mPlayButton = (Button) findViewById(R.id.mPlayButton);
        mStopButton = (Button) findViewById(R.id.mStopButton);

        Log.d(TAG, "UI has been established.");
    }

    /**
     * Start playing song via the MusicService.
     */
    public void playSong(View playButton) {
        Log.d(TAG, "PLAY button was clicked, the url is " + getUrlString());

        // Create an intent that will start the MusicService to play a requested song.
        mMusicServiceIntent = MusicService.makeIntent(this,getUrlString());

        // Start the MusicService via the intent.
        startService(mMusicServiceIntent);

    }

    /**
     * Stop playing song via the MusicService.
     */
    public void stopSong(View stopButton) {
        Log.d(TAG, "STOP button was clicked.");

        // Stop the MusicService via intent.
        if (mMusicServiceIntent != null) {
            stopService(mMusicServiceIntent);
            mMusicServiceIntent = null;
        } else
            showToast("No song is currently playing.");
    }

    /**
     * Read the mUrlEditText and return the String it contains.
     * @return String value in mUrlEditText or the default url
     */
    public String getUrlString() {
        // Get the user input (if any).
        String url = mUrlEditText.getText().toString();

        // Use the default URL if the user doesn't supply one.
        if (url.equals(""))
            url = DEFAULT_SONG;

        return url;
    }

    /**
     * Show a toast to the user.
     */
    public void showToast(String toastString) {
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }
}
