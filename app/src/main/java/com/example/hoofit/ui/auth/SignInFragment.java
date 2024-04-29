package com.example.hoofit.ui.auth;

import android.content.Intent;
import android.icu.util.LocaleData;
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
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.User;
import com.example.hoofit.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignInFragment extends Fragment {
    FragmentSignInBinding binding;
    String email, password;
    FirebaseAuth auth; //для авторизации
    FirebaseDatabase db;
    DatabaseReference users; //таблица в БД

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                email = binding.editTextEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Введите email", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }
                password = binding.editTextPassword.getText().toString();
                if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Успешная аутентификация
                                users.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DataSnapshot dataSnapshot = task.getResult();
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    User user = snapshot.getValue(User.class);
                                                    if (Objects.equals(email, user.getEmail())) {
                                                        HoofitApp.user = user;
                                                        break;
                                                    }
                                                }
                                                WelcomeFragment fragment = new WelcomeFragment();
                                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                                MainActivity.makeTransaction(transaction, fragment);
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Обработка ошибок аутентификации
                                if (e instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(getContext(), "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Ошибка аутентификации" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                binding.textViewToRegistration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RegisterFragment fragment = new RegisterFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, fragment);
                    }
                });
            }
        });
        return binding.getRoot();
    }
}