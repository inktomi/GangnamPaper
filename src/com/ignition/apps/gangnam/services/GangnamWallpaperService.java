package com.ignition.apps.gangnam.services;

import android.opengl.GLSurfaceView;
import com.ignition.apps.gangnam.renderer.GangnamRenderer;

public class GangnamWallpaperService extends OpenGLES2WallpaperService {
    @Override
    GLSurfaceView.Renderer getNewRenderer() {
        return new GangnamRenderer();
    }
}