package com.example.hoofit.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.User;
import com.example.hoofit.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {
    FirebaseAuth auth; //для авторизации
    FirebaseDatabase db;
    DatabaseReference users; //таблица в БД
    FragmentRegisterBinding binding;
    String email, password, name, username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        binding.buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.editTextEmail.getText().toString();
                password = binding.editTextPassword.getText().toString();
                name = binding.editTextName.getText().toString();
                username = binding.editTextUsername.getText().toString();

                auth.createUserWithEmailAndPassword(email,
                                password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User(name, username, email, null,false);
                                HoofitApp.user = user;
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Все круто", Toast.LENGTH_LONG).show();
                                                WelcomeFragment fragment = new WelcomeFragment();
                                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                                MainActivity.makeTransaction(transaction, fragment);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        binding.textViewToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment fragment = new SignInFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        return binding.getRoot();
    }
}