package com.digian.maps.aa.mapdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by forrestal on 20/09/2015.
 */
public class Leg {

    @SerializedName("steps")
    private List<Step> steps;
}
