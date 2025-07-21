package com.example.travelplanningapp;

import android.annotation.SuppressLint;
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

public class AccommodationActivity extends AppCompatActivity {

    private EditText locationEditText;
    private AccommodationAdapter adapter;
    private List<Accommodation> accommodationList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        locationEditText = findViewById(R.id.locationEditText);
        RecyclerView accommodationRecyclerView = findViewById(R.id.accommodationRecyclerView);

        accommodationList = new ArrayList<>();
        adapter = new AccommodationAdapter(accommodationList);
        accommodationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        accommodationRecyclerView.setAdapter(adapter);

        findViewById(R.id.searchButton).setOnClickListener(view -> fetchAccommodations());
        findViewById(R.id.imageButton).setOnClickListener(view -> finish());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAccommodations() {
        String location = locationEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(location)) {
            // Dummy data fetch - Replace with actual API call
            accommodationList.clear();
            accommodationList.add(new Accommodation("Hotel A", location, 150.0, "WiFi, Pool"));
            accommodationList.add(new Accommodation("Hotel B", location, 200.0, "WiFi, Gym"));
            accommodationList.add(new Accommodation("Hotel C", location, 100.0, "WiFi, Free Breakfast"));

            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
        }
    }
}