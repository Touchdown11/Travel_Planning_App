package com.example.travelplanningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private List<Destination> destinationList;

    public DestinationAdapter(List<Destination> destinationList) {
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.nameTextView.setText(destination.getName());
        holder.descriptionTextView.setText(destination.getDescription());
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}