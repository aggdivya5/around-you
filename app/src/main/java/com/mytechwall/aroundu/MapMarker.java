package com.mytechwall.aroundu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarker extends AppCompatActivity
        implements OnMapReadyCallback {

    Double lat;
    Double lng;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        Bundle b = getIntent().getExtras();
        lat = b.getDouble("latitude");
        lng = b.getDouble("longitude");
        name = b.getString("name");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}