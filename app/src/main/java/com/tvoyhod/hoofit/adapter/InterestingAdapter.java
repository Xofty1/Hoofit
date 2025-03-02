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
import com.tvoyhod.hoofit.data.Interesting;
import com.tvoyhod.hoofit.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

/**
 * The type Interesting adapter.
 */
public class InterestingAdapter extends RecyclerView.Adapter<InterestingAdapter.ViewHolder> {
    /**
     * The Context.
     */
    Context context;
    /**
     * The Interestings.
     */
    List<Interesting> interestings;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * Sets on item long click listener.
     *
     * @param listener the listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * Instantiates a new Interesting adapter.
     *
     * @param context      the context
     * @param interestings the interestings
     */
    public InterestingAdapter(Context context, List<Interesting> interestings) {
        this.context = context;
        this.interestings = interestings;
    }

    private OnItemClickListener itemClickListener;

    /**
     * Sets on item click listener.
     *
     * @param itemClickListener the item click listener
     */
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

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Text name.
         */
        TextView textName;
        /**
         * The Text type.
         */
        TextView textType;
        /**
         * The Image.
         */
//        TextView textType;
        ImageView image;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textType = itemView.findViewById(R.id.textType);
            image = itemView.findViewById(R.id.image);
        }
    }

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param interesting the interesting
         */
        void onItemClick(Interesting interesting);
    }

    /**
     * The interface On item long click listener.
     */
    public interface OnItemLongClickListener {
        /**
         * On item long click.
         *
         * @param interesting the interesting
         */
        void onItemLongClick(Interesting interesting);
    }
}
