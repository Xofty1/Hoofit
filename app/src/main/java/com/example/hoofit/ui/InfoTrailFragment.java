package com.example.hoofit.ui;

import android.os.Binder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentInfoTrailBinding;

import java.util.Map;


public class InfoTrailFragment extends Fragment {
    FragmentInfoTrailBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoTrailBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        Trail trail = null;
        if (bundle != null)
        {
            trail = (Trail) bundle.getSerializable("trail");
        }
        Trail finalTrail = trail;
        binding.buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment fragment = new MapFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trail", finalTrail);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        binding.textName.setText(trail.getName());
        binding.textDescription.setText(trail.getDescription());
        return binding.getRoot();
    }
}