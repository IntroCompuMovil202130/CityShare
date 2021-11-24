package main.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import main.Model.Usuario;
import main.Pantallas.ChatActivity;
import main.Pantallas.R;

public class ListUsersAdapter extends ArrayAdapter {

    StorageReference storageReference;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    public ListUsersAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ListUsersAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ListUsersAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Usuario user=(Usuario) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila, parent, false);
        }
        //Todo image view
        ImageView image = (ImageView) convertView.findViewById(R.id.foto);
        TextView nombre = (TextView) convertView.findViewById(R.id.nameUser);
        Button button = (Button) convertView.findViewById(R.id.search);

        nombre.setText(user.getUserName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(v.getContext(), ChatActivity.class);
                i.putExtra("nombre",user.getUserName());
                v.getContext().startActivity(i);
            }
        });
       /* String search = "gs://taller-3-32a3f.appspot.com/images/";
        search = search + user.getCode()+"/"+"contactImage";
        StorageReference gsReference= FirebaseStorage.getInstance().getReferenceFromUrl(search);
        final long ONE_MEGABYTE = 1024 * 1024;
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                user.setImage(bytes);
                if(user.getImage()!=null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(),0,user.getImage().length);
                    image.setImageBitmap(bmp);
                }else{
                    Log.d("Failure", "Failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AHHH","Helper: " + e);
            }
        });*/
        image.setImageResource(R.drawable.nando_lorris);
        return convertView;
    }

}
