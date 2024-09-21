package com.tvoyhod.hoofit.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final int PICK_IMAGE_REQUEST = 1;
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
            Log.d("FFFF", "Ноль");
        }

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    deleteImageFromPreferences(context, imageId);
                                    saveImageToPreferences(context, imageId, resource);
                                }
                            }).start();

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }).addOnFailureListener(exception -> {
            deleteImageFromPreferences(context, imageId);
        });
    }

    public static File openFileChooser(Activity activity) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");

        File photoFile = null;
        try {
            photoFile = createImageFile(activity);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(activity, "com.tvoyhod.hoofit.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }

        Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        activity.startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        return photoFile;
    }


    private static File createImageFile(Activity activity) throws IOException {
        // Создаем имя файла изображения
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //        currentPhotoPath = imageFile.getAbsolutePath(); // Сохраняем путь к файлу
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
