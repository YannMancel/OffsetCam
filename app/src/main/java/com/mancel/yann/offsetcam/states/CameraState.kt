package com.mancel.yann.offsetcam.states

import androidx.camera.core.CameraSelector

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.states
 */
sealed class CameraState(
    val _buttonEnable: Boolean = false,
    val _switchCameraVisible: Boolean = false,
    val _cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK
) {

    // CLASSES -------------------------------------------------------------------------------------

    class SetupCamera(
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )

    class PreviewReady(
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _buttonEnable = true,
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )

    class Error(
        val _errorMessage: String,
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )

    class Loading(
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _buttonEnable = false,
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )

    class PictureSaved(
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _buttonEnable = true,
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )

    class LensFacingSwitch(
        switchCameraVisible: Boolean,
        cameraLensFacing: Int
    ) : CameraState(
        _switchCameraVisible = switchCameraVisible,
        _cameraLensFacing = cameraLensFacing
    )
}