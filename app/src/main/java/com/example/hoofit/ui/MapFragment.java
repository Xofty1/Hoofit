package com.example.hoofit.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.R;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.dataHandler.DataHandler;
import com.example.hoofit.dataHandler.DataInterface;
import com.example.hoofit.databinding.DialogTrailInfoBinding;
import com.example.hoofit.databinding.FragmentMapBinding;
import com.example.hoofit.databinding.FragmentTrailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    public static boolean isMapFragment;
    FragmentMapBinding binding;
    ReserveData reserves = HoofitApp.reserves;
    List<Trail> allTrails = HoofitApp.allTrails;
    private TrailData trailData;
    private AlertDialog dialog = null;
    private MapObjectCollection mapObjects;
    private MapView mapView;
    private MapObjectTapListener[] mapObjectTapListeners;

    public void setTrailData(TrailData trailData) {
        this.trailData = trailData;
    }

//    @Override
//    public void onTrailDataReceived(TrailData trailData) {
//        this.trailData = trailData;
//        mapView.getMap().move(new CameraPosition(new Point(trailData.getTrails().get(0).getCoordinates().get(0).getLatitude(), trailData.getTrails().get(0).getCoordinates().get(0).getLongitude()), 5.0F, 0.0F, 0.0F));
//
//        int trailsCount = trailData.getTrails().size();
//        mapObjectTapListeners = new MapObjectTapListener[trailsCount];
//
//        for (int i = 0; i < trailsCount; i++) {
//            List<Point> points = new ArrayList<>();
//            List<Coordinate> coordinates = trailData.getTrails().get(i).getCoordinates();
//            for (Coordinate coordinate : coordinates) {
//                points.add(new Point(coordinate.getLatitude(), coordinate.getLongitude()));
//            }
//            PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(points));
//            mapObjectTapListeners[i] = createTapListener(i); // Сохраняем ссылку на созданный слушатель
//            polyline.addTapListener(mapObjectTapListeners[i]); // Используем сохраненный слушатель
//            polyline.setStrokeColor(getResources().getColor(R.color.orange));
//        }
//    }


    private void showTrailInfoDialog(Trail trail) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        DialogTrailInfoBinding bindingInfo = DialogTrailInfoBinding.inflate(getLayoutInflater());

        bindingInfo.dialogTitle.setText(trail.getName());
        bindingInfo.dialogMessage.setText(trail.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(bindingInfo.getRoot());

        dialog = builder.create();
        dialog.show();
        bindingInfo.buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapKitFactory.initialize(requireContext());
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        mapView = binding.mapView;
        isMapFragment = true;
        mapObjects = mapView.getMap().getMapObjects().addCollection();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reserveRef = database.getReference("reserves");
//        List<Reserve> rev = new ArrayList<>();
//        reserveRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DataSnapshot dataSnapshot = task.getResult();
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Reserve reserve = snapshot.getValue(Reserve.class);
//                            rev.add(reserve);
//                            for (Trail trail : reserve.getTrails())
//                                allTrails.add(trail);
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "Троп пока что нет", Toast.LENGTH_LONG);
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_LONG);
//                }
//                reserves.setReserves(rev);
//                makeMap();
//            }
//        });
        makeMap();
        return binding.getRoot();
    }


    public void makeMap() {
        mapView.getMap().move(new CameraPosition(new Point(reserves.getReserves().get(0).getTrails().get(0).getCoordinatesList().get(0).getLatitude(), reserves.getReserves().get(0).getTrails().get(0).getCoordinatesList().get(0).getLongitude()), 5.0F, 0.0F, 0.0F));

        int trailsCount = allTrails.size();
        Log.d("firebase", "size " + trailsCount);
        mapObjectTapListeners = new MapObjectTapListener[trailsCount];

        for (int i = 0; i < trailsCount; i++) {
            List<Point> points = new ArrayList<>();
            List<Coordinate> coordinates = allTrails.get(i).getCoordinatesList();
            for (Coordinate coordinate : coordinates) {
                points.add(new Point(coordinate.getLatitude(), coordinate.getLongitude()));
            }
            PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(points));
            mapObjectTapListeners[i] = createTapListener(i); // Сохраняем ссылку на созданный слушатель
            polyline.addTapListener(mapObjectTapListeners[i]); // Используем сохраненный слушатель
            polyline.setStrokeColor(getResources().getColor(R.color.orange));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        isMapFragment = false;
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    private MapObjectTapListener createTapListener(final int index) {
        return new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                Log.d("FFF", "Listener = " + String.valueOf(index));
                showTrailInfoDialog(allTrails.get(index));
                return true;
            }
        };
    }
}