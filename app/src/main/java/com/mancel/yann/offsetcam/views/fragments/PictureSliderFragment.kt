package com.mancel.yann.offsetcam.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.FileProvider
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.animations.DepthPageTransformer
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.views.adapters.PictureAdapter
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

    /*
        Important: (To display a View)
            Old version: ViewPager  + PagerAdapter
            New version: ViewPager2 + RecyclerView.Adapter

        See:
            [1] https://developer.android.com/training/animation/vp2-migration
     */

    // FIELDS --------------------------------------------------------------------------------------

    private val _currentItemIndex by lazy {
        PictureSliderFragmentArgs.fromBundle(this.requireArguments()).currentItemIndex
    }

    private val _pictures by lazy {
        PictureSliderFragmentArgs.fromBundle(this.requireArguments()).pictures.toMutableList()
    }

//    private lateinit var _adapter: PictureSliderAdapter
    private lateinit var _adapter: PictureAdapter

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
            R.id.menu_toolbar_action_share -> {
                this.handleShareAction()
                true
            }
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

        /*
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
        */

        // Adapter
        this._adapter = PictureAdapter(_displayMode = PictureAdapter.DisplayMode.SLIDER_MODE)

        // ViewPager2
        this._rootView.fragment_picture_ViewPager2.adapter = this._adapter

        // Update UI
        this._adapter.updateData(this._pictures)
        this._rootView.fragment_picture_ViewPager2.currentItem = this._currentItemIndex

        // Animation
        this._rootView.fragment_picture_ViewPager2.setPageTransformer(DepthPageTransformer())
    }

    // -- Action --

    /**
     * Handle the delete icon into the toolbar
     */
    private fun handleDeleteAction() {
        DeleteDialogFragment { this.deleteCurrentPicture() }
            .show(this.parentFragmentManager, "Delete dialog")
    }

    /**
     * Handle the share icon into the toolbar
     */
    private fun handleShareAction() {
        // Picture
        val currentIndex = this._rootView.fragment_picture_ViewPager2.currentItem
        val currentPicture = this._pictures[currentIndex]

        // Uri
        val pictureUri = FileProvider.getUriForFile(
            this.requireContext(),
            this.getString(R.string.file_provider_authorities),
            currentPicture.file
        )

        // Intent
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, pictureUri)
            type = "image/*"
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        this.startActivity(
            Intent.createChooser(
                intent,
                this.getString(R.string.title_share_dialog)
            )
        )
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
        val currentIndex = this._rootView.fragment_picture_ViewPager2.currentItem

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
        this._adapter.updateData(this._pictures)
        this._rootView.fragment_picture_ViewPager2.currentItem = nextIndex

        MessageTools.showMessageWithSnackbar(
            this._rootView.fragment_picture_CoordinatorLayout,
            this.getString(R.string.snackbar_delete_action)
        )
    }
}