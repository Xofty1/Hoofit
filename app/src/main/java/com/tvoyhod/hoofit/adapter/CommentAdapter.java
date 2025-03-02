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

/**
 * The type Comment adapter.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    /**
     * The Context.
     */
    Context context;
    /**
     * The Comments.
     */
    List<Comment> comments;
    private OnCommentClickListener listener;

    /**
     * The interface On comment click listener.
     */
    public interface OnCommentClickListener {
        /**
         * On delete comment.
         *
         * @param position the position
         */
        void onDeleteComment(int position);
    }

    /**
     * Instantiates a new Comment adapter.
     *
     * @param context  the context
     * @param comments the comments
     * @param listener the listener
     */
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

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Text name.
         */
        TextView textName;
        /**
         * The Text message.
         */
        TextView textMessage;
        /**
         * The Text date.
         */
        TextView textDate;
        /**
         * The Rating bar.
         */
        RatingBar ratingBar;
        /**
         * The Image.
         */
        ImageView image;
        /**
         * The Delete comment.
         */
        ImageView deleteComment;


        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
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

    /**
     * Remove item.
     *
     * @param position the position
     */
    public void removeItem(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }
}