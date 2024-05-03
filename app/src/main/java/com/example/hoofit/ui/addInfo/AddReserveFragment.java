package com.example.hoofit.ui.addInfo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hoofit.R;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.databinding.FragmentAddReserveBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddReserveFragment extends Fragment {
    FragmentAddReserveBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageRef;
    private StorageReference storageReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddReserveBinding.inflate(getLayoutInflater());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        return binding.getRoot();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            binding.imageView.setImageURI(filePath);
            binding.imageView.setVisibility(View.VISIBLE);
            binding.uploadButton.setVisibility(View.VISIBLE);
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String name = binding.editTextName.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
            String id = reservesRef.push().getKey(); // Получаем уникальный ключ

            Reserve newReserve = new Reserve(id, name, description, null);
            reservesRef.child(id).setValue(newReserve);

            StorageReference ref = storageReference.child("images/" + name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Выберите изображение", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Разрешение на чтение внешнего хранилища не предоставлено, запрашиваем его
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
        else {
            openFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на чтение внешнего хранилища предоставлено
                // Вы можете выполнять нужные действия, которые требуют доступа к файлам
                openFileChooser();
            } else {
                // Разрешение не было предоставлено
                // Обработайте эту ситуацию, например, сообщите пользователю о том, что
                // доступ к файлам не предоставлен и приложение не сможет выполнить нужные действия
            }
        }
    }
}