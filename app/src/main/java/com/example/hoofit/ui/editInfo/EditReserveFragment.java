package com.example.hoofit.ui.editInfo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentEditReserveBinding;
import com.example.hoofit.ui.ReserveFragment;
import com.example.hoofit.ui.TrailFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditReserveFragment extends Fragment {
    FragmentEditReserveBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    Reserve reserve = null;
    private StorageReference storageReference;

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
            binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
                    String name = binding.editTextName.getText().toString();
                    String description = binding.editTextDescription.getText().toString();
                    updateData(name, description, reservesRef);
                    ReserveFragment fragment = new ReserveFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction, fragment);
                }
            });
        } else {
            binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
                    String name = binding.editTextName.getText().toString();
                    String description = binding.editTextDescription.getText().toString();
                    addData(name, description, reservesRef);

                }
            });
        }
        binding.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = null;
                binding.constrWrapper.setVisibility(View.INVISIBLE);
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
            binding.constrWrapper.setVisibility(View.VISIBLE);
            binding.uploadButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateData(String name, String description, DatabaseReference reservesRef) {
        reserve.setDescription(description);
        reserve.setName(name);
        reservesRef.child(reserve.getId()).setValue(reserve);
        for (Interesting interesting : HoofitApp.interestings) {
            if (interesting.getReserve() != null && interesting.getReserve().equals(reserve)) {
                Toast.makeText(getActivity(), "Найдено", Toast.LENGTH_SHORT).show();

                interesting.setReserve(reserve);
                DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
                interestingRef.child(interesting.getId()).setValue(interesting);
                break;
            }
        }


        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + reserve.getId());
            ref.delete();
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
    }

    private void addData(String name, String description, DatabaseReference reservesRef) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String id = reservesRef.push().getKey(); // Получаем уникальный ключ

            Reserve newReserve = new Reserve(id, name, description, null);
            reservesRef.child(id).setValue(newReserve);

            StorageReference ref = storageReference.child("images/" + newReserve.getId());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                            ReserveFragment fragment = new ReserveFragment();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            MainActivity.makeTransaction(transaction, fragment);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Выберите изображение", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Разрешение на чтение внешнего хранилища не предоставлено, запрашиваем его
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
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
                Toast.makeText(getActivity(), "Нет доступа к файлам", Toast.LENGTH_SHORT).show();
            }
        }
    }
}