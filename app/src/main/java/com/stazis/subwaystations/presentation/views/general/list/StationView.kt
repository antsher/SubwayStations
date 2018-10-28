package com.stazis.subwaystations.presentation.views.general.list

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import kotlinx.android.synthetic.main.view_station.view.*

@SuppressLint("ViewConstructor")
class StationView(context: Context?, station: Station, stationDistance: Int, onClicked: () -> Unit) :
    RelativeLayout(context) {

    companion object {

        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val EXPANDED_KEY = "EXPANDED_KEY"
        private const val ANIMATION_DURATION = 300L
    }

    private var expanded = false
    private var animationInProgress = false
    private val dpRatio = resources.displayMetrics.density

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station, this, true)
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("%dm", stationDistance)
        expand.setOnClickListener { switchExpandedState() }
        setOnClickListener { onClicked() }
    }

    private fun switchExpandedState() {
        ifNotAnimationInProgress {
            if (expanded) {
                collapse()
            } else {
                expand()
            }
        }
    }

    private fun ifNotAnimationInProgress(f: () -> Unit) {
        if (!animationInProgress) {
            f()
        }
    }

    private fun expand() = hiddenView.animate()
        .alpha(1f)
        .translationY(0f)
        .setDuration(ANIMATION_DURATION)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                hiddenView.visibility = VISIBLE
                animationInProgress = true
                expanded = true
            }

            override fun onAnimationEnd(animation: Animator) {
                animationInProgress = false
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        .start()

    private fun collapse() = hiddenView.animate()
        .alpha(0f)
        .translationY(-50 * dpRatio)
        .setDuration(ANIMATION_DURATION)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                animationInProgress = true
                expanded = false
            }

            override fun onAnimationEnd(animation: Animator) {
                animationInProgress = false
                hiddenView.visibility = GONE
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        .start()

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        putBoolean(EXPANDED_KEY, expanded)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
            expanded = state.getBoolean(EXPANDED_KEY).apply {
                if (this) {
                    hiddenView.visibility = VISIBLE
                    hiddenView.alpha = 1f
                    hiddenView.translationY = 0f
                } else {
                    hiddenView.visibility = GONE
                    hiddenView.alpha = 0f
                    hiddenView.translationY = -50 * dpRatio
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}