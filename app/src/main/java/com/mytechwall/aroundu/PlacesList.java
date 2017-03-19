package com.mytechwall.aroundu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlacesList extends AppCompatActivity {
    ListView listView;

    String[] icon;
    String[] name;
    String[] latitude;
    String[] longitude;
    String[] rating;
    String[] vicinity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        listView = (ListView) findViewById(R.id.list);

        Bundle b = getIntent().getExtras();
        ArrayList<MyPlace> places = (ArrayList<MyPlace>) b.get("places");
        Toast.makeText(this, places.size() + " Length", Toast.LENGTH_SHORT).show();
        name = new String[places.size()];
        icon = new String[places.size()];
        vicinity = new String[places.size()];
        rating = new String[places.size()];
        latitude = new String[places.size()];
        longitude = new String[places.size()];
        for (int i = 0; i < places.size(); i++) {
            name[i] = places.get(i).getName();
            icon[i] = places.get(i).getIcon();
            vicinity[i] = places.get(i).getVicinity();
            rating[i] = places.get(i).getRating();
            latitude[i] = places.get(i).getLatitude();
            longitude[i] = places.get(i).getLongitude();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, name);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(PlacesList.this, name[position] +" clicked", Toast.LENGTH_SHORT).show();

            }

        });
    }
}
