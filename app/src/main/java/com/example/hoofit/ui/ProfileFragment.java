package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.databinding.FragmentProfileBinding;
import com.example.hoofit.ui.profile.HelpFragment;
import com.example.hoofit.ui.profile.SettingsFragment;

import java.io.Serializable;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        binding.buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction,fragment);
            }
        });

        binding.buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment fragment = new HelpFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction,fragment);
            }
        });
        binding.buttonSavedTrails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HoofitApp.user.getLikedTrails().size() == 0) {
                    Toast.makeText(getContext(), "У вас нет сохраненных троп", Toast.LENGTH_SHORT).show();
                } else {
                    TrailFragment fragment = new TrailFragment();
                    Bundle bundleTrail = new Bundle();
                    bundleTrail.putSerializable("trails", (Serializable) HoofitApp.user.getLikedTrails());
                    fragment.setArguments(bundleTrail);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction,fragment);
                }
            }
        });
        return binding.getRoot();
    }
}