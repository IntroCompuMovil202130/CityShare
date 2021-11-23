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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import main.DTOs.StoryPrincipal;

public class preguessActivity extends AppCompatActivity {

    TextView estadistica;
    TextView name;
    ImageView photo;
    ImageView profile;
    Button adivinar;

    private StoryPrincipal story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguess);

        inflate();

        updateUI();

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
    }

    private void inflate() {
        estadistica = findViewById(R.id.textView6);
        adivinar = findViewById(R.id.buttonAdivinar);
        name = findViewById(R.id.preGuessName);
        photo = findViewById(R.id.preGuessPhoto);
        profile = findViewById(R.id.preGuessProfile);
        story = (StoryPrincipal) getIntent().getSerializableExtra("Historia");
    }
}