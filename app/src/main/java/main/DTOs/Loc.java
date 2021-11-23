package main.DTOs;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class Loc implements Serializable {
    private Double latitude;
    private Double longitude;

    public Loc(Double lat, Double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public Loc(LatLng latLng){
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Loc() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loc loc = (Loc) o;
        return Objects.equals(latitude, loc.latitude) && Objects.equals(longitude, loc.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Loc{" +
                "lat=" + latitude +
                ", lon=" + longitude +
                '}';
    }
}
