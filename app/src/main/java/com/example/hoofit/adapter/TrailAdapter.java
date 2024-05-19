package com.example.hoofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoofit.R;
import com.example.hoofit.data.Trail;

import java.util.List;

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
        holder.textDescription.setText(trails.get(position).getDescription());
        holder.textLevel.setText("Сложность: " + trails.get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return trails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textLevel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textDescription = itemView.findViewById(R.id.text_description);
            textLevel = itemView.findViewById(R.id.text_level);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Trail trail);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Trail trail);
    }
}