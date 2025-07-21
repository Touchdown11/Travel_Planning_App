package com.example.travelplanningapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText destinationEditText, dateEditText, activityEditText;
    private ItineraryAdapter adapter;
    private List<Itinerary> itineraryList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        db = FirebaseFirestore.getInstance();

        destinationEditText = findViewById(R.id.destinationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        activityEditText = findViewById(R.id.activityEditText);
        RecyclerView itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView);

        itineraryList = new ArrayList<>();
        adapter = new ItineraryAdapter(itineraryList);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itineraryRecyclerView.setAdapter(adapter);

        findViewById(R.id.addButton).setOnClickListener(view -> addItinerary());
        findViewById(R.id.clearButton).setOnClickListener(view -> clearItinerary());
        findViewById(R.id.imageButton).setOnClickListener(view -> finish());

        loadItinerary();
    }

    private void addItinerary() {
        String destination = destinationEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String activity = activityEditText.getText().toString().trim();

        if (TextUtils.isEmpty(destination)) {
            Toast.makeText(this, "Please enter a destination.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please enter a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(activity)) {
            Toast.makeText(this, "Please enter an activity.", Toast.LENGTH_SHORT).show();
            return;
        }

        Itinerary itinerary = new Itinerary(destination, date, activity);
        db.collection("itineraries").add(itinerary)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ItineraryActivity.this, "Itinerary added!", Toast.LENGTH_SHORT).show();
                    loadItinerary();
                })
                .addOnFailureListener(e -> {
                    Log.e("ItineraryActivity", "Error adding itinerary: ", e);
                    Toast.makeText(ItineraryActivity.this, "Failed to add itinerary!", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadItinerary() {
        db.collection("itineraries").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itineraryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Itinerary itinerary = document.toObject(Itinerary.class);
                            itineraryList.add(itinerary);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("ItineraryActivity", "Error loading itineraries", task.getException());
                        Toast.makeText(ItineraryActivity.this, "Failed to load itinerary!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearItinerary() {
        db.collection("itineraries").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("itineraries").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Update UI after each successful deletion
                                        Toast.makeText(ItineraryActivity.this, "All itineraries cleared!", Toast.LENGTH_SHORT).show();
                                        itineraryList.clear();
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ItineraryActivity", "Error deleting itinerary", e);
                                        Toast.makeText(ItineraryActivity.this, "Failed to clear itinerary!", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.e("ItineraryActivity", "Error loading itineraries for clearing", task.getException());
                        Toast.makeText(ItineraryActivity.this, "Failed to load itineraries for clearing!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
