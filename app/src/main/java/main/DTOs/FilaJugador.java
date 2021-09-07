package main.DTOs;


import android.graphics.drawable.Drawable;

import main.Pantallas.R;

public class FilaJugador {
    private Drawable picture;
    private String name;
    private String content;


    public FilaJugador() {
    }

    public FilaJugador(Drawable picture, String name, String content) {
        this.picture = picture;
        this.name = name;
        this.content = content;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
