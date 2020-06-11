package com.mancel.yann.offsetcam.views.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.models.Picture
import com.mancel.yann.offsetcam.states.GalleryState
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.viewModels.GalleryViewModel
import com.mancel.yann.offsetcam.views.adapters.AdapterCallback
import com.mancel.yann.offsetcam.views.adapters.PictureAdapter
import kotlinx.android.synthetic.main.fragment_gallery.view.*

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * A [BaseFragment] subclass which implements [AdapterCallback].
 */
class GalleryFragment : BaseFragment(), AdapterCallback {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var _adapter: PictureAdapter
    private lateinit var _viewModel: GalleryViewModel

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_gallery

    override fun configureDesign(savedInstanceState: Bundle?) {
        this.configureRecyclerView()
        this.configureGalleryState(savedInstanceState)
    }

    // -- AdapterCallback --

    override fun onDataChanged() {
        // No data
        if (this._adapter.itemCount == 0) {
            MessageTools.showMessageWithSnackbar(
                this._rootView.fragment_gallery_CoordinatorLayout,
                this.getString(R.string.no_picture_into_gallery)
            )
        }
    }

    override fun pictureClicked(picture: Picture) {
        // Pictures
        val pictures = this._adapter.getPictures()

        // Current index
        val index = pictures.indexOf(picture)

        // By action (Safe Args)
        val action = GalleryFragmentDirections.actionFromGalleryFragmentToPictureSliderFragment(
            index,
            pictures.toTypedArray()
        )
        this.findNavController().navigate(action)
    }

    // -- RecyclerView --

    /**
     * Configures the RecyclerView
     */
    private fun configureRecyclerView() {
        // Adapter
        this._adapter = PictureAdapter(_callback = this@GalleryFragment)

        // RecyclerView
        with(this._rootView.fragment_gallery_RecyclerView) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                this@GalleryFragment.requireContext(),
                3
            )
            adapter = this@GalleryFragment._adapter
        }
    }

    // -- LiveData --

    /**
     * Configures the [GalleryState]
     */
    private fun configureGalleryState(savedInstanceState: Bundle?) {
        // ViewModel
        this._viewModel = ViewModelProvider(this@GalleryFragment).get(
                              GalleryViewModel::class.java
                          )

        // Gallery state
        this._viewModel.getGalleryState().observe(
            this.viewLifecycleOwner,
            Observer {
                this.updateUI(it)
            }
        )

        if (savedInstanceState == null)
            this._viewModel.loadPictures()
        else
            this._viewModel.pictureReady()
    }

    // -- UI --

    /**
     * Updates the UI according a [GalleryState]
     * @param state a [GalleryState]
     */
    private fun updateUI(state: GalleryState) {
        // Specific state
        when (state) {
            is GalleryState.PictureReady -> this.handleStatePictureReady(state)
        }
    }

    // -- State --

    /**
     * Handles the [GalleryState.PictureReady]
     */
    private fun handleStatePictureReady(state: GalleryState.PictureReady) {
        this._adapter.updateData(state._pictures)
    }
}