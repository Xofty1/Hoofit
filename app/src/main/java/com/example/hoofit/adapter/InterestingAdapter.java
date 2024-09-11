package com.example.hoofit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hoofit.R;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.utils.Utils;
import com.example.hoofit.data.Trail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.List;
import java.util.Objects;

public class InterestingAdapter extends RecyclerView.Adapter<InterestingAdapter.ViewHolder> {
    Context context;
    List<Interesting> interestings;
    private OnItemLongClickListener onItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public InterestingAdapter(Context context, List<Interesting> interestings) {
        this.context = context;
        this.interestings = interestings;
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public InterestingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interesting_item, parent, false);
        return new InterestingAdapter.ViewHolder(view);
    }
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Interesting interesting = interestings.get(position);

    holder.itemView.setOnClickListener(v -> {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(interesting);
        }
    });

    holder.itemView.setOnLongClickListener(v -> {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(interesting);
            return true;
        }
        return false;
    });

    String imageId = interesting.getId();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef = storageRef.child("images/" + imageId);
    Utils.loadImage(context, imageId, holder.image, imageRef);

    String description = interesting.getDescription();
    if (description.length() > 30) {
        description = description.substring(0, 27) + "...";
    }
    holder.textName.setText(interesting.getName());
    if (Objects.equals(interesting.getType(), "RESERVE"))
        holder.textType.setText("Заповедник");
    else if (Objects.equals(interesting.getType(), "TRAIL"))
        holder.textType.setText("Тропа");
    else
        holder.textType.setText("Интернет-ресурс");
}



    @Override
    public int getItemCount() {
        return interestings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textType;
//        TextView textType;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textType = itemView.findViewById(R.id.textType);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Interesting interesting);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Interesting interesting);
    }
}
