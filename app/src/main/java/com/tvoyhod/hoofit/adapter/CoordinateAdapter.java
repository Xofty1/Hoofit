package com.tvoyhod.hoofit.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tvoyhod.hoofit.R;
import com.tvoyhod.hoofit.data.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Coordinate adapter.
 */
public class CoordinateAdapter extends RecyclerView.Adapter<CoordinateAdapter.ViewHolder> {
    private Context context;
    private List<Coordinate> coordinates;

    /**
     * Instantiates a new Coordinate adapter.
     *
     * @param context     the context
     * @param coordinates the coordinates
     */
    public CoordinateAdapter(Context context, List<Coordinate> coordinates) {
        this.context = context;
        if (coordinates != null) {
            this.coordinates = coordinates;
        } else {
            this.coordinates = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public CoordinateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coordinate_item, parent, false);
        return new CoordinateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coordinate coordinate = coordinates.get(position);
        holder.textLatitude.setText(String.valueOf(coordinate.getLatitude()));
        holder.textLongitude.setText(String.valueOf(coordinate.getLongitude()));
        holder.bind(coordinate);
    }

    @Override
    public int getItemCount() {
        return coordinates.size();
    }

    /**
     * Add coordinate.
     *
     * @param coordinate the coordinate
     */
    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
        notifyItemInserted(coordinates.size() - 1);
    }

    /**
     * Remove coordinate.
     */
    public void removeCoordinate() {
        if (!coordinates.isEmpty()) {
            coordinates.remove(coordinates.size() - 1);
            notifyItemRemoved(coordinates.size());
        }
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Text longitude.
         */
        EditText textLongitude;
        /**
         * The Text latitude.
         */
        EditText textLatitude;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textLongitude = itemView.findViewById(R.id.editTextLongitude);
            textLatitude = itemView.findViewById(R.id.editTextLatitude);
            textLatitude.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        coordinates.get(getAdapterPosition()).setLatitude(Double.parseDouble(s.toString()));
                    } catch (NumberFormatException e) {
                    }
                }
            });

            // Add TextWatcher for textLongitude
            textLongitude.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        coordinates.get(getAdapterPosition()).setLongitude(Double.parseDouble(s.toString()));
                    } catch (NumberFormatException e) {
                        // Handle the case where the input is not a valid double
                        Log.d("FFF", "Invalid longitude value");
                    }
                }
            });
        }

        /**
         * Bind.
         *
         * @param coordinate the coordinate
         */
        public void bind(Coordinate coordinate) {
            textLatitude.setText(String.valueOf(coordinate.getLatitude()));
            textLongitude.setText(String.valueOf(coordinate.getLongitude()));
        }
    }
}

