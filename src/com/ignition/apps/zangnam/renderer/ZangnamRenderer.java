/**
 * 
 */
package com.ignition.apps.zangnam.renderer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.ignition.apps.zangnam.R;
import com.ignition.apps.zangnam.preferences.WallpaperPreferences;
import com.ignition.apps.zangnam.shapes.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author impaler
 *
 */
public class ZangnamRenderer implements Renderer {

    private static final String TAG = ZangnamRenderer.class.getName();
    private static final long ELEVATOR_INTERIOR_ANIMATION_DURATION = 4000;
    private static final long DANCE_INTERIOR_ANIMATION_DURATION = 13000;
    private static final float ELEVATOR_INTERIOR_DOOR_BOUNDARY = .9f;
    private static final float DANCE_INTERIOR_DOOR_BOUNDARY = 1.9f;

    private Context context;
    private MediaPlayer mMediaPlayer;

    private boolean isDoorsClosing = Boolean.FALSE;
    private boolean elevatorDoorsAnimating = Boolean.FALSE;
    private long elevatorOpenStartTime;
    private long elevatorPauseTime;
    private long elevatorResumeTime;
    private long elevatorPausedMillis;

    // doors
    private LeftDoor leftDoor;
    private RightDoor rightDoor;
    private Badge smsBadge;
    private float leftDoorX = 0;
    private float rightDoorX = 0;

    // elevator interior
    private ElevatorInterior elevatorInterior;
    private boolean showElevatorInterior;
    private int lastElevatorInteriorFrame;
    private long timeOfLastElevatorInteriorFrame;
    private int currentElevatorInteriorAudioClip;

    // dance interior
    private DanceInterior danceInterior;
    private boolean showDanceInterior;
    private int lastDanceInteriorFrame;
    private long timeOfLastDanceInteriorFrame;

    // dance
    private Dance dance;
    private int lastDanceFrame;
    private long timeOfLastDanceFrame;
    private float danceX;

    private boolean hasNewTextMessage;
    private boolean reloadTextures;

    private int[] elevatorInteriorAudioClips = {
            R.raw.elevator_interior1,
            R.raw.elevator_interior2,
            R.raw.elevator_interior3,
            R.raw.elevator_interior4
    };

    private int danceAudioClip = R.raw.dance;

	/** Constructor to set the handed over context */
	public ZangnamRenderer(Context context) {
		this.context = context;
		
		// initialise the elevator interior
		this.elevatorInterior = new ElevatorInterior();

        // initialise the left door
        this.leftDoor = new LeftDoor();

        // initialise the right door
        this.rightDoor = new RightDoor();

        // initialize the barn interior
        this.danceInterior = new DanceInterior();

        // initialize the sms badge
        this.smsBadge = new Badge();

        // initialize the dance
        this.dance = new Dance();
	}

    private boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    public void showElevatorInterior() {

        // only animate if not already animating
        if (!isElevatorDoorsAnimating()) {
            animateElevatorDoorsOpen();
            showElevatorInterior = true;
        }
    }

    public void showDanceInterior() {

        // only animate if not already animating
        if (!isElevatorDoorsAnimating()) {
            animateElevatorDoorsOpen();
            showDanceInterior = true;
            showElevatorInterior = false;
        }
    }

    public boolean isElevatorDoorsAnimating() {
        return elevatorDoorsAnimating;
    }

    public void newTextMessage() {
        showDanceInterior();
        hasNewTextMessage = true;
    }

    private void animateElevatorDoorsOpen() {
        elevatorDoorsAnimating = true;
        elevatorOpenStartTime = System.currentTimeMillis();
    }

    public void onPause() {
        if (isElevatorDoorsAnimating()) {
            elevatorPauseTime = System.currentTimeMillis();

            // if music is playing, kill the music!
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
    }

    public void onResume() {
        if (isElevatorDoorsAnimating()) {
            elevatorResumeTime = System.currentTimeMillis();
            elevatorPausedMillis += (elevatorResumeTime - elevatorPauseTime);
        }
    }

    public void onDoubleTap(MotionEvent event) {

        // launch text message app
        if (isElevatorDoorsAnimating() && hasNewTextMessage) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("vnd.android-dir/mms-sms");
            context.startActivity(intent);
        } else {
            showElevatorInterior();
        }
    }

    public boolean onScale(ScaleGestureDetector detector) {
        if (detector.getScaleFactor() >= 1.5) {
            showDanceInterior();
            return true;
        }
        return false;
    }

    public void reloadTextures() {
        this.reloadTextures = true;
    }

    @Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

        // move the camera out 5
        gl.glTranslatef(0.0f, 0.0f, -5.0f);

        if(elevatorDoorsAnimating){
            float doorBoundary = 0.0f;
            long animationDuration = 0;
            long now = System.currentTimeMillis();
            long timePlaying = now - (elevatorPausedMillis + elevatorOpenStartTime);

            // show elevator interior
            if (showElevatorInterior) {
                if (WallpaperPreferences.playElevatorMusic(context)) {
                    if (mMediaPlayer == null) {
                        mMediaPlayer = MediaPlayer.create(context, elevatorInteriorAudioClips[currentElevatorInteriorAudioClip]);

                        // seek to the right part of the song (user may have enabled music after doors were already open
                        mMediaPlayer.seekTo((int)timePlaying);
                        mMediaPlayer.start();
                        if (currentElevatorInteriorAudioClip < elevatorInteriorAudioClips.length - 1) {
                            currentElevatorInteriorAudioClip++;
                        } else {
                            currentElevatorInteriorAudioClip = 0;
                        }
                    }
                }

                doorBoundary = ELEVATOR_INTERIOR_DOOR_BOUNDARY;
                animationDuration = ELEVATOR_INTERIOR_ANIMATION_DURATION;
                final boolean showNewElevatorInteriorFrame = System.currentTimeMillis() - timeOfLastElevatorInteriorFrame >= (1000/58);
                if (lastElevatorInteriorFrame < elevatorInterior.getTextures().length - 1) {
                    if (showNewElevatorInteriorFrame) {
                        lastElevatorInteriorFrame++;
                    }
                } else {
                    if (showNewElevatorInteriorFrame) {

                        // reset the frame to first one
                        lastElevatorInteriorFrame = 0;
                    }
                }
                if (showNewElevatorInteriorFrame) {
                    timeOfLastElevatorInteriorFrame = System.currentTimeMillis();
                }

                drawElevatorInteriorFrame(gl, lastElevatorInteriorFrame);

            }  else if (showDanceInterior) {
                if (WallpaperPreferences.playElevatorMusic(context) && !hasNewTextMessage) {
                    if (mMediaPlayer == null) {
                        mMediaPlayer = MediaPlayer.create(context, R.raw.dance);

                        // seek to the right part of the song (user may have enabled music after doors were already open
                        mMediaPlayer.seekTo((int)timePlaying);
                        mMediaPlayer.start();
                    }
                }
                doorBoundary = DANCE_INTERIOR_DOOR_BOUNDARY;
                animationDuration = DANCE_INTERIOR_ANIMATION_DURATION;
                final boolean showNewDanceInteriorFrame = System.currentTimeMillis() - timeOfLastDanceInteriorFrame >= 300;
                if (lastDanceInteriorFrame < danceInterior.getTextures().length - 1) {
                    if (showNewDanceInteriorFrame) {
                        lastDanceInteriorFrame++;
                    }
                } else {
                    if (showNewDanceInteriorFrame) {

                        // reset the frame to first one
                        lastDanceInteriorFrame = 0;
                    }
                }
                if (showNewDanceInteriorFrame) {
                    timeOfLastDanceInteriorFrame = System.currentTimeMillis();
                }

                drawDanceInteriorFrame(gl, lastDanceInteriorFrame);

                final boolean showNewDanceFrame = System.currentTimeMillis() - timeOfLastDanceFrame >= (1000/10);
                if (lastDanceFrame < dance.getTextures().length - 1) {
                    if (showNewDanceFrame) {
                        lastDanceFrame++;
                    }
                } else {
                    if (showNewDanceFrame) {

                        // reset the frame to first one
                        lastDanceFrame = 0;
                    }
                }
                if (showNewDanceFrame) {
                    timeOfLastDanceFrame = System.currentTimeMillis();
                }

                drawDanceFrame(gl, lastDanceFrame);

                if (hasNewTextMessage) {
                    drawSmsBadge(gl);
                }
            }

            drawDoors(gl);

            // the doors are open if they are at their boundaries
            boolean isDoorsOpen = leftDoorX <= -doorBoundary && rightDoorX >= doorBoundary;

            // the doors are closed if they aren't currently closing and they aren't at 0
            boolean isDoorsClosed = isDoorsClosing && leftDoorX >= 0 && rightDoorX <= 0;

            // if doors are closed, reset all state
            if (isDoorsClosed) {
                leftDoorX = 0;
                rightDoorX = 0;
                elevatorDoorsAnimating = false;
                showElevatorInterior = false;
                showDanceInterior = false;
                hasNewTextMessage = false;
                elevatorPauseTime = 0;
                elevatorResumeTime = 0;
                elevatorPausedMillis = 0;

                // if media player is running stop it
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                    mMediaPlayer = null;
                }
                isDoorsClosing = Boolean.FALSE;
            }

            if (isDoorsOpen) {
                boolean shouldCloseDoors = timePlaying >= animationDuration;
                if (shouldCloseDoors) {
                    isDoorsClosing = Boolean.TRUE;
                }
            }

            // if doors are closing and aren't fully closed, move them slightly inward to animate them closed
            if (isDoorsClosing && !isDoorsClosed) {
                leftDoorX = leftDoorX + 0.02f;
                rightDoorX = rightDoorX - 0.02f;

            // if the doors aren't closing and they aren't fully open, move them slightly outward to animate them open
            } else if (!isDoorsOpen) {
                leftDoorX = leftDoorX - 0.02f;
                rightDoorX = rightDoorX + 0.02f;
            }

        } else {
            leftDoorX = 0;
            rightDoorX = 0;
            drawDoors(gl);
        }
	}

    private void drawElevatorInteriorFrame(GL10 gl, int lastElevatorInteriorFrame) {
        elevatorInterior.draw(gl, lastElevatorInteriorFrame);
    }

    private void drawDanceInteriorFrame(GL10 gl, int lastDanceInteriorFrame) {
        danceInterior.draw(gl, lastDanceInteriorFrame);
    }

    private void drawDanceFrame(GL10 gl, int lastDanceFrame) {
        if (lastDanceFrame >= 39 && lastDanceFrame < 53) {
            danceX += 0.008f;
        } else if (lastDanceFrame >= 53 && lastDanceFrame < 67) {
            danceX -= 0.008f;
        } else if (lastDanceFrame >= 67 && lastDanceFrame < 81) {
            danceX -= 0.008f;
        } else if (lastDanceFrame >= 81 && lastDanceFrame < 95) {
            danceX += 0.008f;
        } else {
            danceX = 0.0f;
        }
        gl.glTranslatef(danceX, -0.5f, 0.0f);
        dance.draw(gl, lastDanceFrame);
        gl.glTranslatef(-danceX, 0.5f, 0.0f);
    }

    private void drawDoors(GL10 gl) {
        gl.glTranslatef(leftDoorX, 0.0f, 0.0f);
        leftDoor.draw(gl);
        gl.glTranslatef(-leftDoorX, 0.0f, 0.0f);
        gl.glTranslatef(rightDoorX, 0.0f, 0.0f);
        rightDoor.draw(gl);
        gl.glTranslatef(-rightDoorX, 0.0f, 0.0f);
    }

    private void drawSmsBadge(GL10 gl) {
        gl.glTranslatef(-0.8f, 0.7f, 0.0f);
        smsBadge.draw(gl);
        gl.glTranslatef(0.8f, -0.7f, 0.0f);
    }

    @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

        if (reloadTextures) {
            initializeTextures(gl);
        }

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
    }

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        initializeTextures(gl);
	}

    private void initializeTextures(GL10 gl) {
        final int doorColorFilter = WallpaperPreferences.getDoorColorFilter(this.context);
        final boolean isLandscape = isLandscape();
        elevatorInterior.initializeTextures(gl, this.context);
        danceInterior.initializeTextures(gl, this.context);
        dance.initializeTextures(gl, this.context);
        leftDoor.setLandscape(isLandscape);
        leftDoor.setColorFilter(doorColorFilter);
        leftDoor.initializeTextures(gl, this.context);
        rightDoor.setLandscape(isLandscape);
        rightDoor.setColorFilter(doorColorFilter);
        rightDoor.initializeTextures(gl, this.context);
        smsBadge.initializeTextures(gl, this.context);
        reloadTextures = false;
    }

}
