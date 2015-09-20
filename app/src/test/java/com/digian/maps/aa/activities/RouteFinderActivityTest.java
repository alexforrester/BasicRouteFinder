package com.digian.maps.aa.activities;

import android.content.res.Resources;

import com.digian.maps.aa.presenters.RouterFinderPresenter;
import com.digian.maps.aa.views.RouteFinderView;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by forrestal on 19/09/2015.
 */
public class RouteFinderActivityTest extends TestCase {

    RouteFinderActivity mClassUnderTest;
    MapFragment mMapFragment = mock(MapFragment.class);
    Resources mResources =  mock(Resources.class);
    RouterFinderPresenter mRouterFinderPresenter  = mock(RouterFinderPresenter.class);

    @Before
    public void setUp() {
        mClassUnderTest = spy(new RouteFinderActivity());
        doReturn(mMapFragment).when(mClassUnderTest).buildMapFragment();
        doReturn(mRouterFinderPresenter).when(mClassUnderTest).buildRouterFinderPresenter();
        doNothing().when(mClassUnderTest).bindViews();
        doReturn(mResources).when(mClassUnderTest).getResources();
        when(mResources.getString(anyInt())).thenReturn("Error Message");
        doNothing().when(mClassUnderTest).displayError(anyString());
    }

    @Test
    public void testActivitySetUpCorrectlyWithMapFragmentAdded() {
        mClassUnderTest.onCreate(null);
        verify(mClassUnderTest, times(1)).buildMapFragment();
    }

    @Test
    public void testOnCreateBindsButterKnifeViews() {
        mClassUnderTest.onCreate(null);
        verify(mClassUnderTest, times(1)).bindViews();
    }

    @Test
    public void testOnCreateBuildsPresenter() {
        mClassUnderTest.onCreate(null);
        verify(mClassUnderTest, times(1)).buildRouterFinderPresenter();
    }

    @Test
    public void testActivityImplementsOnMapReadyCallback() {
        assertTrue("Activity should implement OnMapReadyCallback", mClassUnderTest instanceof OnMapReadyCallback);
    }

    @Test
    public void testActivityImplementsRouterFinderView() {
        assertTrue("Activity should implement RouterFinderView", mClassUnderTest instanceof RouteFinderView);
    }

    @Test
    public void testOnDestroyUnbindsButterKnifeViews() {
        mClassUnderTest.onDestroy();
        verify(mClassUnderTest, times(1)).unBindViews();
        verify(mClassUnderTest, times(1)).clearMembers();
    }

    @After
    public void tearDown(){
        mClassUnderTest = null;
    }

}