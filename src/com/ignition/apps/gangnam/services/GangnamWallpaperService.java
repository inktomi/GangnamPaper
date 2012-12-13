package com.ignition.apps.gangnam.services;

import android.opengl.GLSurfaceView;

public class GangnamWallpaperService extends OpenGLES2WallpaperService {
    @Override
    GLSurfaceView.Renderer getNewRenderer() {
//        return new GangnamRenderer();
        return null;
    }
}