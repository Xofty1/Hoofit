package com.example.hoofit.ui.editInfo;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentEditInterestingBinding;
import com.example.hoofit.ui.InfoReserveFragment;
import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.ReserveFragment;
import com.example.hoofit.ui.infoTrail.InfoTrailFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditInterestingFragment extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    FragmentEditInterestingBinding binding;
    Interesting interesting;
    boolean isNewInteresting = false;
    private Uri filePath;
    private StorageReference storageReference;
    boolean isDeletedCurrentImage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditInterestingBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (bundle != null) {
            interesting = (Interesting) bundle.getSerializable("interesting");
            binding.editTextDescription.setText(interesting.getDescription());
            binding.editTextName.setText(interesting.getName());
            if (interesting.getTrail() != null)
                binding.editTextResource.setText(interesting.getTrail().getName());
            else if (interesting.getReserve() != null)
                binding.editTextResource.setText(interesting.getReserve().getName());
            else binding.editTextResource.setText(interesting.getUri());
        } else {
            interesting = new Interesting();
            isNewInteresting = true;
        }
        DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        if (!isNewInteresting) {
            binding.deleteButton.setVisibility(View.VISIBLE); // можно исправить покороче
        }

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestingRef.child(interesting.getId()).removeValue();
                HoofitApp.interestings.remove(interesting);
                StorageReference interest = storageReference.child("images/" + interesting.getId());
                interest.delete();
                MainFragment fragment = new MainFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Реагируем на выбор элемента
                switch (position) {
                    case 0:
                        interesting.setType("RESERVE");
                        binding.editTextResource.setHint("Введите название заповедника");
                        break;
                    case 1:
                        interesting.setType("TRAIL");
                        binding.editTextResource.setHint("Введите название тропы");
                        break;
                    case 2:
                        interesting.setType("RESOURCE");
                        binding.editTextResource.setHint("Введите ссылку");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Действие, если ничего не выбрано (можно оставить пустым)
            }
        });

        if (!HoofitApp.user.isAdmin()) {
            binding.saveButton.setVisibility(View.INVISIBLE);
        }
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resource = binding.editTextResource.getText().toString();
                boolean find = false;
                if (interesting.getType().equals("RESERVE")) {
                    for (Reserve reserve : HoofitApp.reserves.getReserves()) {
                        if (reserve.getName().equals(resource)) {
                            interesting.setReserve(reserve);
                            interesting.setTrail(null);
                            interesting.setUri(null);
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        Toast.makeText(getContext(), "Заповедника " + resource + " нет", Toast.LENGTH_SHORT).show();
                    }
                } else if (interesting.getType().equals("TRAIL")) {

                    for (Trail trail : HoofitApp.allTrails) {
                        if (trail.getName().equals(resource)) {
                            interesting.setTrail(trail);
                            interesting.setReserve(null);
                            interesting.setUri(null);
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        Toast.makeText(getContext(), "Такой тропы нет", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    interesting.setUri(resource);
                    interesting.setReserve(null);
                    interesting.setTrail(null);
                    find = true;
                }
                if (find) {
                    boolean isCorrect = true;
                    interesting.setName(binding.editTextName.getText().toString());
                    if (interesting.getName().isEmpty()) {
                        Toast.makeText(getContext(), "Введите название", Toast.LENGTH_SHORT).show();
                        isCorrect = false;
                    }
                    interesting.setDescription(binding.editTextDescription.getText().toString());
                    if (interesting.getDescription().isEmpty()) {
                        Toast.makeText(getContext(), "Введите описание", Toast.LENGTH_SHORT).show();
                        isCorrect = false;
                    }
                    if (isCorrect) {
                        if (isNewInteresting) {
                            String id = interestingRef.push().getKey();
                            interesting.setId(id);
                            HoofitApp.interestings.add(interesting);
                        }
                        interestingRef.child(interesting.getId()).setValue(interesting).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                updateData();
                            }
                        });
                    }
                }
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
        binding.selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + interesting.getId());
        imageRef.getDownloadUrl().

                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
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
                    public void onFailure(@NonNull Exception exception) {
                        binding.imageView.setImageResource(R.drawable.logo);

                    }
                });
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

    // Метод для получения Uri изображения из Bitmap
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public void updateData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        if (filePath != null) {
            showProgressDialog("Загрузка...", progressDialog);
            StorageReference interest = storageReference.child("images/" + interesting.getId());
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
            navigateToMainFragment();
        }
    }

    private void showProgressDialog(String title, ProgressDialog progressDialog) {

        progressDialog.setTitle(title);
        progressDialog.show();
    }

    private void handleSuccess(String message, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        navigateToMainFragment();
    }

    private void handleFailure(Exception e, ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        navigateToMainFragment();
    }

    private void deleteImage(ProgressDialog progressDialog) {
        showProgressDialog("Загрузка...", progressDialog);

        if (interesting != null && interesting.getId() != null) {
            StorageReference interest = storageReference.child("images/" + interesting.getId());
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

    private void navigateToMainFragment() {
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        MainActivity.makeTransaction(transaction, fragment);
    }


}