package com.example.hoofit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hoofit.R;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.dataHandler.DataHandler;
import com.example.hoofit.dataHandler.DataInterface;
import com.example.hoofit.databinding.FragmentTrailBinding;
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

public class TrailFragment extends Fragment implements DataInterface {
    FragmentTrailBinding binding;
    String file;
    private TrailData trailData;
    private MapObjectCollection mapObjects;
    private MapView mapView;

    public void setTrailData(TrailData trailData) {
        this.trailData = trailData;
    }
    @Override
    public void onTrailDataReceived(TrailData trailData) {
        this.trailData = trailData;
        mapView.getMap().move(new CameraPosition(new Point(trailData.getTrails().get(0).getCoordinates().get(0).getLatitude(), trailData.getTrails().get(0).getCoordinates().get(0).getLongitude()), 5.0F, 0.0F, 0.0F));
        for (int i = 0; i < trailData.getTrails().size();i++) {
            MapObjectCollection mapObjects1 = mapView.getMap().getMapObjects().addCollection();
            List<Point> points = new ArrayList<>();
            List<Coordinate> coordinates = trailData.getTrails().get(i).getCoordinates();
            for (Coordinate coordinate : coordinates) {
                points.add(new Point(coordinate.getLatitude(), coordinate.getLongitude()));
            }
            PolylineMapObject polyline = mapObjects1.addPolyline(new Polyline(points));
            int colorRes = getResources().getColor(R.color.orange);
            polyline.setStrokeColor(colorRes);

            polyline.addTapListener(new MapObjectTapListener() {
                @Override
                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                    showTrailInfoDialog();
                    return true;
                }
            });
        }
    }

    private void showTrailInfoDialog() {
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_trail_info, null);

        TextView titleTextView = customLayout.findViewById(R.id.dialog_title);
        TextView messageTextView = customLayout.findViewById(R.id.dialog_message);
        Button okButton = customLayout.findViewById(R.id.dialog_button);

        titleTextView.setText("Информация о тропе");
        messageTextView.setText("Описание тропы, дополнительная информация и т.д.");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
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
        binding = FragmentTrailBinding.inflate(getLayoutInflater());
        mapView = binding.mapView;
//        mapObjects = mapView.getMap().getMapObjects().addCollection();
        new DataHandler(getContext(), this).execute();
       return binding.getRoot();
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
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }



}