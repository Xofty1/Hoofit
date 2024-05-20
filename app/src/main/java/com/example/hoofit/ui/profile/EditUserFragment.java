package com.example.hoofit.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.R;
import com.example.hoofit.data.User;
import com.example.hoofit.databinding.FragmentEditReserveBinding;
import com.example.hoofit.databinding.FragmentEditUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditUserFragment extends Fragment {
FragmentEditUserBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(getLayoutInflater());
        binding.editTextName.setText(HoofitApp.user.getName());
        binding.editTextEmail.setText(HoofitApp.user.getEmail());
        binding.editTextUsername.setText(HoofitApp.user.getUsername());

        binding.buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                HoofitApp.user.setEmail(email);
                HoofitApp.user.setUsername(username);
                HoofitApp.user.setName(name);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (!password.isEmpty()) {
                        changeUserPassword(user, password);
                    }
                    changeUserEmail(user, email);
                } else {
                    Toast.makeText(getContext(), "User not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }

    public void changeUserPassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changeUserEmail(FirebaseUser user, String newEmail) {
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}