package com.stazis.subwaystations.presentation.views.general.list

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.AnimatedStationPresenter
import kotlinx.android.synthetic.main.view_station.view.*

@SuppressLint("ViewConstructor")
class AnimatedStationWidget(context: Context?, station: Station, stationDistance: Int, onClicked: () -> Unit) :
    RelativeLayout(context), AnimatedStationView {

    companion object {

        private const val ANIMATION_DURATION = 300L
    }

    @InjectPresenter
    lateinit var presenter: AnimatedStationPresenter
    private lateinit var mvpDelegate: MvpDelegate<AnimatedStationWidget>
    private var expanded = false
    private var animationInProgress = false
    private val dpRatio = resources.displayMetrics.density

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station, this, true)
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("%dm", stationDistance)
        switchState.setOnClickListener { switch() }
        setOnClickListener { onClicked() }
    }

    fun initDelegate(parentDelegate: MvpDelegate<out Any>, name: String) {
        mvpDelegate = MvpDelegate(this)
        mvpDelegate.setParentDelegate(parentDelegate, name)
        mvpDelegate.onCreate()
        mvpDelegate.onAttach()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mvpDelegate.onSaveInstanceState()
        mvpDelegate.onDetach()
    }

    private fun switch() = ifNotAnimationInProgress {
        expanded = !expanded
        if (expanded) {
            animateExpand()
            presenter.makeExpanded()
        } else {
            animateCollapse()
            presenter.makeCollapsed()
        }
    }

    private inline fun ifNotAnimationInProgress(f: () -> Unit) {
        if (!animationInProgress) {
            f()
        }
    }

    private fun animateExpand() = hiddenView.animate()
        .alpha(1f)
        .translationY(0f)
        .setDuration(ANIMATION_DURATION)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                hiddenView.visibility = VISIBLE
                animationInProgress = true
            }

            override fun onAnimationEnd(animation: Animator) {
                animationInProgress = false
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        .start()

    override fun makeExpanded() {
        expanded = true
        if (!animationInProgress) {
            with(hiddenView) {
                hiddenView.alpha = 1f
                hiddenView.translationY = 0f
                hiddenView.visibility = VISIBLE
            }
        }
    }

    private fun animateCollapse() {
        if (hiddenView.visibility != View.GONE) {
            hiddenView.animate()
                .alpha(0f)
                .translationY(-50 * dpRatio)
                .setDuration(ANIMATION_DURATION)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        animationInProgress = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        animationInProgress = false
                        hiddenView.visibility = GONE
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()
        }
    }

    override fun makeCollapsed() {
        expanded = false
        if (!animationInProgress) {
            with(hiddenView) {
                hiddenView.alpha = 0f
                hiddenView.translationY = -50 * dpRatio
                hiddenView.visibility = GONE
            }
        }
    }
}