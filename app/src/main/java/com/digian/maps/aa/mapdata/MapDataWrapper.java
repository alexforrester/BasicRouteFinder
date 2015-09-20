package com.digian.maps.aa.mapdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by forrestal on 19/09/2015.
 */
public class MapDataWrapper {

    @SerializedName("status")
    private String Status;

    public String getStatus() {
        return Status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @SerializedName("routes")
    private List<Route> routes;



}
