package com.example.hoofit.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.adapter.TrailAdapter;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.dataHandler.DataHandler;
import com.example.hoofit.dataHandler.DataInterface;
import com.example.hoofit.databinding.DialogTrailInfoBinding;
import com.example.hoofit.databinding.FragmentTrailBinding;
import com.example.hoofit.mainMenu.OnFragmentInteractionListener;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class TrailFragment extends Fragment{
    FragmentTrailBinding binding;
    private OnFragmentInteractionListener listener;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrailBinding.inflate(getLayoutInflater());

        Bundle bundle = getArguments();
        if (bundle != null) {
            Reserve reserve = (Reserve) bundle.getSerializable("reserve");
            TrailAdapter adapter = new TrailAdapter(getContext(), reserve);
            adapter.setOnItemClickListener(new TrailAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Trail trail) {
                    InfoTrailFragment fragment = new InfoTrailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trail", trail);
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    MainActivity.makeTransaction(transaction, fragment);
                }
            });
            binding.listTrail.setHasFixedSize(true);
            binding.listTrail.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listTrail.setAdapter(adapter);
        }
        return binding.getRoot();
    }


}
//if (listener != null) {
//        listener.onFragmentChanged(R.id.map);
//        }