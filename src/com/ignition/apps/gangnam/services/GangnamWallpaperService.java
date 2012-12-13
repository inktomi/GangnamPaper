package com.ignition.apps.gangnam.services;

public class GangnamWallpaperService extends GLWallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }
}