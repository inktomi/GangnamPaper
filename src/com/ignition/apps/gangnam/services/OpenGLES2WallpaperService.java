package com.ignition.apps.gangnam.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;
import com.ignition.apps.gangnam.renderer.GangnamRenderer;

public abstract class OpenGLES2WallpaperService extends GLWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new OpenGLES2Engine();
    }

    abstract GLSurfaceView.Renderer getNewRenderer();

    class OpenGLES2Engine extends GLWallpaperService.GLEngine {

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // Check if the system supports OpenGL ES 2.0.
            final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            if (supportsEs2) {
                // Request an OpenGL ES 2.0 compatible context.
                setEGLContextClientVersion(2);

                // Set the renderer to our user-defined renderer.
                setRenderer(getNewRenderer());
            } else {
                // This is where you could create an OpenGL ES 1.x compatible
                // renderer if you wanted to support both ES 1 and ES 2.
                return;
            }
        }
    }

    public class GangnamWallpaperService extends OpenGLES2WallpaperService {
        @Override
        GLSurfaceView.Renderer getNewRenderer() {
            return new GangnamRenderer();
        }
    }
}