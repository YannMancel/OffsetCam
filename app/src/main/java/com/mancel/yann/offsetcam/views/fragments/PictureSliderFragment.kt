package com.mancel.yann.offsetcam.views.fragments

import android.os.Bundle
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.views.adapters.PictureSliderAdapter
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
        PictureSliderFragmentArgs.fromBundle(this.requireArguments()).pictures.toList()
    }

    private lateinit var _adapter: PictureSliderAdapter

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_slider_picture

    override fun configureDesign(savedInstanceState: Bundle?) {
        this.configureViewPager()
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
}