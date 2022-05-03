package com.reactnativezbar.zbar;

import android.hardware.Camera;
import android.util.Log;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.nio.charset.StandardCharsets;

public class CameraEvent implements Camera.PreviewCallback {
    private final String TAG = "CameraEvent";

    private ImageScanner mScammer;

    public CameraEvent() {
        mScammer = new ImageScanner();
        mScammer.setConfig(0, Config.X_DENSITY, 3);
        mScammer.setConfig(0, Config.Y_DENSITY, 3);
//        mScammer.setConfig(0, Config.ENABLE, 0);
        mScammer.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        Log.d(TAG, "Created Scanner");
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Parameters mParams = camera.getParameters();
        Camera.Size mSize = mParams.getPictureSize();
        int width = mSize.width;
        int height = mSize.height;

        Image mImage = new Image(width, height, "Y800");
        mImage.setData(bytes);
        int result = mScammer.scanImage(mImage);
        String resultString = "";
        if (result != 0 || result != -1) {
            SymbolSet symbolSet = mScammer.getResults();
            for (Symbol symbol : symbolSet) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    resultString = new String(symbol.getDataBytes(), StandardCharsets.UTF_8);
                } else {
                    resultString = symbol.getData();
                }
                Log.d(TAG, "Result: " + resultString);
            }
        } else {
            Log.d(TAG, "Result: Not Found");
        }
    }
}
