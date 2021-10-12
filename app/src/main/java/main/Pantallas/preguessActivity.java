package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class preguessActivity extends AppCompatActivity {

    TextView estadistica;
    Button adivinar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguess);

        inflate();

        estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScoreActivity.class));
            }
        });

        adivinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GuessActivity.class));
            }
        });
    }

    private void inflate() {
        estadistica = findViewById(R.id.textView6);
        adivinar = findViewById(R.id.buttonAdivinar);
    }
}