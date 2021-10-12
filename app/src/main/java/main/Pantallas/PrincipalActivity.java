package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PrincipalActivity extends AppCompatActivity {

    String galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int GALLERY_ID = 5001;
    public static final int IMAGE_PICK_CODE = 9;

    ImageButton botonCrearPostCamara;
    ImageButton botonCrearPostGaleria;
    ImageView historiaUsuario;
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

//    public void launchPostsActivity(View v){
//        startActivity(new Intent(this, PostsActivity.class));  //cambiar para que venga desde la actitividad de Menchu con botón de "adivinar"
//    }

    public void createStoryCam(View v){

    }

    public void createStoryGal(View v){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            String[] permissions = {galleryPermission};
            requestPermissions(permissions, GALLERY_ID);

        }
        else{
            pickImageFromGallery();
        }
    }

    public void userStorySee(View v){

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
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == GALLERY_ID ){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                pickImageFromGallery();
            }
        }
    }

    private void inflate() {
        botonCrearPostCamara = findViewById(R.id.botonPost);
        botonCrearPostGaleria = findViewById(R.id.botonPost2);
        historiaUsuario = findViewById(R.id.imageView5);
        botonHistorias = findViewById(R.id.botonHistorias);
        chat = findViewById(R.id.botonChat);
        imagenEjemplo = findViewById(R.id.imageView6);
    }
}