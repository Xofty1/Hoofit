package com.example.hoofit.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hoofit.R;
import com.example.hoofit.data.Reserve;
import com.example.hoofit.data.ReserveData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        holder.textName.setText(reserves.getReserves().get(position).getName());
        String description = reserves.getReserves().get(position).getDescription();
        if (description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }
        holder.textDescription.setText(description);
        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Получаем ссылку на изображение в Firebase Storage
        StorageReference imageRef = storageRef.child("images/" + reserves.getReserves().get(position).getId());

// Получаем URI изображения и загружаем его в ImageView
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Загружаем изображение в ImageView
                Glide.with(context)
                        .load(uri)
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка ошибок при загрузке изображения
//                Log.e(TAG, "Ошибка загрузки изображения: " + exception.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reserves.getReserves().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textDescription = itemView.findViewById(R.id.text_description);
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
