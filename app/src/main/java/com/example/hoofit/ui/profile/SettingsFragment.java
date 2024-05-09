package com.example.hoofit.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.AuthActivity;
import com.example.hoofit.R;
import com.example.hoofit.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
FragmentSettingsBinding binding;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        binding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), AuthActivity.class));
            }
        });
        return binding.getRoot();
    }
}