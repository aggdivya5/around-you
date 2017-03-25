package com.mytechwall.aroundu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dummy extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, LocationListener {

    int prog = 0;
    String[] mTestArray;
    String distance = "10";
    String type;
    Button button;
    private String REGISTER_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.820762,75.200446&radius=500&type=restaurant&key=AIzaSyAFK3BjW-X9-K0gegSsC2GjltQGfw1NGUc";
    ArrayList<MyPlace> places;
    int category = -1;
    SeekBar seekBar1;
    EditText dist;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    Double lat = 0.0;
    Double lng = 0.0;
    boolean done = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        button = (Button) findViewById(R.id.button);
        places = new ArrayList<>();
        dist = (EditText) findViewById(R.id.ed_distance);
        mTestArray = getResources().getStringArray(R.array.categories);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar);
        seekBar1.setOnSeekBarChangeListener(this);
        type = " ";
        Toast.makeText(getApplicationContext(), "Maximum allowed distance is 5 kms", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            lng = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            done = true;
        } else if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            lng = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            done = true;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer.parseInt(dist.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(dist.getText().toString()) > 5000) {
                    Toast.makeText(Dummy.this, "Too large distance", Toast.LENGTH_SHORT).show();
                } else {
                    REGISTER_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                    REGISTER_URL += "location=" + lat + "," + lng;
                    distance = dist.getText().toString();
                    REGISTER_URL += "&radius=" + distance;
                    new MaterialDialog.Builder(Dummy.this)
                            .title("Category")
                            .items(R.array.categories)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    category = which;
                                    type = mTestArray[category];
                                    REGISTER_URL += "&type=" + type + "&key=AIzaSyAFK3BjW-X9-K0gegSsC2GjltQGfw1NGUc";

                                    try {
                                        getdata();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Dummy.this, "JSON Exception", Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                }
                            })
                            .positiveText("Go")
                            .show();
                }


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Dummy.this, "request code is 0 ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getdata() throws JSONException {
        StringRequest stringRequest = (StringRequest) new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        // pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            JSONArray results = jsonObject.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {

                                JSONObject place = results.getJSONObject(i);
                                Log.i("Dummy :", "1");

                                String name = place.getString("name");
                                Log.i("Dummy :", "2");

                                String rating;
                                try {
                                    rating = place.getString("rating");
                                } catch (Exception e) {
                                    rating = "No Rating";
                                }
                                Log.i("Dummy :", "3");

                                String vicinity = place.getString("vicinity");
                                Log.i("Dummy :", "4");

                                String icon = place.getString("icon");
                                Log.i("Dummy :", "5");

                                JSONObject locationJSON = place.getJSONObject("geometry").getJSONObject("location");
                                Log.i("Dummy :", "6");

                                String lat = locationJSON.getString("lat");
                                Log.i("Dummy :", "7");

                                String lng = locationJSON.getString("lng");
                                Log.i("Dummy :", "8");

                                places.add(new MyPlace(icon, name, lat, lng, rating, vicinity));
                            }

                            // progress.dismiss();
                            Toast.makeText(Dummy.this, status, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Dummy.this, PlacesList.class);
                            i.putExtra("places", places);
                            if (!done) {
                                Toast.makeText(Dummy.this, "Network or GPS unavailable", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(i);
                            Dummy.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Dummy.this, "JSON EXCEPTION", Toast.LENGTH_LONG).show();

                            //  progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Dummy.this, "EXCEPTION", Toast.LENGTH_LONG).show();
                            // progress.dismiss();
                        } finally {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SendData.this, error.toString(), Toast.LENGTH_LONG).show();
                        //   progress.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("latitude", latitude + "");
                return params;
            }
        }.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
        prog = progress;
        dist.setText(prog + "");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Distance shown is in meters", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "disable");
    }
}
