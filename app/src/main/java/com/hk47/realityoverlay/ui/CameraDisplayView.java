package com.hk47.realityoverlay.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraDisplayView extends SurfaceView implements SurfaceHolder.Callback {

    float mVerticalFOV;
    float mHorizontalFOV;

    SurfaceHolder mHolder;
    Activity mActivity;
    Camera mCamera;

    public CameraDisplayView(Context context) {
        super(context);
    }

    public CameraDisplayView(Context context, Activity activity) {
        super(context);

        mActivity = activity;
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        mCamera.setDisplayOrientation((info.orientation - degrees + 360) % 360);

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();

        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        // Get the camera's field of vision
        mVerticalFOV = params.getVerticalViewAngle();
        mHorizontalFOV = params.getHorizontalViewAngle();

        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public float getVerticalFOV() {
        return mVerticalFOV;
    }

    public float getHorizontalFOV() {
        return mHorizontalFOV;
    }
}
