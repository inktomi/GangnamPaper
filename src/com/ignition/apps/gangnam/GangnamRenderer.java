/**
 * 
 */
package com.ignition.apps.gangnam;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import com.ignition.apps.gangnam.shapes.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author impaler
 *
 */
public class GangnamRenderer implements Renderer {

    private static final String TAG = GangnamRenderer.class.getName();
    private static final long ELEVATOR_INTERIOR_ANIMATION_DURATION = 4000;
    private static final long DANCE_INTERIOR_ANIMATION_DURATION = 10000;
    private static final float ELEVATOR_INTERIOR_DOOR_BOUNDARY = 1.0f;
    private static final float DANCE_INTERIOR_DOOR_BOUNDARY = 2.0f;

    private Context context;
    private MediaPlayer mMediaPlayer;

    private boolean isClosing = Boolean.FALSE;
    private boolean elevatorDoorsAnimating = Boolean.FALSE;
    private long elevatorOpenStartTime;

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
    private boolean shouldDrawSmsBadge;

    // dance interior
    private DanceInterior danceInterior;
    private boolean showDanceInterior;
    private int lastDanceInteriorFrame;
    private long timeOfLastDanceInteriorFrame;

    private boolean hasNewTextMessage;

    private int[] elevatorInteriorAudioClips = {
            R.raw.arun1,
            R.raw.arun2,
            R.raw.arun3,
            R.raw.arun4
    };

	/** Constructor to set the handed over context */
	public GangnamRenderer(Context context) {
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
	}

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    public void showElevatorInterior() {

        // only animate if not already animating
        if (!isElevatorDoorsAnimating()) {
            animateElevatorDoorsOpen();
        }
        showElevatorInterior = true;
    }

    public void showDanceInterior() {

        // only animate if not already animating
        if (!isElevatorDoorsAnimating()) {
            animateElevatorDoorsOpen();
        }
        showDanceInterior = true;
        showElevatorInterior = false;
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

    @Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

        if(elevatorDoorsAnimating){
            float doorBoundary = 0.0f;
            long animationDuration = 0;

            // Drawing
            gl.glTranslatef(0.0f, 0.0f, -5.0f);

            // show elevator interior
            if (showElevatorInterior) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = MediaPlayer.create(context, elevatorInteriorAudioClips[currentElevatorInteriorAudioClip]);
                    mMediaPlayer.start();
                    if (currentElevatorInteriorAudioClip < elevatorInteriorAudioClips.length - 1) {
                        currentElevatorInteriorAudioClip++;
                    } else {
                        currentElevatorInteriorAudioClip = 0;
                    }
                }

                doorBoundary = ELEVATOR_INTERIOR_DOOR_BOUNDARY;
                animationDuration = ELEVATOR_INTERIOR_ANIMATION_DURATION;
                final boolean showNewFrame = System.currentTimeMillis() - timeOfLastElevatorInteriorFrame >= (1000/58);
                if (lastElevatorInteriorFrame < elevatorInterior.getTextures().length - 1) {
                    if (showNewFrame) {
                        lastElevatorInteriorFrame++;
                    }
                } else {
                    if (showNewFrame) {

                        // reset the frame to first one
                        lastElevatorInteriorFrame = 0;
                    }
                }
                if (showNewFrame) {
                    timeOfLastElevatorInteriorFrame = System.currentTimeMillis();
                }
                elevatorInterior.draw(gl, lastElevatorInteriorFrame);
            }  else if (showDanceInterior) {
                doorBoundary = DANCE_INTERIOR_DOOR_BOUNDARY;
                animationDuration = DANCE_INTERIOR_ANIMATION_DURATION;
                final boolean showNewFrame = System.currentTimeMillis() - timeOfLastDanceInteriorFrame >= 300;
                if (lastDanceInteriorFrame < danceInterior.getTextures().length - 1) {
                    if (showNewFrame) {
                        lastDanceInteriorFrame++;
                    }
                } else {
                    if (showNewFrame) {

                        // reset the frame to first one
                        lastDanceInteriorFrame = 0;
                    }
                }
                if (showNewFrame) {
                    timeOfLastDanceInteriorFrame = System.currentTimeMillis();
                }
                danceInterior.draw(gl, lastDanceInteriorFrame);

                if( hasNewTextMessage ){
                    gl.glTranslatef(-0.50f, -1.0f, 0.0f);
                    smsBadge.draw(gl);

                    // Reset.
                    gl.glTranslatef(0.50f, 1.0f, 0.0f);
                }
            }

            gl.glTranslatef(leftDoorX, 0.0f, 0.0f);

            leftDoor.draw(gl);

            gl.glTranslatef(-leftDoorX, 0.0f, 0.0f);
            gl.glTranslatef(rightDoorX, 0.0f, 0.0f);

            rightDoor.draw(gl);

            boolean isOpen = leftDoorX <= -doorBoundary && rightDoorX >= doorBoundary;
            boolean isClosed = isClosing && leftDoorX >= 0 && rightDoorX <= 0;
            if(isClosed) {
                leftDoorX = 0;
                rightDoorX = 0;
                elevatorDoorsAnimating = Boolean.FALSE;
                showElevatorInterior = false;
                showDanceInterior = false;
                hasNewTextMessage = false;
                isClosing = Boolean.FALSE;
            }

            if(isOpen) {
                boolean shouldClose = System.currentTimeMillis() - elevatorOpenStartTime >= animationDuration;
                if (shouldClose) {
                    isClosing = Boolean.TRUE;
                }
            }

            if(isClosing && !isClosed) {
                leftDoorX = leftDoorX + 0.02f;
                rightDoorX = rightDoorX - 0.02f;
            } else if (!isOpen) {
                leftDoorX = leftDoorX - 0.02f;
                rightDoorX = rightDoorX + 0.02f;
            }

        } else {
            gl.glTranslatef(0.0f, 0.0f, -5.0f);
            leftDoor.draw(gl);
            rightDoor.draw(gl);
            mMediaPlayer = null;
        }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

        final boolean isLandscape = isLandscape();

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix

        elevatorInterior.initializeTextures(gl, this.context);
        danceInterior.initializeTextures(gl, this.context);
        leftDoor.setLandscape(isLandscape);
        leftDoor.initializeTextures(gl, this.context);
        rightDoor.setLandscape(isLandscape);
        rightDoor.initializeTextures(gl, this.context);

        smsBadge.initializeTextures(gl, this.context);
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

	}

}
