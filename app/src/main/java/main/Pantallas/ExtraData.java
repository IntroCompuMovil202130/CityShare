package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import main.model.MyUser;

public class ExtraData extends AppCompatActivity {
    Double latX;
    Double lonX;
    public static final int GALLERY_ID=0;
    public static final int IMAGE_PICK_CODE = 1;
    public static final int permission_id = 2;
    public static final int permission_coarse_id = 3;
    String permissionGallery= Manifest.permission.READ_EXTERNAL_STORAGE;
    static final String PATH_USERS="users/";
    String permissionLocation=Manifest.permission.ACCESS_FINE_LOCATION;
    String permissionCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Uri filePath;
    EditText nombre,usuario;
    StorageReference storageReference;
    FirebaseStorage storage;
    Button register,SL;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FusedLocationProviderClient fLP;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    public static final String TAG=" Taller3 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_data);
        //requestPermission(this,permissionLocation,"Get Lat and Long",permission_id);
        //requestPermission(this,permissionCoarseLocation,"To easily update position",permission_coarse_id);
        inflate();
        initView();
        SL.setOnClickListener(new View.OnClickListener() {
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
                //Sign Up
                String nombreX;
                String usuariox;

                nombreX=nombre.getText().toString();
                usuariox=usuario.getText().toString();
                Boolean flag=true;
                if(nombreX.isEmpty()||usuariox.isEmpty()){
                    Toast.makeText(getApplicationContext(),TAG+" Se deben rellenar todos los campos ",Toast.LENGTH_LONG).show();
                    flag=false;
                }
                if(flag==true){
                    saveData(nombreX,usuariox);
                    uploadFile();
                    //Intent i = new Intent(getApplicationContext(),LogInActivity.class);
                    //startActivity(i);
                }

            }
        });
    }
    public void inflate(){
        nombre=findViewById(R.id.editTextNombre);
        usuario=findViewById(R.id.editTextUserName);
        register=findViewById(R.id.boton_registro);
        SL=findViewById(R.id.select_image_contact);
        database= FirebaseDatabase.getInstance();
        fLP= LocationServices.getFusedLocationProviderClient(this);
        mAuth= FirebaseAuth.getInstance();
        imageView=findViewById(R.id.imagenC);
        storage=FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
    private void saveData(String nombre, String usuario) {
        MyUser user=new MyUser();
        user.setNombre(nombre);
        user.setUsuario(usuario);
        //write data
        myRef=database.getReference(PATH_USERS);
        String key= mAuth.getUid();
        myRef=database.getReference(PATH_USERS+key);
        myRef.setValue(user);
    }

    private void uploadFile(){
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
                                    + mAuth.getUid());
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
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == GALLERY_ID){
            pickImageFromGallery();
        }
        if(requestCode==permission_id){
            initView();
        }
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
    private void initView(){
        if(ContextCompat.checkSelfPermission(this,permissionLocation)== PackageManager.PERMISSION_GRANTED){
            fLP.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        lonX=Double.valueOf(location.getLongitude());
                        latX=Double.valueOf(location.getLatitude());
                    }
                }
            });
        }
    }
    public static void requestPermission(Activity context, String permission, String justification, int id){
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){

        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, id);
        }
    }
}