package com.example.hoofit.ui.editInfo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentEditReserveBinding;
import com.example.hoofit.ui.ReserveFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditReserveFragment extends Fragment {
    FragmentEditReserveBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    Reserve reserve = null;
    boolean isDeletedCurrentImage = false;
    private StorageReference storageReference;
    boolean isNewReserve = false;
    String currentPhotoPath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditReserveBinding.inflate(getLayoutInflater());
        storageReference = FirebaseStorage.getInstance().getReference();
        Bundle bundle = getArguments();

        if (bundle != null) {
            binding.deleteButton.setVisibility(View.VISIBLE);
            reserve = (Reserve) bundle.getSerializable("reserve");
            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

            binding.editTextDescription.setText(reserve.getDescription());
            binding.editTextName.setText(reserve.getName());

        } else {
            reserve = new Reserve();
            isNewReserve = true;
        }

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
                if (isNewReserve) {
                    String id = reservesRef.push().getKey();
                    reserve.setId(id);
                    HoofitApp.reserves.getReserves().add(reserve);
                }


                String name = binding.editTextName.getText().toString();
                String description = binding.editTextDescription.getText().toString();
                reserve.setDescription(description);
                reserve.setName(name);
                reservesRef.child(reserve.getId()).setValue(reserve).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateData();
                    }
                });


            }
        });
        binding.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = null;
                isDeletedCurrentImage = true;
                binding.deleteImageButton.setVisibility(View.GONE);
                binding.imageView.setImageResource(R.drawable.logo);
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
                reservesRef.child(reserve.getId()).removeValue();
                HoofitApp.reserves.getReserves().remove(reserve);

                for (Interesting interesting : HoofitApp.interestings) {
                    if (interesting.getReserve() != null && interesting.getReserve().equals(reserve)) {
                        Toast.makeText(getActivity(), "Найдено", Toast.LENGTH_SHORT).show();
                        DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
                        interestingRef.child(interesting.getId()).removeValue();
                        HoofitApp.interestings.remove(interesting);
                        break;
                    }
                }
                if (reserve.getTrails() != null) {
                    for (Interesting interesting : HoofitApp.interestings) {
                        for (Trail trail : reserve.getTrails()) {
                            if (interesting.getTrail() != null && interesting.getTrail().equals(trail)) {
                                Toast.makeText(getActivity(), "Найдено", Toast.LENGTH_SHORT).show();
                                DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
                                interestingRef.child(interesting.getId()).removeValue();
                                HoofitApp.interestings.remove(interesting);
                                break;
                            }
                        }

                    }
                    for (Trail trail : reserve.getTrails()) {
                        HoofitApp.allTrails.remove(trail);
                        HoofitApp.user.getLikedTrails().remove(trail);
                        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
                        users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
                    }
                }
                StorageReference ref = storageReference.child("images/" + reserve.getId());
                ref.delete();
                ReserveFragment fragment = new ReserveFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);

            }
        });

        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });
        if (reserve != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + reserve.getId());
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Загружаем изображение в ImageView
                    binding.imageView.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(uri)
                            .into(binding.imageView);
                    binding.deleteImageButton.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Обработка ошибок при загрузке изображения
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.imageView.setImageResource(R.drawable.logo);
                }
            });
        } else {
            binding.imageView.setImageResource(R.drawable.logo);
        }
        return binding.getRoot();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Разрешение на чтение внешнего хранилища не предоставлено, запрашиваем его
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            // Разрешение на чтение внешнего хранилища уже предоставлено, открываем диалог выбора файла
            openFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(getActivity(), "Доступ запрещен", Toast.LENGTH_SHORT).show();

            }
        }
    }


//    private void openFileChooser() {
//        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickPhotoIntent.setType("image/*");
//
//        // Создаем Intent для сделать фотографию
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // Создаем Intent для открытия диалога выбора изображения или фотографирования
//        Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
//
//        // Запускаем Intent для выбора изображения из галереи или сделать фотографию
//        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
//                && data != null) {
//            if (data.getData() != null) {
//                filePath = data.getData();
//                binding.imageView.setImageURI(filePath);
//                binding.saveButton.setVisibility(View.VISIBLE);
//                binding.deleteImageButton.setVisibility(View.VISIBLE);
//            } else if (data.getExtras() != null && data.getExtras().containsKey("data")) {
//                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                Uri tempUri = getImageUri(requireContext(), imageBitmap);
//                filePath = tempUri;
//                binding.imageView.setImageURI(filePath);
//                binding.saveButton.setVisibility(View.VISIBLE);
//                binding.deleteImageButton.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    // Метод для получения Uri изображения из Bitmap
//    private Uri getImageUri(Context context, Bitmap bitmap) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
//        return Uri.parse(path);
//    }
private void openFileChooser() {
    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    pickPhotoIntent.setType("image/*");

    File photoFile = null;
    try {
        photoFile = createImageFile();
    } catch (IOException ex) {
        ex.printStackTrace();
    }

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (photoFile != null) {
        Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.example.hoofit.fileprovider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
    }

    Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

    startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Обработка изображения, выбранного из галереи
                filePath = data.getData();
                binding.imageView.setImageURI(filePath);
            } else {
                // Обработка изображения, сделанного с камеры
                if (currentPhotoPath != null) {
                    File photoFile = new File(currentPhotoPath);
                    if (photoFile.exists()) {
                        filePath = Uri.fromFile(photoFile);
                        binding.imageView.setImageURI(filePath);
                    }
                }
            }
            binding.constrWrapper.setVisibility(View.VISIBLE);
            binding.deleteImageButton.setVisibility(View.VISIBLE);
        }
    }


    private File createImageFile() throws IOException {
        // Создаем имя файла изображения
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath(); // Сохраняем путь к файлу
        return imageFile;
    }
    public void updateData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        if (filePath != null) {
            showProgressDialog("Загрузка...", progressDialog);
            StorageReference interest = storageReference.child("images/" + reserve.getId());
            interest.delete();
            interest.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            handleSuccess("Загружено", progressDialog);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleFailure(e, progressDialog);
                        }
                    });
        } else if (isDeletedCurrentImage) {
            deleteImage(progressDialog);
        } else {
            navigateToReserveFragment();
        }
    }

    private void showProgressDialog(String title, ProgressDialog progressDialog) {

        progressDialog.setTitle(title);
        progressDialog.show();
    }

    private void handleSuccess(String message, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        navigateToReserveFragment();
    }

    private void handleFailure(Exception e, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        navigateToReserveFragment();
    }

    private void deleteImage(ProgressDialog progressDialog) {
        showProgressDialog("Загрузка...", progressDialog);

        if (reserve != null && reserve.getId() != null) {
            StorageReference interest = storageReference.child("images/" + reserve.getId());
            interest.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            handleSuccess("Загружено", progressDialog);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleFailure(e, progressDialog);
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Не удалось найти изображение для удаления", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToReserveFragment() {
        ReserveFragment fragment = new ReserveFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        MainActivity.makeTransaction(transaction, fragment);
    }
}