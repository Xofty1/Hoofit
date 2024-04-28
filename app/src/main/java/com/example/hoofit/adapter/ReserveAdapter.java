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

import org.w3c.dom.Text;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {
    Context context;
    ReserveData reserves;
    private OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
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
        holder.textName.setText(reserves.getReserves().get(position).getName());
        holder.textDescription.setText(reserves.getReserves().get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return reserves.getReserves().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textDescription = itemView.findViewById(R.id.text_description);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Reserve reserve);
    }
}
