package com.ignition.apps.gangnam;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
	
	/** The OpenGL view */
	private GangnamSurfaceView glSurfaceView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initiate the Open GL view and
        // create an instance with this activity
        glSurfaceView = new GangnamSurfaceView(this);

        setContentView(glSurfaceView);
    }

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

}