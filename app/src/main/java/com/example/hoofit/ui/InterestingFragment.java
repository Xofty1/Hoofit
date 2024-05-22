package com.example.hoofit.ui;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.databinding.FragmentInterestingBinding;
import com.example.hoofit.ui.editInfo.EditInterestingFragment;
import com.example.hoofit.ui.infoTrail.InfoTrailFragment;

public class InterestingFragment extends Fragment {
    FragmentInterestingBinding binding;
    Interesting interesting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInterestingBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        if (bundle != null) {
            interesting = (Interesting) bundle.getSerializable("interesting");
            binding.textDescription.setText(interesting.getDescription());
            binding.textName.setText(interesting.getName());
            binding.buttonoResource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (interesting.getType().equals("RESERVE")) {
                        InfoReserveFragment fragment = new InfoReserveFragment();
                        Bundle bundleFragment = new Bundle();
                        bundleFragment.putSerializable("reserve", interesting.getReserve());
                        fragment.setArguments(bundleFragment);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, fragment);
                    } else if (interesting.getType().equals("TRAIL")) {
                        InfoTrailFragment fragment = new InfoTrailFragment();
                        Bundle bundleFragment = new Bundle();
                        bundleFragment.putSerializable("trail", interesting.getTrail());
                        fragment.setArguments(bundleFragment);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, fragment);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            Uri uri = Uri.parse(interesting.getUri());
                            intent.setData(uri);

                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "No app found to handle the link", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IllegalArgumentException e) {
                            // Show a toast with a message about the bad link
                            Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        return binding.getRoot();
    }
}