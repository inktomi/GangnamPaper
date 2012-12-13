package com.ignition.apps.gangnam;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.*;

public class GangnamSurfaceView extends GLSurfaceView {

    private GangnamRenderer mGlRenderer;

    public GangnamSurfaceView(Context context) {
        super(context);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        boolean isLandscape = rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270;
        mGlRenderer = new GangnamRenderer(context, isLandscape);

        setRenderer(mGlRenderer);
    }

    public GangnamRenderer getRenderer() {
        return mGlRenderer;
    }

    private class GangnamGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mGlRenderer.setAnimating(Boolean.TRUE);
            return Boolean.TRUE;
        }
    }
}
