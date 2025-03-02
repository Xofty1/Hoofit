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

/**
 * The type Reserve adapter.
 */
public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {
    /**
     * The Context.
     */
    Context context;
    /**
     * The Reserves.
     */
    ReserveData reserves;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * Sets on item click listener.
     *
     * @param listener the listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Sets on item long click listener.
     *
     * @param listener the listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * Instantiates a new Reserve adapter.
     *
     * @param context  the context
     * @param reserves the reserves
     */
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

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Text name.
         */
        TextView textName;
        /**
         * The Text count of trail.
         */
        TextView textCountOfTrail;
        /**
         * The Image.
         */
        ImageView image;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textCountOfTrail = itemView.findViewById(R.id.textCountOfTrail);
            image = itemView.findViewById(R.id.imageView);

        }
    }

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param reserve the reserve
         */
        void onItemClick(Reserve reserve);
    }

    /**
     * The interface On item long click listener.
     */
    public interface OnItemLongClickListener {
        /**
         * On item long click.
         *
         * @param reserve the reserve
         */
        void onItemLongClick(Reserve reserve);
    }

}
