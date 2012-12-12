package com.ignition.apps.gangnam;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

public class GangnamWallpaperService  extends GLWallpaperService {
    public GangnamWallpaperService() {
        super();
    }

    public Engine onCreateEngine() {
        MyEngine engine = new MyEngine();
        return engine;
    }

    class MyEngine extends GLEngine {
        GangnamRenderer renderer;
        public MyEngine() {
            super();
            // handle prefs, other initialization
            renderer = new GangnamRenderer();
            setRenderer(renderer);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }

        public void onDestroy() {
            super.onDestroy();
            if (renderer != null) {
                renderer.release();
            }
            renderer = null;
        }
    }
}