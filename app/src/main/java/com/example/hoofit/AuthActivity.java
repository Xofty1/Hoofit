package com.example.hoofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reserveRef = database.getReference("reserves");
//        List<Reserve> rev = new ArrayList<>();
//        File file = new File(this.getFilesDir(), HoofitApp.fileNameReserve);
//        String filePath = file.getAbsolutePath();
//        reserveRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {// если есть подключение к интернету берем данные из Firebase
//                    Toast.makeText(AuthActivity.this, "Клоуны", Toast.LENGTH_LONG).show();
//                    DataSnapshot dataSnapshot = task.getResult();
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Reserve reserve = snapshot.getValue(Reserve.class);
//                            rev.add(reserve);
//                            HoofitApp.allTrails.addAll(reserve.getTrails());
//                        }
//                        HoofitApp.reserves.setReserves(rev);
//                    } else {
//                        Toast.makeText(AuthActivity.this, "Троп пока что нет", Toast.LENGTH_LONG).show();
//                    }
//                } else { // если подключения к Интернету нет, то берем данные из JSON-файла
//                    Toast.makeText(AuthActivity.this, "Ошибка соединения", Toast.LENGTH_LONG).show();
//                    try {
//
//                        String json = JsonUtils.readFile(filePath);
//                        ReserveData reserveData = JsonUtils.convertJsonToObject(json, ReserveData.class);
//                        HoofitApp.reserves = reserveData;
//                        for (int i = 0; i < reserveData.getReserves().size(); i++) {
//                            HoofitApp.allTrails.addAll(reserveData.getReserves().get(i).getTrails());
//                        }
//                    } catch (IOException e) {
//                        Toast.makeText(AuthActivity.this, "Не удалось получить данные", Toast.LENGTH_LONG).show();
//                    }
//                }
//                Toast.makeText(AuthActivity.this, "Данные получены", Toast.LENGTH_LONG).show();
//            }
//        });


        File fileUser = new File(this.getFilesDir(), HoofitApp.fileNameUser);
        String filePathUser = fileUser.getAbsolutePath();
        try {
            String json = JsonUtils.readFile(filePathUser);
            User user = JsonUtils.convertJsonToObject(json, User.class);
            HoofitApp.user = user;
            replaceFragment(new SignInFragment());
        } catch (
                IOException e) {
            Log.e("FileWriteError2", "Ошибка при чтении данных о пользователе в файл", e);
            Toast.makeText(this, "Не удалось получить данные о пользователе", Toast.LENGTH_LONG).show();
            replaceFragment(new RegisterFragment());
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}