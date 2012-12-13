package com.ignition.apps.gangnam;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.*;

public class GangnamSurfaceView extends GLSurfaceView {

    private GangnamRenderer mGlRenderer;

    public GangnamSurfaceView(Context context) {
        super(context);
        mGlRenderer = new GangnamRenderer(context);
        setRenderer(mGlRenderer);
    }

    public GangnamRenderer getRenderer() {
        return mGlRenderer;
    }

}
