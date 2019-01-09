package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.repositories.StationRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetStationsTest {

    @Mock
    lateinit var mockStationRepository: StationRepository

    private val getStations by lazy { GetStations(mockStationRepository) }

    @Before
    fun setUp() = MockitoAnnotations.initMocks(this)

    @Test
    fun `getStations IfStationsEmpty ReturnsEmptyList`() {
        `when`(mockStationRepository.getStations())
            .thenReturn(Single.create<List<Station>> { it.onSuccess(emptyList()) })

        with(getStations.execute().test()) {
            assertNoErrors()
            assertValue { it.isEmpty() }
        }
    }

    @Test
    fun `getStations IfStationsContainsOne ReturnsOneStation`() {
        Station("Уручча", 53.9453522, 27.687875).let { station ->
            `when`(mockStationRepository.getStations())
                .thenReturn(Single.create<List<Station>> { it.onSuccess(listOf(station)) })

            with(getStations.execute().test()) {
                assertNoErrors()
                assertValue { it == listOf(station) }
            }
        }
    }

    @Test
    fun `getStations IfStationsContainsMultiple ReturnsMultipleStations`() {
        listOf(
            Station("Уручча1", 53.9453522, 27.687875),
            Station("Уручча2", 53.9453523, 27.687876),
            Station("Уручча3", 53.9453524, 27.687877),
            Station("Уручча4", 53.9453525, 27.687878)
        ).let { stations ->
            `when`(mockStationRepository.getStations())
                .thenReturn(Single.create<List<Station>> { it.onSuccess(stations) })

            with(getStations.execute().test()) {
                assertNoErrors()
                assertValue { it == stations }
            }
        }
    }
}