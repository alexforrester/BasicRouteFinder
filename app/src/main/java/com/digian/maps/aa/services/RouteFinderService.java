package com.digian.maps.aa.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.network.RouteFinderRequestHelper;
import com.digian.maps.aa.network.RouteFinderRequestResult;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by forrestal on 19/09/2015.
 */
public class RouteFinderService extends IntentService implements RouteFinderRequestResult {

    private static final String TAG = RouteFinderService.class.getSimpleName();

    /**
     * Required public constructor
     */
    public RouteFinderService() {
        super("RouteFinderService");
        Log.v(TAG, "RouteFinderService()");
    }


    @Override
    protected void onHandleIntent(@NonNull final Intent intent) {
        Log.v(TAG, "onHandleIntent(final Intent intent)");

        switch(intent.getAction()) {
            case Constants.ROUTE_FINDER_DESTINATION_PLOTTER:

                String requestString = intent.getStringExtra(Constants.DESTINATION_LOCATION);
                Location origin = intent.getParcelableExtra(Constants.ORIGIN_LOCATION);

                sendRouteFinderRequest(origin, requestString);
        }
    }

    @VisibleForTesting
    void sendRouteFinderRequest(@NonNull final Location originLocation, @NonNull final String destination) {
        Log.v(TAG, "sendRouteFinderRequest(@NonNull final Location origin, @NonNull final String destination)");


        StringBuilder origin = new StringBuilder(String.valueOf(originLocation.getLatitude()));
        origin.append(",");
        origin.append(String.valueOf(originLocation.getLongitude()));

        RouteFinderRequestHelper routeFinderRequestHelper = RouteFinderRequestHelper.newInstance();
        routeFinderRequestHelper.getMapData(origin.toString(),destination);
    }

    @Override
    public void onRequestSuccess(final PolylineOptions polylineOptions) {

    }

    @Override
    public void onRequestFailure(final String errorMessage) {

    }
}
