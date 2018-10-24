package com.stazis.subwaystations.presentation.presenters

import com.stazis.subwaystations.domain.interactors.GetStation
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.info.StationInfoRepresentation
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class StationInfoPresenterTest {

    @Mock
    lateinit var mockGetStation: GetStation

    @Mock
    lateinit var mockStationInfoRepresentation: StationInfoRepresentation

    private lateinit var stationInfoPresenter: StationInfoPresenter

    private lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        stationInfoPresenter = StationInfoPresenter(mockGetStation)
    }

    @Test
    fun testGetDetails_error() {
        val error = "Test error"
        val stationName = "Уручча"
        val single: Single<Station> = Single.create { emitter -> emitter.onError(Exception(error)) }

        `when`(mockGetStation.execute(stationName)).thenReturn(single)

        stationInfoPresenter.attachView(mockStationInfoRepresentation)
        stationInfoPresenter.getStation(stationName)

        testScheduler.triggerActions()

        verify(mockStationInfoRepresentation).showLoading()
        verify(mockStationInfoRepresentation).hideLoading()
        verify(mockStationInfoRepresentation).showError(error)
    }

    @Test
    fun testGetDetails_success() {
        val station = Station("Уручча", 53.9453522, 27.687875)
        val stationName = "Уручча"
        val single: Single<Station> = Single.create { emitter -> emitter.onSuccess(station) }

        `when`(mockGetStation.execute(stationName)).thenReturn(single)

        stationInfoPresenter.attachView(mockStationInfoRepresentation)
        stationInfoPresenter.getStation(stationName)

        testScheduler.triggerActions()

        verify(mockStationInfoRepresentation).showLoading()
        verify(mockStationInfoRepresentation).hideLoading()
        verify(mockStationInfoRepresentation).updateStationInfo(station)
    }
}