package com.digian.maps.aa.services;

import android.content.Intent;
import android.location.Location;
import android.os.Build;

import com.digian.maps.aa.BuildConfig;
import com.digian.maps.aa.Constants;
import com.digian.maps.aa.CustomRobolectricRunner;
import com.digian.maps.aa.OutlineShadow;
import com.digian.maps.aa.TestConstants;
import com.digian.maps.aa.network.RouteFinderRequestResult;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

/**
 * Created by forrestal on 20/09/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class RouteFinderServiceTest extends TestCase {

    RouteFinderServiceMock mClassUnderTest;
    Intent mStartServiceIntent;
    private static final String LOCATION_PROVIDER = "LOCATION_PROVIDER";
    private static final double LAT_AND_LON_VALUE = 5.0;
    private Location originLocation;

    static {
        ShadowLog.stream = System.out;
    }

    @Before
    public void setUp() {
        mStartServiceIntent = new Intent(RuntimeEnvironment.application, RouteFinderServiceMock.class);
        mStartServiceIntent.setAction(Constants.ROUTE_FINDER_DESTINATION_PLOTTER);
        originLocation = new Location(LOCATION_PROVIDER);
        originLocation.setLatitude(LAT_AND_LON_VALUE);
        originLocation.setLongitude(LAT_AND_LON_VALUE);

        mStartServiceIntent.putExtra(Constants.ORIGIN_LOCATION, originLocation);
        mStartServiceIntent.putExtra(Constants.DESTINATION_LOCATION, TestConstants.PICCADILLY_CIRCUS_POSTCODE);
    }

    @Test
    public void testOnHandleIntentHasRouteFinderDestinationPlotter_ThenRouteFinderRequestHelperCalled() throws Exception {

        mClassUnderTest = new RouteFinderServiceMock();
        mClassUnderTest.onCreate();
        mClassUnderTest.onHandleIntent(mStartServiceIntent);

        assertTrue("Origin Location should be the one passed in intent", mClassUnderTest.originLocation.equals(originLocation));
        assertTrue("Destination Location should be the one passed in intent, so Picadilly circus", mClassUnderTest.destinationLocation.contentEquals(TestConstants.PICCADILLY_CIRCUS_POSTCODE));
        assertTrue("sendRouteFinderRequest should have been called proving that intent with action Constants.ROUTE_FINDER_DESTINATION_PLOTTER handled correctly ", mClassUnderTest.routeFinderRequestCalled == true);
    }

    @Test
    public void testServiceImplementsRouteFinderRequestResult() throws Exception {

        mClassUnderTest = new RouteFinderServiceMock();

        assertTrue("class needs to implement RouteFinderRequestResult so success or failure can be broadcast back to presenter", mClassUnderTest instanceof RouteFinderRequestResult);
    }

    static class RouteFinderServiceMock extends RouteFinderService {

        public boolean routeFinderRequestCalled = false;
        public Location originLocation;
        public String destinationLocation;

        public RouteFinderServiceMock() {
            super();
        }

        @Override
        void sendRouteFinderRequest(final Location origin, final String requestString) {
            originLocation = origin;
            destinationLocation = requestString;
            routeFinderRequestCalled = true;
        }
    }
}