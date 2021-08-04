package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PantallaLogIn extends AppCompatActivity {
    EditText correo;
    EditText password;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallalogin);

        inflate();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Se hara inicio de sesión",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void inflate(){
        correo = findViewById(R.id.correoLogIn);
        password = findViewById(R.id.pswLogIn);
        logIn = findViewById(R.id.botonLogIn);
    }
}