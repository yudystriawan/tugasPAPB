package com.example.yudystriawan.tugaspapb;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText searchTxt;
    private Button searchBtn,locationBtn;
    private TextView textTemp, textCity, textDesc, textDate;
    private ImageView weatherImg;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient mFusedLocationClient;
    String lokasi;
    double latitude, longitude;
    private ArrayList<Restoran> listRestSample;
    int sizeData = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTxt =  findViewById(R.id.searchText);
        searchBtn = findViewById(R.id.searchButton);
        textTemp =  findViewById(R.id.temp);
        textCity =  findViewById(R.id.city);
        textDesc =  findViewById(R.id.desc);
        textDate =  findViewById(R.id.date);
        weatherImg =  findViewById(R.id.weather_img);
        locationBtn =  findViewById(R.id.locationBtn);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cari = searchTxt.getText().toString();
                findWeather(cari);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startTrackingLocation();
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("lat", latitude );
                intent.putExtra("lon", longitude);
                startActivity(intent);
            }
        });

    }

    //ini buat search
    private void findWeather(String Lokasi) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+Lokasi+"&appid=3fd8da85e581b3ff8dfb191ea4454620";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");

                    JSONObject object = array.getJSONObject(0);

                    String temp = String.valueOf(Math.round((main_object.getDouble("temp")-273.15)));
                    String city = response.getString("name");
                    String weather = object.getString("main");
                    String detail = object.getString("description");

                    textTemp.setText(temp);
                    textCity.setText(city);
                    textDesc.setText(detail);

                    switch(weather){
                        case "Clouds":
                            weatherImg.setImageResource(R.drawable.cloudy);
                            break;
                        case "Clear":
                            weatherImg.setImageResource(R.drawable.sunny);
                            break;
                        case "Rain":
                            weatherImg.setImageResource(R.drawable.rain);
                            break;
                        case "Thunderstorm":
                            weatherImg.setImageResource(R.drawable.storm);
                            break;
                        case "Drizzle":
                            weatherImg.setImageResource(R.drawable.drop);
                            break;
                        case "Snow":
                            weatherImg.setImageResource(R.drawable.snow);
                            break;
                        default:
                            weatherImg.setImageResource(R.drawable.haze);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ServerError) {
                    message = "The location could not be found. Please try again";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    //ini buat lokasi(GL)
    private void findWeather(double lat, double lon) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=3fd8da85e581b3ff8dfb191ea4454620";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");

                    JSONObject object = array.getJSONObject(0);

                    String temp = String.valueOf(Math.round((main_object.getDouble("temp")-273.15)));
                    String city = response.getString("name");
                    String weather = object.getString("main");
                    String detail = object.getString("description");

                    textTemp.setText(temp);
                    textCity.setText(city);
                    textDesc.setText(detail);

                    switch(weather){
                        case "Clouds":
                            weatherImg.setImageResource(R.drawable.cloudy);
                            break;
                        case "Clear":
                            weatherImg.setImageResource(R.drawable.sunny);
                            break;
                        case "Rain":
                            weatherImg.setImageResource(R.drawable.rain);
                            break;
                        case "Thunderstorm":
                            weatherImg.setImageResource(R.drawable.storm);
                            break;
                        case "Drizzle":
                            weatherImg.setImageResource(R.drawable.drop);
                            break;
                        case "Snow":
                            weatherImg.setImageResource(R.drawable.snow);
                            break;
                        default:
                            weatherImg.setImageResource(R.drawable.haze);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ServerError) {
                    message = "The location could not be found. Please try again";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }


    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                try {
                                latitude =  location.getLatitude();
                                longitude =  location.getLongitude();

                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    if (addresses.size() > 0) {
                                        findWeather(location.getLatitude(), location.getLongitude());
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else { // Show "no location" }
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
// If the permission is granted, get the location, otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    private void readFB() {

        db = FirebaseFirestore.getInstance();

        db.collection("DaftarMakananSample")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeData = queryDocumentSnapshots.size();
                        String id, name, phone, rating, type, weather, latitude, longitude;
                        for (int i = 0; i < sizeData ; i++){
                            id = queryDocumentSnapshots.getDocuments().get(i).get("Id").toString();
                            name = queryDocumentSnapshots.getDocuments().get(i).get("Name").toString();
                            phone = queryDocumentSnapshots.getDocuments().get(i).get("Phone").toString();
                            rating = queryDocumentSnapshots.getDocuments().get(i).get("Rating").toString();
                            type = queryDocumentSnapshots.getDocuments().get(i).get("Type").toString();
                            weather = queryDocumentSnapshots.getDocuments().get(i).get("Weather").toString();
                            latitude = queryDocumentSnapshots.getDocuments().get(i).get("Latitude").toString();
                            longitude = queryDocumentSnapshots.getDocuments().get(i).get("Longitude").toString();

                            listRestSample.add(new Restoran(id, name, phone, rating, type, weather, latitude, longitude));
                        }
                    }
                });

    }
}

