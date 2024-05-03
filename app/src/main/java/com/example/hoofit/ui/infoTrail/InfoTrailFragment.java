package com.example.hoofit.ui.infoTrail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hoofit.MainActivity;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentInfoTrailBinding;
import com.example.hoofit.ui.map.MapFragment;


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
        return binding.getRoot();
    }
}
