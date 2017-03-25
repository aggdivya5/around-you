package com.mytechwall.aroundu;

import java.io.Serializable;

public class MyPlace implements Serializable {

    String icon;
    String name;
    String latitude;
    String longitude;
    String rating;
    String vicinity;

    public MyPlace(String icon, String name, String latitude, String longitude, String rating, String vicinity) {
        this.icon = icon;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.vicinity = vicinity;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRating() {
        return rating;
    }

    public String getVicinity() {
        return vicinity;
    }
}
