package com.mancel.yann.offsetcam.viewModels

import androidx.camera.core.CameraSelector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private var mCameraState: MutableLiveData<CameraState>? = null

    private var mIsSwitchCameraEnable = true
    private var mCameraLensDirection: Int = CameraSelector.LENS_FACING_FRONT

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the [LiveData] of [CameraState]
     * @return the [LiveData] of [CameraState]
     */
    fun getCameraState(): LiveData<CameraState> {
        if (this.mCameraState == null) {
            this.mCameraState = MutableLiveData<CameraState>()
        }
        return this.mCameraState!!
    }

    /**
     * Manages the permission denied
     */
    fun errorPermissionDenied() {
        this.mCameraState?.value = CameraState.Error(
            errorMessage = "Permission denied: Cannot take pictures!",
            isVisibleSwitchCamera = this.mIsSwitchCameraEnable,
            cameraLensDirection = this.mCameraLensDirection
        )
    }
}