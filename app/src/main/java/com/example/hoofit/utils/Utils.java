package com.example.hoofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.StorageReference;

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
    public static void deleteImageFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("images", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void loadImage(Context context, String imageId, ImageView imageView, StorageReference imageRef) {
        Bitmap savedImage = getImageFromPreferences(context, imageId);

        if (savedImage != null) {
            imageView.setImageBitmap(savedImage);
        }

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            deleteImageFromPreferences(context, imageId);
                            saveImageToPreferences(context, imageId, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // This is called when the View is cleared.
                            // Make sure to remove any references to the bitmap here.
                        }
                    });
        }).addOnFailureListener(exception -> {
            deleteImageFromPreferences(context, imageId);
        });
    }
}
