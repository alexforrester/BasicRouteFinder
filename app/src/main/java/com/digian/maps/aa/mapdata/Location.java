package com.digian.maps.aa.mapdata;

import com.google.gson.annotations.SerializedName;

/**
 * Created by forrestal on 20/09/2015.
 */
public class Location {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
