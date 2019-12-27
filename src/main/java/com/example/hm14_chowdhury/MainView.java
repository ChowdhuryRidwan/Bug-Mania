package com.example.hm14_chowdhury;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hm14_chowdhury.R;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder = null;
    Context context;
    private MainThread t = null;

    // Constructor
    @SuppressWarnings("deprecation")
    public MainView (Context context) {
        super(context);
        // Save context
        this.context = context;
        // Retrieve the SurfaceHolder instance associated with this SurfaceView.
        holder = getHolder();
        // Initialize variables
        this.context = context;
        Assets.state = Assets.GameState.GettingReady;
        Assets.livesLeft = 3;

        // Load the sound effects
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Assets.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            Assets.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();

        }
        Assets.getReadySound = Assets.soundPool.load(context, R.raw.getready, 1);
        Assets.squishSound = Assets.soundPool.load(context, R.raw.squish, 1);
        Assets.thumpSound = Assets.soundPool.load(context, R.raw.thump, 1);
        Assets.squishSound1 = Assets.soundPool.load(context, R.raw.squish1, 1);
        Assets.squishSound2 = Assets.soundPool.load(context, R.raw.squish2, 1);
        Assets.superSquishSound = Assets.soundPool.load(context, R.raw.super_squish, 1);
        Assets.clapSound = Assets.soundPool.load(context, R.raw.bugreach, 1);
        Assets.bugReachSound = Assets.soundPool.load(context, R.raw.bugreach, 1);




        // Specify this class (MainView) as the class that implements the three callback methods required by SurfaceHolder.Callback.
        holder.addCallback(this);
    }

    public void pause ()
    {
        t.setRunning(false);
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume ()
    {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x, y;
        int action = event.getAction();
        x = event.getX();
        y = event.getY();

        if (action==MotionEvent.ACTION_DOWN){
            if (t != null)
                t.setXY ((int)x, (int)y);
        }
        return true;
    }

    @Override
    public void surfaceCreated (SurfaceHolder holder) {
        // Create and start a drawing thread whose Runnable object is defined by this class (MainView)
        if (t == null) {
            t = new MainThread(holder, context);
            t.setRunning(true);
            t.start();
            setFocusable(true);
        }
    }

    @Override public void surfaceChanged(SurfaceHolder sh, int f, int w, int h) {}
    @Override public void surfaceDestroyed(SurfaceHolder sh) {}
}
