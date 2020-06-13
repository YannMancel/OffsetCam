package com.mancel.yann.offsetcam.views.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.views.adapters.PictureSliderAdapter
import com.mancel.yann.offsetcam.views.dialogs.DeleteDialogFragment
import kotlinx.android.synthetic.main.fragment_slider_picture.view.*

/**
 * Created by Yann MANCEL on 11/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class PictureSliderFragment : BaseFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private val _currentItemIndex by lazy {
        PictureSliderFragmentArgs.fromBundle(this.requireArguments()).currentItemIndex
    }

    private val _pictures by lazy {
        PictureSliderFragmentArgs.fromBundle(this.requireArguments()).pictures.toMutableList()
    }

    private lateinit var _adapter: PictureSliderAdapter

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_slider_picture

    override fun configureDesign(savedInstanceState: Bundle?) {
        this.configureViewPager()
    }

    // -- Fragment --

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_toolbar_action_delete -> {
                this.handleDeleteAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // -- ViewPager --

    /**
     * Configure the ViewPager
     */
    private fun configureViewPager() {
        // Adapter
        this._adapter = PictureSliderAdapter(
            this.requireContext(),
            this._pictures
        )

        // ViewPager
        this._rootView.fragment_picture_ViewPager.adapter = this._adapter

        // Update UI
        this._adapter.notifyDataSetChanged()
        this._rootView.fragment_picture_ViewPager.currentItem = this._currentItemIndex
    }

    // -- Action --

    /**
     * Handle the delete icon into the toolbar
     */
    private fun handleDeleteAction() {
        DeleteDialogFragment {
            this.deleteCurrentPicture()
        }
        .show(this.parentFragmentManager, "Delete dialog")
    }

    // -- Picture --

    /**
     * Deletes the current picture
     */
    private fun deleteCurrentPicture() {
        // No picture
        if (this._pictures.isEmpty()) {
            return
        }

        // Index
        val currentIndex = this._rootView.fragment_picture_ViewPager.currentItem

        val nextIndex =
            if (currentIndex == (this._pictures.size - 1))
                currentIndex - 1
            else
                currentIndex

        val picture = this._pictures[currentIndex]

        // Delete data
        picture.file.delete()
        this._pictures.removeAt(currentIndex)

        // Update UI
        this._adapter.notifyDataSetChanged()
        this._rootView.fragment_picture_ViewPager.currentItem = nextIndex

        MessageTools.showMessageWithSnackbar(
            this._rootView.fragment_picture_CoordinatorLayout,
            this.getString(R.string.snackbar_delete_action)
        )
    }
}