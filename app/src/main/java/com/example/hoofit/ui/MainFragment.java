package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        CardView cardView = binding.getRoot().findViewById(R.id.cardView1);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reserveRef = database.getReference("reserves");
////        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Reserve reserve = new Reserve();
//        reserve.setId(1);
//        reserve.setName("Forest Silence Reserve");
//        reserve.setDescription("The Forest Silence Reserve is located in a picturesque corner of nature, home to many rare species of flora and fauna.");
//
//        List<Trail> trails = new ArrayList<Trail>();
//
//        Trail trail1 = new Trail();
//        trail1.setId(101);
//        trail1.setName("Elk Tracks Trail");
//        trail1.setLength(5.2);
//        trail1.setDifficulty("medium");
//        trail1.setTimeRequired("2 hours");
//        trail1.setDescription("Interesting trail.");
//        List<Coordinate> coordinatesList1 = new ArrayList<Coordinate>();
//        coordinatesList1.add(new Coordinate(55.12345, 37.54321));
//        coordinatesList1.add(new Coordinate(55.12456, 37.54231));
//        coordinatesList1.add(new Coordinate(55.12567, 37.54141));
//        coordinatesList1.add(new Coordinate(55.12678, 37.54051));
//        trail1.setCoordinatesList(coordinatesList1);
//
//        trails.add(trail1);
//
//        Trail trail2 = new Trail();
//        trail2.setId(102);
//        trail2.setName("Among Ancient Trees Trail");
//        trail2.setLength(3.8);
//        trail2.setDifficulty("easy");
//        trail2.setTimeRequired("1.5 hours");
//        trail2.setDescription("Interesting trail2.");
//        List<Coordinate> coordinatesList2 = new ArrayList<Coordinate>();
//        coordinatesList2.add(new Coordinate(55.13579, 37.55432));
//        coordinatesList2.add(new Coordinate(55.13679, 37.55342));
//        coordinatesList2.add(new Coordinate(55.13779, 37.55252));
//        trail2.setCoordinatesList(coordinatesList2);
//
//        trails.add(trail2);
//
//        reserve.setTrails(trails);
//        reserveRef.push().setValue(reserve);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fTrans = getParentFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_container, new ReserveFragment());
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
