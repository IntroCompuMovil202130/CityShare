package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity {

    ImageButton botonCrearPost;
    Button botonHistorias;
    Button chat;
    ImageView imagenEjemplo;
    ImageButton signOut;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mAuth= FirebaseAuth.getInstance();
        inflate();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        imagenEjemplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GuessActivity.class));
            }
        });
    }


    public void launchPostsActivity(View v){
        startActivity(new Intent(this, PostsActivity.class));
    }
    public void launchStoryActivity(View v){
        startActivity(new Intent(this,StoryActivity.class));
    }
    public void launchChatActivity(View v){
        startActivity(new Intent(this,ChatActivity.class));
    }
    private void inflate() {
        botonCrearPost = findViewById(R.id.botonPost);
        botonHistorias = findViewById(R.id.botonHistorias);
        chat = findViewById(R.id.botonChat);
<<<<<<< Updated upstream
        imagenEjemplo = findViewById(R.id.imagenEjemplo);
=======
        imagenEjemplo = findViewById(R.id.imageView6);
        signOut=findViewById((R.id.signOut));
>>>>>>> Stashed changes
    }
}