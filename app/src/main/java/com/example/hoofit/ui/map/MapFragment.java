package com.example.hoofit.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.ReserveData;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.DialogTrailInfoBinding;
import com.example.hoofit.databinding.FragmentMapBinding;
import com.example.hoofit.ui.infoTrail.InfoTrailFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private MapViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    public static boolean isMapFragment;
    FragmentMapBinding binding;
    ReserveData reserves = HoofitApp.reserves;
    private MapObjectTapListener[] mapObjectTapListeners;
    private MapObjectTapListener mapTrailListener;
    List<Trail> allTrails = HoofitApp.allTrails;
    private AlertDialog dialog = null;
    private MapObjectCollection mapObjects;
    private MapView mapView;
    Location currentLocation = null;
    private PlacemarkMapObject userLocationMarker;


    private void showTrailInfoDialog(Trail trail, PolylineMapObject polyline) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        DialogTrailInfoBinding bindingInfo = DialogTrailInfoBinding.inflate(getLayoutInflater());

        bindingInfo.dialogTitle.setText(trail.getName());
        bindingInfo.dialogMessage.setText(trail.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(bindingInfo.getRoot());

        dialog = builder.create();

        // Установка начального масштаба на 0
        bindingInfo.getRoot().setScaleX(0);
        bindingInfo.getRoot().setScaleY(0);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // Анимация масштабирования при появлении диалогового окна
                bindingInfo.getRoot().animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(300)
                        .start();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bindingInfo.buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                polyline.setStrokeColor(getResources().getColor(R.color.orange));
            }
        });

        bindingInfo.buttonToTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                polyline.setStrokeColor(getResources().getColor(R.color.orange));
                InfoTrailFragment fragment = new InfoTrailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trail", trail);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates(); // Остановить обновление местоположения при переходе на другой фрагмент
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates(); // Возобновить обновление местоположения при возврате на фрагмент
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
//        viewModel.loadTrails();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapKitFactory.initialize(requireContext());

        binding = FragmentMapBinding.inflate(getLayoutInflater());
        mapView = binding.mapView;
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        requestLocationPermission();
        Bundle bundle = getArguments();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateMarker(new Point(location.getLatitude(), location.getLongitude()));

                    Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    if (currentLocation == null && bundle == null)
                    {
                        mapView.getMap().move(new CameraPosition(new Point(location.getLatitude(), location.getLongitude()), 5.0F, 0.0F, 0.0F));
                    }
                    currentLocation = location;
                }
            }
        };
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }

        if (bundle != null) {
            Trail trail = (Trail) bundle.getSerializable("trail");
            makeTrail(trail);
        } else {
            makeMap();
        }

        return binding.getRoot();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на местоположение получено, начать обновления местоположения
                startLocationUpdates();
            } else {
                // Разрешение на местоположение не было предоставлено, обработать это событие соответственно
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // Метод для создания запроса местоположения

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
    private void updateMarker(Point point) {
        if (userLocationMarker == null) {
            userLocationMarker = mapObjects.addPlacemark(point, ImageProvider.fromResource(requireContext(), R.drawable.search_result));
        } else {
            userLocationMarker.setGeometry(point);
        }

        // Установка размеров изображения маркера
        userLocationMarker.setIconStyle(new IconStyle().setScale(0.7f)); // Установка масштаба изображения маркера
        // Установка других свойств стиля маркера, если необходимо
    }

    public void makeMap() {

//        mapView.getMap().move(new CameraPosition(new Point(reserves.getReserves().get(0).getTrails().get(0).getCoordinatesList().get(0).getLatitude(), reserves.getReserves().get(0).getTrails().get(0).getCoordinatesList().get(0).getLongitude()), 5.0F, 0.0F, 0.0F));

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
            mapObjectTapListeners[i] = createTapListener(allTrails.get(i), polyline); // Сохраняем ссылку на созданный слушатель
            polyline.addTapListener(mapObjectTapListeners[i]); // Используем сохраненный слушатель
            polyline.setStrokeColor(getResources().getColor(R.color.orange));
        }
    }

    public void makeTrail(Trail trail) {
        mapView.getMap().move(new CameraPosition(new Point(trail.getCoordinatesList().get(0).getLatitude(), trail.getCoordinatesList().get(0).getLongitude()), 8.0F, 0.0F, 0.0F));
        List<Coordinate> coordinates = trail.getCoordinatesList();
        List<Point> points = new ArrayList<>();

        for (Coordinate coordinate : coordinates) {
            points.add(new Point(coordinate.getLatitude(), coordinate.getLongitude()));
        }

        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(points));
        mapTrailListener = createTapListener(trail, polyline);
        polyline.addTapListener(mapTrailListener);
        polyline.setStrokeColor(getResources().getColor(R.color.orange));
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();

    }

    private MapObjectTapListener createTapListener(final Trail trail, final PolylineMapObject polyline) {
        return new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                polyline.setStrokeColor(getResources().getColor(R.color.dark_blue));
                showTrailInfoDialog(trail, polyline);
                return true;
            }
        };
    }
}