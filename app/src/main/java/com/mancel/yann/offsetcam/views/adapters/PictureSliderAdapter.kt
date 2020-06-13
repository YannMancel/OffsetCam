package com.mancel.yann.offsetcam.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.models.Picture
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*

/**
 * Created by Yann MANCEL on 11/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.adapters
 *
 * A [PagerAdapter] subclass.
 */
class PictureSliderAdapter(
    context: Context,
    private val _pictures: List<Picture>
) : PagerAdapter() {

    // FIELDS --------------------------------------------------------------------------------------

    private val _inflater = LayoutInflater.from(context)

    // METHODS -------------------------------------------------------------------------------------

    // -- PagerAdapter --

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = this._pictures.size

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = this._inflater.inflate(R.layout.item_picture, container, false)
        val picture = this._pictures[position]

        this.configureUiOfEachItem(view, picture)

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    // -- UI --

    private fun configureUiOfEachItem(
        view: View,
        picture: Picture
    ) {
        // Picture
        Picasso.get()
            .load(picture.file)
            .placeholder(R.drawable.ic_photo_camera)
            .into(view.item_picture_image)
    }
}