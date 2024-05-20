package com.example.hoofit.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.User;
import com.example.hoofit.databinding.FragmentEditReserveBinding;
import com.example.hoofit.databinding.FragmentEditUserBinding;
import com.example.hoofit.ui.ReserveFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserFragment extends Fragment {
    FragmentEditUserBinding binding;
    FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(getLayoutInflater());
        binding.editTextName.setText(HoofitApp.user.getName());
        binding.textEmail.setText("Email: " + HoofitApp.user.getEmail());
        binding.editTextUsername.setText(HoofitApp.user.getUsername());
        binding.buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (!password.isEmpty()) {
                    changeUserPassword(user, password);
                }
                HoofitApp.user.setUsername(username);
                HoofitApp.user.setName(name);
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
                users.child(HoofitApp.user.getId()).setValue(HoofitApp.user);
                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
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
}