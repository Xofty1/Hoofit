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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

public class EditInterestingFragment extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    FragmentEditInterestingBinding binding;
    Interesting interesting;
    boolean isNewInteresting = false;
    private Uri filePath;
    private StorageReference storageReference;

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
//        interesting.setType("RESERVE");
        DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        if (!isNewInteresting) {
            binding.deleteButton.setVisibility(View.VISIBLE);
        }
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestingRef.child(interesting.getId()).removeValue();
                HoofitApp.interestings.remove(interesting);
                Log.d("FFF", HoofitApp.interestings.size() + " размер0");
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
                        interestingRef.child(interesting.getId()).setValue(interesting);

                        updateData();

                        MainFragment fragment = new MainFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, fragment);
                    }
                }
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void updateData() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference interest = storageReference.child("images/" + interesting.getId());
            interest.delete();
            interest.putFile(filePath)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            binding.imageView.setImageURI(filePath);
            binding.imageView.setVisibility(View.VISIBLE);
//            binding.uploadButton.setVisibility(View.VISIBLE);
        }
    }
}