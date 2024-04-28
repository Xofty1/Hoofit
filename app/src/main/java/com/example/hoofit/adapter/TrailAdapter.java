package com.example.hoofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoofit.R;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.ViewHolder> {
    Context context;
    Reserve reserve;

    public TrailAdapter(Context context, Reserve reserve) {
        this.context = context;
        this.reserve = reserve;
    }

    @NonNull
    @Override
    public TrailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailAdapter.ViewHolder holder, int position) {
        holder.textName.setText(reserve.getTrails().get(position).getName());
        holder.textDescription.setText(reserve.getTrails().get(position).getDescription());
        holder.textLevel.setText(reserve.getTrails().get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return reserve.getTrails().size();
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
}