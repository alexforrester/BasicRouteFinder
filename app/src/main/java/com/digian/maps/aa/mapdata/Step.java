package com.digian.maps.aa.mapdata;

import com.google.gson.annotations.SerializedName;

/**
 * Created by forrestal on 20/09/2015.
 */
public class Step {

    @SerializedName("polyline")
    private Polyline polyline;

    public Polyline getPolyline() {
        return polyline;
    }

}

