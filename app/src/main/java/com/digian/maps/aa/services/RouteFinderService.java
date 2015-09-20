package com.digian.maps.aa.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.LocalBroadcastManager;
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
                Log.v(TAG, "processing intent with action ROUTE_FINDER_DESTINATION_PLOTTER)");
                String requestString = intent.getStringExtra(Constants.DESTINATION_LOCATION);
                Location origin = intent.getParcelableExtra(Constants.ORIGIN_LOCATION);

                sendRouteFinderRequest(origin, requestString);
        }
    }

    @VisibleForTesting
    void sendRouteFinderRequest(@NonNull final Location originLocation, @NonNull final String destinationLocation) throws IllegalStateException {
        Log.v(TAG, "sendRouteFinderRequest(@NonNull final Location origin, @NonNull final String destinationLocation)");

        if (originLocation == null) throw new IllegalStateException("originLocation cannot be null");

        if (destinationLocation == null) throw new IllegalStateException("destinationLocation cannot be null");

        StringBuilder origin = new StringBuilder(String.valueOf(originLocation.getLatitude()));
        origin.append(",");
        origin.append(String.valueOf(originLocation.getLongitude()));

        RouteFinderRequestHelper routeFinderRequestHelper = RouteFinderRequestHelper.newInstance(this.getApplicationContext(),this);
        routeFinderRequestHelper.getMapData(origin.toString(),destinationLocation);
    }

    @Override
    public void onRequestSuccess(@NonNull final PolylineOptions polylineOptions) {
        Log.v(TAG, "onRequestSuccess(@NonNull final PolylineOptions polylineOptions)");

        if (polylineOptions == null) throw new IllegalStateException("polylineOptions cannot be null");

        Intent localIntent = new Intent(Constants.BROADCAST_MAP_RETRIEVED_SUCCESS).putExtra(Constants.POLYLINE_OPTIONS_DATA, polylineOptions);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    @Override
    public void onRequestFailure(final String errorMessage) {
        Log.v(TAG, "onRequestFailure(final String errorMessage)");

        if (errorMessage == null) throw new IllegalStateException("errorMessage cannot be null");

        Intent localIntent = new Intent(Constants.BROADCAST_MAP_RETRIEVED_FAILURE).putExtra(Constants.FAILURE_ERROR_MESSAGE, errorMessage);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
