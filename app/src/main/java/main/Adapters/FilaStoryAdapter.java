package main.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
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


//        switch (filaStory.getPicture()) {
//            case 1:
//                picture.setImageResource(R.drawable.miami);
//            case 2:
//                picture.setImageResource(R.drawable.boston);
//            case 3:
//                picture.setImageResource(R.drawable.london);
//            case 4:
//                picture.setImageResource(R.drawable.miami);
//            case 5:
//                picture.setImageResource(R.drawable.boston);
//            case 6:
//                picture.setImageResource(R.drawable.london);
//            default:
//                picture.setImageResource(R.drawable.miami);
//        }

        picture.setImageResource(R.drawable.miami3);

        String sourceString = "<b>Estado: </b>" + "Disponible";
        status.setText(Html.fromHtml(sourceString));
        sourceString = "<b>No. Adivinanzas: </b>" + filaStory.getGuesses();
        guesses.setText(Html.fromHtml(sourceString));
        sourceString = "<b>Dist. Promedio: </b>" + filaStory.getDistance();
        distance.setText(Html.fromHtml(sourceString));

        return convertView;
    }
}
