package com.digian.maps.aa.network;

import android.util.Log;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.mapdata.MapDataWrapper;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by forrestal on 20/09/2015.
 */
public class RouteFinderRequestHelper {

    private static final String TAG = RouteFinderRequestHelper.class.getSimpleName();

    public static RouteFinderRequestHelper newInstance() {
        return new RouteFinderRequestHelper();
    }

    public void getMapData(final String origin, final String destination) {
        Log.v(TAG,"getMapData(final String origin, final String destination)");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.GOOGLE_DIRECTIONS_API_REQUEST_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RouteFinderRequest routeFinderRequest = retrofit.create(RouteFinderRequest.class);

        Call<MapDataWrapper> call = routeFinderRequest.getMapData(origin,destination,Constants.GOOGLE_MAPS_KEY);

        call.enqueue(new Callback<MapDataWrapper>() {
            @Override
            public void onResponse(Response<MapDataWrapper> response) {
                Log.v(TAG,"Successful Response");
                MapDataWrapper mapDataWrapper = response.body();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(TAG,"Failure Response");
                t.getMessage();
            }
        });
    }

    void processMapData(MapDataWrapper mapDataWrapper) {

        //Check Status and if not Ok report back error

        //Create Iterable<LatLng> and use PolylineOptions.addAll(Iterable<LatLng>)

        //




        /*Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0)); // Closes the polyline.
                */




    }


}
