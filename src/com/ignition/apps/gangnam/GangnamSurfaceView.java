package com.ignition.apps.gangnam;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GangnamSurfaceView extends GLSurfaceView {

    private GestureDetector mGestureDetector;
    private GlRenderer mGlRenderer;

    public GangnamSurfaceView(Context context) {
        super(context);

        mGlRenderer = new GlRenderer(context);

        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                mGlRenderer.setAnimating(Boolean.TRUE);
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
