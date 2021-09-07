package main.DTOs;


import android.graphics.drawable.Drawable;

import main.Pantallas.R;

public class FilaJugador {
    private Integer picture;
    private String name;
    private String content;


    public FilaJugador() {
    }

    public FilaJugador(Integer picture, String name, String content) {
        this.picture = picture;
        this.name = name;
        this.content = content;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
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
