package com.ignition.apps.gangnam.shapes;

import android.content.Context;
import android.graphics.*;
import com.ignition.apps.gangnam.utils.BitmapUtils;

public abstract class Door extends Shape {

    private boolean isLandscape;
    private int colorFilter;

    public boolean isLandscape() {
        return isLandscape;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }

    public void setColorFilter(int colorFilter) {
        this.colorFilter = colorFilter;
    }

    @Override
    protected Bitmap modifyTexture(Context context, Bitmap texture) {
        if (colorFilter != 0) {
            return BitmapUtils.applyColorFilter(texture, colorFilter);
        } else {
            return texture;
        }
    }

}
