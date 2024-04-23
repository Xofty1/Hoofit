package com.example.hoofit;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.MapFragment;
import com.example.hoofit.ui.ProfileFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hoofit.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MainFragment());
        binding.navView.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.main)
            {
                replaceFragment(new MainFragment());
            }
            else if (item.getItemId() == R.id.map)
            {
                replaceFragment(new MapFragment());
            }
            else if (item.getItemId() == R.id.profile)
            {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}