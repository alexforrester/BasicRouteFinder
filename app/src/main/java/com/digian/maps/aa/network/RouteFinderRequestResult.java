package com.digian.maps.aa.network;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by forrestal on 20/09/2015.
 */
public interface RouteFinderRequestResult {
    void onRequestSuccess(PolylineOptions polylineOptions);
    void onRequestFailure(String errorMessage);
}
