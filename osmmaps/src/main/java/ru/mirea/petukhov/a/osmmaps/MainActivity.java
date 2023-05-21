package ru.mirea.petukhov.a.osmmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.petukhov.a.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MapView mapView = null;
    private ActivityMainBinding binding;
    private IMapController mapController;
    private GeoPoint startPoint;
    private MyLocationNewOverlay locationNewOverlay;
    private final int PERMISSION_REQUEST_CODE = 100;
    private CompassOverlay compassOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        mapView = binding.mapView;
        // region Масштабирование
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);
        // endregion
        // region Map moving
        mapController = mapView.getController();
        mapController.setZoom(15.0);

        startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);
        // endregion
        checkPermissions();
        // region Geolocation
        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mapView);
        locationNewOverlay.enableMyLocation();

        mapView.getOverlays().add(this.locationNewOverlay);
        // endregion
        // region Add Compass
        compassOverlay = new CompassOverlay(getApplicationContext(),
                new InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
        // endregion
        // region Metrics Scale
        final Context context = this.getApplicationContext();
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(metrics.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);
        // endregion
        // region Markers
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(55.794229, 37.700772));
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), "РТУ МИРЭА, Преображенская площадь", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me, null));
        marker.setTitle("РТУ МИРЭА, С-20");

        Marker v78Marker = new Marker(mapView);
        v78Marker.setPosition(new GeoPoint(55.669791, 37.480673));
        v78Marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), "РТУ МИРЭА, Пр-т Вернадского 78", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mapView.getOverlays().add(v78Marker);
        v78Marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.center, null));
        v78Marker.setTitle("РТУ МИРЭА, В-78");

        // endregion
    }

    private void checkPermissions() {
        int fineLocPermStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocPermStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if(!(fineLocPermStatus == PackageManager.PERMISSION_GRANTED && coarseLocPermStatus == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if(mapView != null){
            mapView.onResume();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if(mapView != null){
            mapView.onPause();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}