package com.mancel.yann.offsetcam.states

import com.mancel.yann.offsetcam.models.Picture

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.states
 */
sealed class GalleryState {

    // CLASSES -------------------------------------------------------------------------------------

    class PictureReady(val _pictures: List<Picture>) : GalleryState()
}