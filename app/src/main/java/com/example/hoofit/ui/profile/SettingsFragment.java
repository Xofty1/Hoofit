package com.example.hoofit.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.AuthActivity;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.DialogTrailInfoBinding;
import com.example.hoofit.databinding.FragmentSettingsBinding;
import com.example.hoofit.ui.InfoReserveFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.yandex.mapkit.map.PolylineMapObject;


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
                showTrailInfoDialog();
            }
        });
        binding.buttonEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserFragment fragment = new EditUserFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
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