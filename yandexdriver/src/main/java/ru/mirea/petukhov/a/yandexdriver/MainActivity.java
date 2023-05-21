package ru.mirea.petukhov.a.yandexdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.petukhov.a.yandexdriver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener, LocationListener {

    private Point ROUTE_START_LOCATION = new Point(55.670005, 37.479894);
    private final Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);
    private Point SCREEN_CENTER;

    private ActivityMainBinding binding;
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private LocationManager locationManager;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private final int PERMISSION_REQUEST_CODE = 100;
    private boolean canShowMap = false;
    public double longitude;
    public double latitude;
    private PlacemarkMapObject marker;
    private CurrentLocationHandler currentLocationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapView = binding.mapView;
        mapView.setVisibility(View.GONE);

        checkPermissions();

        marker = mapView.getMap().getMapObjects().addPlacemark(
                ROUTE_END_LOCATION,
                ImageProvider.fromResource(this,
                        android.R.drawable.arrow_down_float));
        marker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                Toast.makeText(getApplication(), "РТУ МИРЭА", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void checkPermissions() {
        int fineLocPermStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocPermStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!(fineLocPermStatus == PackageManager.PERMISSION_GRANTED && coarseLocPermStatus == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            canShowMap = true;
            binding.mapView.setVisibility(View.VISIBLE);
            currentLocationHandler = new CurrentLocationHandler(this);
        }
    }
    public void initializeMap() {
        mapView.getMap().setRotateGesturesEnabled(false);

        ROUTE_START_LOCATION = new Point(latitude, longitude);

        SCREEN_CENTER = new Point(
                (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
                (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2);

        mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0, 0));
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        submitRequest();
    }

    private void submitRequest() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(4);

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();


        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null));

        // просим у сервера маршруты (???)
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            canShowMap = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (canShowMap) {
                mapView.setVisibility(View.VISIBLE);
                currentLocationHandler = new CurrentLocationHandler(this);
            }
        }
    }


    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for(int i = 0; i < list.size(); i++){
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "UNKNOWN ERROR";
        if(error instanceof RemoteError){
            errorMessage = "REMOTE ERROR";
        }
        else if(error instanceof NetworkError){
            errorMessage = "NETWORK ERROR";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}