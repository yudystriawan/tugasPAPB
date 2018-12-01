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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private Button searchBtn,locationBtn, detailBtn;
    private TextView textTemp, textCity, textDesc, textDate, textWind , textHumidity, textPressure, locationText;
    private ImageView weatherImg;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient mFusedLocationClient;
    String lokasi;
    double latitude, longitude;


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
        textWind =  findViewById(R.id.wind);
        textHumidity =  findViewById(R.id.humidity);
        textPressure =  findViewById(R.id.pressure);
        weatherImg =  findViewById(R.id.weather_img);
        locationBtn =  findViewById(R.id.locationBtn);
        detailBtn =  findViewById(R.id.detailBtn);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cari = searchTxt.getText().toString();
                findWeather(cari);
            }
        });

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra("lat", latitude );
                intent.putExtra("lon", longitude);
                startActivity(intent);
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

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    JSONObject wind_object = response.getJSONObject("wind");
                    String wind = wind_object.getString("speed")+" m/s";

                    String humidity = main_object.getString("humidity")+ "%";
                    String pressure = main_object.getString("pressure") + " hpa";

                    textTemp.setText(temp);
                    textCity.setText(city);
                    textDesc.setText(detail);
                    textDate.setText(formatted_date);
                    textWind.setText(wind);
                    textHumidity.setText(humidity);
                    textPressure.setText(pressure);

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

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    JSONObject wind_object = response.getJSONObject("wind");
                    String wind = wind_object.getString("speed")+" m/s";

                    String humidity = main_object.getString("humidity")+ "%";
                    String pressure = main_object.getString("pressure") + " hpa";

                    textTemp.setText(temp);
                    textCity.setText(city);
                    textDesc.setText(detail);
                    textDate.setText(formatted_date);
                    textWind.setText(wind);
                    textHumidity.setText(humidity);
                    textPressure.setText(pressure);

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

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


}

