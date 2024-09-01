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
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.adapter.TrailAdapter;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentTrailBinding;
import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.example.hoofit.ui.editInfo.EditTrailFragment;
import com.example.hoofit.ui.infoTrail.InfoTrailFragment;

import java.util.List;

public class TrailFragment extends Fragment {
    FragmentTrailBinding binding;
    private OnFragmentInteractionListener listener;
    Reserve reserve = null;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrailBinding.inflate(getLayoutInflater());

        handleBundleArguments(getArguments());

        setupAddButton();

        return binding.getRoot();
    }

    private void handleBundleArguments(Bundle bundle) {
        TrailAdapter adapter;

        if (HoofitApp.allTrails == null) {
            Toast.makeText(getContext(), "Нет троп", Toast.LENGTH_SHORT).show();
        } else {
            List<Trail> trails;
            if (bundle != null) {
                reserve = (Reserve) bundle.getSerializable("reserve");
                if (reserve == null) {
                    trails = (List<Trail>) bundle.getSerializable("trails");
                } else {
                    trails = reserve.getTrails();
                }
                adapter = new TrailAdapter(getContext(), trails);
            } else {
                adapter = new TrailAdapter(getContext(), HoofitApp.allTrails);
            }
            if (reserve == null || !HoofitApp.user.isAdmin())
                binding.buttonAddTrail.setVisibility(View.INVISIBLE);

            setupRecyclerView(adapter);
            if (HoofitApp.user.isAdmin() && reserve != null) {
                setupItemLongClickListener(adapter);
            }
            setupItemClickListener(adapter);
        }
    }

    private void setupRecyclerView(TrailAdapter adapter) {
        binding.listTrail.setHasFixedSize(true);
        binding.listTrail.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listTrail.setAdapter(adapter);
    }

    private void setupItemLongClickListener(TrailAdapter adapter) {
        adapter.setOnItemLongClickListener(new TrailAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Trail trail) {
                EditTrailFragment fragment = new EditTrailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("reserve", reserve);
                bundle.putSerializable("trail", trail);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
    }

    private void setupItemClickListener(TrailAdapter adapter) {
        adapter.setOnItemClickListener(new TrailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trail trail) {
                InfoTrailFragment fragment = new InfoTrailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trail", trail);
                bundle.putSerializable("reserve", reserve);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
    }

    private void setupAddButton() {
        if (!HoofitApp.user.isAdmin()) {
            binding.buttonAddTrail.setVisibility(View.INVISIBLE);
        }
        binding.buttonAddTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTrailFragment fragment = new EditTrailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("reserve", reserve);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
    }
}