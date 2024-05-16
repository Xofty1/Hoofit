package com.example.hoofit.ui.editInfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.R;
import com.example.hoofit.adapter.CoordinateAdapter;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentEditTrailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class EditTrailFragment extends Fragment {
    FragmentEditTrailBinding binding;
    Trail trail = null;
    Reserve reserve;
    CoordinateAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTrailBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");

        boolean isNewTrail = false;
        if (bundle != null) {
            trail = (Trail) bundle.getSerializable("trail");
            reserve = (Reserve) bundle.getSerializable("reserve");
            if (trail == null) {
                trail = new Trail();
                isNewTrail = true;


                Toast.makeText(getContext(), "мы создаем тропу", Toast.LENGTH_SHORT).show();

            }
        }
        DatabaseReference trailsRef = reservesRef.child(reserve.getId()).child("trails");
        List<Trail> trails = new ArrayList<>();
        if (isNewTrail){
            trails.add(trail);
            reserve.setTrails(trails);
            String trailId = trailsRef.push().getKey();
            trail.setId(trailId);
        }
        else {
            trails = reserve.getTrails();
            trails.add(trail);
            reserve.setTrails(trails);
        }

        adapter = new CoordinateAdapter(getContext(), trail.getCoordinatesList());

        binding.editTextDescription.setText(trail.getDescription());
        binding.editTextName.setText(trail.getName());
        binding.editTextDifficulty.setText(trail.getDifficulty());
        binding.editTextLength.setText(String.valueOf(trail.getLength()));
        binding.editTextTimeRequired.setText(trail.getTimeRequired());

        List<Trail> finalTrails = trails;
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();
                String description = binding.editTextDescription.getText().toString();
                String difficulty = binding.editTextDifficulty.getText().toString();
                String length = binding.editTextLength.getText().toString();
                String timeRequired = binding.editTextTimeRequired.getText().toString();
                List<Coordinate> coordinates = adapter.getCoordinates();


//                    String id = reservesRef.push().getKey();
                trail.setDescription(description);
                trail.setDifficulty(difficulty);
                trail.setName(name);
                trail.setLength(Double.parseDouble(length));
                trail.setTimeRequired(timeRequired);
                trail.setCoordinatesList(coordinates);

                trailsRef.setValue(finalTrails);
//                reservesRef.child(reserve.getId()).child("trails").child(trail.getId()).setValue(trail);
                // Здесь вы можете сохранить trail с обновленными данными в базу данных
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
        return binding.getRoot();
    }
}
