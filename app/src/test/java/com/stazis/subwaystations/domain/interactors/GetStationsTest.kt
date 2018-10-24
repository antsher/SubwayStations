package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.repositories.StationRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetStationsTest {

    @Mock
    lateinit var mockStationRepository: StationRepository

    private lateinit var getStations: GetStations

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getStations = GetStations(mockStationRepository)
    }

    @Test
    fun testExecute_stationsWithOneItem_emitListWithOneStation() {
        val station = Station("Уручча", 53.9453522, 27.687875)
        val mockSingle = Single.create { e: SingleEmitter<List<Station>> -> e.onSuccess(listOf(station)) }

        `when`(mockStationRepository.getStations()).thenReturn(mockSingle)

        val resultSingle = getStations.execute()
        val testObserver = resultSingle.test()

        testObserver.assertNoErrors()
        testObserver.assertValue { stations: List<Station> -> stations.size == 1 }
        testObserver.assertValue { stations: List<Station> -> stations[0] == station }
    }

    @Test
    fun testExecute_stationsEmpty_emitEmptyStations() {
        val mockSingle = Single.create { e: SingleEmitter<List<Station>> -> e.onSuccess(emptyList()) }

        Mockito.`when`(mockStationRepository.getStations()).thenReturn(mockSingle)

        val resultSingle = getStations.execute()
        val testObserver = resultSingle.test()

        testObserver.assertNoErrors()
        testObserver.assertValue { stations: List<Station> -> stations.isEmpty() }
    }
}