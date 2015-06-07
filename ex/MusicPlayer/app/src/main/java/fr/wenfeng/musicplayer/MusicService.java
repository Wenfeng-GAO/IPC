package fr.wenfeng.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    /**
     * Dubugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName().toString();

    /**
     * Intent action used to start the Service.
     */
    private static final String ACTION_PLAY = "fr.wenfeng.musicplayer.action.PLAY";

    /**
     * Keep track of whether a song is currently playing.
     */
    private static boolean isPlaying = false;

    /**
     * The MediaPlayer that plays a song in the background.
     */
    private MediaPlayer mPlayer;

    /**
     * This factory method returns an intent used to play and stop
     * playing a song, which is designated by the @songUrl
     * @param context
     * @param songUrl
     * @return
     */
    public static Intent makeIntent(final Context context, String songUrl) {
        // Create and return an intent that point to the MusicService.
        return new Intent(ACTION_PLAY, Uri.parse(songUrl), context, MusicService.class);
    }

    /**
     * Hook method called when a new instance of Service is created.
     * One time initialization code goes here.
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "OnCreate() entered");

        super.onCreate();

        // Create a MediaPlayer that will play the requested song.
        mPlayer = new MediaPlayer();

        // Indicate the MediaPlayer will stream the audio.
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * Hook method called every time startService() is called with an intent associated
     * with this MusicService.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "OnStartCommand() entered");

        // Extract the URL for the song to play.
        String songUrl = intent.getDataString();

        Log.d(TAG, "onStartCommand() with song URL: " + songUrl);

        // Stop playing the current song if it does.
        if (isPlaying)
            stopSong();

        try {
            Log.d(TAG, "onStartCommand() start to setDataSource()");

            // Note that the song is now playing.
            isPlaying = true;

            // Indicate the URL of the song to play.
            mPlayer.setDataSource(songUrl);

            Log.d(TAG, "onStartCommand() end of setDataSource()");

            // Register to play the song.
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared() entered");

                    // Just play the song once, rather than have it loop endlessly.
                    mp.setLooping(false);

                    // Start playing the song.
                    mp.start();
                }
            });

            // This call doesn't block the UI Thread.
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Don't restart Service if it shuts down.
        return START_NOT_STICKY;
    }

    /**
     * Stop MediaPlayer from playing the song.
     */
    private void stopSong() {
        Log.d(TAG, "stopSong() entered");

        // Stop playing the song.
        mPlayer.stop();

        // Reset state of the MediaPlayer.
        mPlayer.reset();

        // Note that no song is playing.
        isPlaying = false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "OnDestroy() entered");

        // Stop playing the song.
        stopSong();

        super.onDestroy();
    }

    /**
     * Necessary for Service
     * @param intent
     * @return null, Since MusicService is a so-called "Started Service"
     */
    @Override
    public IBinder onBind(Intent intent) { return null; }

}
