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
public class GlRenderer implements Renderer {

	private Frame frame;		// the frame
    private LeftDoor leftDoor;  // The Left Door
    private RightDoor rightDoor; // The right door

	private Context 	context;

    private boolean isAnimating = Boolean.FALSE;

    private int[] frames = new int[] {
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

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }

    private int leftDoorTexture = R.drawable.door_left;
    private int rightDoorTexture = R.drawable.door_right;

    private float ldx = 0;
    private float rdx = 0;
	
	/** Constructor to set the handed over context */
	public GlRenderer(Context context) {
		this.context = context;
		
		// initialise the frame
		this.frame = new Frame();

        // initialise the left door
        this.leftDoor = new LeftDoor();
        this.rightDoor = new RightDoor();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

		// Drawing
		gl.glTranslatef(0.0f, 0.0f, -5.0f);		// move 5 units INTO the screen
												// is the same as moving the camera 5 units away
//		gl.glScalef(0.5f, 0.5f, 0.5f);			// scale the frame to 50%
												// otherwise it will be too large
		frame.draw(gl);						// Draw the triangle

        gl.glTranslatef(0.0f, 0.0f, 2.0f);		// move 5 units INTO the screen


        if( isAnimating ){
            gl.glTranslatef(ldx, 0.0f, 0.0f);

            leftDoor.draw(gl);

            gl.glTranslatef(0.0f, 0.0f, 0.0f);
            gl.glTranslatef(rdx, 0.0f, 0.0f);

            rightDoor.draw(gl);

            ldx = ldx - 0.1f;
            rdx = rdx + 0.1f;

            if( ldx > 2 && rdx > 2 ){
                isAnimating = Boolean.FALSE;
            }
        } else {
            leftDoor.draw(gl);
            rightDoor.draw(gl);
        }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
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
		// Load the texture for the frame
		frame.loadGLTexture(gl, this.context, frames[0]);
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
