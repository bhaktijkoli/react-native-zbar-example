package com.reactnativezbar.zbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.reactnativezbar.R;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();
    }

    protected  void initialize() {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        Camera.Parameters mParams = mCamera.getParameters();
        mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(mParams);
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        mCamera.setPreviewCallback(new CameraEvent());
        mFrameLayout = (FrameLayout)findViewById(R.id.mFrameLayout);
        mFrameLayout.addView(mPreview);

    }
}