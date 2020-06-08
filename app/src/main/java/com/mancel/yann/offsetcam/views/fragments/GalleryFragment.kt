package com.mancel.yann.offsetcam.views.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.viewModels.GalleryViewModel

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class GalleryFragment : BaseFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var _viewModel: GalleryViewModel

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_gallery

    override fun configureDesign() {
        this.configureGalleryState()
    }

    // -- LiveData --

    /**
     * Configures the [GalleryState]
     */
    private fun configureGalleryState() {
        // ViewModel
        this._viewModel = ViewModelProvider(this@GalleryFragment).get(
                              GalleryViewModel::class.java
                          )

        // Gallery state
        this._viewModel.getGalleryState().observe(
            this.viewLifecycleOwner,
            Observer {
                /* Do something here */
            }
        )
    }
}