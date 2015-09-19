package com.digian.maps.aa.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
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
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(final Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
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

        MapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(this);

        mRouterFinderPresenter = RouterFinderPresenter.newInstance(this, this);
        bindViews();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    @OnClick(R.id.destination_button)
    void sendLocation() {

        if (mDestinationLocation == null || mDestinationLocation.getText() == null || mDestinationLocation.getText().length() == 0){
            displayError(getResources().getString(R.string.no_text_entered));
            return;
        }

        Location location = mMap.getMyLocation();
        mRouterFinderPresenter.getRouteLegs(location, mDestinationLocation.getText().toString());
    }

    @VisibleForTesting
    @NonNull
    MapFragment getMapFragment() {
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mapFragment);
        fragmentTransaction.commit();
        return mapFragment;
    }

    @Override
    public void displayRoute() {

    }

    @Override
    public void displayError(final String errorMessage) {
        Toast.makeText(this.getApplicationContext(),errorMessage,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy (){
        unBindViews();
        super.onDestroy();
    }

    @VisibleForTesting
    void bindViews(){
        ButterKnife.bind(this);
    }

    @VisibleForTesting
    void unBindViews(){
        ButterKnife.unbind(this);
    }

    private void clearOnMyLocationChangeListener() {
        mMap.setOnMyLocationChangeListener(null);
    }
}
