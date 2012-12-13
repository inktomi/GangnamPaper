/**
 * 
 */
package com.ignition.apps.gangnam;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author impaler
 *
 */
public class GangnamRenderer implements Renderer {

    private static final String TAG = GangnamRenderer.class.getName();
    private static final long ANIMATION_DURATION = 5000;

    private Context 	context;

    private boolean isLandscape = Boolean.FALSE;
    private boolean isClosing = Boolean.FALSE;
    private boolean isAnimating = Boolean.FALSE;
    private long animationStartTime;

    // doors
    private LeftDoor leftDoor;
    private RightDoor rightDoor;
    private float leftDoorX = 0;
    private float rightDoorX = 0;
    private int leftDoorTexture = R.drawable.door_left;
    private int rightDoorTexture = R.drawable.door_right;

    // elevator interior
    private ElevatorInterior elevatorInterior;
    private int lastElevatorInteriorFrame;
    private long timeOfLastElevatorInteriorFrame;
    private int[] elevatorInteriorFrames = new int[] {
            R.drawable.e42,
            R.drawable.e43,
            R.drawable.e44,
            R.drawable.e45,
            R.drawable.e46,
            R.drawable.e47,
            R.drawable.e48,
            R.drawable.e49,
            R.drawable.e50,
            R.drawable.e51,
            R.drawable.e52,
            R.drawable.e53,
            R.drawable.e54,
            R.drawable.e55,
            R.drawable.e56,
            R.drawable.e57,
            R.drawable.e58,
            R.drawable.e59,
            R.drawable.e60,
            R.drawable.e61,
            R.drawable.e62,
            R.drawable.e63,
            R.drawable.e64,
            R.drawable.e65,
            R.drawable.e66,
            R.drawable.e67,
            R.drawable.e68,
            R.drawable.e69,
            R.drawable.e70,
            R.drawable.e71,
            R.drawable.e72,
            R.drawable.e73,
            R.drawable.e74,
            R.drawable.e75,
            R.drawable.e76,
            R.drawable.e77,
            R.drawable.e78,
            R.drawable.e79,
            R.drawable.e80,
            R.drawable.e81,
            R.drawable.e82,
            R.drawable.e83,
            R.drawable.e84,
            R.drawable.e85,
            R.drawable.e86,
            R.drawable.e87,
            R.drawable.e88,
            R.drawable.e89,
            R.drawable.e90,
            R.drawable.e91,
            R.drawable.e92,
            R.drawable.e93,
            R.drawable.e94,
            R.drawable.e95,
            R.drawable.e96,
            R.drawable.e97,
            R.drawable.e98,
            R.drawable.e99,
    };

	/** Constructor to set the handed over context */
	public GangnamRenderer(Context context, boolean isLandscape) {
		this.context = context;
		
		// initialise the elevator interior
		this.elevatorInterior = new ElevatorInterior();

        // initialise the left door
        this.leftDoor = new LeftDoor();
        this.leftDoor.setLandscape(isLandscape);

        // initialise the right door
        this.rightDoor = new RightDoor();
        this.rightDoor.setLandscape(isLandscape);
	}

    public void setAnimating(boolean animating) {
        isAnimating = animating;
        animationStartTime = System.currentTimeMillis();
    }

    @Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

        if( isAnimating ){
            // Drawing
            gl.glTranslatef(0.0f, 0.0f, -5.0f);

            if (lastElevatorInteriorFrame < elevatorInteriorFrames.length - 1) {
                if (System.currentTimeMillis() - timeOfLastElevatorInteriorFrame >= (1000/58)) {
                    lastElevatorInteriorFrame++;
                    timeOfLastElevatorInteriorFrame = System.currentTimeMillis();
                }
            } else {
                lastElevatorInteriorFrame = 0;
            }

            elevatorInterior.draw(gl, lastElevatorInteriorFrame);

            // We want the doors to be "closer" if we're in landscape mode.
            if( isLandscape ){
                gl.glTranslatef(0.0f, 0.0f, 1.0f);
            } else {
                gl.glTranslatef(0.0f, 0.0f, 1.9f);
            }

            gl.glTranslatef(leftDoorX, 0.0f, 0.0f);

            leftDoor.draw(gl);

            gl.glTranslatef(-leftDoorX, 0.0f, 0.0f);
            gl.glTranslatef(rightDoorX, 0.0f, 0.0f);

            rightDoor.draw(gl);

            boolean isOpen = leftDoorX <= -0.55 && rightDoorX >= 0.55;
            boolean isClosed = leftDoorX >= 0 && rightDoorX <= 0;
            if(isClosed) {
                leftDoorX = 0;
                rightDoorX = 0;
                isAnimating = Boolean.FALSE;
                isClosing = Boolean.FALSE;
            }

            if(isOpen) {
                boolean shouldClose = System.currentTimeMillis() - animationStartTime >= ANIMATION_DURATION;
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
            // We want the doors to be "closer" if we're in landscape mode.
            if( isLandscape ){
                gl.glTranslatef(0.0f, 0.0f, -4.0f);
            } else {
                gl.glTranslatef(0.0f, 0.0f, -3.1f);
            }

            leftDoor.draw(gl);
            rightDoor.draw(gl);
        }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
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

		elevatorInterior.loadGLTextures(gl, this.context, elevatorInteriorFrames);
        leftDoor.loadGLTexture(gl, this.context, leftDoorTexture);
        rightDoor.loadGLTexture(gl, this.context, rightDoorTexture);
		
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
