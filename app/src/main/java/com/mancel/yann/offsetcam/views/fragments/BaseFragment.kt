package com.mancel.yann.offsetcam.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * An abstract [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    // FIELDS --------------------------------------------------------------------------------------

    protected lateinit var _rootView: View

    companion object {
        const val REQUEST_CODE_PERMISSION_CAMERA = 1000
        const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 2000
    }

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the fragment layout
     * @return an integer that corresponds to the fragment layout
     */
    @LayoutRes
    protected abstract fun getFragmentLayout(): Int

    /**
     * Configures the design of each daughter class
     * @param savedInstanceState a [Bundle] to check the configuration changes of [Fragment]
     */
    protected abstract fun configureDesign(savedInstanceState: Bundle?)

    // -- Fragment --

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this._rootView = inflater.inflate(this.getFragmentLayout(), container, false)

        this.configureDesign(savedInstanceState)

        return this._rootView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            // Access to the external storage or the camera of device
            REQUEST_CODE_PERMISSION_CAMERA,
            REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    this.actionAfterPermission()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // -- Permission --

    /**
     * Checks the permission: CAMERA
     */
    protected fun checkCameraPermission(): Boolean {
        val permissionResult = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.CAMERA
        )

        return when (permissionResult) {
            PackageManager.PERMISSION_GRANTED -> true

            else -> {
                this.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE_PERMISSION_CAMERA
                )

                false
            }
        }
    }

    /**
     * Checks the permission: WRITE_EXTERNAL_STORAGE
     */
    protected fun checkWriteExternalStoragePermission(): Boolean {
        val permissionResult = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        return when (permissionResult) {
            PackageManager.PERMISSION_GRANTED -> true

            else -> {
                this.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
                )

                false
            }
        }
    }

    /**
     * Method to override to perform action after the granted permission
     */
    protected open fun actionAfterPermission() {/* Do nothing here */}
}