package com.digian.maps.aa.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.services.RouteFinderService;
import com.digian.maps.aa.views.RouteFinderView;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by forrestal on 19/09/2015.
 */
public class RouterFinderPresenter {

    private static final String TAG = RouterFinderPresenter.class.getSimpleName();

    @VisibleForTesting final Context mContext;
    @VisibleForTesting final RouteFinderView mRoutFinderView;
    @VisibleForTesting final MapDataStateReceiver mMapDataStateReceiver;

    public static RouterFinderPresenter newInstance(@NonNull final Context context, @NonNull final RouteFinderView routeFinderView) throws IllegalStateException {
        return new RouterFinderPresenter(context,routeFinderView);
    }

    @VisibleForTesting
    RouterFinderPresenter(@NonNull final Context context,@NonNull final RouteFinderView routeFinderView){
        Log.v(TAG,"RouterFinderPresenter(@NonNull final Context context,@NonNull final RouteFinderView routeFinderView)");

        if (context == null) throw new IllegalStateException("context cannot be null");

        if (routeFinderView == null) throw new IllegalStateException("routeFinderView cannot be null");

        mContext = context;
        mRoutFinderView = routeFinderView;
        mMapDataStateReceiver = MapDataStateReceiver.newInstance(this);
        setUpMapDataStateReceiver();
    }

    public void getRouteLegs(@NonNull final Location originlocation,@NonNull final String destination) {
        Log.v(TAG,"getRouteLegs(@NonNull final Location originlocation,@NonNull final String destination)");

        if (originlocation == null) throw new IllegalStateException("origin location cannot be null");

        if (destination == null) throw new IllegalStateException("destination cannot be null");

        final Intent serviceIntent = new Intent(mContext,RouteFinderService.class);
        serviceIntent.setAction(Constants.ROUTE_FINDER_DESTINATION_PLOTTER);
        serviceIntent.putExtra(Constants.ORIGIN_LOCATION, originlocation);
        serviceIntent.putExtra(Constants.DESTINATION_LOCATION, destination);
        mContext.startService(serviceIntent);
    }

    @VisibleForTesting
    void setUpMapDataStateReceiver() {
        Log.v(TAG,"setUpMapDataStateReceiver()");

        IntentFilter intentFilterMapSuccess = new IntentFilter(Constants.BROADCAST_MAP_RETRIEVED_SUCCESS);
        IntentFilter intentFilterMapFailure = new IntentFilter(Constants.BROADCAST_MAP_RETRIEVED_FAILURE);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.registerReceiver(mMapDataStateReceiver, intentFilterMapSuccess);
        localBroadcastManager.registerReceiver(mMapDataStateReceiver, intentFilterMapFailure);
    }

    void processSuccessfulMapData(@NonNull final PolylineOptions polylineOptions) {
        Log.v(TAG,"processSuccessfulMapData(PolylineOptions polylineOptions)");
        mRoutFinderView.displayRoute(polylineOptions);
    }

    void processFailureMapData(@NonNull final String errorMessage) {
        Log.v(TAG,"processFailureMapData(String errorMessage)");
        mRoutFinderView.displayError(errorMessage);
    }

    static class MapDataStateReceiver extends BroadcastReceiver{

        private static final String TAG = MapDataStateReceiver.class.getSimpleName();
        RouterFinderPresenter mRouteFinderPresenter;

        public static MapDataStateReceiver newInstance(@NonNull final RouterFinderPresenter routerFinderPresenter) {
            return new MapDataStateReceiver(routerFinderPresenter);
        }

        private MapDataStateReceiver(@NonNull final RouterFinderPresenter routerFinderPresenter) {
            Log.v(TAG,"MapDataStateReceiver(@NonNull final RouterFinderPresenter routerFinderPresenter) ");

            if (routerFinderPresenter == null) throw new IllegalStateException("routerFinderPresenter cannot be null");

            mRouteFinderPresenter = routerFinderPresenter;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v(TAG,"onReceive(final Context context, final Intent intent)");

            switch(intent.getAction()) {
                case Constants.BROADCAST_MAP_RETRIEVED_SUCCESS:
                    Log.v(TAG, "intent with action Constants.BROADCAST_MAP_RETRIEVED_SUCCESS");
                    PolylineOptions polylineOptions = (PolylineOptions) intent.getParcelableExtra(Constants.POLYLINE_OPTIONS_DATA);
                    mRouteFinderPresenter.processSuccessfulMapData(polylineOptions);
                    break;
                case Constants.BROADCAST_MAP_RETRIEVED_FAILURE:
                    Log.v(TAG, "intent with action Constants.BROADCAST_MAP_RETRIEVED_FAILURE");
                    String failureErrorMessage = intent.getStringExtra(Constants.FAILURE_ERROR_MESSAGE);
                    mRouteFinderPresenter.processFailureMapData(failureErrorMessage);
                    break;
            }
        }
    }
}
