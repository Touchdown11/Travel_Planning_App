package com.example.travelplanningapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class DestinationsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText preferencesEditText;
    private RecyclerView destinationsRecyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        db = FirebaseFirestore.getInstance();

        preferencesEditText = findViewById(R.id.preferencesEditText);
        destinationsRecyclerView = findViewById(R.id.destinationsRecyclerView);

        destinationList = new ArrayList<>();
        adapter = new DestinationAdapter(destinationList);
        destinationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        destinationsRecyclerView.setAdapter(adapter);

        findViewById(R.id.searchDestinationsButton).setOnClickListener(view -> fetchDestinations());
        findViewById(R.id.openMapButton).setOnClickListener(view -> openMap());
        findViewById(R.id.imageButton).setOnClickListener(view -> finish());
    }

    private void fetchDestinations() {
        String preferences = preferencesEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(preferences)) {
            // Dummy data fetch - Replace with actual API call
            destinationList.clear();
            destinationList.add(new Destination("Beach Paradise", "A serene beach destination", 37.7749, -122.4194));
            destinationList.add(new Destination("Mountain Escape", "A picturesque mountain retreat", 34.0522, -118.2437));
            destinationList.add(new Destination("City Lights", "Vibrant urban nightlife", 40.7128, -74.0060));

            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Please enter your preferences.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMap() {
        // Get the selected destination (this can be from a list or user input)
        String selectedDestination = preferencesEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(selectedDestination)) {
            // Pass the destination name to MapActivity
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("destination_name", selectedDestination);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter your preferences.", Toast.LENGTH_SHORT).show();
        }
    }
}