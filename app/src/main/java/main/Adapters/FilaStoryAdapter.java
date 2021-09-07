package main.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

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
        FilaStory filaStory = (FilaStory) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_story, parent, false);
        }

        ImageView picture = (ImageView) convertView.findViewById(R.id.fotoStory);
        TextView status = (TextView) convertView.findViewById(R.id.estadoStory);
        TextView guesses = (TextView) convertView.findViewById(R.id.noAdivinanzas);
        TextView distance = (TextView) convertView.findViewById(R.id.distPromedio);

        switch (filaStory.getPicture()) {
            case 1:
                picture.setImageResource(R.drawable.camera);
            case 2:
                picture.setImageResource(R.drawable.camera);
            case 3:
                picture.setImageResource(R.drawable.camera);
            case 4:
                picture.setImageResource(R.drawable.camera);
            case 5:
                picture.setImageResource(R.drawable.camera);
            case 6:
                picture.setImageResource(R.drawable.camera);
            default:
                picture.setImageResource(R.drawable.camera);
        }

        status.setText(filaStory.getStatus());
        guesses.setText(filaStory.getGuesses().toString());
        distance.setText(filaStory.getDistance().toString());

        return convertView;
    }
}
