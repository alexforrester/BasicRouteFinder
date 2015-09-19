package com.digian.maps.aa.network;

import com.digian.maps.aa.mapdata.MapDataWrapper;
import com.google.android.gms.maps.model.LatLng;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by forrestal on 19/09/2015.
 */
public interface RouteFinderRequest {

        @GET("/maps/api/directions/json?origin={originlatlng}&destination={destinationString}&key={mapKey}")
        MapDataWrapper listRepos(@Path("originlatlng") LatLng originLatlng, @Path("destinationString") String destinationString, @Path("mapKey") String mapKey );
}
