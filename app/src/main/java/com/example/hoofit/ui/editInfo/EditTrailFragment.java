package com.example.hoofit.ui.editInfo;

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
import com.example.hoofit.adapter.CoordinateAdapter;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentEditTrailBinding;
import com.example.hoofit.ui.ReserveFragment;
import com.example.hoofit.ui.TrailFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditTrailFragment extends Fragment {
    FragmentEditTrailBinding binding;
    Trail trail = null;
    Reserve reserve;
    CoordinateAdapter adapter;
    boolean isNewTrail = false;
    DatabaseReference reservesRef,trailsRef;
    List<Trail> trails;

    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public boolean isNewTrail() {
        return isNewTrail;
    }

    public void setNewTrail(boolean newTrail) {
        isNewTrail = newTrail;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTrailBinding.inflate(getLayoutInflater());
        initBundle();
        initDatabaseReferences();
        initData();
        initUI();
        return binding.getRoot();
    }

    public void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            trail = (Trail) bundle.getSerializable("trail");
            reserve = (Reserve) bundle.getSerializable("reserve");
            if (trail == null) {
                trail = new Trail();
                isNewTrail = true;
                Toast.makeText(getContext(), "мы создаем тропу", Toast.LENGTH_SHORT).show();
            } else {
                binding.deleteButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initDatabaseReferences() {
        reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
        trailsRef = reservesRef.child(reserve.getId()).child("trails");
    }

    public void initData() {
        if (reserve.getTrails() == null)
            trails = new ArrayList<>();
        else
            trails = reserve.getTrails();
        if (isNewTrail) {
            String trailId = reservesRef.push().getKey();
            trail.setId(trailId);
        }
        adapter = new CoordinateAdapter(getContext(), trail.getCoordinatesList());
    }

    private void initUI() {
        binding.editTextDescription.setText(trail.getDescription());
        binding.editTextName.setText(trail.getName());
        binding.editTextDifficulty.setText(trail.getDifficulty());
        binding.editTextLength.setText(String.valueOf(trail.getLength()));
        binding.editTextTimeRequired.setText(trail.getTimeRequired());

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrail();
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTrail();
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addCoordinate(new Coordinate(0, 0));
            }
        });

        binding.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.removeCoordinate();
            }
        });

        binding.recyclerViewCoordinates.setHasFixedSize(false);
        binding.recyclerViewCoordinates.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCoordinates.setAdapter(adapter);
    }

    private void saveTrail() {
        String name = binding.editTextName.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String difficulty = binding.editTextDifficulty.getText().toString().trim();
        String length = binding.editTextLength.getText().toString().trim();
        String timeRequired = binding.editTextTimeRequired.getText().toString().trim();
        List<Coordinate> coordinates = adapter.getCoordinates();

        if (isValidInput(name, description, difficulty, length, timeRequired, coordinates)) {
            trail.setDescription(description);
            trail.setDifficulty(difficulty);
            trail.setName(name);
            trail.setLength(Double.parseDouble(length));
            trail.setTimeRequired(timeRequired);
            trail.setCoordinatesList(coordinates);
            reserve.setTrails(trails);
            if (isNewTrail) {
                trails.add(trail);
            }
                trailsRef.setValue(trails);

            for (Interesting interesting : HoofitApp.interestings) {
                if (interesting.getTrail() != null && interesting.getTrail().equals(trail)) {
                    Toast.makeText(getActivity(), "Найдено", Toast.LENGTH_SHORT).show();

                    interesting.setTrail(trail);
                    DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
                    interestingRef.child(interesting.getId()).setValue(interesting);
                    break;
                }
            }
            TrailFragment fragment = new TrailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("reserve", reserve);
            Toast.makeText(getContext(), "Тропа успешно добавлена", Toast.LENGTH_SHORT).show();

            fragment.setArguments(bundle);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            MainActivity.makeTransaction(transaction, fragment);
        }
    }

    public boolean isValidInput(String name, String description, String difficulty, String length, String timeRequired, List<Coordinate> coordinates) {
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Не введены данные в поле Имя", Toast.LENGTH_SHORT).show();
            return false;
        } else if (description.isEmpty()) {
            Toast.makeText(getContext(), "Не введены данные в поле Описание", Toast.LENGTH_SHORT).show();
            return false;
        } else if (difficulty.isEmpty()) {
            Toast.makeText(getContext(), "Не введены данные в поле Сложность", Toast.LENGTH_SHORT).show();
            return false;
        } else if (length.isEmpty()) {
            Toast.makeText(getContext(), "Не введены данные в поле Длина", Toast.LENGTH_SHORT).show();
            return false;
        } else if (timeRequired.isEmpty()) {
            Toast.makeText(getContext(), "Не введены данные в поле Время", Toast.LENGTH_SHORT).show();
            return false;
        }else if (coordinates.size() < 2) {
            Toast.makeText(getContext(), "Добавьте координаты для тропы", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void deleteTrail() {
        reserve.getTrails().remove(trail);
        trailsRef.setValue(trails);
        HoofitApp.user.getLikedTrails().remove(trail);
        for (Interesting interesting : HoofitApp.interestings){
            if (interesting.getTrail() != null && interesting.getTrail().equals(trail)){
                DatabaseReference interestingRef = FirebaseDatabase.getInstance().getReference("interesting");
                interestingRef.child(interesting.getId()).removeValue();
                HoofitApp.interestings.remove(interesting);
                break;
            }
        }
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
        TrailFragment fragment = new TrailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("reserve", reserve);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        MainActivity.makeTransaction(transaction, fragment);
    }
}
