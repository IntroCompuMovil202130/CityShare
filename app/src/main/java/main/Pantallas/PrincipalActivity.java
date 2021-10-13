package main.Pantallas;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;

import com.google.firebase.auth.FirebaseAuth;


public class PrincipalActivity extends AppCompatActivity {

    String galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    String cameraPermission = Manifest.permission.CAMERA;
    public static final int GALLERY_ID_PERMISSION = 5001;
    public static final int CAMERA_ID_PERMISSION = 5002;
    public static final int IMAGE_PICK_ACTIVITY = 9;
    public static final int TAKE_PICTURE_ACTIVITY = 10;
    private boolean storyTaken = false;

    ImageButton botonCrearPostCamara;
    ImageButton botonCrearPostGaleria;
    ImageView historiaUsuario;
    Button botonHistorias;
    Button chat;
    ImageView imagenEjemplo;
    ImageButton signOut;
    private FirebaseAuth mAuth;
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
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

//    public void launchPostsActivity(View v){
//        startActivity(new Intent(this, PostsActivity.class));  //cambiar para que venga desde la actitividad de Menchu con botón de "adivinar"
//    }

    public void createStoryCam(View v){
        if(checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            String[] permissions = {cameraPermission};
            requestPermissions(permissions, CAMERA_ID_PERMISSION);

        }
        else{
            takePicture();
        }
    }

    public void createStoryGal(View v){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            String[] permissions = {galleryPermission};
            requestPermissions(permissions, GALLERY_ID_PERMISSION);

        }
        else{
            pickImageFromGallery();
        }
    }

    public void userStorySee(View v) {
        if (storyTaken) {
            historiaUsuario.setDrawingCacheEnabled(true);
            Bitmap b = historiaUsuario.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(new Intent(this, UserStoryActivity.class));

            intent.putExtra("picture", byteArray);
            startActivity(intent);
        } else {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {cameraPermission};
                requestPermissions(permissions, CAMERA_ID_PERMISSION);

            } else {
                takePicture();
            }
        }
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

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_ACTIVITY);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(intent, TAKE_PICTURE_ACTIVITY);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == GALLERY_ID_PERMISSION ){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                pickImageFromGallery();
            }
        }
        else if(requestCode == CAMERA_ID_PERMISSION ){
            if(checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED){

                takePicture();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Req code " + requestCode);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_ACTIVITY){
            historiaUsuario.setImageURI(data.getData());
            storyTaken = true;
        }

        else if(resultCode == RESULT_OK && requestCode == TAKE_PICTURE_ACTIVITY){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            historiaUsuario.setImageBitmap(imageBitmap);
            storyTaken = true;
        }
    }

    private void inflate() {
        botonCrearPostCamara = findViewById(R.id.botonPost);
        botonCrearPostGaleria = findViewById(R.id.botonPost2);
        historiaUsuario = findViewById(R.id.imageView5);
        botonHistorias = findViewById(R.id.botonHistorias);
        chat = findViewById(R.id.botonChat);
        imagenEjemplo = findViewById(R.id.imageView6);
        mAuth= FirebaseAuth.getInstance();
        signOut=findViewById((R.id.signout));
    }
}