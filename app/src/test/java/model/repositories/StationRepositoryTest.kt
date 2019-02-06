package model.repositories

import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.helpers.PreferencesHelper
import com.stazis.subwaystations.model.entities.DetailedStation
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.persistence.daos.DetailedStationDao
import com.stazis.subwaystations.model.repositories.RealStationRepository
import com.stazis.subwaystations.model.repositories.StationRepository
import com.stazis.subwaystations.model.services.StationService
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class StationRepositoryTest {

    @Mock
    lateinit var mockStationService: StationService

    @Mock
    lateinit var mockDetailedStationDao: DetailedStationDao

    @Mock
    lateinit var mockConnectionHelper: ConnectionHelper

    @Mock
    lateinit var mockPreferencesHelper: PreferencesHelper

    @Mock
    lateinit var mockStationsSingle: Single<List<Station>>

    private lateinit var stationRepository: StationRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stationRepository = RealStationRepository(
            mockStationService,
            mockDetailedStationDao,
            mockConnectionHelper,
            mockPreferencesHelper
        )
    }

    @Test
    fun testGetStations_isOnlineReceivedEmptyList_emitEmptyList() {
        emptyList<DetailedStation>().let { stations ->
            setUpMocks(stations, true)
            with(stationRepository.getStations().test()) {
                assertNoErrors()
                assertValue { it.isEmpty() }
            }
            verify(mockDetailedStationDao).insertAll(stations)
        }
    }

    @Test
    fun testGetStations_isOnlineReceivedStationsWithOneItem_emitListWithOneStation() {
        listOf(DetailedStation("Уручча", 53.9453522, 27.687875, "")).let { stations ->
            setUpMocks(stations, true)
            with(stationRepository.getStations().test()) {
                assertNoErrors()
                assertValue { it == stations }
            }
            verify(mockDetailedStationDao).insertAll(stations)
        }
    }

    private fun setUpMocks(stations: List<DetailedStation>, isOnline: Boolean) {
        `when`(mockConnectionHelper.isOnline()).thenReturn(isOnline)
        mockStationsSingle =
                Single.create { stations.map { station -> Station(station.name, station.latitude, station.longitude) } }
        `when`(mockStationService.getStations()).thenReturn(mockStationsSingle)
        `when`(stationRepository.getStations()).thenReturn(mockStationsSingle)
        `when`(mockDetailedStationDao.getAll()).thenReturn(stations.map { Station(it.name, it.latitude, it.longitude) })
    }
}