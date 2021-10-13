package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.service.quickaccesswallet.GetWalletCardsCallback;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.IOException;
import java.util.List;

public class GuessActivity extends AppCompatActivity {

    TextView tvNombre;
    private static final double RADIUS_OF_EARTH_KM = 6371;
    MapView map;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    static final int LOCATION_REQ = 0;
    GeoPoint location;
    Marker locationMarker;
    Marker longPressedMarker;
    Task<LocationSettingsResponse> task;
    Geocoder geocoder;
    SensorManager manager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;
    GeoPoint rome = new GeoPoint(41.889467,12.492081);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "location", LOCATION_REQ);

        inflate();

        checkGPS();

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                    startLocationUpdates();
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if(location != null) {
                    Location l = locationResult.getLastLocation();
                    location.setLongitude(l.getLongitude());
                    location.setLatitude(l.getLatitude());
                    location.setAltitude(l.getAltitude());
                    if (locationMarker != null)
                        map.getOverlays().remove(locationMarker);
                    locationMarker = createMarker(location, "Estas aqui", null);
                    map.getOverlays().add(locationMarker);
                    map.invalidate();
                }
            }
        };
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("SENSOR","Entre: "+sensorEvent.values[0]);
                if(sensorEvent.values[0] < 5000) {
                    Log.d("SENSOR", "Estoy con menos de 5000");
                    map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                }
                else{
                    Log.d("SENSOR","Estoy con mas o igual de 5000");
                    //map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        manager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);
        map.getOverlays().add(createOverlayEvents());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMarker();
            } else {
                Toast.makeText(getApplicationContext(), "Necesitamos ubicacion", Toast.LENGTH_LONG).show();
            }
        }

    }
    private MapEventsOverlay createOverlayEvents() {
        return new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                longPressOnMap(p);
                return true;
            }
        });
    }

    private void longPressOnMap(GeoPoint p) {
        if(longPressedMarker!=null)
            map.getOverlays().remove(longPressedMarker);
        String locationName;
        try {
            List<Address> list = geocoder.getFromLocation(p.getLatitude(),p.getLongitude(),2);
            if(!list.isEmpty()) {
                Address result = list.get(0);
                locationName = result.getAddressLine(0);
                longPressedMarker = createMarker(p, locationName, null);
                map.getOverlays().add(longPressedMarker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeMarker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location loc) {
                    if (loc != null) {
                        location = new GeoPoint(loc.getLatitude(), loc.getLongitude());
                        locationMarker = createMarker(location,"Estas aquí","");
                        map.getOverlays().add(locationMarker);
                        map.getController().setCenter(location);
                    }
                }
            });
        }
    }
    public void guess(View v){
        Polyline line = new Polyline(map);
        line.addPoint(rome);
        GeoPoint p = longPressedMarker.getPosition();
        line.addPoint(p);
        map.getOverlays().add(line);
        Marker romeMarker = createMarker(rome,"Ubicación real",null);
        map.getOverlays().add(romeMarker);
        String dist = String.valueOf(distance(rome.getLatitude(),rome.getLongitude()
                ,p.getLatitude(),p.getLongitude()));
        Toast.makeText(this,"Distancia hacia allá: "+dist,Toast.LENGTH_LONG).show();
    }

    private Marker createMarker(GeoPoint p, String title, String desc){
        Marker marker = new Marker(map);
        marker.setPosition(p);
        marker.setIcon(ContextCompat.getDrawable(GuessActivity.this, R.drawable.location_black));
        marker.setImage(ContextCompat.getDrawable(GuessActivity.this, R.drawable.location_black));
        marker.setTitle(title);
        marker.setSubDescription(desc);
        return marker;
    }

    private void checkGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        task = client.checkLocationSettings(builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        startLocationUpdates();
        initializeMarker();
        IMapController controller = map.getController();
        controller.setZoom(18.0);
        if(location!=null)
            Log.d("LOCATION",location.toString());
        controller.setCenter(this.location);
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        stopLocationUpdates();
        manager.unregisterListener(lightSensorListener,lightSensor);
    }

    private void stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        }
    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show();
            }
            // request the permission.
            ActivityCompat.requestPermissions(context, new String[]{permission}, id);
        }
    }

    private void inflate() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        geocoder = new Geocoder(getBaseContext());
    }

    private LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void centerCamera(View v){
        map.getController().setCenter(location);
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }
}