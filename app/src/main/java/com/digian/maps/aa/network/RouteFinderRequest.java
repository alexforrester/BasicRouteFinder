package com.digian.maps.aa.network;

import com.digian.maps.aa.mapdata.MapDataWrapper;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by forrestal on 19/09/2015.
 */
public interface RouteFinderRequest {

        @GET("/maps/api/directions/json")
        Call<MapDataWrapper> getMapData(@Query("origin") String orig, @Query("destination") String dest, @Query("region") String reg, @Query("mapKey") String mapKey );
}
