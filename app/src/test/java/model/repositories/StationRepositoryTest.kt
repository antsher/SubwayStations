package model.repositories

import com.nhaarman.mockitokotlin2.verify
import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.persistence.daos.StationDao
import com.stazis.subwaystations.model.repositories.RealStationRepository
import com.stazis.subwaystations.model.repositories.StationRepository
import com.stazis.subwaystations.model.services.StationService
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class StationRepositoryTest {

    @Mock
    lateinit var mockStationService: StationService

    @Mock
    lateinit var mockStationDao: StationDao

    @Mock
    lateinit var mockConnectionHelper: ConnectionHelper

    @Mock
    lateinit var mockStationsCall: Call<List<Station>>

    @Mock
    lateinit var mockStationResponse: Response<List<Station>>

    private lateinit var stationRepository: StationRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stationRepository = RealStationRepository(mockStationService, mockStationDao, mockConnectionHelper)
    }

    @Test
    fun testGetStations_isOnlineReceivedEmptyList_emitEmptyList() {
        val stations = emptyList<Station>()

        setUpMocks(stations, true)
        val testObserver = stationRepository.getStations().test()

        testObserver.assertNoErrors()
        testObserver.assertValue { stationsResult: List<Station> -> stationsResult.isEmpty() }
        verify(mockStationDao).insertAll(stations)
    }

    @Test
    fun testGetStations_isOnlineReceivedStationsWithOneItem_emitListWithOneStation() {
        val stations = listOf(Station("Уручча", 53.9453522, 27.687875))

        setUpMocks(stations, true)
        val testObserver = stationRepository.getStations().test()

        testObserver.assertNoErrors()
        testObserver.assertValue { stationsResult: List<Station> -> stationsResult.size == 1 }
        verify(mockStationDao).insertAll(stations)
    }

    private fun setUpMocks(stations: List<Station>, isOnline: Boolean) {
        `when`(mockConnectionHelper.isOnline()).thenReturn(isOnline)
        `when`(mockStationService.getStations()).thenReturn(mockStationsCall)
        `when`(mockStationsCall.execute()).thenReturn(mockStationResponse)
        `when`(mockStationResponse.body()).thenReturn(stations)
        `when`(mockStationDao.getAll()).thenReturn(stations)
    }
}