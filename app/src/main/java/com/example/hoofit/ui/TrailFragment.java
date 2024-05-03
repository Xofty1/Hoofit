package com.example.hoofit.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.adapter.TrailAdapter;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentTrailBinding;
import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.example.hoofit.ui.infoTrail.InfoTrailFragment;

public class TrailFragment extends Fragment{
    FragmentTrailBinding binding;
    private OnFragmentInteractionListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrailBinding.inflate(getLayoutInflater());

        Bundle bundle = getArguments();
        if (bundle != null) {
            Reserve reserve = (Reserve) bundle.getSerializable("reserve");
            TrailAdapter adapter = new TrailAdapter(getContext(), reserve.getTrails());
            adapter.setOnItemClickListener(new TrailAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Trail trail) {
                    InfoTrailFragment fragment = new InfoTrailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trail", trail);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction, fragment);
                }
            });
            binding.listTrail.setHasFixedSize(true);
            binding.listTrail.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listTrail.setAdapter(adapter);
        }
        else{
            TrailAdapter adapter = new TrailAdapter(getContext(), HoofitApp.allTrails);
            adapter.setOnItemClickListener(new TrailAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Trail trail) {
                    InfoTrailFragment fragment = new InfoTrailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trail", trail);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction, fragment);
                }
            });
            binding.listTrail.setHasFixedSize(true);
            binding.listTrail.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listTrail.setAdapter(adapter);
        }
        return binding.getRoot();
    }

}