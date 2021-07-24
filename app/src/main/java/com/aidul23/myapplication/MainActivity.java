package com.aidul23.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.aidul23.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        else {
            //when permission granted
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    //initialize location
                    Location location = task.getResult();
                    if (location != null) {

                        //initialize geocoder
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                        //initialize address list
                        try {
                            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            //set latitude to text view
                            binding.latitude.setText(Html.fromHtml("<font color= '#6200EE'><b>Latitude :</b><br></font>" + address.get(0).getLatitude()));
                            Log.d(TAG, "onComplete: " + address.get(0).getLatitude());

                            //set longitude to text view
                            binding.longitude.setText(Html.fromHtml("<font color= '#6200EE'><b>Longitude :</b><br></font>" + address.get(0).getLatitude()));

                            //set country name to text view
                            binding.country.setText(Html.fromHtml("<font color= '#6200EE'><b>Country :</b><br></font>" + address.get(0).getCountryName()));

                            //set locality to text view
                            binding.locality.setText(Html.fromHtml("<font color= '#6200EE'><b>Locality :</b><br></font>" + address.get(0).getLocality()));

                            //set address to text view
                            binding.address.setText(Html.fromHtml("<font color= '#6200EE'><b>Address :</b><br></font>" + address.get(0).getAddressLine(0)));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

    }
}