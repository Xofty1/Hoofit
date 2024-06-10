package com.example.hoofit.ui.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.databinding.FragmentEditUserBinding;
import com.example.hoofit.ui.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditUserFragment extends Fragment {
    FragmentEditUserBinding binding;
    FirebaseUser user;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private StorageReference storageReference;
    boolean isDeletedCurrentImage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(getLayoutInflater());
        binding.editTextName.setText(HoofitApp.user.getName());
        storageReference = FirebaseStorage.getInstance().getReference();
        binding.textEmail.setText("Email: " + HoofitApp.user.getEmail());
        binding.editTextUsername.setText(HoofitApp.user.getUsername());
        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });
        binding.buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.isEmpty()) {
                    Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.length() < 4) {
                    Toast.makeText(getContext(), "Логин должен быть длинее 3 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.length() >= 15) {
                    Toast.makeText(getContext(), "Логин должен быть короче 16 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.isEmpty()) {
                    changeUserPassword(user, password);
                }
                HoofitApp.user.setUsername(username);
                HoofitApp.user.setName(name);
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
                users.child(HoofitApp.user.getId()).setValue(HoofitApp.user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateData();
                    }
                });
            }
        });
        binding.editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.editTextPassword.getText().toString().isEmpty()) {
                        binding.textInputLayoutPassword.setHint("Введите пароль");
                    }
                } else {
                    binding.textInputLayoutPassword.setHint("");
                }
            }
        });
        binding.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = null;
                isDeletedCurrentImage = true;
                binding.deleteImageButton.setVisibility(View.GONE);
                binding.imageView.setImageResource(R.drawable.noname);
            }
        });


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef = storageRef.child("images/" + HoofitApp.user.getId());
        imageRef.getDownloadUrl().

    addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess (Uri uri){
            // Загружаем изображение в ImageView
            binding.imageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(uri)
                    .into(binding.imageView);
            binding.deleteImageButton.setVisibility(View.VISIBLE);
        }
    }).

    addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure (@NonNull Exception exception){
            binding.imageView.setImageResource(R.drawable.noname);

        }
    });
        return binding.getRoot();
}


    public void updateData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        if (filePath != null) {
            showProgressDialog("Загрузка...", progressDialog);
            StorageReference interest = storageReference.child("images/" + HoofitApp.user.getId());
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
            navigateToProfileFragment();
        }
    }

    private void showProgressDialog(String title, ProgressDialog progressDialog) {

        progressDialog.setTitle(title);
        progressDialog.show();
    }

    private void handleSuccess(String message, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        navigateToProfileFragment();
    }

    private void handleFailure(Exception e, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        navigateToProfileFragment();
    }

    private void deleteImage(ProgressDialog progressDialog) {
        showProgressDialog("Загрузка...", progressDialog);

        if (HoofitApp.user != null && HoofitApp.user.getId() != null) {
            StorageReference interest = storageReference.child("images/" + HoofitApp.user.getId());
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

    private void navigateToProfileFragment() {
        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        MainActivity.makeTransaction(transaction, fragment);
    }

    public void changeUserPassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });
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


    private void openFileChooser() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");

        // Создаем Intent для сделать фотографию
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Создаем Intent для открытия диалога выбора изображения или фотографирования
        Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        // Запускаем Intent для выбора изображения из галереи или сделать фотографию
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null) {
            if (data.getData() != null) {
                filePath = data.getData();
                binding.imageView.setImageURI(filePath);
                binding.constrWrapper.setVisibility(View.VISIBLE);
                binding.deleteImageButton.setVisibility(View.VISIBLE);
                // binding.uploadButton.setVisibility(View.VISIBLE);
            } else if (data.getExtras() != null && data.getExtras().containsKey("data")) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(requireContext(), imageBitmap);
                filePath = tempUri;
                binding.imageView.setImageURI(filePath);
                binding.constrWrapper.setVisibility(View.VISIBLE);
                binding.deleteImageButton.setVisibility(View.VISIBLE);
                // binding.uploadButton.setVisibility(View.VISIBLE);
            }
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1000, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}