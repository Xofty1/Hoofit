package com.example.hoofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static void saveImageToPreferences(Context context, String key, Bitmap bitmap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("images", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        editor.putString(key, encodedImage);
        editor.apply();
    }

    public static Bitmap getImageFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("images", Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString(key, null);
        if (encodedImage != null) {
            byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return null;
    }
}
