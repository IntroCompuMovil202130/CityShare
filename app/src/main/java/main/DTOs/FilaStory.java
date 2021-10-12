package main.DTOs;

import android.graphics.drawable.Drawable;

public class FilaStory {
    private Integer picture;
    private String status;
    private Integer guesses;
    private Double distance;

    public FilaStory() {
    }

    public FilaStory(Integer picture, String status, Integer guesses, Double distance) {
        this.picture = picture;
        this.status = status;
        this.guesses = guesses;
        this.distance = distance;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
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
}
