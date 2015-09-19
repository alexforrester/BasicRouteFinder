package com.digian.maps.aa.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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

    }
}
