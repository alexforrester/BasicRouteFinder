package com.digian.maps.aa.presenters;

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
public class MapDataStateReceiverTest extends TestCase {

    private RouterFinderPresenter.MapDataStateReceiver mClassUnderTest;
    private RouterFinderPresenter mRouteFinderPresenter = mock(RouterFinderPresenter.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNewInstanceSetUpCorrectlyWithRouterFinderPresenterSet() throws Exception {
        mClassUnderTest = RouterFinderPresenter.MapDataStateReceiver.newInstance(mRouteFinderPresenter);

        assertNotNull("mClassUnderTest should be instantiated correctly", mClassUnderTest);
        assertSame("instance created should be from passed in parameter", mClassUnderTest.mRouteFinderPresenter, mRouteFinderPresenter);
    }

    @Test
    public void testIllegalStateExceptionThrownIfRouteFinderPresenterNotPassedIn() throws Exception {
        thrown.expect(IllegalStateException.class);
        mClassUnderTest = RouterFinderPresenter.MapDataStateReceiver.newInstance(null);
    }
}