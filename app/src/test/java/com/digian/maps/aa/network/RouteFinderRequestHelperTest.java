package com.digian.maps.aa.network;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by forrestal on 20/09/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteFinderRequestHelperTest extends TestCase {

    RouteFinderRequestHelper mClassUnderTest;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public void testNewInstanceSetUpWithCorrectData() throws Exception {

        RouteFinderRequestResult routeFinderRequestResult = mock(RouteFinderRequestResult.class);
        Context context = mock(Context.class);

        mClassUnderTest = RouteFinderRequestHelper.newInstance(context, routeFinderRequestResult);

        assertNotNull("Context should be instantiated", mClassUnderTest.mContext);
        assertNotNull("RouteFinderRequestResult should be instantiated", mClassUnderTest.mRouteFinderRequestResult);
        assertEquals("Passed in RouteFinderRequestResult should be set as member", routeFinderRequestResult, mClassUnderTest.mRouteFinderRequestResult);
        assertEquals("Passed in Context should be set as member", context, mClassUnderTest.mContext);
    }

    @Test
    public void testIllegalStateExceptionThrownIfRouteFinderRequestResultNotPassedIn() throws Exception {
        Context context = mock(Context.class);

        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouteFinderRequestHelper.newInstance(context, null);
    }

    @Test
    public void testIllegalStateExceptionThrownIfContextNotPassedIn() throws Exception {
        RouteFinderRequestResult routeFinderRequestResult = mock(RouteFinderRequestResult.class);

        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouteFinderRequestHelper.newInstance(null, routeFinderRequestResult);
    }


}