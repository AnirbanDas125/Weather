package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity {

    TextView  date, temperature, weatherType , myCity2,pressure,humidity;
    EditText myCity;
    String defaultCity = "kolkata";
    String cityName;
    ImageButton searchCity;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = findViewById(R.id.swipe);
        myCity = findViewById(R.id.myCity);
        myCity2 = findViewById(R.id.myCity2);
        date = findViewById(R.id.date);
        temperature = findViewById(R.id.temperature);
        weatherType = findViewById(R.id.weatherType);
        pressure=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        searchCity = findViewById(R.id.imageButton);

        searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = myCity.getText().toString().trim().toLowerCase() ;
                myCity.setText("");
                getWeather(cityName);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeather();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getWeather();
    }

    public void getWeather() {
    String url = "https://api.openweathermap.org/data/2.5/weather?q="+
            defaultCity+"&appid=6b20d56c2e732e188653cd74593b7ae6" ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObjectMain = response.getJSONObject("main");
                    JSONArray  jsonArrayWeather = response.getJSONArray("weather");
                    JSONObject Object = jsonArrayWeather.getJSONObject(0);
                    String jsonTemperature = String.valueOf((int)((jsonObjectMain.getDouble("temp")-273.15)));
                    temperature.setText(jsonTemperature);
                    String jsonPressure = String.valueOf(jsonObjectMain.getDouble("pressure"));
                    pressure.setText("Pr: "+jsonPressure);
                    String jsonHumidity = String.valueOf((int)(jsonObjectMain.getDouble("humidity")));
                    humidity.setText("Hum: "+jsonHumidity+"%");
                    String jsonWeatherType = Object.getString("description");
                    weatherType.setText(jsonWeatherType);
                    myCity2.setText(response.getString("name"));

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    date.setText(sdf.format(calendar.DATE));
                }catch(JSONException e){
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

   }
   public void getWeather(String city){
       String url = "https://api.openweathermap.org/data/2.5/weather?q="
               +city+"&appid=6b20d56c2e732e188653cd74593b7ae6";
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
               null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try{
                   JSONObject jsonObjectMain = response.getJSONObject("main");
                   JSONArray  jsonArrayWeather = response.getJSONArray("weather");
                   JSONObject Object = jsonArrayWeather.getJSONObject(0);
                   String jsonTemperature = String.valueOf((int)((jsonObjectMain.getDouble("temp")-273.15)));
                   temperature.setText(jsonTemperature);
                   String jsonPressure = String.valueOf(jsonObjectMain.getDouble("pressure"));
                   pressure.setText("Pr: "+jsonPressure);
                   String jsonHumidity = String.valueOf((int)(jsonObjectMain.getDouble("humidity")));
                   humidity.setText("Hum: "+jsonHumidity+"%");
                   String jsonWeatherType = Object.getString("description");
                   weatherType.setText(jsonWeatherType);
                   myCity2.setText(response.getString("name"));

                   Calendar calendar = Calendar.getInstance();
                   SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                   date.setText(sdf.format(calendar.DATE));
               }catch(JSONException e){
                   Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
       requestQueue.add(jsonObjectRequest);

   }

}