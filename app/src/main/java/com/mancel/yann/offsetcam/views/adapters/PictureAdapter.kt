package com.mancel.yann.offsetcam.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.models.Picture
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yann MANCEL on 11/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.adapters
 *
 * A [RecyclerView.Adapter] subclass.
 */
class PictureAdapter(
    private val _displayMode: DisplayMode = DisplayMode.GALLERY_MODE,
    private val _callback: AdapterCallback? = null
) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    // Note: With ViewPager2, the dimensions of root XML layout must be in match_parent.

    // ENUMS ---------------------------------------------------------------------------------------

    enum class DisplayMode { GALLERY_MODE, SLIDER_MODE }

    // FIELDS --------------------------------------------------------------------------------------

    private val _pictures = mutableListOf<Picture>()

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.Adapter --

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        // Display mode
        val layout = when (this._displayMode) {
            DisplayMode.GALLERY_MODE -> R.layout.item_picture
            DisplayMode.SLIDER_MODE -> R.layout.item_picture_slider
        }

        // Creates the View thanks to the inflater
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return PictureViewHolder(view, WeakReference(this._callback))
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(this._pictures[position])
    }

    override fun getItemCount(): Int = this._pictures.size

    // -- Picture --

    /**
     * Updates data of [PictureAdapter]
     * @param newPictures a [List] of [Picture]
     */
    fun updateData(newPictures: List<Picture>) {
        // Optimizes the performances of RecyclerView
        val diffCallback  = PictureDiffCallback(this._pictures, newPictures)
        val diffResult  = DiffUtil.calculateDiff(diffCallback )

        // New data
        with(this._pictures) {
            clear()
            addAll(newPictures)
        }

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this@PictureAdapter)

        // Callback
        this._callback?.onDataChanged()
    }

    /**
     * Gets all [Picture]
     */
    fun getPictures(): List<Picture> = this._pictures

    // NESTED CLASSES ------------------------------------------------------------------------------

    /**
     * A [RecyclerView.ViewHolder] subclass.
     */
    class PictureViewHolder(
        itemView: View,
        private var _weakRef: WeakReference<AdapterCallback?>
    ) : RecyclerView.ViewHolder(itemView) {

        // METHODS ---------------------------------------------------------------------------------

        /**
         * Binds the [PictureAdapter] et the [PictureViewHolder]
         */
        fun bind(picture: Picture) {
            // Listener of CardView
            this.itemView.item_picture_CardView.setOnClickListener {
                this._weakRef.get()?.pictureClicked(picture)
            }

            // Picture
            Picasso.get()
                   .load(picture.file)
                   .placeholder(R.drawable.ic_photo_camera)
                   .into(this.itemView.item_picture_image)
        }
    }
}