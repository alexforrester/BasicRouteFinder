package com.digian.maps.aa.mapdata;


import com.google.gson.annotations.SerializedName;

/**
 * Created by forrestal on 20/09/2015.
 */
public class Step {

    @SerializedName("start_location")
    private Location startLocation;

    @SerializedName("end_location")
    private Location endLocation;

    @SerializedName("polyline")
    private Polyline polyline;
}

