package com.digian.maps.aa.presenters;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.digian.maps.aa.views.RouteFinderView;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by forrestal on 19/09/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouterFinderPresenterTest extends TestCase {

    private RouterFinderPresenter mClassUnderTest;
    private Context mContext = mock(Context.class);
    private RouteFinderView mRouteFinderView = mock(RouteFinderView.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNewInstanceCreatedCorrectlyWithPresenterCreated() throws Exception {
        mClassUnderTest = RouterFinderPresenterSub.newInstance(mContext, mRouteFinderView);

        assertNotNull("mClassUnderTest should not be null after factory method run", mClassUnderTest);
    }

    @Test
    public void testNewInstanceThrowsException_WhenContextIsNull() {
        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouterFinderPresenterSub.newInstance(null, mRouteFinderView);
    }

    @Test
    public void testNewInstanceThrowsException_WhenRouteFinderViewIsNull() {
        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouterFinderPresenterSub.newInstance(mContext, null);
    }

    @Test
    public void testContextAndRouteFinderViewSetToCorrectObjects() {
        mClassUnderTest = RouterFinderPresenterSub.newInstance(mContext, mRouteFinderView);

        assertEquals("Context should be same object passed in", mContext, mClassUnderTest.mContext);
        assertEquals("RouteFinderView should be same object passed in", mRouteFinderView, mClassUnderTest.mRoutFinderView);
    }

    @Test
    public void testGetRouteLegsThrowsIllegalStateExceptionIfOriginLocationNotPassedIn() throws Exception {
        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouterFinderPresenterSub.newInstance(mContext, mRouteFinderView);
        mClassUnderTest.getRouteLegs(null, "a string");
    }

    @Test
    public void testGetRouteLegsThrowsIllegalStateExceptionIfDestinationLocationNotPassedIn() throws Exception {
        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouterFinderPresenterSub.newInstance(mContext, mRouteFinderView);

        Location location = mock(Location.class);
        mClassUnderTest.getRouteLegs(location, null);
    }

    private static class RouterFinderPresenterSub extends RouterFinderPresenter {

        public static RouterFinderPresenterSub newInstance(@NonNull final Context context, @NonNull final RouteFinderView routeFinderView) throws IllegalStateException {
            return new RouterFinderPresenterSub(context,routeFinderView);
        }

        private RouterFinderPresenterSub(@NonNull final Context context, @NonNull final RouteFinderView routeFinderView) {
            super(context, routeFinderView);
        }

        @Override
        void setUpMapDataStateReceiver() {
        }
    }


}