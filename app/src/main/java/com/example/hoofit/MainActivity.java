package com.example.hoofit;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.MapFragment;
import com.example.hoofit.ui.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hoofit.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    ActivityMainBinding binding;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        requestLocationPermission();
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    Log.d("FFFF", "long " + location.getLongitude() + ", lati " + location.getLatitude());
//                    Toast.makeText(MainActivity.this, "long " + location.getLongitude() + ", lati " + location.getLatitude(), Toast.LENGTH_LONG).show();
//                }
//            }
//        };
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            startLocationUpdates();
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }

//    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
//    }

//    private void stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

//    private void requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_FINE_LOCATION);
//        }
//    }


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
        fragmentTransaction.commit();
    }
}