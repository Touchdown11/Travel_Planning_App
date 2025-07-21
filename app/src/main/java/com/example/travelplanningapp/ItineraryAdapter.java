package com.example.travelplanningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {

    private final List<Itinerary> itineraryList;

    public ItineraryAdapter(List<Itinerary> itineraryList) {
        this.itineraryList = itineraryList;
    }

    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itinerary, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        Itinerary itinerary = itineraryList.get(position);
        holder.destinationTextView.setText(itinerary.getDestination());
        holder.dateTextView.setText(itinerary.getDate());
        holder.activityTextView.setText(itinerary.getActivity());
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView, dateTextView, activityTextView;

        public ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            activityTextView = itemView.findViewById(R.id.activityTextView);
        }
    }
}