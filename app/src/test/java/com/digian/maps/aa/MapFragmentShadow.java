package com.digian.maps.aa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

/**
 * Created by forrestal on 19/09/2015.
 */
@Implements(com.google.android.gms.maps.MapFragment.class)
public class MapFragmentShadow {

    @RealObject
    private com.google.android.gms.maps.MapFragment mapFragment;

    @Implementation
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new FrameLayout(inflater.getContext());
    }

}
