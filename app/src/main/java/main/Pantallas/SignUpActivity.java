package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    EditText nombre;
    EditText correo;
    EditText password1;
    EditText password2;
    public static final String TAG=" Cityshare";
    private FirebaseAuth mAuth;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inflate();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;
                String password;
                String passwordx;
                email= correo.getText().toString();
                password=password1.getText().toString();
                passwordx=password2.getText().toString();
                Boolean flag=true;
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correo.setError("Formato de correo invalido");
                    Toast.makeText(getApplicationContext(),TAG+" Formato de correo invalido ",Toast.LENGTH_LONG).show();
                    flag=false;
                }else{
                    flag=true;
                }
                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(),TAG+" Se requiere un correo electronico ",Toast.LENGTH_LONG).show();
                    correo.setError("Se require un correo electronico");
                    flag=false;
                }else{
                    flag=true;
                }
                if (password.isEmpty()||password.length()<6) {
                    Toast.makeText(getApplicationContext(),TAG+" Se requiere una contraseña de mas de 6 caracteres ",Toast.LENGTH_LONG).show();
                    password1.setError(" Se requiere una contraseña de mas de 6 caracteres ");
                    flag=false;
                }else{
                    flag=true;
                }
                if(password.equals(passwordx)&&flag==true) {
                    registerUser(email, password);
                }else{
                    Toast.makeText(getApplicationContext(),TAG+" Las contraseñas no coinciden ",Toast.LENGTH_LONG).show();
                }
            }
        });

        }

    public void launchLogIn(View v){
        startActivity(new Intent(this, LogInActivity.class));
    }
    public void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Regist Success
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                      //Success
                        Intent i = new Intent(getApplicationContext(),LogInActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),TAG+" Registro completado! ",Toast.LENGTH_LONG).show();
                    }
                }else if(!task.isSuccessful()){
                    //not success
                    Toast.makeText(getApplicationContext(),TAG+" Hubo un error en el registro ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void inflate() {
        nombre = findViewById(R.id.editTextNombre);
        correo = findViewById(R.id.editTextCorreo);
        password1 = findViewById(R.id.editTextPassword1);
        password2 = findViewById(R.id.editTextPassword2);
        register=findViewById(R.id.register_button);
        mAuth= FirebaseAuth.getInstance();
    }
}