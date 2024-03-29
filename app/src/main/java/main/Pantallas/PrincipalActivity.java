package main.Pantallas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import main.Adapters.StoryPrincipalAdapter;
import main.DTOs.Loc;
import main.DTOs.StoryPrincipal;
import main.DTOs.onStoryListener;
import main.Model.Usuario;


public class PrincipalActivity extends AppCompatActivity implements onStoryListener, SensorEventListener {

    String galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    String cameraPermission = Manifest.permission.CAMERA;
    public static final int GALLERY_ID_PERMISSION = 5001;
    public static final int CAMERA_ID_PERMISSION = 5002;
    public static final int IMAGE_PICK_ACTIVITY = 9;
    public static final int TAKE_PICTURE_ACTIVITY = 10;
    private boolean storyTaken = false;
    static final int LOCATION_REQ = 0;
    static final String permLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private FusedLocationProviderClient locationProviderClient;
    private LatLng currentLocation;

    ImageButton botonCrearPostCamara;
    ImageButton botonCrearPostGaleria;
    ImageView historiaUsuario;
    Button botonHistorias;
    Button chat;
    ImageView imagenEjemplo;
    ImageButton signOut;
    TextView userName;
    TextView puntos;
    TextView partidas;
    TextView dist;
    RecyclerView stories;
    List<StoryPrincipal> storiesList;
    StoryPrincipalAdapter adapterStory;
    TextView recommendedActivityTextView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //For temperature
    private SensorManager mgr;
    private Sensor temp;
    private StringBuilder msg = new StringBuilder(2048);
    private SensorEvent event;
    private String tempVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //For temp
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        temp = mgr.getDefaultSensor( Sensor.TYPE_AMBIENT_TEMPERATURE);

        inflate();
        updateName();
        getRecommendedActivity();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesList = new ArrayList<>();
        updateUI();
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, temp);
        super.onPause();
    }

    public void onSensorChanged(SensorEvent event) {
        //msg.insert(0, event.values[0] + " °C");
        tempVal = String.valueOf(event.values[0]);
        Log.d("TEMP", String.valueOf(event.values[0]));
    }

    private void updateUI() {
        List<String> keys = new ArrayList<>();
        ref.child("Stories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey() != user.getUid())
                        keys.add(snapshot.getKey());
                }
                getAllStories(keys);
                Log.d("LIST","Llaves: "+keys.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getAllStories(List<String> keys) {
        for (String key:keys) {
            ref.child("Stories").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        storiesList.add(snapshot.getValue(StoryPrincipal.class));
                    }
                    Log.d("LIST","historias: "+storiesList.toString());
                    adapterStory = new StoryPrincipalAdapter(storiesList,PrincipalActivity.this);
                    stories.setAdapter(adapterStory);
                    stories.setClickable(true);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void updateName() {
        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(mAuth.getUid().toString().equals(snapshot.getKey())) {
                        Usuario myUser = snapshot.getValue(Usuario.class);
                        Log.i("TAG", "Encontró usuario: " + myUser.getName());
                        String name = myUser.getName();
                        userName.setText(name);

                        Double puntosAct = myUser.getPoints();
                        Log.i("TAG", "Puntos: " + puntosAct);
                        puntos.setText(String.valueOf(puntosAct));
                        Integer partidasAct = myUser.getPartidas();
                        Log.i("TAG", "Partidas : " + partidasAct);
                        partidas.setText(String.valueOf(partidasAct));
                        Double promedio = myUser.getPromedio();
                        dist.setText(String.valueOf(promedio));

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB","Error de consulta");
            }
        });
    }

    public void createStoryCam(View v){
        if(checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            String[] permissions = {cameraPermission};
            requestPermissions(permissions, CAMERA_ID_PERMISSION);

        }
        else{
            takePicture();
        }
    }

    public void createStoryGal(View v){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            String[] permissions = {galleryPermission};
            requestPermissions(permissions, GALLERY_ID_PERMISSION);

        }
        else{
            pickImageFromGallery();
        }
    }

    public void launchStoryActivity(View v){
        startActivity(new Intent(this,StoryActivity.class));
    }

    public void launchChatActivity(View v){
        startActivity(new Intent(this,ChallengesActivity.class));
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_ACTIVITY);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(intent, TAKE_PICTURE_ACTIVITY);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GALLERY_ID_PERMISSION ){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                pickImageFromGallery();
            }
        }
        else if(requestCode == CAMERA_ID_PERMISSION ){
            if(checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }
        else if(requestCode == LOCATION_REQ) {
            if (ActivityCompat.checkSelfPermission(this, permLocation)
                    == PackageManager.PERMISSION_GRANTED) {
                locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        if (loc != null) {
                            currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Req code " + requestCode);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_ACTIVITY){
            //historiaUsuario.setImageURI(data.getData());
            storyTaken = true;
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Hubo un problema con obtener la foto"
                        ,Toast.LENGTH_LONG).show();
            }
            uploadStoryImage(bitmap);
        }

        else if(resultCode == RESULT_OK && requestCode == TAKE_PICTURE_ACTIVITY){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //historiaUsuario.setImageBitmap(imageBitmap);
            storyTaken = true;
            uploadStoryImage(imageBitmap);
        }
    }

    private void uploadStoryImage(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();
        String uri = "images/Historias/" + user.getUid() + UUID.randomUUID();
        UploadTask uploadTask = storageReference.child(uri).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PrincipalActivity.this, "Hubo un error subiendo la imagen",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PrincipalActivity.this, "Se subió la imagen",
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, permLocation)
                == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location loc) {
                    if (loc != null) {
                        currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                        Log.d("LOC","Tenemos ubicacion");
                        uploadStory(uri);
                    }
                }
            });
        }
        else{
            requestPermission(PrincipalActivity.this,permLocation,"",LOCATION_REQ);
        }
    }

    private void uploadStory(String uri) {
        Log.d("LOC", currentLocation.toString());
        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(mAuth.getUid().equals(snapshot.getKey())) {
                        Usuario myUser = snapshot.getValue(Usuario.class);
                        myUser.add();
                        ref.child("Users").child(user.getUid()).setValue(myUser);
                        //TODO: Lectura de sensor de temperatura
                        StoryPrincipal story = new StoryPrincipal(myUser.getUserName(),uri,
                                "images/"+user.getUid()+"/contactImage",new Loc(currentLocation),
                                Double.valueOf(tempVal), 0, 0d);
                        ref.child("Stories").child(user.getUid()).child(myUser.getStories().toString()).setValue(story);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB","Error de consulta");
            }
        });
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
        botonCrearPostCamara = findViewById(R.id.botonPost);
        botonCrearPostGaleria = findViewById(R.id.botonPost2);
        botonHistorias = findViewById(R.id.botonHistorias);
        userName = findViewById(R.id.tvUserName);
        puntos = findViewById(R.id.puntos);
        partidas = findViewById(R.id.partidas);
        dist = findViewById(R.id.dist);
        chat = findViewById(R.id.botonChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PrincipalActivity.this,LinearLayoutManager.HORIZONTAL,false);
        stories = findViewById(R.id.rvStories);
        stories.setLayoutManager(layoutManager);
        mAuth= FirebaseAuth.getInstance();
        signOut=findViewById((R.id.signout));
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        recommendedActivityTextView = findViewById(R.id.textView9);
    }

    @Override
    public void onStoryClick(int position) {
        Log.d("Follow up", "Aqui llamo preguess");
        Intent intent = new Intent(PrincipalActivity.this,preguessActivity.class);
        intent.putExtra("Historia",storiesList.get(position));
        startActivity(intent);
    }

    private void getRecommendedActivity(){
        String url = "https://www.boredapi.com/api/activity";
        Log.d("API TEST", url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String recommendedActivity = StringUtils.substringBetween(response,"\"activity\":" , ",");
                    recommendedActivityTextView.setText(recommendedActivity);
                    Log.d("Follow up2", recommendedActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        });
        queue.add(request);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}