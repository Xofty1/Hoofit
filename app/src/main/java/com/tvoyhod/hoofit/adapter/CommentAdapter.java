package com.tvoyhod.hoofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Comment;
import com.tvoyhod.hoofit.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;
    private OnCommentClickListener listener;
    public interface OnCommentClickListener {
        void onDeleteComment(int position);
    }
    public CommentAdapter(Context context, List<Comment> comments, OnCommentClickListener listener) {
        this.context = context;
        this.comments = comments;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        if (HoofitApp.user.isAdmin()){
            holder.deleteComment.setVisibility(View.VISIBLE);
        }
        holder.deleteComment.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteComment(position);
            }
        });
        holder.textName.setText(comments.get(position).getUser().getName());
        holder.textDate.setText(comments.get(position).getDate());
        String message = comments.get(position).getMessage();
        holder.textMessage.setText(message);
        holder.ratingBar.setRating(comments.get(position).getStars());
        String imageId = comments.get(position).getUser().getId();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + imageId);
        Utils.loadImage(context, imageId, holder.image, imageRef);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textMessage;
        TextView textDate;
        RatingBar ratingBar;
        ImageView image;
        ImageView deleteComment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteComment = itemView.findViewById(R.id.delete_comment);
            textName = itemView.findViewById(R.id.text_name);
            textMessage = itemView.findViewById(R.id.text_message);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            image = itemView.findViewById(R.id.imageView_avatar);
            textDate = itemView.findViewById(R.id.text_date);

        }
    }
    public void removeItem(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }
}