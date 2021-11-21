package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import main.Model.Usuario;

public class SignUpActivity extends AppCompatActivity {

    EditText nombre;
    EditText correo;
    EditText usuario;
    EditText password1;
    EditText password2;
    ImageView contacto;
    private Uri filePath;
    FirebaseStorage storage;
    String permissionGallery= Manifest.permission.READ_EXTERNAL_STORAGE;
    static final String PATH_USERS="users/";
    public static final String TAG=" Cityshare";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    StorageReference storageReference;
    private DatabaseReference ref;
    Button register,selectImage;
    public static final int GALLERY_ID=0;
    public static final int IMAGE_PICK_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inflate();
        selectImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    String[] permissions = {permissionGallery};
                    requestPermissions(permissions, GALLERY_ID);
                }
                else{
                    pickImageFromGallery();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;
                String password;
                String passwordx;
                Log.d("REGISTER",validateFields().toString());
                if(!validateFields())
                    return;
                email= correo.getText().toString();
                password=password1.getText().toString();
                passwordx=password2.getText().toString();
                String name = nombre.getText().toString();
                String user = usuario.getText().toString();
                boolean flag=true;
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
                if(password.equals(passwordx)&& flag) {
                    Log.d("REGISTER","Voy a registrar");
                    registerUser(email, password, user, name);
                }else{
                    Toast.makeText(getApplicationContext(),TAG+" Las contraseñas no coinciden ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Boolean validateFields() {
        return !nombre.getText().toString().equals("") && !correo.getText().toString().equals("") &&
                !usuario.getText().toString().equals("") && !password1.getText().toString().equals("")
                && !password2.getText().toString().equals("");
    }

    public void launchLogIn(View v){
        startActivity(new Intent(this, LogInActivity.class));
    }
    public void registerUser(String email, String password, String username, String name){
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
                        FirebaseUser us = mAuth.getCurrentUser();
                        saveData(name,username,email,us);
                        uploadFile(us);

                        Toast.makeText(getApplicationContext(),TAG+" Registro completado! ",Toast.LENGTH_LONG).show();
                    }
                }else if(!task.isSuccessful()){
                    //not success
                    Toast.makeText(getApplicationContext(),TAG+" Hubo un error en el registro ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void saveData(String name, String username, String email,FirebaseUser us) {
        Usuario usuario = new Usuario(name,username,email);
        //write data
        ref.child("Users").child(us.getUid()).setValue(usuario);
    }
    private void pickImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                IMAGE_PICK_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GALLERY_ID){
            pickImageFromGallery();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE&&data!=null&&data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                contacto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    private void uploadFile(FirebaseUser us){
        if(filePath!=null){
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + us.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
    private void inflate() {
        nombre = findViewById(R.id.editTextNombre);
        correo = findViewById(R.id.editTextCorreo);
        usuario = findViewById(R.id.editTextUserName);
        password1 = findViewById(R.id.editTextPassword1);
        password2 = findViewById(R.id.editTextPassword2);
        register=findViewById(R.id.register_button);
        mAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        contacto=findViewById(R.id.contactImage);
        selectImage=findViewById(R.id.subirImagen);
        storage=FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
}