package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.adapter.TrailAdapter;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.databinding.FragmentInfoReserveBinding;
import com.example.hoofit.databinding.FragmentReserveBinding;

import java.io.Serializable;

public class InfoReserveFragment extends Fragment {
    FragmentInfoReserveBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoReserveBinding.inflate(getLayoutInflater());
        FragmentInfoReserveBinding binding = FragmentInfoReserveBinding.inflate(getLayoutInflater());

        Bundle bundle = getArguments();
        if (bundle != null) {
            Reserve reserve = (Reserve) bundle.getSerializable("reserve");
            binding.buttonToTrails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reserve.getTrails() == null)
                    {
                        Toast.makeText(getContext(), "У этого заповедника пока что нет троп", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Bundle bundleTrail = new Bundle();
                        bundleTrail.putSerializable("trails", (Serializable) reserve.getTrails());
                        TrailFragment trailFragment = new TrailFragment();
                        trailFragment.setArguments(bundleTrail);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction,trailFragment);
//                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                        transaction.replace(R.id.fragment_container, trailFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
                    }
                }
            });
            binding.textName.setText(reserve.getName());
            binding.textDescription.setText(reserve.getDescription());
        }
        return binding.getRoot();
    }
}