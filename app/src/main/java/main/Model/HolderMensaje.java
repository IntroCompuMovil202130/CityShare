package main.Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import main.Pantallas.R;

public class HolderMensaje extends RecyclerView.ViewHolder {
    TextView nombre;
    TextView mensaje;
    TextView hora;
    ImageView fotoMensaje;
    public HolderMensaje(View itemView) {
        super(itemView);
        nombre=(TextView) itemView.findViewById(R.id.nombreMensaje);
        mensaje=itemView.findViewById(R.id.messageMessage);
        hora=itemView.findViewById(R.id.horaMensaje);
        fotoMensaje=itemView.findViewById(R.id.fotoPerfilMensaje);
    }

    public TextView getNombrex() {
        return nombre;
    }

    public void setNombrex(TextView nombrex) {
        this.nombre = nombrex;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }
}
