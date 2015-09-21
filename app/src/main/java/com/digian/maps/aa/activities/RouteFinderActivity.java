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
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digian.maps.aa.Constants;
import com.digian.maps.aa.R;
import com.digian.maps.aa.presenters.RouterFinderPresenter;
import com.digian.maps.aa.views.RouteFinderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
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

    private ProgressBar mProgressBar;
    private GoogleMap mMap;
    private RouterFinderPresenter mRouterFinderPresenter;
    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;
    private String mDestination;
    private Marker mOriginMarker;
    private Marker mDestinationMarker;
    private PolylineOptions mPolylineOptions;
    private Polyline mPolylineAdded;
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

        if (mDestinationLatLng != null)
            showDrawnRouteFinderMap(mPolylineOptions);
        else
            mMap.setOnMyLocationChangeListener(originMyLocationChangeListener);
    }

    @VisibleForTesting
    @OnClick(R.id.destination_button)
    void sendLocation() {
        Log.v(TAG, "sendLocation()");

        if (mPolylineAdded != null) mPolylineAdded.remove();
        if (mOriginMarker != null) mOriginMarker.remove();
        if (mDestinationMarker != null) mDestinationMarker.remove();

        if (mDestinationLocation == null || mDestinationLocation.getText() == null || mDestinationLocation.getText().length() == 0){
            displayError(getResources().getString(R.string.no_text_entered));
            return;
        }

        mDestination =  mDestinationLocation.getText().toString();
        Location originlocation = mMap.getMyLocation();
        mRouterFinderPresenter.getRouteLegs(originlocation, mDestination);

        hideKeyboard();
        showProgressBar();
    }

    private void showProgressBar() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);

        if (mProgressBar == null) {
            mProgressBar = new ProgressBar(this.getApplicationContext(), null, android.R.attr.progressBarStyleSmall);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 80, Gravity.CENTER_HORIZONTAL|Gravity.TOP);
            mProgressBar.setLayoutParams(params);
            mProgressBar.setIndeterminate(true);
            mProgressBar.setVisibility(View.VISIBLE);
            frameLayout.addView(mProgressBar);
        }
        else if (mProgressBar.getVisibility() == View.INVISIBLE)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    private void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
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
    public void displayRoute(@NonNull final PolylineOptions polylineOptions) {
        Log.v(TAG, "displayRoute(PolylineOptions polylineOptions)");

        polylineOptions.width(6).color(Color.BLUE).geodesic(true);
        mPolylineOptions = polylineOptions;
        List<LatLng> latLngPoints = polylineOptions.getPoints();
        mDestinationLatLng = latLngPoints.get(latLngPoints.size() - 1);

        showDrawnRouteFinderMap(mPolylineOptions);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOriginLatLng = savedInstanceState.getParcelable(Constants.ORIGIN_LAT_LNG);
        mDestinationLatLng = savedInstanceState.getParcelable(Constants.DESTINATION_LAT_LNG);
        mPolylineOptions = savedInstanceState.getParcelable(Constants.DRAWN_POLYLINE_OPTIONS);
        mDestination = savedInstanceState.getString(Constants.DESTINATION_TITLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.ORIGIN_LAT_LNG, mOriginLatLng);
        outState.putParcelable(Constants.DESTINATION_LAT_LNG, mDestinationLatLng);
        outState.putParcelable(Constants.DRAWN_POLYLINE_OPTIONS,mPolylineOptions);
        outState.putString(Constants.DESTINATION_TITLE, mDestination);
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

    private void showDrawnRouteFinderMap(final PolylineOptions polylineOptions) {
        hideProgressBar();
        List<LatLng> mapBounds = getCorrectRectangularMapBoundCoordinates(mOriginLatLng, mDestinationLatLng);
        addDestinationMarker();
        addOriginMarker();
        addPolyline(polylineOptions);
        LatLngBounds routeMapBounds = new LatLngBounds(mapBounds.get(0), mapBounds.get(1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(routeMapBounds, 60));
    }

    private List<LatLng> getCorrectRectangularMapBoundCoordinates(LatLng firstPoint, LatLng secondPoint) {

        double southWestLatitude;
        double southWestLongitude;
        double northEastLatitude;
        double northEastLongitude;

        if (firstPoint.latitude < secondPoint.latitude) {
            southWestLatitude = firstPoint.latitude;
            northEastLatitude = secondPoint.latitude;
        }
        else {
            southWestLatitude = secondPoint.latitude;
            northEastLatitude = firstPoint.latitude;
        }

        if (firstPoint.longitude > secondPoint.longitude) {
            northEastLongitude = firstPoint.longitude;
            southWestLongitude = secondPoint.longitude;
        }
        else {
            northEastLongitude = secondPoint.longitude;
            southWestLongitude = firstPoint.longitude;
        }

        LatLng southWest = new LatLng(southWestLatitude,southWestLongitude);
        LatLng northEast = new LatLng(northEastLatitude,northEastLongitude);

        return Arrays.asList(new LatLng[]{southWest, northEast});
    }

    private void clearOnMyLocationChangeListener() {
        Log.v(TAG, "clearOnMyLocationChangeListener()");
        mMap.setOnMyLocationChangeListener(null);
    }

    private void addPolyline(final PolylineOptions polylineOptions) {
        mPolylineAdded = mMap.addPolyline(polylineOptions);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void addDestinationMarker() {
        mDestinationMarker = mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title(mDestination));
    }

    private void addOriginMarker() {
        mOriginMarker = mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title(getString(R.string.route_start)));
    }
}