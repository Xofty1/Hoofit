package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.InterestingAdapter;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentMainBinding;
import com.example.hoofit.ui.editInfo.EditInterestingFragment;
import com.example.hoofit.ui.editInfo.EditReserveFragment;
import com.example.hoofit.ui.profile.SettingsFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        Log.d("FFF", HoofitApp.interestings.size()+ " размер");
        CardView reserves = binding.getRoot().findViewById(R.id.reserves);
        CardView trails = binding.getRoot().findViewById(R.id.trails);

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
        binding.recyclerViewInteresting.setHasFixedSize(true);
        binding.recyclerViewInteresting.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewInteresting.setAdapter(adapter);
        Log.d("FFF", HoofitApp.interestings.size()+ " размер2");
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
