package com.example.hoofit;

import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.data.User;
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
import com.example.hoofit.dataHandler.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HoofitApp extends Application {
    final static public String fileNameReserve = "reserves.json";
    public static User user;
    public static ReserveData reserves = null;
    public static List<Trail> allTrails;

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("a075c213-cf50-41fb-994d-60f735f08f1b");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reserveRef = database.getReference("reserves");
        List<Reserve> rev = new ArrayList<>();
        File file = new File(this.getFilesDir(), HoofitApp.fileNameReserve);
        String filePath = file.getAbsolutePath();
        Log.d("Tagdd", filePath + " rev");
        reserves = new ReserveData();
        reserveRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {// если есть подключение к интернету берем данные из Firebase
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        List<Trail> trails = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reserve reserve = snapshot.getValue(Reserve.class);
                            trails.addAll(reserve.getTrails());
                            rev.add(reserve);

                        }
                        reserves.setReserves(rev);
                        allTrails = trails;
                    } else {
                        Toast.makeText(HoofitApp.this, "Троп пока что нет", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(HoofitApp.this, "Данные получены", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
