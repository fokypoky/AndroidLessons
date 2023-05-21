package ru.mirea.petukhov.a.yandexdriver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CurrentLocationHandler implements LocationListener {
    private LocationManager locationManager;
    private MainActivity activity;
    private double longitude;
    private double latitude;
    public CurrentLocationHandler(MainActivity activity){
        this.activity = activity;
        locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
        if(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else{
            Log.wtf("LOCATION", "Not permissions");
        }
    }
//    public double getLongitude(){
//        return longitude;
//    }
//    public double getLatitude() {
//        return latitude;
//    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        activity.longitude = location.getLongitude();
        activity.latitude = location.getLatitude();
        activity.initializeMap();
    }
}
