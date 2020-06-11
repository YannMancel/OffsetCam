package com.mancel.yann.offsetcam.views.adapters

import androidx.recyclerview.widget.DiffUtil
import com.mancel.yann.offsetcam.models.Picture

/**
 * Created by Yann MANCEL on 11/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.adapters
 *
 * A [DiffUtil.Callback] subclass.
 */
class PictureDiffCallback(
    private val _oldList: List<Picture>,
    private val _newList: List<Picture>
) : DiffUtil.Callback() {

    // METHODS -------------------------------------------------------------------------------------

    // -- DiffUtil.Callback --

    override fun getOldListSize(): Int = this._oldList.size

    override fun getNewListSize(): Int = this._newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison based on absolutePath of its File:
        val oldName = this._oldList[oldItemPosition].file.absolutePath
        val newName = this._newList[newItemPosition].file.absolutePath

        return oldName == newName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Comparison on all fields
        return this._oldList[oldItemPosition] == this._newList[newItemPosition]
    }
}