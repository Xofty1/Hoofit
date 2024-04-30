package com.example.hoofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.User;
import com.example.hoofit.dataHandler.JsonUtils;
import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.auth.RegisterFragment;
import com.example.hoofit.ui.auth.SignInFragment;
import com.example.hoofit.ui.auth.WelcomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        if (HoofitApp.reserves.getReserves() == null) {
            Toast.makeText(this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            try {
                File file = new File(this.getFilesDir(), HoofitApp.fileNameReserve);
                String filePath = file.getAbsolutePath();
                String json = JsonUtils.readFile(filePath);
                ReserveData reserveData = JsonUtils.convertJsonToObject(json, ReserveData.class);
                HoofitApp.reserves = reserveData;
                for (int i = 0; i < reserveData.getReserves().size(); i++) {
                    HoofitApp.allTrails.addAll(reserveData.getReserves().get(i).getTrails());
                }
            } catch (Exception e) {
                Toast.makeText(this, "Не удалось получить данные " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Данные из JSON получены", Toast.LENGTH_SHORT).show();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            replaceFragment(new RegisterFragment());
        else
            startActivity(new Intent(this, MainActivity.class));
    }
}