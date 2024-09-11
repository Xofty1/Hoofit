package com.tvoyhod.hoofit.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tvoyhod.hoofit.HoofitApp;
import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Trail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.ViewHolder> {
    Context context;
    List<Trail> trails;
    private OnItemLongClickListener onItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public TrailAdapter(Context context, List<Trail> trails) {
        this.context = context;
        this.trails = trails;
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TrailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailAdapter.ViewHolder holder, int position) {
        int orangeColor = ContextCompat.getColor(context, R.color.orange);
        int textColor = ContextCompat.getColor(context, R.color.text_color);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(trails.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(trails.get(position));
                    return true;
                }
                return false;
            }
        });
        holder.textName.setText(trails.get(position).getName());
        String description = trails.get(position).getDescription();
        if (description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }


        ImageView buttonLike = holder.buttonLike;
        String currentId = trails.get(position).getId();
        Trail currentTrail = null;
        final boolean[] isLiked = {false};
        for (Trail trail : HoofitApp.user.getLikedTrails()) // следует оптимизировать
        {
            if (Objects.equals(trail.getId(), currentId)) {
                currentTrail = trail;
                buttonLike.setColorFilter(ContextCompat.getColor(context, R.color.orange), PorterDuff.Mode.SRC_IN);
                isLiked[0] = true;
                break;
            }
        }
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        Trail finalCurrentTrail = currentTrail;
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimator;
                if (isLiked[0]){
                    colorAnimator = ValueAnimator.ofArgb(orangeColor, textColor);
                    HoofitApp.user.getLikedTrails().remove(finalCurrentTrail);
                }
                else{
                    colorAnimator = ValueAnimator.ofArgb(textColor, orangeColor);
                    HoofitApp.user.getLikedTrails().add(trails.get(position));
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
                isLiked[0] = !isLiked[0];
                colorAnimator.start();
                users.child(HoofitApp.user.getId()).child("likedTrails").setValue(HoofitApp.user.getLikedTrails());
            }
        });
//        holder.textDescription.setText(description);
//        holder.textLevel.setText("Сложность: " + trails.get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return trails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textLevel;
        ImageView buttonLike;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textDescription = itemView.findViewById(R.id.text_description);
            buttonLike = itemView.findViewById(R.id.buttonLike);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Trail trail);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Trail trail);
    }
}