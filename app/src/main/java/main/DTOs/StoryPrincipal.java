package main.DTOs;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class StoryPrincipal implements Serializable {
    private String nombre;
    private String photo;
    private String profile;
    private Loc ubicacion;
    private Double temperatura;
    private int intentos;
    private Double promedio;

    public StoryPrincipal(String nombre, String photo, String profile, Loc ubicacion, Double temperatura) {
        this.nombre = nombre;
        this.photo = photo;
        this.profile = profile;
        this.ubicacion = ubicacion;
        this.temperatura = temperatura;
        this.intentos = 0;
        this.promedio = 0d;
    }

    public void addIntentos(){
        this.intentos = this.intentos +1;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public void addPromedio(Double extra){
        this.promedio = (this.promedio+extra)/this.intentos;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

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
                ", intentos=" + intentos +
                ", promedio=" + promedio +
                '}';
    }
}
