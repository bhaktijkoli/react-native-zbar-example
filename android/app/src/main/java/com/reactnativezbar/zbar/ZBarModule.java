package com.reactnativezbar.zbar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

public class ZBarModule extends ReactContextBaseJavaModule {
    private static final String TAG = "ZBarModule";

    static {
        System.loadLibrary("iconv");
    }

    ZBarModule(ReactApplicationContext context) {
        super(context);
    }
    @Override
    public String getName() {
        return "ZBarModule";
    }

    @ReactMethod
    public void start() {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, CameraActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @ReactMethod
    public void scanImageFromURL(String imageUrl, Promise promise) {
        Log.d(TAG, "scanImageFromURI()");
        Log.d(TAG, "scanImageFromURL imageUrl: " + imageUrl);

        // Get Image URI
        Uri imageUri = Uri.parse(imageUrl);
        // Get Image Hight and Width
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(
                    getReactApplicationContext().getContentResolver().openInputStream(imageUri),
                    null,
                    options);
        } catch (FileNotFoundException e) {
            promise.reject("Error reading image bounds", e.getMessage());
            e.printStackTrace();
        }
        // Get Image Data
        byte[] imageData = null;
        try {
            InputStream inputStream = getReactApplicationContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageData = baos.toByteArray();
        } catch (FileNotFoundException e) {
            promise.reject("Error reading image data", e.getMessage());
            e.printStackTrace();
        }

        // Create ZBar Image
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.d(TAG, "scanImageFromURL Image Options: " + "H:"  + imageHeight + ", W:" + imageWidth);
        Image image = new Image(imageWidth, imageHeight, "Y800");
        image.setData(imageData);

        // Create Scanner
        ImageScanner imageScanner = new ImageScanner();
        imageScanner.setConfig(0, Config.X_DENSITY, 3);
        imageScanner.setConfig(0, Config.Y_DENSITY, 3);
        imageScanner.setConfig(0, Config.ENABLE, 0);
        imageScanner.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        // Start Scan
        int result = imageScanner.scanImage(image);
        String resultString = "";
        if (result != 0) {
            SymbolSet symbolSet = imageScanner.getResults();
            for (Symbol symbol : symbolSet) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    resultString = new String(symbol.getDataBytes(), StandardCharsets.UTF_8);
                } else {
                    resultString = symbol.getData();
                }
                Log.d(TAG, "scanImageFromURL Result: " + resultString);
            }
        } else {
            Log.d(TAG, "scanImageFromURL Result:" + " Not Found");
        }
        promise.resolve(resultString);
    }

}