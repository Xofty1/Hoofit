package com.tvoyhod.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.adapter.ReserveAdapter;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.databinding.FragmentReserveBinding;
import com.tvoyhod.hoofit.ui.editInfo.EditReserveFragment;

/**
 * The type Reserve fragment.
 */
public class ReserveFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentReserveBinding binding = FragmentReserveBinding.inflate(getLayoutInflater());
        if (HoofitApp.reserves.getReserves() == null){
            Toast.makeText(getContext(), "Нет заповедников", Toast.LENGTH_SHORT).show();
        }
        else {
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
            if (HoofitApp.user.isAdmin()) {
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
            }

            binding.listReserve.setHasFixedSize(true);
            binding.listReserve.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.listReserve.setAdapter(adapter);
        }
        if (!HoofitApp.user.isAdmin()) {
            binding.buttonAddReserve.setVisibility(View.INVISIBLE);
        }
        binding.buttonAddReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditReserveFragment fragment = new EditReserveFragment();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        return binding.getRoot();
    }
}