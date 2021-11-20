package main.model;

public class MyUser {
    String nombre;
    String usuario;

    public MyUser(String nombre, String usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public MyUser() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
