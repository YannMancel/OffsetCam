package com.mancel.yann.offsetcam.views.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.states.CameraState
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.viewModels.OffsetCamViewModel
import kotlinx.android.synthetic.main.fragment_camera.view.*

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class CameraFragment : BaseFragment() {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mViewModel: OffsetCamViewModel

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_camera

    override fun configureDesign() {
        // LiveData
        this.configureCameraStateLiveData()
    }

    override fun showMessage(message: String) {
        MessageTools.showMessageWithSnackbar(
            this.mRootView.fragment_camera_CoordinatorLayout,
            message
        )
    }

    override fun actionAfterPermission() = this.bindCameraUseCase()

    // -- Fragment --

    override fun onResume() {
        super.onResume()
        this.bindCameraUseCase()
    }

    // -- LiveData --

    /**
     * Configures the [LiveData] of [CameraState]
     */
    private fun configureCameraStateLiveData() {
        this.mViewModel = ViewModelProvider(this@CameraFragment).get(
                              OffsetCamViewModel::class.java
                          )

        this.mViewModel.getCameraState().observe(
            this.viewLifecycleOwner,
            Observer {
                this.updateUI(it)
            }
        )
    }

    // -- UI --

    /**
     * Updates the UI
     * @param state a [CameraState]
     */
    private fun updateUI(state: CameraState) {

    }

    // -- Camera --

    /**
     * Binds the camera to the use case
     */
    private fun bindCameraUseCase() {
        if (this.checkCameraPermission()
            && this.checkWriteExternalStoragePermission()) {
            // todo: 11/04/2020 - Add action
        }
    }
}