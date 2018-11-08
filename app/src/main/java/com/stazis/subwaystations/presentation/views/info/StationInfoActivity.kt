package com.stazis.subwaystations.presentation.views.info

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationInfoPresenter
import com.stazis.subwaystations.presentation.views.common.BaseMvpActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_station_info.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationInfoActivity : BaseMvpActivity<StationInfoPresenter>(), StationInfoView {

    companion object {

        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
        const val STATION_KEY = "STATION_KEY"
    }

    @Inject
    @InjectPresenter
    override lateinit var presenter: StationInfoPresenter
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    @ProvidePresenter
    fun providePresenter() = presenter.apply { name = intent.getParcelableExtra<Station>(STATION_KEY).name }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_station_info)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            updateUIWithIntentData(
                intent.getParcelableExtra(CURRENT_LOCATION_KEY),
                intent.getParcelableExtra(STATION_KEY)
            )
        }
        description.onTextUpdated = this::onDescriptionUpdated
    }

    private fun updateUIWithIntentData(currentLocation: LatLng, station: Station) {
        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
        presenter.getDescription()
    }

    override fun updateUI(stationDescription: String) {
        description.savedText = stationDescription
        description.enable()
    }

    private fun onDescriptionUpdated() {
        firebaseAnalytics.logEvent("updated_station_description", null)
        presenter.updateStationDescription(description.savedText)
    }
}