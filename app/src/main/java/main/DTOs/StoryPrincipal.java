package main.DTOs;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class StoryPrincipal implements Serializable {
    private String nombre;
    private String photo;
    private String profile;
    private Loc ubicacion;
    private Double temperatura;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Loc getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Loc ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public StoryPrincipal(String nombre, String photo, String profile, Loc ubicacion, Double temperatura) {
        this.nombre = nombre;
        this.photo = photo;
        this.profile = profile;
        this.ubicacion = ubicacion;
        this.temperatura = temperatura;
    }

    public StoryPrincipal() {
    }

    @Override
    public String toString() {
        return "StoryPrincipal{" +
                "nombre='" + nombre + '\'' +
                ", photo='" + photo + '\'' +
                ", profile='" + profile + '\'' +
                ", ubicacion=" + ubicacion +
                ", temperatura=" + temperatura +
                '}';
    }
}
