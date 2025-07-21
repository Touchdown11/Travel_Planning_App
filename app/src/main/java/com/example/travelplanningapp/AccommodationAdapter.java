package com.example.travelplanningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private List<Accommodation> accommodationList;

    public AccommodationAdapter(List<Accommodation> accommodationList) {
        this.accommodationList = accommodationList;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accommodation, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodationList.get(position);
        holder.nameTextView.setText(accommodation.getName());
        holder.locationTextView.setText(accommodation.getLocation());
        holder.priceTextView.setText("Price: $" + accommodation.getPrice());
        holder.amenitiesTextView.setText("Amenities: " + accommodation.getAmenities());
    }

    @Override
    public int getItemCount() {
        return accommodationList.size();
    }

    static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView, priceTextView, amenitiesTextView;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            amenitiesTextView = itemView.findViewById(R.id.amenitiesTextView);
        }
    }
}