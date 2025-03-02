package com.tvoyhod.hoofit.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.tvoyhod.hoofit.AuthActivity;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.databinding.DialogTrailInfoBinding;
import com.tvoyhod.hoofit.databinding.FragmentSettingsBinding;
import com.tvoyhod.hoofit.databinding.RequestPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * The type Settings fragment.
 */
public class SettingsFragment extends Fragment {
    /**
     * The Binding.
     */
    FragmentSettingsBinding binding;
    /**
     * The M auth.
     */
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
                showTrailInfoDialog();
            }
        });
        binding.buttonEditData.setOnClickListener(new View.OnClickListener() {
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
                bindingPassword.editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus) {
                            if (bindingPassword.editTextPassword.getText().toString().isEmpty()) {
                                bindingPassword.textInputLayoutPassword.setHint("Введите пароль");
                            }
                        } else {
                            bindingPassword.textInputLayoutPassword.setHint("");
                        }
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
        return binding.getRoot();
    }

    private void showTrailInfoDialog() {
        AlertDialog dialog;
        DialogTrailInfoBinding bindingInfo = DialogTrailInfoBinding.inflate(getLayoutInflater());

        bindingInfo.dialogTitle.setText("Вы точно хотите выйти из аккаунта?");
        bindingInfo.dialogMessage.setText("");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(bindingInfo.getRoot());

        dialog = builder.create();

        // Установка начального масштаба на 0
        bindingInfo.getRoot().setScaleX(0);
        bindingInfo.getRoot().setScaleY(0);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // Анимация масштабирования при появлении диалогового окна
                bindingInfo.getRoot().animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(300)
                        .start();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bindingInfo.buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), AuthActivity.class));
                getActivity().finish();

            }
        });
        bindingInfo.buttonToTrail.setText("Закрыть");
        bindingInfo.buttonDismiss.setText("Выйти");
        bindingInfo.buttonToTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}