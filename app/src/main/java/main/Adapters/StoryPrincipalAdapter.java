package main.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import main.DTOs.StoryPrincipal;
import main.DTOs.onStoryListener;
import main.Pantallas.R;

public class StoryPrincipalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<StoryPrincipal> list;
    private onStoryListener storyListener;

    public StoryPrincipalAdapter(List<StoryPrincipal> list, onStoryListener storyListener) {
        this.list = list;
        this.storyListener = storyListener;
    }

    private class CustomAdapteritemView extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView photo;
        ImageView profile;
        TextView name;

        onStoryListener storyListener;

        public CustomAdapteritemView(final View itemView, onStoryListener listener) {
            super(itemView);
            this.photo = itemView.findViewById(R.id.storyFotoPrincipal);
            this.profile = itemView.findViewById(R.id.storyFotoPerfil);
            this.name = itemView.findViewById(R.id.storyNombreUsuario);
            this.storyListener = listener;

            itemView.setOnClickListener(this);
        }
        void bind(int position){
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();

            //Get profile
            final long MEGA = 1024*1024;
            Log.d("PROFILE",list.get(position).getProfile());
            ref.child(list.get(position).getProfile()).getBytes(MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    profile.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("STORAGE", e.toString());
                }
            });
            //Get Photo
            ref.child(list.get(position).getPhoto()).getBytes(MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    photo.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("STORAGE", e.toString());
                }
            });
            //Get name
            name.setText(list.get(position).getNombre());
        }

        @Override
        public void onClick(View view) {
            storyListener.onStoryClick(getAdapterPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_principal, parent, false);
        return new CustomAdapteritemView(view, storyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomAdapteritemView)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
