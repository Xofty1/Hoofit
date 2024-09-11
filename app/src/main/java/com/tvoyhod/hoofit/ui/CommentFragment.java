package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoofit.HoofitApp;
import com.example.hoofit.MainActivity;
import com.example.hoofit.R;
import com.example.hoofit.adapter.CommentAdapter;
import com.example.hoofit.adapter.ReserveAdapter;
import com.example.hoofit.data.Comment;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.Trail;
import com.example.hoofit.databinding.FragmentCommentBinding;
import com.example.hoofit.ui.editInfo.EditCommentsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentFragment extends Fragment implements CommentAdapter.OnCommentClickListener{
    Trail trail;
    Reserve reserve;
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

            binding.buttonToComments.setOnClickListener(view -> {
                Bundle bundleToComments = new Bundle();
                bundleToComments.putSerializable("trail", trail);
                bundleToComments.putSerializable("reserve", reserve);
                EditCommentsFragment trailFragment = new EditCommentsFragment();
                trailFragment.setArguments(bundleToComments);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                MainActivity.makeTransaction(transaction, trailFragment);
            });
            adapter = new CommentAdapter(getContext(), trail.getComments(),this);
            binding.listComment.setHasFixedSize(true);
            binding.listComment.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listComment.setAdapter(adapter);
        }
        return binding.getRoot();
    }

    @Override
    public void onDeleteComment(int position) {
        if (adapter != null) {
            trail.setCommentsCounter(trail.getCommentsCounter()-1);

            trail.setStars(trail.getStars()-trail.getComments().get(position).getStars());

            adapter.removeItem(position);
            commentsRef.setValue(trail.getComments());
            adapter.notifyDataSetChanged();
        }
    }
}