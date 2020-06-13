package com.mancel.yann.offsetcam.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mancel.yann.offsetcam.R

/**
 * Created by Yann MANCEL on 13/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.dialogs
 *
 * A [DialogFragment] subclass.
 */
class DeleteDialogFragment(
    private val _onClickPositiveButton: () -> Unit
) : DialogFragment() {

    // METHODS -------------------------------------------------------------------------------------

    // -- DialogFragment --

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(R.string.title_delete_dialog)
            .setPositiveButton(R.string.positive_button_delete_dialog) { _,_ ->
                this._onClickPositiveButton()
            }
            .setNegativeButton(android.R.string.no) { _,_ ->
                /* Do nothing here */
            }
            .create()
    }
}