package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PrincipalActivity extends AppCompatActivity {

    ImageButton botonCrearPost;
    Button botonHistorias;
    Button chat;
    ImageView imagenEjemplo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        inflate();

        imagenEjemplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),preguessActivity.class));
            }
        });
    }

    public void launchPostsActivity(View v){
        startActivity(new Intent(this, PostsActivity.class));  //cambiar para que venga desde la actitividad de Menchu con botón de "adivinar"
    }

    public void launchStoryActivity(View v){
        startActivity(new Intent(this,StoryActivity.class));
    }

    public void launchChatActivity(View v){
        startActivity(new Intent(this,ChallengesActivity.class));
    }
    private void inflate() {
        botonCrearPost = findViewById(R.id.botonPost);
        botonHistorias = findViewById(R.id.botonHistorias);
        chat = findViewById(R.id.botonChat);
        imagenEjemplo = findViewById(R.id.imageView6);
    }
}