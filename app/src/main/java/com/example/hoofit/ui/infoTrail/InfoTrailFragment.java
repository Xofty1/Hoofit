package com.example.hoofit.ui.infoTrail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hoofit.AuthActivity;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentInfoTrailBinding;
import com.example.hoofit.ui.map.MapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InfoTrailFragment extends Fragment {
    private FragmentInfoTrailBinding binding;
    private InfoTrailViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InfoTrailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoTrailBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Trail trail = (Trail) bundle.getSerializable("trail");
            viewModel.setTrail(trail);
        }

        viewModel.getTrailLiveData().observe(getViewLifecycleOwner(), trail -> {
            if (trail != null) {
                binding.textName.setText(trail.getName());
                binding.textDescription.setText(trail.getDescription());
            }
        });

        binding.buttonToMap.setOnClickListener(view -> {
            Trail selectedTrail = viewModel.getTrailLiveData().getValue();

            if (selectedTrail != null) {
                MapFragment fragment = new MapFragment();
                Bundle bundleToMap = new Bundle();
                bundleToMap.putSerializable("trail", selectedTrail);
                fragment.setArguments(bundleToMap);
                viewModel.getTrailLiveData().getValue();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);

            }
        });
        if (HoofitApp.user.getLikedTrails().contains(viewModel.getTrailLiveData().getValue())){
            binding.likeButton.setChecked(true);
        }
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        binding.likeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    HoofitApp.user.getLikedTrails().add(viewModel.getTrailLiveData().getValue());
                    Toast.makeText(getContext(), "Добавлено в сохраненные", Toast.LENGTH_SHORT).show();
                    users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
                }
                else {
                    HoofitApp.user.getLikedTrails().remove(viewModel.getTrailLiveData().getValue());
                    Toast.makeText(getContext(), "Удалено из сохраненных", Toast.LENGTH_SHORT).show();

//                    users.child(HoofitApp.user.getId()).setValue(HoofitApp.user);
                    users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
                }
            }
        });
        return binding.getRoot();
    }
}
