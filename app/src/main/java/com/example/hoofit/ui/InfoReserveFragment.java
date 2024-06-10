package com.example.hoofit.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.adapter.TrailAdapter;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.databinding.FragmentInfoReserveBinding;
import com.example.hoofit.databinding.FragmentReserveBinding;
import com.example.hoofit.ui.editInfo.EditTrailFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class InfoReserveFragment extends Fragment {
    FragmentInfoReserveBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoReserveBinding.inflate(getLayoutInflater());
        FragmentInfoReserveBinding binding = FragmentInfoReserveBinding.inflate(getLayoutInflater());

        Bundle bundle = getArguments();
        if (bundle != null) {
            Reserve reserve = (Reserve) bundle.getSerializable("reserve");
            binding.buttonToTrails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reserve.getTrails() == null)
                    {
                        Toast.makeText(getContext(), "У этого заповедника пока что нет троп", Toast.LENGTH_SHORT).show();
                        if (HoofitApp.user.isAdmin()) {
                            Bundle bundleTrail = new Bundle();
                            bundleTrail.putSerializable("reserve", reserve);
                            EditTrailFragment fragment = new EditTrailFragment();
                            fragment.setArguments(bundleTrail);
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            MainActivity.makeTransaction(transaction, fragment);
                        }
                    }
                    else {

                        Bundle bundleTrail = new Bundle();
                        bundleTrail.putSerializable("trails", (Serializable) reserve.getTrails());
                        bundleTrail.putSerializable("reserve", reserve);
                        TrailFragment trailFragment = new TrailFragment();
                        trailFragment.setArguments(bundleTrail);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction,trailFragment);
                    }
                }
            });
            binding.textName.setText(reserve.getName());
            binding.textDescription.setText(reserve.getDescription());
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

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Обработка ошибок при загрузке изображения
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.imageView.setImageResource(R.drawable.logo);
                }
            });
        }
        return binding.getRoot();
    }
}