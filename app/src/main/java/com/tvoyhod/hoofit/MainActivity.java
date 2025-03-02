package com.tvoyhod.hoofit;


import android.os.Bundle;

import com.tvoyhod.hoofit.mainMenu.OnFragmentInteractionListener;
import com.tvoyhod.hoofit.ui.MainFragment;
import com.tvoyhod.hoofit.ui.map.MapFragment;
import com.tvoyhod.hoofit.ui.ProfileFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tvoyhod.hoofit.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationRequest;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    /**
     * The Binding.
     */
    ActivityMainBinding binding;
    /**
     * The Transaction.
     */
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

    /**
     * Make transaction.
     *
     * @param transaction the transaction
     * @param fragment    the fragment
     */
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