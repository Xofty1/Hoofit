package com.example.hoofit;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.map.MapFragment;
import com.example.hoofit.ui.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hoofit.databinding.ActivityMainBinding;
import com.example.hoofit.utils.Utils;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    ActivityMainBinding binding;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        transaction = getSupportFragmentManager().beginTransaction();
        setContentView(binding.getRoot());
        replaceFragment(new MainFragment());
        binding.navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.main) {
                replaceFragment(new MainFragment());
            } else if (item.getItemId() == R.id.map) {
                replaceFragment(new MapFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onFragmentChanged(int itemId) {
        int navItemId;
        if (itemId == R.id.main) {
            navItemId = R.id.main;
        } else if (itemId == R.id.map) {
            navItemId = R.id.map;
        } else if (itemId == R.id.profile) {
            navItemId = R.id.profile;
        } else navItemId = R.id.main;
        binding.navView.setSelectedItemId(navItemId);
    }

    public static void makeTransaction(FragmentTransaction transaction, Fragment fragment) {
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}