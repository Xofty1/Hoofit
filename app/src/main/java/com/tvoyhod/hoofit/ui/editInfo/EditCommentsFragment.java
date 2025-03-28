package com.tvoyhod.hoofit.ui.editInfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Comment;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.Trail;
import com.tvoyhod.hoofit.databinding.FragmentCommentBinding;
import com.tvoyhod.hoofit.databinding.FragmentEditCommentsBinding;
import com.tvoyhod.hoofit.ui.CommentFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * The type Edit comments fragment.
 */
public class EditCommentsFragment extends Fragment {
    /**
     * The Trail.
     */
    Trail trail;
    /**
     * The Reserve.
     */
    Reserve reserve;
    /**
     * The Comment.
     */
    Comment comment;
    /**
     * The Comments ref.
     */
    DatabaseReference commentsRef;

    /**
     * New instance edit comments fragment.
     *
     * @param param1 the param 1
     * @param param2 the param 2
     * @return the edit comments fragment
     */
    public static EditCommentsFragment newInstance(String param1, String param2) {
        EditCommentsFragment fragment = new EditCommentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditCommentsBinding binding = FragmentEditCommentsBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        if (bundle != null) {
            trail = (Trail) bundle.getSerializable("trail");
            reserve = (Reserve) bundle.getSerializable("reserve");
            if (trail.getComments() == null)
                trail.setComments(new ArrayList<>());
            DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
            DatabaseReference trailsRef = reservesRef.child(reserve.getId()).child("trails");
            commentsRef = trailsRef.child(String.valueOf(reserve.getTrails().indexOf(trail))).child("comments");



            binding.buttonAddComment.setOnClickListener(view -> {
                String message = binding.editTextAddMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(getContext(), "Отзыв не должен быть пустым", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = commentsRef.push().getKey();  // Используем ключ из commentsRef для генерации уникального id
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                comment = new Comment(id, message, HoofitApp.user, binding.ratingBar.getRating(), dateFormat.format(currentDate));
                // Добавляем комментарий в локальный список
                trail.getComments().add(comment);
                trail.setStars(trail.getStars()+comment.getStars());


                trail.setCommentsCounter(trail.getCommentsCounter()+1);
                trailsRef.getRef().child(String.valueOf(reserve.getTrails().indexOf(trail))).child("commentsCounter").setValue(trail.getCommentsCounter());
                trailsRef.getRef().child(String.valueOf(reserve.getTrails().indexOf(trail))).child("stars").setValue(trail.getStars());
//                // Сохраняем обновленный список комментариев
                commentsRef.setValue(trail.getComments()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                        Bundle bundleTrail = new Bundle();
                        bundleTrail.putSerializable("trail", trail);
                        bundleTrail.putSerializable("reserve", reserve);
                        CommentFragment trailFragment = new CommentFragment();
                        trailFragment.setArguments(bundleTrail);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        MainActivity.makeTransaction(transaction, trailFragment);
                    } else {
                        Toast.makeText(getContext(), "Failed to add comment: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", "Error adding comment", task.getException());
                    }
                });
            });


        }
        return binding.getRoot();
    }
}