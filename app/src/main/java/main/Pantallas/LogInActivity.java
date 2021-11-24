package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity {
    EditText correo;
    EditText password;
    Button logIn;
    TextView regis;
    public static final String TAG=" Cityshare";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        inflate();
    }
    public void launchSignUpActivity(View v){
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }
    private void inflate(){
        correo = findViewById(R.id.correoLogIn);
        password = findViewById(R.id.pswLogIn);
        logIn = findViewById(R.id.boton);
        mAuth= FirebaseAuth.getInstance();
        regis= findViewById(R.id.regist);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
            Intent intentNotifications = new Intent(LogInActivity.this, NotificationService.class);
            intentNotifications.putExtra("myKey" , mAuth.getUid());
            startService(intentNotifications);
            Intent intentNotificationsChat = new Intent(LogInActivity.this, NotificationServiceChat.class);
            intentNotificationsChat.putExtra("myKey" , mAuth.getUid());
            startService(intentNotificationsChat);
        }else{
            correo.setText("");
            password.setText("");
        }
    }
    public void signInPressed(View V){
        String emailS;
        String passwordS;
        if(!correo.getText().toString().isEmpty()){
            emailS= correo.getText().toString();
        }else{
            emailS="null";
        }
        if (!password.getText().toString().isEmpty()) {
            passwordS = password.getText().toString();
        }else{
            passwordS="null";
        }

         if(validateForm(emailS,passwordS)){
            mAuth.signInWithEmailAndPassword(emailS,passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Autenticación correcta");
                        updateUI(mAuth.getCurrentUser());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), TAG + " Autenticacion fallida: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                        correo.setText("");
                        password.setText("");
                    }
                }
            });
        }
    }
    private boolean validateForm(String email,String password){
        return true;
    }
}