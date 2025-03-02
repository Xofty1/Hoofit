package com.tvoyhod.hoofit.ui.infoTrail;


import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.Trail;
import com.tvoyhod.hoofit.databinding.FragmentInfoTrailBinding;
import com.tvoyhod.hoofit.ui.CommentFragment;
import com.tvoyhod.hoofit.ui.map.MapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


/**
 * The type Info trail fragment.
 */
public class InfoTrailFragment extends Fragment {
    private FragmentInfoTrailBinding binding;
    private InfoTrailViewModel viewModel;
    /**
     * The Is liked.
     */
    boolean isLiked = false;

    /**
     * Sets view model.
     *
     * @param viewModel the view model
     */
    public void setViewModel(InfoTrailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Sets binding.
     *
     * @param binding the binding
     */
    public void setBinding(FragmentInfoTrailBinding binding) {
        this.binding = binding;
    }

    /**
     * Gets binding.
     *
     * @return the binding
     */
    public FragmentInfoTrailBinding getBinding() {
        return binding;
    }

    /**
     * Gets view model.
     *
     * @return the view model
     */
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
            Reserve reserve = (Reserve) bundle.getSerializable("reserve");
            viewModel.setReserve(reserve);
            viewModel.setTrail(trail);


            viewModel.getTrailLiveData().observe(getViewLifecycleOwner(), trailRunner -> {
                if (trailRunner != null) {
                    binding.textName.setText(trailRunner.getName());
                    binding.textDescription.setText(trailRunner.getDescription());
                    binding.textDifficulty.setText("Cложность: " + trailRunner.getDifficulty());
                    binding.textLength.setText("Расстояние: " + String.valueOf(trailRunner.getLength()) + " км");
                    binding.textTimeRequired.setText("Требуемое время: " + trailRunner.getTimeRequired());
                }
            });
            Trail selectedTrail = viewModel.getTrailLiveData().getValue();
            if (reserve == null){

                DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
                reservesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("FirebaseError222", "Ошибка базы ");
                        for (DataSnapshot reserveSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot trailsSnapshot = reserveSnapshot.child("trails");

                            for (DataSnapshot trailSnapshot : trailsSnapshot.getChildren()) {
                                Trail trailSearch = trailSnapshot.getValue(Trail.class);

                                if (trailSearch != null && trailSearch.getId().equals(trail.getId())) {
                                    Reserve foundReserve = reserveSnapshot.getValue(Reserve.class);

                                    // Обновляем через ViewModel
                                    viewModel.setReserve(foundReserve);

                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseError222", "Ошибка базы данных: " + databaseError.getMessage());
                    }
                });
            }
            if (trail.getComments() != null) {
                if (trail.getComments().size() != 0)
                    binding.textViewRating.setText("Средняя оценка: " + String.valueOf(trail.getStars() / trail.getCommentsCounter()));
                else {
                    binding.textViewRating.setText("Оценок пока нет");

                }
            }

            binding.buttonToMap.setOnClickListener(view -> {


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

            binding.buttonToComments.setOnClickListener(view -> {
                CommentFragment fragment = new CommentFragment();
                Bundle bundleToComments = new Bundle();
                bundleToComments.putSerializable("trail", selectedTrail);
                bundleToComments.putSerializable("reserve", viewModel.getReserveLiveData().getValue());
                fragment.setArguments(bundleToComments);
                viewModel.getTrailLiveData().getValue();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, fragment);
            });



    }
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
        int textColor = ContextCompat.getColor(getContext(), R.color.text_color);
        binding.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimator;
                if (isLiked){
                    colorAnimator = ValueAnimator.ofArgb(getResources().getColor(R.color.orange), textColor);
                    HoofitApp.user.getLikedTrails().remove(finalCurrentTrail);
                }
                else{
                    colorAnimator = ValueAnimator.ofArgb(textColor, getResources().getColor(R.color.orange));
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
