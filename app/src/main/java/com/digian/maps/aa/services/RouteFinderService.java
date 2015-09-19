package com.digian.maps.aa.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.digian.maps.aa.Constants;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by forrestal on 19/09/2015.
 */
public class RouteFinderService extends IntentService{

    private static final String TAG = RouteFinderService.class.getSimpleName();

    /**
     * Required public constructor
     */
    public RouteFinderService() {
        super("RouteFinderService");
        Log.v(TAG, "RouteFinderService()");
    }


    @Override
    protected void onHandleIntent(final Intent intent) {

        switch(intent.getAction()) {
            case Constants.ROUTE_FINDER_DESTINATION_PLOTTER:

                String requestString = intent.getStringExtra(Constants.DESTINATION_LOCATION);
                Location origin = intent.getParcelableExtra(Constants.ORIGIN_LOCATION);


                sendRouteFinderRequest(origin, requestString);
        }

    }

    private void sendRouteFinderRequest(final Location origin, final String requestString) {

        LatLng originLatLng = new LatLng(origin.getLatitude(),origin.getLongitude());

    }


}
