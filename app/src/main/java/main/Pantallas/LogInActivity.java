package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    EditText correo;
    EditText password;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        inflate();
    }

    public void launchPrincipalActivity(View v){
        startActivity(new Intent(this,PrincipalActivity.class));
    }

    private void inflate(){
        correo = findViewById(R.id.correoLogIn);
        password = findViewById(R.id.pswLogIn);
        logIn = findViewById(R.id.boton);
    }
}