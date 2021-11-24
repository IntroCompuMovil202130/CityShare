package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Adapters.StoryPrincipalAdapter;
import main.DTOs.StoryPrincipal;
import main.Model.Usuario;

public class GuessActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvNombre;
    private static final double RADIUS_OF_EARTH_KM = 6371;
    static final int LOCATION_REQ = 0;
    SensorManager manager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;
    LatLng real;
    private GoogleMap mMap;
    static final String permLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    LatLng location;
    Task<LocationSettingsResponse> task;
    Marker position;
    Marker guess;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    TextView tvGuess;

    StoryPrincipal story;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                    location = new LatLng(l.getLatitude(),l.getLongitude());
                    if (position != null) {
                        position.remove();
                    }
                    position = mMap.addMarker(new MarkerOptions().position(location).title("Estas aqui"));
                }
            }
        };
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("SENSOR","Entre: "+sensorEvent.values[0]);
                if(sensorEvent.values[0] < 5000) {
                    Log.d("SENSOR", "Estoy con menos de 5000");
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.night_style));
                }
                else{
                    Log.d("SENSOR","Estoy con mas o igual de 5000");
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.day_style));
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        manager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQ) {
            initializeMarker();
        }
    }

    private void initializeMarker() {
        if (ActivityCompat.checkSelfPermission(this, permLocation)
                == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location loc) {
                    if (loc != null) {
                        location = new LatLng(loc.getLatitude(), loc.getLongitude());
                        position = mMap.addMarker(new MarkerOptions().position(location).title("Estas aqui"));
                        mMap.getUiSettings().setZoomGesturesEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                }
            });
        }
    }

    public void guess(View v){
        if(guess == null) {
            Toast.makeText(this, "Eliga una ubicación para adivinar", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.addMarker(new MarkerOptions().position(real).title("Ubicación real"));
        String dist = String.valueOf(distance(real.latitude,real.longitude,guess.getPosition().latitude,guess.getPosition().longitude));
        updateStory(Double.parseDouble(dist));
        String url = getUrl(guess.getPosition(),real);
        Log.d("API TEST", url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    newGuess(dist);
                    JSONObject json = new JSONObject(response);
                    trazarRuta(json);
                    String t = "Estuviste a " + dist + " de la ubicación real";
                    tvGuess.setText(t);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        });
        queue.add(request);
    }

    private void updateStory(double parseDouble) {
        List<String> keys = new ArrayList<>();
        ref.child("Stories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    keys.add(snapshot.getKey());
                }
                update(keys, parseDouble);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void update(List<String> keys, Double prom) {
        for (String key:keys) {
            ref.child("Stories").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        StoryPrincipal st = snapshot.getValue(StoryPrincipal.class);
                        if(st.getUbicacion().equals(story.getUbicacion())){
                            st.addIntentos();
                            st.addPromedio(prom);
                            ref.child("Stories").child(key).child(snapshot.getKey()).setValue(st);
                            return;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void trazarRuta(JSONObject ruta){
        JSONArray jroutes;
        JSONArray jlegs;
        JSONArray jsteps;

        try {
            jroutes = ruta.getJSONArray("routes");
            for(int i = 0; i < jroutes.length();i++){
                jlegs = ((JSONObject)(jroutes.get(i))).getJSONArray("legs");
                for(int j = 0; j < jlegs.length();j++){
                    jsteps = ((JSONObject)(jlegs.get(j))).getJSONArray("steps");
                    for(int k = 0; k < jsteps.length();k++){
                        String polyline = "" + ((JSONObject)((JSONObject)jsteps.get(k)).get("polyline")).get("points");
                        List<LatLng> lista = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(lista).color(Color.CYAN).width(6));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getUrl(LatLng p, LatLng rome) {
        String origin = "origin=" + p.latitude+"," + p.longitude;
        String destination = "destination=" + rome.latitude + "," + rome.longitude;
        String key = "key=" + getString(R.string.google_maps_key);
        return "https://maps.googleapis.com/maps/api/directions/json?"+destination+"&"+origin+"&"+key;
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
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        initializeMarker();
        startLocationUpdates();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                List<Address> addresses;
                try {
                    if(guess != null)
                        guess.remove();
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    guess = mMap.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        story =(StoryPrincipal) getIntent().getSerializableExtra("Historia");
        tvNombre = findViewById(R.id.textViewNombre);
        tvNombre.setText(story.getNombre());
        real = new LatLng(story.getUbicacion().getLatitude(),story.getUbicacion().getLongitude());
        tvGuess = findViewById(R.id.tvGuess);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        geocoder = new Geocoder(getBaseContext());
        mAuth= FirebaseAuth.getInstance();
    }

    private LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void centerCamera(View v){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
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
    public void goBack(View v){
        startActivity(new Intent(this,PrincipalActivity.class));
    }

    private void newGuess(String dist) {
        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(mAuth.getUid().toString().equals(snapshot.getKey())) {
                        Usuario myUser = snapshot.getValue(Usuario.class);
                        Log.i("TAG", "Encontró usuario: " + myUser.getName());


                        //Invocaciones de nuevo guess
                        myUser.addPartidas();
                        myUser.addPoints(dist);
                        myUser.recalcPromedio();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB","Error de consulta");
            }
        });
    }
}