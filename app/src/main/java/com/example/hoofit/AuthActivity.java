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

import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.User;
import com.example.hoofit.ui.LoaderFragment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static boolean isPersistenceEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        replaceFragment(new LoaderFragment());
        mAuth = FirebaseAuth.getInstance();
        if (!isPersistenceEnabled) {
            database.setPersistenceEnabled(true);
            isPersistenceEnabled = true;
        }
        DatabaseReference reserveRef = database.getReference("reserves");
        DatabaseReference interestingRef = database.getReference("interesting");
        List<Reserve> rev = new ArrayList<>();
        HoofitApp.reserves = new ReserveData();
        HoofitApp.interestings = new ArrayList<>();
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

        interestingRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        List<Interesting> interestings = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Interesting interesting = snapshot.getValue(Interesting.class);
                            interestings.add(interesting);
                        }
                        HoofitApp.interestings = interestings;
                    } else {
                        Toast.makeText(AuthActivity.this, "Новостей пока нет", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AuthActivity.this, "Проверьте подключение к Интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        interestingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Interesting> interestings = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Interesting interesting = snapshot.getValue(Interesting.class);
                        interestings.add(interesting);
                    }
                    HoofitApp.interestings = interestings;
                } else {
                    // Обработка случая, когда данных нет
                    Toast.makeText(AuthActivity.this, "Новостей пока что нет", Toast.LENGTH_SHORT).show();
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

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = database.getReference("Users").child(userId);

            // Set a listener specifically for the "admin" field of the current user
            DatabaseReference adminRef = userRef.child("admin");

            adminRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (HoofitApp.user == null) HoofitApp.user = new User();
                        // Update the admin field in the user object
                        HoofitApp.user.setAdmin(dataSnapshot.getValue(Boolean.class));
                    } else {
                        // Handle the case where the admin field does not exist
                        HoofitApp.user.setAdmin(false); // or any default value
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseDatabase", "Failed to read admin field", databaseError.toException());
                }
            });

            // Load the user data initially
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HoofitApp.user = dataSnapshot.getValue(User.class);
                        startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        finish();
                    } else {
                        replaceFragment(new RegisterFragment());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseDatabase", "Failed to read user data", databaseError.toException());
                }
            });
        } else {
            replaceFragment(new RegisterFragment());
        }
    }
}