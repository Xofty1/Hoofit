package com.tvoyhod.hoofit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.tvoyhod.hoofit.databinding.FragmentProfileBinding;
import com.tvoyhod.hoofit.databinding.RequestPasswordBinding;
import com.tvoyhod.hoofit.ui.profile.EditUserFragment;
import com.tvoyhod.hoofit.ui.profile.HelpFragment;
import com.tvoyhod.hoofit.ui.profile.SettingsFragment;
import com.tvoyhod.hoofit.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + HoofitApp.user.getId());
        String imageId = HoofitApp.user.getId();
        Utils.loadImage(requireContext(), imageId, binding.imageView, imageRef);
        binding.buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        binding.textViewEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;
                RequestPasswordBinding bindingPassword = RequestPasswordBinding.inflate(getLayoutInflater());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(bindingPassword.getRoot());

                dialog = builder.create();

                // Установка начального масштаба на 0
                bindingPassword.getRoot().setScaleX(0);
                bindingPassword.getRoot().setScaleY(0);
                bindingPassword.editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus) {
                            if (binding.textViewEditData.getText().toString().isEmpty()) {
                                bindingPassword.textInputLayoutPassword.setHint("Введите пароль");
                            }
                        } else {
                            bindingPassword.textInputLayoutPassword.setHint("");
                        }
                    }
                });
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // Анимация масштабирования при появлении диалогового окна
                        bindingPassword.getRoot().animate()
                                .scaleX(1)
                                .scaleY(1)
                                .setDuration(300)
                                .start();
                    }
                });
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bindingPassword.buttonDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bindingPassword.buttonOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String currentPassword = bindingPassword.editTextPassword.getText().toString();
                        // Check if the password is correct
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Password is correct, navigate to EditUserFragment
                                                dialog.dismiss();
                                                EditUserFragment fragment = new EditUserFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putParcelable("user", user);
                                                fragment.setArguments(bundle);
                                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                                MainActivity.makeTransaction(transaction, fragment);
                                            } else {
                                                Toast.makeText(getContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "User not signed in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        binding.textUsername.setText(HoofitApp.user.getUsername());
        binding.buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment fragment = new HelpFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
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
                    MainActivity.makeTransaction(transaction, fragment);
                }
            }
        });
        return binding.getRoot();
    }
}