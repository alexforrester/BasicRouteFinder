package com.digian.maps.aa.presenters;

import android.content.Context;

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

    private RouterFinderPresenter mRouterFinderPresenter;
    private Context mContext = mock(Context.class);
    private RouteFinderView mRouteFinderView = mock(RouteFinderView.class);

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testNewInstanceCreatedCorrectly() throws Exception {
        mRouterFinderPresenter = RouterFinderPresenter.newInstance(mContext,mRouteFinderView);

        assertNotNull("mRouterFinderPresenter should not be null after factory method run",mRouterFinderPresenter);
        assertTrue("mRouterFinderPresenter should instance of RouterFinderPresenter", mRouterFinderPresenter instanceof RouterFinderPresenter);
    }

    @Test
    public void testNewInstanceThrowsException_WhenContextIsNull(){
        thrown.expect(IllegalStateException.class);
        RouterFinderPresenter.newInstance(null, mRouteFinderView);
    }

    @Test
    public void testNewInstanceThrowsException_WhenRouteFinderViewIsNull() {
        thrown.expect(IllegalStateException.class);
        RouterFinderPresenter.newInstance(mContext,null);
    }

    @Test
    public void testContextAndRouteFinderViewSetToCorrectObjects() {
        mRouterFinderPresenter = RouterFinderPresenter.newInstance(mContext,mRouteFinderView);

        assertEquals("Context should be same object passed in", mContext, mRouterFinderPresenter.mContext);
        assertEquals("RouteFinderView should be same object passed in", mRouteFinderView, mRouterFinderPresenter.mRoutFinderView);
    }


}