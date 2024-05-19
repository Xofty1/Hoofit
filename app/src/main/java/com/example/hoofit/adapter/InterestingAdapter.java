package com.example.hoofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoofit.R;
import com.example.hoofit.data.Interesting;
import com.example.hoofit.data.Trail;

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
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(interestings.get(position));
//                }
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (onItemLongClickListener != null) {
//                    onItemLongClickListener.onItemLongClick(interestings.get(position));
//                    return true;
//                }
//                return false;
//            }
//        });
        holder.textName.setText(interestings.get(position).getName());
        holder.textDescription.setText(interestings.get(position).getDescription());
        holder.textType.setText(interestings.get(position).getType().getDisplayName());
    }


    @Override
    public int getItemCount() {
        return interestings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            textType = itemView.findViewById(R.id.textType);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Interesting interesting);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Interesting interesting);
    }

}
