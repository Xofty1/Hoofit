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

import java.util.List;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(interestings.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(interestings.get(position));
                    return true;
                }
                return false;
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + interestings.get(position).getId());
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
            }
        });
        String description = interestings.get(position).getDescription();
        if (description.length() > 30) {
            description = description.substring(0, 27) + "...";
        }
        holder.textName.setText(interestings.get(position).getName());
        holder.textDescription.setText(description);
//        holder.textType.setVisibility(View.INVISIBLE);
//        holder.textType.setText(interestings.get(position).getType());
    }
//@Override
//public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//    holder.itemView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (itemClickListener != null) {
//                itemClickListener.onItemClick(interestings.get(position));
//            }
//        }
//    });
//    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//        @Override
//        public boolean onLongClick(View v) {
//            if (onItemLongClickListener != null) {
//                onItemLongClickListener.onItemLongClick(interestings.get(position));
//                return true;
//            }
//            return false;
//        }
//    });
//
//    String imageId = interestings.get(position).getId();
//    Bitmap savedImage = Utils.getImageFromPreferences(context, imageId);
//
//    if (savedImage != null) {
//        holder.image.setImageBitmap(savedImage);
//    } else {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference imageRef = storageRef.child("images/" + imageId);
//        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(context)
//                        .asBitmap()
//                        .load(uri)
//                        .into(new CustomTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                holder.image.setImageBitmap(resource);
//                                Utils.saveImageToPreferences(context, imageId, resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//                                // This is called when the View is cleared.
//                                // Make sure to remove any references to the bitmap here.
//                            }
//                        });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Обработка ошибок при загрузке изображения
//            }
//        });
//    }
//
//    String description = interestings.get(position).getDescription();
//    if (description.length() > 30) {
//        description = description.substring(0, 27) + "...";
//    }
//    holder.textName.setText(interestings.get(position).getName());
//    holder.textDescription.setText(description);
//}


    @Override
    public int getItemCount() {
        return interestings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
//        TextView textType;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
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
