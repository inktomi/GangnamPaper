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
    private static final long ELEVATOR_OPEN_ANIMATION_DURATION = 4000;
    private static final long NEW_MESSAGE_ANIMATION_DURATION = 10000;

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

    // barn interior
    private BarnInterior barnInterior;

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
        this.barnInterior = new BarnInterior();

        // initialize the sms badge
        this.smsBadge = new Badge();
	}

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    public void showElevatorInterior() {
        elevatorDoorsAnimating = true;
        showElevatorInterior = true;
        elevatorOpenStartTime = System.currentTimeMillis();
    }

    public boolean isElevatorDoorsAnimating() {
        return elevatorDoorsAnimating;
    }

    public void newTextMessage() {
        shouldDrawSmsBadge = Boolean.TRUE;

        showElevatorInterior();
    }

    @Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

        if(elevatorDoorsAnimating){
            if (mMediaPlayer == null && showElevatorInterior) {
                mMediaPlayer = MediaPlayer.create(context, elevatorInteriorAudioClips[currentElevatorInteriorAudioClip]);
                mMediaPlayer.start();
                if (currentElevatorInteriorAudioClip < elevatorInteriorAudioClips.length - 1) {
                    currentElevatorInteriorAudioClip++;
                } else {
                    currentElevatorInteriorAudioClip = 0;
                }
            }

            // Drawing
            gl.glTranslatef(0.0f, 0.0f, -5.0f);

            // show elevator interior
            if (showElevatorInterior) {
                if (lastElevatorInteriorFrame < elevatorInterior.getTextures().length - 1) {
                    if (System.currentTimeMillis() - timeOfLastElevatorInteriorFrame >= (1000/58)) {
                        lastElevatorInteriorFrame++;
                        timeOfLastElevatorInteriorFrame = System.currentTimeMillis();
                    }
                } else {
                    lastElevatorInteriorFrame = 0;
                }

                elevatorInterior.draw(gl, lastElevatorInteriorFrame);

                // Draw the badges on top of the elevator inside
                smsBadge.draw(gl);
            }

            gl.glTranslatef(0.0f, 0.0f, 0.0f);
            gl.glTranslatef(leftDoorX, 0.0f, 0.0f);

            leftDoor.draw(gl);

            gl.glTranslatef(-leftDoorX, 0.0f, 0.0f);
            gl.glTranslatef(rightDoorX, 0.0f, 0.0f);

            rightDoor.draw(gl);

            boolean isOpen = leftDoorX <= -1.0 && rightDoorX >= 1.0;
            boolean isClosed = isClosing && leftDoorX >= 0 && rightDoorX <= 0;
            if(isClosed) {
                leftDoorX = 0;
                rightDoorX = 0;
                elevatorDoorsAnimating = Boolean.FALSE;
                showElevatorInterior = false;
                isClosing = Boolean.FALSE;
            }

            if(isOpen) {
                boolean shouldClose = System.currentTimeMillis() - elevatorOpenStartTime >= ELEVATOR_OPEN_ANIMATION_DURATION;
                if (shouldClose) {
                    isClosing = Boolean.TRUE;
                }
            }

            if(isClosing && !isClosed) {
                leftDoorX = leftDoorX + 0.01f;
                rightDoorX = rightDoorX - 0.01f;
            } else if (!isOpen) {
                leftDoorX = leftDoorX - 0.01f;
                rightDoorX = rightDoorX + 0.01f;
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
