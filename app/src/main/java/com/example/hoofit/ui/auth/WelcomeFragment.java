package com.example.hoofit.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.AuthActivity;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.User;
import com.example.hoofit.dataHandler.JsonUtils;
import com.example.hoofit.databinding.FragmentWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WelcomeFragment extends Fragment {
    FragmentWelcomeBinding binding;
    FirebaseAuth auth; //для авторизации
    FirebaseDatabase db;
    DatabaseReference users; //таблица в БД
    private static final long TIMEOUT_MILLIS = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(getLayoutInflater());
        binding.textWelcome.setText("Добро поажаловать, " + HoofitApp.user.getName());
        // Получите путь к внутреннему хранилищу приложения


        File file = new File(getContext().getFilesDir(), HoofitApp.fileNameUser);
        String filePath = file.getAbsolutePath();
        String jsonUser = JsonUtils.convertObjectToJson(HoofitApp.user);
        try {
            JsonUtils.saveJsonToFile(jsonUser, filePath);
            Toast.makeText(getContext(), "Успешно записаны данные о пользователе", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileWriteError", "Ошибка при записи данных о пользователе в файл", e);
        }


        File fileReserve = new File(getContext().getFilesDir(), HoofitApp.fileNameReserve);
        String filePathReserve = fileReserve.getAbsolutePath();
        Log.d("Tagdd", filePathReserve + " rev in welcome");
//

        if (HoofitApp.reserves != null) {
            String json = JsonUtils.convertObjectToJson(HoofitApp.reserves);
            try {
                JsonUtils.saveJsonToFile(json, filePathReserve);
                Toast.makeText(getContext(), "Успешно записаны данные о заповедниках", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FileWriteError3", "Ошибка при записи данных о заповедниках в файл", e);
            }
        }
        else{
            Toast.makeText(getContext(), "Проверьте подключение к Интернету", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void startDelayedFragmentSwitch() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    // Создаем Intent для запуска новой активности
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    // Запускаем активность
                    startActivity(intent);
                }
            }

        }, TIMEOUT_MILLIS);
    }
}