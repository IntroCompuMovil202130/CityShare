package main.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import main.DTOs.FilaJugador;
import main.DTOs.FilaStory;
import main.Pantallas.R;

public class FilaStoryAdapter extends ArrayAdapter {
    public FilaStoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public FilaStoryAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FilaStoryAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    public FilaStoryAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FilaStoryAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public FilaStoryAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        FilaStory filaStory = (FilaStory) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_story, parent, false);
        }

        ImageView picture = convertView.findViewById(R.id.fotoStory);
        TextView status = convertView.findViewById(R.id.estadoStory);
        TextView guesses = convertView.findViewById(R.id.noAdivinanzas);
        TextView distance = convertView.findViewById(R.id.distPromedio);

        final long MEGA = 1024*1024;
        ref.child(filaStory.getPicture()).getBytes(MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                picture.setImageBitmap(bitmap);
                status.setText(filaStory.getStatus());
                guesses.setText(String.valueOf(filaStory.getGuesses()));
                distance.setText(String.valueOf(filaStory.getDistance()));
                Log.d("TAG", "onSuccess: "+filaStory.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("STORAGE", e.toString());
            }
        });

        return convertView;
    }
}
