package com.example.hm14_chowdhury;

import android.graphics.Canvas;
import com.example.hm14_chowdhury.R;

public class BigBug {

    // States of a Bug
    enum BugState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    BugState state;			// current state of bug
    int x,y; // location on screen (in screen coordinates)
    int i=1,j=2;
    int k=1,l=2;
    int touchcount=0;
    int newtouch=0;
    double speed;			// speed of bug (in pixels per second)
    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the bug

    // Bug starts not alive
    public BigBug () {
        state = BugState.Dead;
    }

    // Bug birth processing
    public void birth (Canvas canvas) {
        if (state == BugState.Dead) {
            state = BugState.ComingBackToLife;
            startBirthTimer = System.nanoTime() / 1000000000f;
        }
        // Check if bug is alive yet
        else if (state == BugState.ComingBackToLife) {
            float curTime = System.nanoTime() / 1000000000f;
            // Has birth timer expired?
            if (curTime - deathTime >20) {
                // If so, then bring bug to life
                state = BugState.Alive;
                // Set bug starting location at top of screen
                x = (int)(Math.random() * canvas.getWidth());
                // Keep entire bug on screen
                if (x < Assets.bigBug.getWidth()/2)
                    x = Assets.bigBug.getWidth()/2;
                else if (x > canvas.getWidth() - Assets.bigBug.getWidth()/2)
                    x = canvas.getWidth() - Assets.bigBug.getWidth()/2;
                y = 0;
                // Set speed of this bug
                speed = canvas.getHeight() / 6; // no faster than 1/4 a screen per second
                animateTimer = curTime;
            }
        }
    }

    public void move (Canvas canvas) {
        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Get elapsed time since last call here
            float curTime = System.nanoTime() / 1000000000f;
            float elapsedTime = curTime - animateTimer;
            animateTimer = curTime;
            // Compute the amount of pixels to move (vertically down the screen)
            y += (speed * elapsedTime);
            // Draw bug on screen




            if(i<j) {
                canvas.drawBitmap(Assets.bigBug, x, y, null);
                i++;
            }
            else{
                canvas.drawBitmap(Assets.bigBug1, x, y, null);
                j++;
            }




            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                // Kill the bug
                state = BugState.Dead;
                // Subtract 1 life
                Assets.soundPool.play(Assets.bugReachSound, 1, 1, 1, 0, 1);
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills bug - return true if bug killed
    public boolean supertouched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;

        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Compute distance between touch and center of bug
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.bigBug.getWidth()*0.75f) {
                touchcount++;
            }


            if (touchcount-newtouch == 4 && dis <= Assets.bigBug.getWidth()*0.75f) {
                newtouch=touchcount;
                state = BugState.DrawDead;    // need to draw dead body on screen for a while
                touched = true;
                // Record time of death
                deathTime = System.nanoTime() / 1000000000f;
            }
        }
        return (touched);
    }

    // Draw dead bug body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == BugState.DrawDead) {


            canvas.drawBitmap(Assets.bigBug2, x, y, null);




            // Get time since death
            float curTime = System.nanoTime() / 1000000000f;
            float timeSinceDeath = curTime - deathTime;
            if (timeSinceDeath > 4)
                state = BugState.Dead;
        }
    }

}

