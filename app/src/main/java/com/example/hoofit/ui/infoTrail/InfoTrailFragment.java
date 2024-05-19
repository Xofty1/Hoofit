package com.example.hoofit.ui.infoTrail;


import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hoofit.AuthActivity;
import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentInfoTrailBinding;
import com.example.hoofit.ui.map.MapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class InfoTrailFragment extends Fragment {
    private FragmentInfoTrailBinding binding;
    private InfoTrailViewModel viewModel;
    boolean isLiked = false;

    public void setViewModel(InfoTrailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setBinding(FragmentInfoTrailBinding binding) {
        this.binding = binding;
    }

    public FragmentInfoTrailBinding getBinding() {
        return binding;
    }

    public InfoTrailViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InfoTrailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoTrailBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Trail trail = (Trail) bundle.getSerializable("trail");
            viewModel.setTrail(trail);
        }

        viewModel.getTrailLiveData().observe(getViewLifecycleOwner(), trail -> {
            if (trail != null) {
                binding.textName.setText(trail.getName());
                binding.textDescription.setText(trail.getDescription());
                binding.textDifficulty.setText("Cложность: " + trail.getDifficulty());
                binding.textLength.setText("Расстояние: " + String.valueOf(trail.getLength()) + " км");
                binding.textTimeRequired.setText("Требуемое время: " + trail.getTimeRequired());
            }
        });

        binding.buttonToMap.setOnClickListener(view -> {
            Trail selectedTrail = viewModel.getTrailLiveData().getValue();

            if (selectedTrail != null) {
                MapFragment fragment = new MapFragment();
                Bundle bundleToMap = new Bundle();
                bundleToMap.putSerializable("trail", selectedTrail);
                fragment.setArguments(bundleToMap);
                viewModel.getTrailLiveData().getValue();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            }
        });
        ImageView buttonLike = binding.buttonLike;
        String currentId = viewModel.getTrailLiveData().getValue().getId();
        Trail currentTrail = null;
        for (Trail trail : HoofitApp.user.getLikedTrails()) // следует оптимизировать
        {
            if (Objects.equals(trail.getId(), currentId)) {
                currentTrail = trail;
                buttonLike.setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);
                isLiked = true;
                break;
            }
        }
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        Trail finalCurrentTrail = currentTrail;
        binding.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimator;
                if (isLiked){
                    colorAnimator = ValueAnimator.ofArgb(getResources().getColor(R.color.orange), Color.parseColor("#F0F3FF"));
                    HoofitApp.user.getLikedTrails().remove(finalCurrentTrail);
                }
                else{
                    colorAnimator = ValueAnimator.ofArgb(Color.parseColor("#F0F3FF"), getResources().getColor(R.color.orange));
                    HoofitApp.user.getLikedTrails().add(viewModel.getTrailLiveData().getValue());
                }
                colorAnimator.setDuration(200); // Устанавливаем длительность анимации

                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        // Получаем текущий цвет анимации
                        int color = (int) valueAnimator.getAnimatedValue();
                        // Устанавливаем новый цвет сердца
                        buttonLike.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    }
                });
                isLiked = !isLiked;
                colorAnimator.start();
                users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
            }
        });
        return binding.getRoot();
    }
}
