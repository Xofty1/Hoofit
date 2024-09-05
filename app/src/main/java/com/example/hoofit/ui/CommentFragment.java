package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.R;
import com.example.hoofit.adapter.CommentAdapter;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.data.Comment;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentFragment extends Fragment implements CommentAdapter.OnCommentClickListener{

    Trail trail;
    Reserve reserve;
    Comment comment;
    CommentAdapter adapter;
    DatabaseReference commentsRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCommentBinding binding = FragmentCommentBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        if (bundle != null) {
             trail = (Trail) bundle.getSerializable("trail");
             reserve = (Reserve) bundle.getSerializable("reserve");
            if (trail.getComments() == null)
                trail.setComments(new ArrayList<>());
            DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
            DatabaseReference trailsRef = reservesRef.child(reserve.getId()).child("trails");
             commentsRef = trailsRef.child(String.valueOf(reserve.getTrails().indexOf(trail))).child("comments");


            adapter = new CommentAdapter(getContext(), trail.getComments(),this);
            binding.listComment.setHasFixedSize(true);
            binding.listComment.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listComment.setAdapter(adapter);

            binding.buttonAddComment.setOnClickListener(view -> {
                String message = binding.editTextAddMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
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

// Обновите базу данных
//                for (Comment commentC : trail.getComments()) {
//                    // Используйте уникальный идентификатор как ключ
//                    commentsRef.child(commentC.getId()).setValue(commentC)
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    Log.d("Firebase", "Comment saved successfully.");
//                                } else {
//                                    Log.e("Firebase", "Failed to save comment", task.getException());
//                                }
//                            });
//                }

//                // Сохраняем обновленный список комментариев
                commentsRef.setValue(trail.getComments()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to add comment: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", "Error adding comment", task.getException());
                    }
                });
            });


        }
        return binding.getRoot();
    }

    @Override
    public void onDeleteComment(int position) {
        if (adapter != null) {
            trail.setCommentsCounter(trail.getCommentsCounter()-1);
            trail.setStars(trail.getStars()-trail.getComments().get(position).getStars());
            adapter.removeItem(position);
//            trail.getComments().remove(position);

            commentsRef.setValue(trail.getComments());
//             DatabaseReference commentRef = FirebaseDatabase.getInstance()
//                    .getReference("reserves")
//                    .child(reserve.getId())
//                    .child("trails")
//                    .child(trail.getId())
//                    .child("comments")
//                    .child(trail.getComments().get(position).getId());
//             commentRef.removeValue();
        }
    }
}