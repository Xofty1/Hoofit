package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.databinding.FragmentReserveBinding;
import com.example.hoofit.ui.editInfo.EditReserveFragment;

public class ReserveFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentReserveBinding binding = FragmentReserveBinding.inflate(getLayoutInflater());
        ReserveAdapter adapter = new ReserveAdapter(getContext(), HoofitApp.reserves);
        adapter.setOnItemClickListener(new ReserveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Reserve reserve) {
                InfoReserveFragment fragment = new InfoReserveFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("reserve", reserve);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        adapter.setOnItemLongClickListener(new ReserveAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Reserve reserve) {
                EditReserveFragment fragment = new EditReserveFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("reserve", reserve);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        binding.buttonAddReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditReserveFragment fragment = new EditReserveFragment();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        binding.listReserve.setHasFixedSize(true);
        binding.listReserve.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listReserve.setAdapter(adapter);
        return binding.getRoot();
    }
}