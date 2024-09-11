package com.tvoyhod.hoofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Reserve;
import com.tvoyhod.hoofit.data.ReserveData;
import com.tvoyhod.hoofit.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {
    Context context;
    ReserveData reserves;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public ReserveAdapter(Context context, ReserveData reserves) {
        this.context = context;
        this.reserves = reserves;
    }

    @NonNull
    @Override
    public ReserveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserve_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(reserves.getReserves().get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(reserves.getReserves().get(position));
                    return true;
                }
                return false;
            }
        });
        String imageId = reserves.getReserves().get(position).getId();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + imageId);
        Utils.loadImage(context, imageId, holder.image, imageRef);

        holder.textName.setText(reserves.getReserves().get(position).getName());
        String description = reserves.getReserves().get(position).getDescription();
        if (description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }
        String marsh = " маршрутов";
        int counter = reserves.getReserves().get(position).getTrails().size();
        if (counter == 1){
            marsh = " маршрут";
        }
        else if (counter >= 2 && counter <= 4){
            marsh = " маршрута";
        }
        holder.textCountOfTrail.setText(String.valueOf(counter) + marsh);

    }

    @Override
    public int getItemCount() {
        return reserves.getReserves().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textCountOfTrail;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textCountOfTrail = itemView.findViewById(R.id.textCountOfTrail);
            image = itemView.findViewById(R.id.imageView);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Reserve reserve);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Reserve reserve);
    }

}
