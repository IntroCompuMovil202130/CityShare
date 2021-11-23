package main.DTOs;

import android.graphics.drawable.Drawable;

public class FilaStory {
    private String picture;
    private String status;
    private Integer guesses;
    private Double distance;

    public FilaStory() {
    }

    public FilaStory(String picture, String status, Integer guesses, Double distance) {
        this.picture = picture;
        this.status = status;
        this.guesses = guesses;
        this.distance = distance;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getGuesses() {
        return guesses;
    }

    public void setGuesses(Integer guesses) {
        this.guesses = guesses;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "FilaStory{" +
                "picture='" + picture + '\'' +
                ", status='" + status + '\'' +
                ", guesses=" + guesses +
                ", distance=" + distance +
                '}';
    }
}
