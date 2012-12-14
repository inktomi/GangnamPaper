package com.ignition.apps.gangnam.utils;

import android.graphics.*;

public class BitmapUtils {

    public static Bitmap applyColorFilter(Bitmap bitmap, int color) {
        Paint cmPaint = new Paint();
        cmPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        Bitmap filteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(filteredBitmap);
        cv.drawBitmap(bitmap, 0, 0, cmPaint);
        bitmap.recycle();
        return filteredBitmap;
    }
}
