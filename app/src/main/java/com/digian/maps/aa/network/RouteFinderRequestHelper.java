package com.digian.maps.aa.network;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.R;
import com.digian.maps.aa.mapdata.Leg;
import com.digian.maps.aa.mapdata.MapDataWrapper;
import com.digian.maps.aa.mapdata.Route;
import com.digian.maps.aa.mapdata.Step;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by forrestal on 20/09/2015.
 */
public class RouteFinderRequestHelper {

    @VisibleForTesting RouteFinderRequestResult mRouteFinderRequestResult;
    @VisibleForTesting Context mContext;

    private static final String TAG = RouteFinderRequestHelper.class.getSimpleName();

    @CheckResult
    public static RouteFinderRequestHelper newInstance(@NonNull Context context, @NonNull RouteFinderRequestResult routeFinderRequestResult) {
        return new RouteFinderRequestHelper(context, routeFinderRequestResult);
    }

    private RouteFinderRequestHelper(@NonNull final Context context, @NonNull final RouteFinderRequestResult routeFinderRequestResult) {
        Log.v(TAG,"RouteFinderRequestHelper(RouteFinderRequestResult routeFinderRequestResult)");

        if (context == null) throw new IllegalStateException("context cannot be null");

        if (routeFinderRequestResult == null) throw new IllegalStateException("routeFinderRequestResult cannot be null");

        mContext = context;
        mRouteFinderRequestResult = routeFinderRequestResult;
    }

    public void getMapData(final String origin, final String destination) {
        Log.v(TAG,"getMapData(final String origin, final String destination)");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.GOOGLE_DIRECTIONS_API_REQUEST_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RouteFinderRequest routeFinderRequest = retrofit.create(RouteFinderRequest.class);

        Call<MapDataWrapper> call = routeFinderRequest.getMapData(origin,destination,Constants.UK_REGION,Constants.GOOGLE_MAPS_KEY);

        call.enqueue(new Callback<MapDataWrapper>() {
            @Override
            public void onResponse(Response<MapDataWrapper> response) {
                Log.v(TAG, "Successful Response");
                MapDataWrapper mapDataWrapper = response.body();
                processMapData(mapDataWrapper);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(TAG, "Failure Response");
                mRouteFinderRequestResult.onRequestFailure(t.getMessage());
            }
        });
    }

    private void processMapData(MapDataWrapper mapDataWrapper) {
        Log.v(TAG,"processMapData(MapDataWrapper mapDataWrapper)");

        //Check Status and if not Ok report back error
        if (!mapDataWrapper.getStatus().contentEquals(Constants.STATUS_OK)){
            mRouteFinderRequestResult.onRequestFailure(mapDataWrapper.getStatus());
            return;
        }

        List<Route> routes = mapDataWrapper.getRoutes();

        if (routes == null || routes.size() == 0) {
            mRouteFinderRequestResult.onRequestFailure(mContext.getString(R.string.no_routes_returned));
            return;
        }

        List<Leg> legs = routes.get(0).getLegs();

        if (legs == null || legs.size() == 0) {
            mRouteFinderRequestResult.onRequestFailure(mContext.getString(R.string.no_legs_returned));
            return;
        }

        PolylineOptions routePolylineOptions = new PolylineOptions();

        for (Leg leg : legs) {
            for (Step step : leg.getSteps()) {
                com.digian.maps.aa.mapdata.Polyline polyline = step.getPolyline();
                String points = polyline.getPoints();

                List<LatLng> latLngList =  decodePoly(points);

                if (latLngList != null && latLngList.size() > 0) {
                    for (LatLng latLng : latLngList) {
                        routePolylineOptions.add(latLng);
                    }
                }
            }
        }

        mRouteFinderRequestResult.onRequestSuccess(routePolylineOptions);
    }

    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}

