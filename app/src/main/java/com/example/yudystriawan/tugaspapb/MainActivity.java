package com.example.yudystriawan.tugaspapb;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText searchTxt;
    private Button searchBtn,locationBtn, button;
    private TextView textTemp, textCity, textDesc, textDate, textWind , textHumidity, textPressure, locationText, textView;
    private ImageView weatherImg;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient mFusedLocationClient;
    String lokasi;
    ArrayList<Restoran> listRestSample = new ArrayList<Restoran>();
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        searchTxt =  findViewById(R.id.searchText);
//        searchBtn = findViewById(R.id.searchButton);
//        textTemp =  findViewById(R.id.temp);
//        textCity =  findViewById(R.id.city);
//        textDesc =  findViewById(R.id.desc);
//        textDate =  findViewById(R.id.date);
//        textWind =  findViewById(R.id.wind);
//        textHumidity =  findViewById(R.id.humidity);
//        textPressure =  findViewById(R.id.pressure);
//        weatherImg =  findViewById(R.id.weather_img);
//        locationBtn =  findViewById(R.id.locationBtn);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);



//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String cari = searchTxt.getText().toString();
//                findWeather(cari);
//            }
//        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFood();
//            }
//        });

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        startTrackingLocation();
//        locationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startTrackingLocation();
//            }
//        });


        /////////////////////
//        db = FirebaseFirestore.getInstance();
//
//        db.collection("data").document("0")
//            .get()
//            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    textView.setText(documentSnapshot.getData()+"");
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    textView.setText("Failde");
//                }
//            })
//        ;


        readFB();
    }
    int sizeData = 0;

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
                            textView.setText(name);
                        }
                    }
        });
//
//        for (int i = 0; i < sizeData; i++){
//            try {
//                db.collection("DaftarMakananSample").document(String.valueOf(i))
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                String id = documentSnapshot.get("Id").toString();
//                                String name = documentSnapshot.get("Name").toString();
//                                String phone = documentSnapshot.get("Phone").toString();
//                                String rating = documentSnapshot.get("Rating").toString();
//                                String type = documentSnapshot.get("Type").toString();
//                                String weather = documentSnapshot.get("Weather").toString();
//                                String latitude = documentSnapshot.get("Latitude").toString();
//                                String longtitude = documentSnapshot.get("Longtitude").toString();
//
//                                textView.setText(name);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                textView.setText("Failed" + sizeData);
//                            }
//                        });
//            }catch (Exception e){
//                textView.setText(e.getMessage());
//            }
    }


////        db.collection("DaftarMakananSample").document("0")
////                .get()
////                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                    @Override
////                    public void onSuccess(DocumentSnapshot documentSnapshot) {
////
////                    }
////                });
//    }

//    private void getFood() {
//        String url = "https://www.foody.id/__get/Place/HomeListPlace?t=1543576857983&page=1&lat=-7.255521&lon=112.75288&count=12&districtId=990&cateId=&cuisineId=&isReputation=&type=1";
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
//                url,
//                null, new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            String cityID = response.getString("CityId");
//                            textView.setText(cityID);
//
//                        } catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        String message = null;
//                        if (volleyError instanceof NetworkError) {
//                            message = "Cannot connect to Internet...Please check your connection";
//                        } else if (volleyError instanceof ServerError) {
//                            message = "The location could not be found. Please try again";
//                        } else if (volleyError instanceof AuthFailureError) {
//                            message = "Cannot connect to Internet...Please check your connection";
//                        } else if (volleyError instanceof ParseError) {
//                            message = "Parsing error! Please try again after some time";
//                        } else if (volleyError instanceof NoConnectionError) {
//                            message = "Cannot connect to Internet...Please check your connection";
//                        } else if (volleyError instanceof TimeoutError) {
//                            message = "Connection TimeOut! Please check your internet connection.";
//                        }
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("Accept", "application/json, text/plain, */*");
//                params.put("Accept-Encoding", "gzip, deflate, br");
//                params.put("Accept-Language", "id,id-ID;q=0.9,en-US;q=0.8,en;q=0.7");
//                params.put("Connection", "keep-alive");
//                params.put("Cookie", "flg=id; gcat=food; _ga=GA1.2.2082587379.1521334131; ASP.NET_SessionId=2f2s5co0mpbzkrsarwreqrmt; __ondemand_sessionid=afvz1g3dmrre3a1oe4zkm3kt; _gid=GA1.2.1862700872.1543471910; __utmc=20816431; __utmz=20816431.1543471918.3.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); fd.res.view.310=126997; floc=320; _gat=1; _gat_ads=1; __utma=20816431.2082587379.1521334131.1543479554.1543483312.5; __utmt_UA-33292184-1=1; _fbp=fb.1.1543483314682.1905115791; __utmb=20816431.2.10.1543483312");
//                params.put("Host", "www.foody.id");
//                params.put("Referer", "https://www.foody.id/surabaya");
//                params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
//                params.put("X-Requested-With", "XMLHttpRequest");
//
//
//                return params;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jor);    }
//
//
//    private void findWeather(String Lokasi) {
//        String url = "http://api.openweathermap.org/data/2.5/weather?q="+Lokasi+"&appid=3fd8da85e581b3ff8dfb191ea4454620";
//
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
//                url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject main_object = response.getJSONObject("main");
//                    JSONArray array = response.getJSONArray("weather");
//
//                    JSONObject object = array.getJSONObject(0);
//
//                    String temp = String.valueOf(Math.round((main_object.getDouble("temp")-273.15)));
//                    String city = response.getString("name");
//                    String weather = object.getString("main");
//                    String detail = object.getString("description");
//
//                    Calendar calendar = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
//                    String formatted_date = sdf.format(calendar.getTime());
//
//                    JSONObject wind_object = response.getJSONObject("wind");
//                    String wind = wind_object.getString("speed")+" m/s";
//
//                    String humidity = main_object.getString("humidity")+ "%";
//                    String pressure = main_object.getString("pressure") + " hpa";
//
//                    textTemp.setText(temp);
//                    textCity.setText(city);
//                    textDesc.setText(detail);
//                    textDate.setText(formatted_date);
//                    textWind.setText(wind);
//                    textHumidity.setText(humidity);
//                    textPressure.setText(pressure);
//
//                    switch(weather){
//                        case "Clouds":
//                            weatherImg.setImageResource(R.drawable.cloudy);
//                            break;
//                        case "Clear":
//                            weatherImg.setImageResource(R.drawable.sunny);
//                            break;
//                        case "Rain":
//                            weatherImg.setImageResource(R.drawable.rain);
//                            break;
//                        case "Thunderstorm":
//                            weatherImg.setImageResource(R.drawable.storm);
//                            break;
//                        case "Drizzle":
//                            weatherImg.setImageResource(R.drawable.drop);
//                            break;
//                        case "Snow":
//                            weatherImg.setImageResource(R.drawable.snow);
//                            break;
//                        default:
//                            weatherImg.setImageResource(R.drawable.haze);
//                    }
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                String message = null;
//                if (volleyError instanceof NetworkError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof ServerError) {
//                    message = "The location could not be found. Please try again";
//                } else if (volleyError instanceof AuthFailureError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof ParseError) {
//                    message = "Parsing error! Please try again after some time";
//                } else if (volleyError instanceof NoConnectionError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof TimeoutError) {
//                    message = "Connection TimeOut! Please check your internet connection.";
//                }
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//            }
//        }
//        );
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jor);
//    }

//    private void findWeather(double lat, double lon) {
//        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=3fd8da85e581b3ff8dfb191ea4454620";
//
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
//                url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject main_object = response.getJSONObject("main");
//                    JSONArray array = response.getJSONArray("weather");
//
//                    JSONObject object = array.getJSONObject(0);
//
//                    String temp = String.valueOf(Math.round((main_object.getDouble("temp")-273.15)));
//                    String city = response.getString("name");
//                    String weather = object.getString("main");
//                    String detail = object.getString("description");
//
//                    Calendar calendar = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
//                    String formatted_date = sdf.format(calendar.getTime());
//
//                    JSONObject wind_object = response.getJSONObject("wind");
//                    String wind = wind_object.getString("speed")+" m/s";
//
//                    String humidity = main_object.getString("humidity")+ "%";
//                    String pressure = main_object.getString("pressure") + " hpa";
//
//                    textTemp.setText(temp);
//                    textCity.setText(city);
//                    textDesc.setText(detail);
//                    textDate.setText(formatted_date);
//                    textWind.setText(wind);
//                    textHumidity.setText(humidity);
//                    textPressure.setText(pressure);
//
//                    switch(weather){
//                        case "Clouds":
//                            weatherImg.setImageResource(R.drawable.cloudy);
//                            break;
//                        case "Clear":
//                            weatherImg.setImageResource(R.drawable.sunny);
//                            break;
//                        case "Rain":
//                            weatherImg.setImageResource(R.drawable.rain);
//                            break;
//                        case "Thunderstorm":
//                            weatherImg.setImageResource(R.drawable.storm);
//                            break;
//                        case "Drizzle":
//                            weatherImg.setImageResource(R.drawable.drop);
//                            break;
//                        case "Snow":
//                            weatherImg.setImageResource(R.drawable.snow);
//                            break;
//                        default:
//                            weatherImg.setImageResource(R.drawable.haze);
//                    }
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                String message = null;
//                if (volleyError instanceof NetworkError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof ServerError) {
//                    message = "The location could not be found. Please try again";
//                } else if (volleyError instanceof AuthFailureError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof ParseError) {
//                    message = "Parsing error! Please try again after some time";
//                } else if (volleyError instanceof NoConnectionError) {
//                    message = "Cannot connect to Internet...Please check your connection";
//                } else if (volleyError instanceof TimeoutError) {
//                    message = "Connection TimeOut! Please check your internet connection.";
//                }
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//            }
//        }
//        );
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jor);
//    }


//    private void startTrackingLocation() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                            {Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION_PERMISSION);
//        } else {
//            mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                    new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                try {
////                                String data = "Latitude: " + location.getLatitude()
////                                        + "\n Longitude: " + location.getLongitude();
//
//                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                                    if (addresses.size() > 0) {
//                                        findWeather(location.getLatitude(), location.getLongitude());
//                                    }
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else { // Show "no location" }
//                            }
//                        }
//                    });
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions,
//                                           int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_LOCATION_PERMISSION:
//// If the permission is granted, get the location, otherwise, show a Toast
//                if (grantResults.length > 0
//                        && grantResults[0]
//                        == PackageManager.PERMISSION_GRANTED) {
//                    startTrackingLocation();
//                } else {
//                    Toast.makeText(this,
//                            "Permission denied",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        }
//    }
//
//    private LocationRequest getLocationRequest() {
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        return locationRequest;
//    }


}