package com.mancel.yann.offsetcam.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.viewModels
 *
 * A class which implements [ViewModelProvider.Factory].
 */
@Suppress("UNCHECKED_CAST")
class OffsetCamViewModelFactory(
    private val _cameraCount: Int
) : ViewModelProvider.Factory {

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModelProvider.Factory interface --

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OffsetCamViewModel::class.java))
            return OffsetCamViewModel(this._cameraCount) as T
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}