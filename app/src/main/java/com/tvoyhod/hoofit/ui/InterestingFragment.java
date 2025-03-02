package com.tvoyhod.hoofit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Interesting;
import com.tvoyhod.hoofit.databinding.FragmentInterestingBinding;
import com.tvoyhod.hoofit.ui.infoTrail.InfoTrailFragment;

/**
 * The type Interesting fragment.
 */
public class InterestingFragment extends Fragment {
    /**
     * The Binding.
     */
    FragmentInterestingBinding binding;
    /**
     * The Interesting.
     */
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
                                Toast.makeText(getContext(), "Нет приложений чтобы открыть ссылку", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IllegalArgumentException e) {
                            Toast.makeText(getContext(), "Непреавильная ссылка", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        return binding.getRoot();
    }
}