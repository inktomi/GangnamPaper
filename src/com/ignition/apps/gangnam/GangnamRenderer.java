package com.ignition.apps.gangnam;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GangnamRenderer implements GLWallpaperService.Renderer {
    public void onDrawFrame(GL10 gl) {
        // Your rendering code goes here

        gl.glClearColor(0.2f, 0.4f, 0.2f, 1f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    /**
     * Called when the engine is destroyed. Do any necessary clean up because
     * at this point your renderer instance is now done for.
     */
    public void release() {
    }

}
