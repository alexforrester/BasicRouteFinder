package com.digian.maps.aa.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digian.maps.aa.R;
import com.digian.maps.aa.presenters.RouterFinderPresenter;
import com.digian.maps.aa.views.RouteFinderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity which displays a search box and button in a fragment and a map fragment from which to plot route from origin
 * current location to destination location entered into search box
 */
public class RouteFinderActivity extends Activity implements OnMapReadyCallback, RouteFinderView {

    private static final String TAG = RouteFinderActivity.class.getSimpleName();

    private GoogleMap mMap;
    private RouterFinderPresenter mRouterFinderPresenter;
    private LatLng mOriginLatLng;
    private String mDestination;
    private com.google.android.gms.maps.model.Polyline mPolylineAdded;
    private GoogleMap.OnMyLocationChangeListener originMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(final Location location) {
            LatLng loc = mOriginLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                clearOnMyLocationChangeListener();
            }
        }
    };

    @Bind(R.id.destination_location) EditText mDestinationLocation;
    @Bind(R.id.destination_button) Button mDestinationButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        setContentView(R.layout.activity_route_finder);

        MapFragment mapFragment = buildMapFragment();
        mapFragment.getMapAsync(this);

        mRouterFinderPresenter = buildRouterFinderPresenter();
        bindViews();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.v(TAG, "onMapReady(GoogleMap map)");
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(originMyLocationChangeListener);
    }

    @VisibleForTesting
    @OnClick(R.id.destination_button)
    void sendLocation() {
        Log.v(TAG, "sendLocation()");

        if (mPolylineAdded != null) mPolylineAdded.remove();

        if (mDestinationLocation == null || mDestinationLocation.getText() == null || mDestinationLocation.getText().length() == 0){
            displayError(getResources().getString(R.string.no_text_entered));
            return;
        }

        mDestination =  mDestinationLocation.getText().toString();
        Location originlocation = mMap.getMyLocation();
        mRouterFinderPresenter.getRouteLegs(originlocation, mDestination);

        hideKeyboard();
    }

    @VisibleForTesting
    @NonNull
    MapFragment buildMapFragment() {
        Log.v(TAG, "buildMapFragment()");
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mapFragment);
        fragmentTransaction.commit();
        return mapFragment;
    }

    @VisibleForTesting
    @NonNull
    RouterFinderPresenter buildRouterFinderPresenter() {
        Log.v(TAG, "buildRouterFinderPresenter()");
        return RouterFinderPresenter.newInstance(this, this);
    }

    @Override
    public void displayRoute(PolylineOptions polylineOptions) {
        Log.v(TAG, "displayRoute(PolylineOptions polylineOptions)");

        polylineOptions.width(6).color(Color.BLUE).geodesic(true);
        mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title(getString(R.string.route_start)));
        mPolylineAdded = mMap.addPolyline(polylineOptions);
        List<LatLng> latLngPoints = polylineOptions.getPoints();
        LatLng lastPoint = latLngPoints.get(latLngPoints.size() - 1);

        Log.d(TAG,"Last Point Lat: "+lastPoint.latitude);
        Log.d(TAG,"Last Point Lng: "+lastPoint.longitude);
        mMap.addMarker(new MarkerOptions().position(lastPoint).title(mDestination));

        //LatLngBounds routeMapBounds = new LatLngBounds(mOriginLatLng, lastPoint);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(routeMapBounds, 0));
    }

    @Override
    public void displayError(final String errorMessage) {
        Log.v(TAG, "displayError(final String errorMessage)");
        Toast.makeText(this.getApplicationContext(),errorMessage,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy()");
        unBindViews();
        clearMembers();
        super.onDestroy();
    }

    @VisibleForTesting
    void bindViews(){
        Log.v(TAG, "bindViews()");
        ButterKnife.bind(this);
    }

    @VisibleForTesting
    void unBindViews(){
        Log.v(TAG, "unBindViews()");
        ButterKnife.unbind(this);
    }

    @VisibleForTesting
    void clearMembers() {
        Log.v(TAG, "clearMembers()");
        mRouterFinderPresenter = null;
        mMap = null;
        mOriginLatLng = null;
    }

    private void clearOnMyLocationChangeListener() {
        Log.v(TAG, "clearOnMyLocationChangeListener()");
        mMap.setOnMyLocationChangeListener(null);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
