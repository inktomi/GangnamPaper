package com.ignition.apps.gangnam;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GangnamActivity extends Activity {

    private GestureDetector mGestureDetector;

    /** The OpenGL view */
	private GangnamSurfaceView mSurfaceView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initiate the Open GL view and
        // create an instance with this activity
        mSurfaceView = new GangnamSurfaceView(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTapEvent(MotionEvent e) {
                if (!mSurfaceView.getRenderer().isElevatorDoorsAnimating()) {
                    mSurfaceView.getRenderer().openElevator();
                }

                return super.onDoubleTapEvent(e);
            }
        });

        setContentView(mSurfaceView);
    }

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mSurfaceView.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mSurfaceView.onPause();
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return mGestureDetector.onTouchEvent(event);
    }

}