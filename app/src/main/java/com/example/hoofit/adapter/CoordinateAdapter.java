package com.example.hoofit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoofit.R;
import com.example.hoofit.data.Coordinate;
import com.example.hoofit.data.Trail;

import java.util.ArrayList;
import java.util.List;

public class CoordinateAdapter extends RecyclerView.Adapter<CoordinateAdapter.ViewHolder> {
    private Context context;
    private List<Coordinate> coordinates;

    public CoordinateAdapter(Context context, List<Coordinate> coordinates) {
        this.context = context;
        if (coordinates != null)
            this.coordinates = coordinates;
        else {this.coordinates = new ArrayList<>();
            Log.d("FFF", "создан массив для координат");
            Coordinate coordinate = new Coordinate(5.6,15.5454);
            this.coordinates.add(coordinate);
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
        holder.textLatitude.setText(String.valueOf(coordinates.get(position).getLatitude()));
        holder.textLongitude.setText(String.valueOf(coordinates.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return coordinates.size();
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
        notifyItemInserted(coordinates.size() - 1);
        Log.d("FFF", "Размер массива " + coordinates.size() );
    }

    public void removeCoordinate() {
        if (!coordinates.isEmpty()) {
            coordinates.remove(coordinates.size() - 1);
            notifyItemRemoved(coordinates.size());
        }
    }

    public List<Coordinate> getCoordinates() {
        List<Coordinate> currentCoordinates = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            currentCoordinates.add(new Coordinate(coordinate.getLatitude(), coordinate.getLongitude()));
        }
        return currentCoordinates;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText textLongitude;
        EditText textLatitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textLongitude = itemView.findViewById(R.id.editTextLongitude);
            textLatitude = itemView.findViewById(R.id.editTextLatitude);
        }
    }
}
