package com.tvoyhod.hoofit.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.adapter.InterestingAdapter;
import com.tvoyhod.hoofit.data.Interesting;
import com.tvoyhod.hoofit.databinding.FragmentMainBinding;
import com.tvoyhod.hoofit.ui.editInfo.EditInterestingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;
    FragmentTransaction fTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        CardView reserves = binding.getRoot().findViewById(R.id.reserves);
        CardView trails = binding.getRoot().findViewById(R.id.trails);

        if (!HoofitApp.user.isAdmin()) {
            binding.buttonAddInteresting.setVisibility(View.INVISIBLE);
        }
        binding.buttonAddInteresting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditInterestingFragment fragment = new EditInterestingFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        reserves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fTrans = getParentFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_container, new ReserveFragment());
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });
        trails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fTrans = getParentFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_container, new TrailFragment());
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });
        InterestingAdapter adapter = new InterestingAdapter(getContext(), reverseList(HoofitApp.interestings));
        adapter.setOnItemClickListener(new InterestingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Interesting interesting) {
                InterestingFragment fragment = new InterestingFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("interesting", interesting);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        if (HoofitApp.user.isAdmin()) {
            adapter.setOnItemLongClickListener(new InterestingAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Interesting interesting) {
                    EditInterestingFragment fragment = new EditInterestingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("interesting", interesting);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction, fragment);
                }
            });
        }
        binding.recyclerViewInteresting.setHasFixedSize(true);
        binding.recyclerViewInteresting.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewInteresting.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static List<Interesting> reverseList(List<Interesting> list) {
        List<Interesting> reversedList = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            reversedList.add(list.get(i));
        }
        return reversedList;
    }
}
