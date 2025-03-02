package com.tvoyhod.hoofit.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.User;
import com.tvoyhod.hoofit.databinding.FragmentSignInBinding;
import com.tvoyhod.hoofit.ui.profile.HelpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * The type Sign in fragment.
 */
public class SignInFragment extends Fragment {
    /**
     * The Binding.
     */
    FragmentSignInBinding binding;
    /**
     * The Email.
     */
    String email, /**
     * The Password.
     */
    password;
    /**
     * The Auth.
     */
    FirebaseAuth auth; //для авторизации
    /**
     * The Db.
     */
    FirebaseDatabase db;
    /**
     * The Users.
     */
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
                                                startActivity(new Intent(getContext(), MainActivity.class));
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


            }

        });
        binding.textViewToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction( transaction,fragment);
            }
        });
        return binding.getRoot();
    }
}