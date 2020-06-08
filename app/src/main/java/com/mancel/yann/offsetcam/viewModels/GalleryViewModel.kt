package com.mancel.yann.offsetcam.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mancel.yann.offsetcam.states.GalleryState

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.viewModels
 *
 * A [ViewModel] subclass.
 */
class GalleryViewModel : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

    private var _galleryState: MutableLiveData<GalleryState>? = null

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --
    /**
     * Gets the [LiveData] of [GalleryState]
     * @return the [LiveData] of [GalleryState]
     */
    fun getGalleryState(): LiveData<GalleryState> {
        if (this._galleryState == null) {
            this._galleryState = MutableLiveData<GalleryState>()
        }
        return this._galleryState!!
    }

    // -- GalleryState --

    /**
     * Manages the setup of the camera
     */
    fun setupCamera() {
        this._galleryState?.value = GalleryState.PictureReady(
            _pictures = listOf()
        )
    }
}