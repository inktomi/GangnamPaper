package com.ignition.apps.gangnam.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import com.ignition.apps.gangnam.GangnamRenderer;
import zh.wang.android.utils.YahooWeather4a.WeatherInfo;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherUtils;

public class GangnamWallpaperService extends WallpaperService {

    private static final String TAG = GangnamWallpaperService.class.getName();

    private static GLEngine ENGINE;
    private BroadcastReceiver mBroadcastReceiever;

    @Override
    public void onCreate() {
        super.onCreate();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionReceiver.SMS_ACTION);

        mBroadcastReceiever = new ActionReceiver();

        this.registerReceiver(mBroadcastReceiever, intentFilter);

        Handler weatherDownloader = new Handler();
        weatherDownloader.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        new WeatherDownloader().execute("89074");
                    }
                }, 5000 // every 5 seconds.
        );
    }

    @Override
    public Engine onCreateEngine() {
        ENGINE = new GLEngine();

        return ENGINE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(mBroadcastReceiever);
    }

    private static class ActionReceiver extends BroadcastReceiver {
        private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_ACTION)) {
                ((GangnamRenderer) ENGINE.getRenderer()).newTextMessage();
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

    private class WeatherDownloader extends AsyncTask<String, WeatherInfo, WeatherInfo> {
        @Override
        protected WeatherInfo doInBackground(String... zipCodes) {
            YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
            WeatherInfo weatherInfo = yahooWeatherUtils.queryYahooWeather(getApplicationContext(), zipCodes[0]);

            return weatherInfo;
        }

        @Override
        protected void onPostExecute(WeatherInfo weatherInfo) {
            super.onPostExecute(weatherInfo);

            // Update the weather panel
            ((GangnamRenderer) ENGINE.getRenderer()).updateWeatherInformation(weatherInfo.getForecast1Text());
        }
    }
}
