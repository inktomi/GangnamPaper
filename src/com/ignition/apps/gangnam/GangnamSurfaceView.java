package com.ignition.apps.gangnam;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.*;

public class GangnamSurfaceView extends GLSurfaceView {

    private GestureDetector mGestureDetector;
    private GangnamRenderer mGlRenderer;

    public GangnamSurfaceView(Context context) {
        super(context);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        boolean isLandscape = rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270;
        mGlRenderer = new GangnamRenderer(context, isLandscape);

        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                if (!mGlRenderer.isAnimating()) {
                    mGlRenderer.setAnimating(Boolean.TRUE);
                }
            }
        });

        setRenderer(mGlRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    private class GangnamGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mGlRenderer.setAnimating(Boolean.TRUE);
            return Boolean.TRUE;
        }
    }
}
