package com.ignition.apps.gangnam.services;

import android.content.*;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import com.ignition.apps.gangnam.GangnamRenderer;
import com.ignition.apps.gangnam.WallpaperPreferences;

public class GangnamWallpaperService extends WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = GangnamWallpaperService.class.getName();

    private static GLEngine sEngine;
    private BroadcastReceiver mBroadcastReceiever;

    @Override
    public void onCreate() {
        super.onCreate();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionReceiver.SMS_ACTION);

        mBroadcastReceiever = new ActionReceiver();

        this.registerReceiver(mBroadcastReceiever, intentFilter);
        WallpaperPreferences.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Engine onCreateEngine() {
        sEngine = new GLEngine();

        return sEngine;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(mBroadcastReceiever);
        WallpaperPreferences.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(WallpaperPreferences.COLOR_FILTER, key)) {
            Log.d(TAG, "new color filter: " + WallpaperPreferences.getSharedPreferences(this).getInt(WallpaperPreferences.COLOR_FILTER, 0x00000000));

            // not sure if this is the best way to refresh or not, but it works :D
            onCreateEngine();
        }
    }

    private static class ActionReceiver extends BroadcastReceiver {
        private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_ACTION)) {
                ((GangnamRenderer) sEngine.getRenderer()).newTextMessage();
            }
        }
    }

    public class GLEngine extends Engine {
        private final String TAG = GLEngine.class.getName();

        private ScaleGestureDetector mScaleGestureDetector;
        private GestureDetector mGestureDetector;
        private WallpaperGLSurfaceView glSurfaceView;

        private GangnamRenderer mRenderer;

        private boolean rendererHasBeenSet;

        public GLEngine() {
            this.glSurfaceView = new WallpaperGLSurfaceView(getApplicationContext());
        }

        public GLSurfaceView.Renderer getRenderer() {
            return mRenderer;
        }

        public WallpaperGLSurfaceView getSurfaceView() {
            return this.glSurfaceView;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            mScaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    if( detector.getScaleFactor() >= 1.0 ){
                        if (!mRenderer.isElevatorDoorsAnimating()) {
                            mRenderer.showDanceInterior();
                            return true;
                        }
                    }

                    return false;
                }
            });

            mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    if (!mRenderer.isElevatorDoorsAnimating()) {
                        mRenderer.showElevatorInterior();
                    }

                    return super.onDoubleTapEvent(e);
                }
            });
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    if (!isPreview()) {
                        glSurfaceView.onPause();
                    }
                }

                if( isPreview() ){
                    mRenderer.showDanceInterior();
                }
            }
        }

        protected void setEGLContextClientVersion(int version) {
            glSurfaceView.setEGLContextClientVersion(version);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            glSurfaceView.onDestroy();
        }

        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {
                super(context);
                mRenderer = new GangnamRenderer(context);
                setRenderer(mRenderer);
                rendererHasBeenSet = true;
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }
    }
}
