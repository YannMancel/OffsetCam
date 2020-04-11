package com.mancel.yann.offsetcam

import android.app.Application
import timber.log.Timber

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam
 *
 * An [Application] subclass.
 */
class OffsetCameraApplication : Application() {

    // METHODS -------------------------------------------------------------------------------------

    // -- Application --

    override fun onCreate() {
        super.onCreate()

        // Timber: Logger
        Timber.plant(Timber.DebugTree())
    }
}