package com.example.travelplanningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button logoutButton = findViewById(R.id.logoutButton);
        FirebaseUser currentUser = mAuth.getCurrentUser();if (currentUser != null) {
            welcomeTextView.setText("Welcome, " + currentUser.getEmail() + "!");
        }

        // Setting click listeners for each button to start respective activities
        /*findViewById(R.id.profileButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));*/
        findViewById(R.id.itineraryButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, ItineraryActivity.class)));
        findViewById(R.id.destinationButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, DestinationsActivity.class)));
        findViewById(R.id.accommodationButton).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, AccommodationActivity.class)));
        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
