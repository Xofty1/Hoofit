package com.tvoyhod.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvoyhod.hoofit.MainActivity;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.adapter.CommentAdapter;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.Trail;
import com.tvoyhod.hoofit.databinding.FragmentCommentBinding;
import com.tvoyhod.hoofit.ui.editInfo.EditCommentsFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
            DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("reserves");
            DatabaseReference trailsRef = reservesRef.child(reserve.getId()).child("trails");
            trail.setStars(trail.getStars()-trail.getComments().get(position).getStars());
            trailsRef.getRef().child(String.valueOf(reserve.getTrails().indexOf(trail))).child("commentsCounter").setValue(trail.getCommentsCounter());
            trailsRef.getRef().child(String.valueOf(reserve.getTrails().indexOf(trail))).child("stars").setValue(trail.getStars());
            adapter.removeItem(position);
            commentsRef.setValue(trail.getComments());
            adapter.notifyDataSetChanged();
        }
    }
}