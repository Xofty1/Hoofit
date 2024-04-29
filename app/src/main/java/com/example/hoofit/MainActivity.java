package com.example.hoofit;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.example.hoofit.ui.MainFragment;
import com.example.hoofit.ui.MapFragment;
import com.example.hoofit.ui.ProfileFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hoofit.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
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
    @Override
    public void onFragmentChanged(int itemId) {
        int navItemId;
        if (itemId== R.id.main)
        {
            navItemId = R.id.main;
        }
        else if (itemId == R.id.map)
        {
            navItemId = R.id.map;
        }
        else if (itemId == R.id.profile)
        {
            navItemId = R.id.profile;
        }
        else navItemId = R.id.main;
        binding.navView.setSelectedItemId(navItemId);
    }
 public static void makeTransaction(FragmentTransaction transaction, Fragment fragment){
    transaction.replace(R.id.fragment_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
}
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(
//                R.anim.fragment_slide_in,  // Анимация входа нового фрагмента
//                R.anim.fragment_slide_out, // Анимация выхода старого фрагмента
//                R.anim.fragment_slide_in,  // Анимация входа старого фрагмента (обратно)
//                R.anim.fragment_slide_out  // Анимация выхода нового фрагмента (обратно)
//        );
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}