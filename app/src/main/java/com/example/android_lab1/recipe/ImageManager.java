package com.example.android_lab1.recipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings({"unused","UnusedReturnValue"})

/**
 * This class control image load
 * @ author peng
 * @ since 03.12.2019
 * @ version 1.0
 *
 */
public class ImageManager {
    public static Bitmap download(Context context, String url) {
        Bitmap image = null;
        try {
            URL imageURL = new URL(url);
            HttpURLConnection imageConnection = (HttpURLConnection) imageURL.openConnection();
            imageConnection.connect();
            int responseCode = imageConnection.getResponseCode();
            if (responseCode == 200) {
                image = BitmapFactory.decodeStream(imageConnection.getInputStream());
                Log.i("ImageManager", "Image downloaded " + url);
            }

        } catch(IOException e) {
            Log.e("ImageManager", "The image could not be downloaded  "+ url);
        }
        return image;
    }

    public static boolean save(Context context, String fileName, Bitmap image) {
        boolean saved = false;
        if(!fileName.equals("null")) {
            if (!ImageManager.imageExists(context, fileName)) {

                try {
                    FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.JPEG, 1, fos);
                    fos.flush();
                    saved = true;
                    Log.i("ImageManager", "Image " + fileName + " saved");
                } catch (IOException e) {
                    Log.e("ImageManager", "Image " + fileName + " already exists and  not saved");
                }
            }
        }
        return saved;
    }

    public static Bitmap open(Context context, String fileName) {
        Bitmap image = null;
        if(ImageManager.imageExists(context, fileName)) {
            try {
                FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), fileName));
                image = BitmapFactory.decodeStream(fis);
                Log.i("ImageManager", "Image " + fileName + " was found and loaded");
            } catch (IOException e) {
            }
        }
        return image;
    }


    public static boolean delete(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        boolean deleted = false;
        if (ImageManager.imageExists(context, fileName)) {
            deleted = file.delete();
            Log.i("ImageManager", context.getFilesDir() + fileName +" was successfully deleted");
        }
        Log.e("ImageManager", context.getFilesDir() + fileName +" was not found");
        return deleted;
    }


    public static boolean imageExists(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }
}
