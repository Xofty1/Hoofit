package com.example.hoofit.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.MainActivity;
import com.example.hoofit.data.User;
import com.example.hoofit.databinding.FragmentRegisterBinding;
import com.example.hoofit.ui.profile.HelpFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

        binding.buttonInfo.setOnClickListener(view ->{
            HelpFragment fragment = new HelpFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            MainActivity.makeTransaction(transaction, fragment);
        });
        binding.editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.editTextPassword.getText().toString().isEmpty()) {
                        binding.textInputLayoutPassword.setHint("Введите пароль");
                    }
                } else {
                    binding.textInputLayoutPassword.setHint("");
                }
            }
        });
        binding.buttonRegistration.setOnClickListener(new View.OnClickListener() {
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
                if (password.length() <= 5){
                    Toast.makeText(getContext(), "Пароль должен быть больше 5 символов", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }
                name = binding.editTextName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }

                username = binding.editTextUsername.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }
                if (username.length() < 4) {
                    Toast.makeText(getContext(), "Логин должен быть длинее 3 символов", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }
                if (username.length() >= 15) {
                    Toast.makeText(getContext(), "Логин должен быть короче 16 символов", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода, если email пустой
                }


                auth.createUserWithEmailAndPassword(email,
                                password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                User user = new User(id, name, username, email, new ArrayList<>(),false);
                                users.child(id)
                                        .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Авторизируйтесь", Toast.LENGTH_LONG).show();
                                                SignInFragment fragment = new SignInFragment();
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