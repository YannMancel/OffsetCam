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
    val mCameraLensDirection: Int = CameraSelector.LENS_FACING_FRONT
) {

    // CLASSES -------------------------------------------------------------------------------------

    class SetupCamera(val isVisibleSwitchCamera: Boolean,
                      val cameraLensDirection: Int
    ) : CameraState(
        mIsVisibleSwitchCamera = isVisibleSwitchCamera,
        mCameraLensDirection = cameraLensDirection
    )

    class Error(val errorMessage: String,
                val isVisibleSwitchCamera: Boolean,
                val cameraLensDirection: Int
    ) : CameraState(
        mIsVisibleSwitchCamera = isVisibleSwitchCamera,
        mCameraLensDirection = cameraLensDirection
    )
}