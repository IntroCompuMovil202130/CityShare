package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import main.DTOs.StoryPrincipal;

public class preguessActivity extends AppCompatActivity {

    TextView estadistica;
    TextView name;
    ImageView photo;
    ImageView profile;
    Button adivinar;
    TextView hintTemp;
    TextView hintWeather;


    private StoryPrincipal story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguess);

        inflate();

        updateUI();

        //For hint
        getWeatherForecastRest();

        estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScoreActivity.class));
            }
        });

        adivinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GuessActivity.class);
                intent.putExtra("Historia",story);
                startActivity(intent);
            }
        });
    }

    //Consumes rest service for hint pointing
    private void getWeatherForecastRest(){
        String lat = story.getUbicacion().getLatitude().toString();
        String lon = story.getUbicacion().getLongitude().toString();
        String url = "https://www.7timer.info/bin/api.pl?lon=" + lon + "&lat=" + lat + "&product=two&output=json";
//        String url = "https://www.boredapi.com/api/activity";
        Log.d("API TEST", url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String weatherType = StringUtils.substringBetween(response,"\"weather\" : " , "}");
                    hintWeather.setText("Pista 2 - El clima es: " + weatherType);
                    Log.d("Follow up", weatherType);
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

    private void updateUI() {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        final long MEGA = 1024*1024;
        ref.child(story.getProfile()).getBytes(MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profile.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("STORAGE", e.toString());
            }
        });
        //Get Photo
        ref.child(story.getPhoto()).getBytes(MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                photo.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("STORAGE", e.toString());
            }
        });
        //Get name
        name.setText(story.getNombre());

        //Get temp hint
        hintTemp.setText("Pista 1 - La temperatura es: " + story.getTemperatura().toString() + " °C");
    }

    private void inflate() {
        estadistica = findViewById(R.id.textView6);
        adivinar = findViewById(R.id.buttonAdivinar);
        name = findViewById(R.id.preGuessName);
        photo = findViewById(R.id.preGuessPhoto);
        profile = findViewById(R.id.preGuessProfile);
        story = (StoryPrincipal) getIntent().getSerializableExtra("Historia");
        hintTemp = findViewById(R.id.textView10);
        hintWeather = findViewById(R.id.textView11);
    }
}