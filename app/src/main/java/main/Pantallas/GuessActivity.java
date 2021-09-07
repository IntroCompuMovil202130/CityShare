package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.quickaccesswallet.GetWalletCardsCallback;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class GuessActivity extends AppCompatActivity {

    MapView map;
    TextView tvNombre;
    private FusedLocationProviderClient fusedLocationClient;
    public static final int PERMISSION_LOCATION = 0;
    GeoPoint ubicacion = new GeoPoint(4.62,-74.07);
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        //inflate
        map = findViewById(R.id.mapView);
        tvNombre = findViewById(R.id.textViewNombre);
        //Obtener ubicacion
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "", PERMISSION_LOCATION);
        obtenerUbicacion();
        //iniciar mapa
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx));
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        //Obtener Ubicacion
        obtenerUbicacion();
        marker = new Marker(map);
        marker.setTitle("Marcador");
        Drawable icon = getResources().getDrawable(R.drawable.location_blue,this.getTheme());
        marker.setIcon(icon);
        marker.setPosition(ubicacion);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        IMapController controller = map.getController();
        controller.setZoom(18.0);
        controller.setCenter(ubicacion);
        map.getOverlays().add(marker);
    }

    private void obtenerUbicacion() {
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        ubicacion.setLatitude(location.getLatitude());
                        ubicacion.setLongitude(location.getLongitude());
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        obtenerUbicacion();
        IMapController controller = map.getController();
        controller.setZoom(18.0);
        controller.setCenter(ubicacion);
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private Marker createMarker(GeoPoint p, String title, String desc, int iconID){
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (desc != null) marker.setSubDescription(desc);
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, this.getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
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
}