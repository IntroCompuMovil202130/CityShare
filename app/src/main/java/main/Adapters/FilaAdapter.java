package main.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import main.DTOs.FilaJugador;
import main.Pantallas.R;

public class FilaAdapter extends ArrayAdapter {
    public FilaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public FilaAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FilaAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    public FilaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FilaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public FilaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FilaJugador filaJugador = (FilaJugador) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila, parent, false);
        }

        ImageView picture = (ImageView) convertView.findViewById(R.id.foto);
        TextView nombre = (TextView) convertView.findViewById(R.id.nameUser);
        TextView contenido = (TextView) convertView.findViewById(R.id.search);

        picture.setImageResource(R.drawable.user);
        nombre.setText(filaJugador.getName());

        String sourceString = filaJugador.getContent();
        contenido.setText(Html.fromHtml(sourceString));

        return convertView;
    }
}
