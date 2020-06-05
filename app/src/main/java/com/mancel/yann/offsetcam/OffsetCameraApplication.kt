package com.mancel.yann.offsetcam

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import timber.log.Timber

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam
 *
 * An [Application] subclass which implements [CameraXConfig.Provider].
 */
class OffsetCameraApplication : Application(), CameraXConfig.Provider {

    // METHODS -------------------------------------------------------------------------------------

    // -- Application --

    override fun onCreate() {
        super.onCreate()

        // Timber: Logger
        Timber.plant(Timber.DebugTree())
    }

    // -- CameraXConfig.Provider interface --

    /*
        If you want fine control over when CameraX gets initialized,
        you can implement the CameraXConfig.Provider interface in your Application class.
        Note that most apps do not require this level of control.

        See: https://developer.android.com/training/camerax/preview
     */
    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()
}