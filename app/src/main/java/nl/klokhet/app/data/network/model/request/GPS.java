package nl.klokhet.app.data.network.model.request;

import java.io.Serializable;

public class GPS implements Serializable {
    private String lat;
    private String lon;

    public GPS() {
    }

    public GPS(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
