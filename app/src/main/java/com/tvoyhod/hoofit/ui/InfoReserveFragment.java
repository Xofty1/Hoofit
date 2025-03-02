package com.tvoyhod.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.databinding.FragmentInfoReserveBinding;
import com.tvoyhod.hoofit.databinding.FragmentReserveBinding;
import com.tvoyhod.hoofit.ui.editInfo.EditTrailFragment;
import com.tvoyhod.hoofit.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * The type Info reserve fragment.
 */
public class InfoReserveFragment extends Fragment {
    /**
     * The Binding.
     */
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
                    if (reserve.getTrails() == null) {
                        Toast.makeText(getContext(), "У этого заповедника пока что нет троп", Toast.LENGTH_SHORT).show();
                        if (HoofitApp.user.isAdmin()) {
                            Bundle bundleTrail = new Bundle();
                            bundleTrail.putSerializable("reserve", reserve);
                            EditTrailFragment fragment = new EditTrailFragment();
                            fragment.setArguments(bundleTrail);
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            MainActivity.makeTransaction(transaction, fragment);
                        }
                    } else {

                        Bundle bundleTrail = new Bundle();
                        bundleTrail.putSerializable("trails", (Serializable) reserve.getTrails());
                        bundleTrail.putSerializable("reserve", reserve);
                        TrailFragment trailFragment = new TrailFragment();
                        trailFragment.setArguments(bundleTrail);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, trailFragment);
                    }
                }
            });
            binding.textName.setText(reserve.getName());
            binding.textDescription.setText(reserve.getDescription());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + reserve.getId());
            String imageId = reserve.getId();
            Utils.loadImage(requireContext(), imageId, binding.imageView, imageRef);
        }
        return binding.getRoot();
    }
}