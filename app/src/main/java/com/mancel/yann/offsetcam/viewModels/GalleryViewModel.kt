package com.mancel.yann.offsetcam.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mancel.yann.offsetcam.OffsetCameraApplication
import com.mancel.yann.offsetcam.models.Picture
import com.mancel.yann.offsetcam.states.GalleryState
import com.mancel.yann.offsetcam.utils.FileTools

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
    private val _pictures = mutableListOf<Picture>()

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
    fun pictureReady() {
        this._galleryState?.value = GalleryState.PictureReady(
            _pictures = this._pictures
        )
    }

    // -- File --

    /**
     * loads the pictures
     */
    fun loadPictures() {
        val files = FileTools.getJpegFilesFromDir(OffsetCameraApplication.galleryDir)

        with(this._pictures) {
            clear()
            files.forEach { file ->
                this.add(Picture(file))
            }
        }

        this.pictureReady()
    }
}