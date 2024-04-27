package com.example.hoofit;

import android.app.AlertDialog;
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.databinding.FragmentMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;
import java.util.List;

public class HoofitApp extends Application {
    public static ReserveData reserves = new ReserveData();
    public static List<Trail> allTrails = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("a075c213-cf50-41fb-994d-60f735f08f1b");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reserveRef = database.getReference("reserves");
        List<Reserve> rev = new ArrayList<>();
        reserveRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reserve reserve = snapshot.getValue(Reserve.class);
                            rev.add(reserve);
                            for (Trail trail : reserve.getTrails())
                                allTrails.add(trail);
                        }
                    } else {
                        Toast.makeText(HoofitApp.this, "Троп пока что нет", Toast.LENGTH_LONG);
                    }
                } else {
                    Toast.makeText(HoofitApp.this, "Ошибка соединения", Toast.LENGTH_LONG);
                }
                reserves.setReserves(rev);
            }
        });
    }}
