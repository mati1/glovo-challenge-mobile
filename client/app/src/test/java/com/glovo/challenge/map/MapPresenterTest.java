package com.glovo.challenge.map;

import com.glovo.challenge.location.LocationModel;
import com.glovo.challenge.rules.RxImmediateSchedulerRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MapPresenterTest {

    private MapPresenter mMapPresenter;

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock
    private MapContract.View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mMapPresenter = Mockito.spy(new MapPresenter(new LocationModel()));
        mMapPresenter.attachView(view);
    }

    @After
    public void tearDown() {
        mMapPresenter.detachView();
    }

    @Test
    public void nullCity() {
        mMapPresenter.loadCityDetail(null);

        Mockito.verify(view, Mockito.times(1)).showLoadingCityDetail();
        Mockito.verify(view, Mockito.times(1)).showManualLocation();
    }

}