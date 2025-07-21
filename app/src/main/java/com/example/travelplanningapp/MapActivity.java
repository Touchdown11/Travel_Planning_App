package com.example.travelplanningapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);  // Default OSM tile source
        mapView.setMultiTouchControls(true);

        // Get the destination name from the Intent
        String destinationName = getIntent().getStringExtra("destination_name");

        if (destinationName != null && !destinationName.isEmpty()) {
            centerMapOnLocation(destinationName);
        } else {
            // Default location if no destination is passed
            centerMapOnLocation("San Francisco");
        }
    }

    private void centerMapOnLocation(String location) {
        Geocoder geocoder = new Geocoder(this);
        try {
            // Geocode the location name to get latitude and longitude
            List<Address> addresses = geocoder.getFromLocationName(location, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Center the map on the location
                IMapController mapController = mapView.getController();
                mapController.setZoom(10);
                mapController.setCenter(new GeoPoint(latitude, longitude));

                // Add a marker at the location
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(latitude, longitude));
                marker.setTitle(location);
                mapView.getOverlays().clear();  // Remove previous markers
                mapView.getOverlays().add(marker);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoding failed", Toast.LENGTH_SHORT).show();
        }
    }
}
