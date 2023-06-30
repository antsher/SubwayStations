package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.repositories.StationRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

private const val STATION_NAME = "STATION_NAME"

class GetStationDescriptionTest {

    @Mock
    lateinit var mockStationRepository: StationRepository

    private val getStationDescription by lazy { GetStationDescription(mockStationRepository) }

    @Before
    fun setUp() = MockitoAnnotations.initMocks(this)

    @Test
    fun `getStationDescription IfEmptyString ReturnsEmptyString`() {
        "".let { emptyString ->
            `when`(mockStationRepository.getStationDescription(STATION_NAME))
                .thenReturn(Single.create { it.onSuccess(emptyString) })

            with(getStationDescription(STATION_NAME).test()) {
                assertNoErrors()
                assertValue { it == emptyString }
            }
        }
    }

    @Test
    fun `getStationDescription IfNonEmptyString ReturnsSameString`() {
        "Description".let { nonEmptyString ->
            `when`(mockStationRepository.getStationDescription(STATION_NAME))
                .thenReturn(Single.create { it.onSuccess(nonEmptyString) })

            with(getStationDescription(STATION_NAME).test()) {
                assertNoErrors()
                assertValue { it == nonEmptyString }
            }
        }
    }
}