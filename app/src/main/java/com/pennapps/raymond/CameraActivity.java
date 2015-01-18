package com.pennapps.raymond;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 */
public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
        type = getIntent().getStringExtra("Category");
        ((TextView)findViewById(R.id.gallerybutton)).setText("Check " + type +" and Current Uploads");
    }

    public void openGallery(View view){
        Intent intent = new Intent(this, Gallery.class);
        intent.putExtra("Category",type);
        startActivity(intent);
    }

    public void savePicture(View view){
        mCamera.takePicture(null, null,
                new PictureHandler(getApplicationContext(), type));
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(checkCameraHardware(this)) {
            // Create an instance of Camera
            mCamera = getCameraInstance();
            Camera.Parameters params = mCamera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.fullscreen_content);
            preview.addView(mPreview);

        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    @Override
    protected void onPause() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        if(checkCameraHardware(this)&& mCamera==null ){
            mCamera = getCameraInstance();
            Camera.Parameters params = mCamera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(params);
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.fullscreen_content);
            preview.addView(mPreview);
        }
        super.onResume();
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        private static final String TAG = "CameraPreview";

        private Context mContext;
        private SurfaceHolder mHolder;
        private Camera mCamera;
        private List<Camera.Size> mSupportedPreviewSizes;
        private Camera.Size mPreviewSize;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mContext = context;
            mCamera = camera;
            setFocusable(true);
            setFocusableInTouchMode(true);
            // supported preview sizes
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            for(Camera.Size str: mSupportedPreviewSizes)
                Log.e(TAG, str.width + "/" + str.height);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public boolean onTouchEvent(MotionEvent event){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("down", "focusing now");
                mCamera.cancelAutoFocus();
                Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);

                Camera.Parameters parameters = mCamera.getParameters();
                if (parameters.getFocusMode() != Camera.Parameters.FOCUS_MODE_AUTO) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                mylist.add(new Camera.Area(new Rect(-1000, -1000, 1000, 0), 750));
                parameters.setFocusAreas(mylist);

                try {
                    mCamera.cancelAutoFocus();
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (!camera.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                                Camera.Parameters parameters = camera.getParameters();
                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                                parameters.setFocusAreas(null);
                                camera.setParameters(parameters);
                                camera.startPreview();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        private Rect calculateTapArea(float x, float y, float coefficient) {
            int areaSize = Float.valueOf(72 * coefficient).intValue();

            int left = clamp((int) x - areaSize / 2, 0, getWidth() - areaSize);
            int top = clamp((int) y - areaSize / 2, 0, getHeight() - areaSize);

            RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

            return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
        }
        private int clamp(int x, int min, int max) {
            if (x > max) {
                return max;
            }
            if (x < min) {
                return min;
            }
            return x;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // empty. surfaceChanged will take care of stuff
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.e(TAG, "surfaceChanged => w=" + w + ", h=" + h);
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or reformatting changes here
            // start preview with new settings
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                mCamera.setParameters(parameters);
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

            if (mSupportedPreviewSizes != null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            }

            float ratio;
            if(mPreviewSize.height >= mPreviewSize.width)
                ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
            else
                ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

            // One of these methods should be used, second method squishes preview slightly
            setMeasuredDimension(width, (int) (width * ratio));
//        setMeasuredDimension((int) (width * ratio), height);
        }


        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) h / w;

            if (sizes == null)
                return null;

            Camera.Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            for (Camera.Size size : sizes) {
                double ratio = (double) size.height / size.width;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;

                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }

            return optimalSize;
        }
    }


}
