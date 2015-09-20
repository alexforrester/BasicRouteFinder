package com.digian.maps.aa.views;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by forrestal on 19/09/2015.
 */
public interface RouteFinderView {
    void displayRoute(PolylineOptions polylineOptions);
    void displayError(String errorMessage);
}
