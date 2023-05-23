package ru.mirea.petukhov.a.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import ru.mirea.petukhov.a.mireaproject.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener {
    private ActivityMapBinding binding;
    private final Point ROUTE_START_LOCATION = new Point(55.755811, 37.617617);
    private final Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) /
                    2);
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);

        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0,0));

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        PlacemarkMapObject templeMarker = mapView.getMap().getMapObjects().addPlacemark(
                new Point(55.744897, 37.604909), ImageProvider.fromResource(this, android.R.drawable.arrow_down_float)
        );
        PlacemarkMapObject cemeteryMarker = mapView.getMap().getMapObjects().addPlacemark(
                new Point(55.768243, 37.548961), ImageProvider.fromResource(this, android.R.drawable.arrow_down_float)
        );
        PlacemarkMapObject islandMarker = mapView.getMap().getMapObjects().addPlacemark(
                new Point(55.869432, 37.834108), ImageProvider.fromResource(this, android.R.drawable.arrow_down_float)
        );
        templeMarker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                submitRequest(new Point(point.getLatitude(), point.getLongitude()));
                Toast.makeText(getApplicationContext(), "Храм Х. Спасителя", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        cemeteryMarker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                submitRequest(new Point(point.getLatitude(), point.getLongitude()));
                Toast.makeText(getApplicationContext(), "Ваганьковское кладбище", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        islandMarker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                submitRequest(new Point(point.getLatitude(), point.getLongitude()));
                Toast.makeText(getApplicationContext(), "Лосиный остров", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void submitRequest(Point endPoint){
        drivingSession = null;

        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(4);

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT,null));
        requestPoints.add(new RequestPoint(endPoint, RequestPointType.WAYPOINT, null));

        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
    }

    @Override
    public void onStart(){
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
    @Override
    public void onStop(){
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "UNKNOWN ERROR";
        if (error instanceof RemoteError) {
            errorMessage = "REMOTE ERROR";
        } else if (error instanceof NetworkError) {
            errorMessage = "NETWORK ERROR";
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}