package com.stazis.subwaystations.presentation.views.info

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.databinding.ActivityStationInfoBinding
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationInfoPresenter
import com.stazis.subwaystations.presentation.views.common.BaseMvpActivity
import dagger.android.AndroidInjection
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
    private lateinit var binding: ActivityStationInfoBinding
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    @ProvidePresenter
    fun providePresenter() = presenter.apply { name = intent.getParcelableExtra<Station>(STATION_KEY)!!.name }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityStationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            updateUIWithIntentData(
                intent.getParcelableExtra(CURRENT_LOCATION_KEY)!!,
                intent.getParcelableExtra(STATION_KEY)!!
            )
        }
        binding.description.onTextUpdated = this::onDescriptionUpdated
    }

    private fun updateUIWithIntentData(currentLocation: LatLng, station: Station) {
        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        binding.name.text = station.name
        binding.latitude.text = String.format("Latitude: %f", station.latitude)
        binding.longitude.text = String.format("Longitude: %f", station.longitude)
        binding.distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
        presenter.getDescription()
    }

    override fun updateUI(stationDescription: String) {
        binding.description.savedText = stationDescription
        binding.description.enable()
    }

    private fun onDescriptionUpdated() {
        firebaseAnalytics.logEvent("updated_station_description", null)
        presenter.updateStationDescription(binding.description.savedText)
    }
}