package com.mancel.yann.offsetcam.states

import androidx.camera.core.CameraSelector

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.states
 */
sealed class CameraState(
    val mIsEnableButton: Boolean = false,
    val mIsVisibleSwitchCamera: Boolean = false,
    val mLensFacing: Int = CameraSelector.LENS_FACING_BACK
) {

    // CLASSES -------------------------------------------------------------------------------------

    class SetupCamera(val isVisibleSwitchCamera: Boolean,
                      val lensFacing: Int
    ) : CameraState(
        mIsVisibleSwitchCamera = isVisibleSwitchCamera,
        mLensFacing = lensFacing
    )

    class PreviewReady(val isVisibleSwitchCamera: Boolean,
                       val lensFacing: Int
    ) : CameraState(
        mIsEnableButton = true,
        mIsVisibleSwitchCamera = isVisibleSwitchCamera,
        mLensFacing = lensFacing
    )

    class Error(val errorMessage: String,
                val isVisibleSwitchCamera: Boolean,
                val lensFacing: Int
    ) : CameraState(
        mIsVisibleSwitchCamera = isVisibleSwitchCamera,
        mLensFacing = lensFacing
    )
}