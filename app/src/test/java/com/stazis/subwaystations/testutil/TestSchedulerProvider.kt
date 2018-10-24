package com.stazis.subwaystations.testutil

import com.stazis.subwaystations.utils.SchedulerProvider
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun uiScheduler() = testScheduler

    override fun ioScheduler() = testScheduler
}