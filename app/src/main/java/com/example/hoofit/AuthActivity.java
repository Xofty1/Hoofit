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
import com.example.hoofit.data.Trail;
import com.example.hoofit.dataHandler.JsonUtils;
import com.example.hoofit.ui.auth.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private static boolean isPersistenceEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (!isPersistenceEnabled) {
            database.setPersistenceEnabled(true);
            isPersistenceEnabled = true;
        }
        DatabaseReference reserveRef = database.getReference("reserves");
        List<Reserve> rev = new ArrayList<>();
        HoofitApp.reserves = new ReserveData();
        reserveRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        List<Trail> trails = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reserve reserve = snapshot.getValue(Reserve.class);
                            if (reserve != null && reserve.getTrails() != null)
                                trails.addAll(reserve.getTrails());
                            rev.add(reserve);
                        }
                        HoofitApp.reserves.setReserves(rev);
                        HoofitApp.allTrails = trails;
                    } else {
                        Toast.makeText(AuthActivity.this, "Троп пока что нет", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AuthActivity.this, "Проверьте подключение к Интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reserveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Reserve> rev = new ArrayList<>();
                    List<Trail> trails = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reserve reserve = snapshot.getValue(Reserve.class);
                        if (reserve != null && reserve.getTrails() != null) {
                            trails.addAll(reserve.getTrails());
                        }
                        rev.add(reserve);
                    }
                    HoofitApp.reserves.setReserves(rev);
                    HoofitApp.allTrails = trails;
//                    goToMainActivity();
                } else {
                    // Обработка случая, когда данных нет
                    Toast.makeText(AuthActivity.this, "Троп пока что нет", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки, если она произошла
                Toast.makeText(AuthActivity.this, "Ошибка при получении данных: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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