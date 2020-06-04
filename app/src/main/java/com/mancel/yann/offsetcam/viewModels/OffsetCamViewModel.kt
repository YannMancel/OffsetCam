package com.mancel.yann.offsetcam.viewModels

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.states.CameraState

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.viewModels
 *
 * A [ViewModel] subclass.
 */
class OffsetCamViewModel : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

    private var _cameraState: MutableLiveData<CameraState>? = null
    private var _switchCameraVisible = true
    private var _cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the [LiveData] of [CameraState]
     * @return the [LiveData] of [CameraState]
     */
    fun getCameraState(): LiveData<CameraState> {
        if (this._cameraState == null) {
            this._cameraState = MutableLiveData<CameraState>()
        }
        return this._cameraState!!
    }

    /**
     * Manages the permission denied
     * @param context a [Context]
     */
    fun errorPermissionDenied(context: Context) {
        this._cameraState?.value = CameraState.Error(
            _errorMessage = context.getString(R.string.error_camera_state),
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }

    /**
     * Manages the setup of the camera
     */
    fun setupCamera() {
        this._cameraState?.value = CameraState.SetupCamera(
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }

    /**
     * Manages the preview ready
     */
    fun previewReady() {
        this._cameraState?.value = CameraState.PreviewReady(
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }
}