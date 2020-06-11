package com.mancel.yann.offsetcam.views.adapters

import com.mancel.yann.offsetcam.models.Picture

/**
 * Created by Yann MANCEL on 11/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.adapters
 */
interface AdapterCallback {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Called with the updateData method of adapter
     */
    fun onDataChanged()

    /**
     * Called when a view has been clicked.
     */
    fun pictureClicked(picture: Picture)
}