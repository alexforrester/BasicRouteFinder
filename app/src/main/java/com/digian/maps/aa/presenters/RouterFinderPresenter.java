package com.digian.maps.aa.presenters;

import android.content.Context;
import android.content.Intent;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.services.RouteFinderService;
import com.digian.maps.aa.views.RouteFinderView;

/**
 * Created by forrestal on 19/09/2015.
 */
public class RouterFinderPresenter {

    final Context mContext;
    final RouteFinderView mRoutFinderView;

    public static RouterFinderPresenter newInstance(final Context context, final RouteFinderView routeFinderView) throws IllegalStateException {
        return new RouterFinderPresenter(context,routeFinderView);
    }

    private RouterFinderPresenter(final Context context,final RouteFinderView routeFinderView){

        if (context == null) throw new IllegalStateException("context cannot be null");

        if (routeFinderView == null) throw new IllegalStateException("routeFinderView cannot be null");

        mContext = context;
        mRoutFinderView = routeFinderView;
    }

    public void getRouteLegs(final String destination) {
        final Intent serviceIntent = new Intent(mContext,RouteFinderService.class);
        serviceIntent.setAction(Constants.ROUTE_FINDER_DESTINATION_PLOTTER);
        serviceIntent.putExtra(Constants.DESTINATION_LOCATION, destination);
        mContext.startService(serviceIntent);
    }
}
