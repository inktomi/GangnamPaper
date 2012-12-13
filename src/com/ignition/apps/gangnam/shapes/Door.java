package com.ignition.apps.gangnam.shapes;

public abstract class Door extends Shape {

    private boolean isLandscape;

    public boolean isLandscape() {
        return isLandscape;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }
}
