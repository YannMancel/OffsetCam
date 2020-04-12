package com.mancel.yann.offsetcam.views.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.common.util.concurrent.ListenableFuture
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.states.CameraState
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.viewModels.OffsetCamViewModel
import kotlinx.android.synthetic.main.fragment_camera.view.*
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.views.fragments
 *
 * A [BaseFragment] subclass.
 */
class CameraFragment : BaseFragment() {

    // See: https://github.com/android/camera-samples/blob/master/CameraXBasic

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mViewModel: OffsetCamViewModel
    private lateinit var mCameraState: CameraState

    private lateinit var mCameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var mCamera: Camera? = null
    private var mPreview: Preview? = null
    private var mImageCapture: ImageCapture? = null
    private lateinit var mCameraExecutor: ExecutorService

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_camera

    override fun configureDesign() {
        // Listener
        this.configureListeners()

        // LiveData
        this.configureCameraStateLiveData()
    }

    override fun actionAfterPermission() = this.bindCameraUseCases()

    // -- Fragment --

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        this.mCameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        this.mRootView.fragment_camera_PreviewView.post {
            this.mViewModel.setupCamera()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Shut down our background executor
        this.mCameraExecutor.shutdown()
    }

    // -- Listeners --

    /**
     * Configures the listeners
     */
    private fun configureListeners() {
        // FAB
        this.mRootView.fragment_camera_FAB.setOnClickListener {
            this.takePicture()
        }
    }

    // -- LiveData --

    /**
     * Configures the [CameraState]
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
        // Keep the current state
        this.mCameraState = state

        // Specific state
        when (state) {
            is CameraState.SetupCamera -> this.handleStateSetupCamera()
            is CameraState.PreviewReady -> this.handleStatePreviewReady()
            is CameraState.Error -> this.handleStateError(state)
        }

        // General state
        with(state) {
            // FAB
            this@CameraFragment.mRootView.fragment_camera_FAB.isEnabled = this.mIsEnableButton
        }
    }

    // -- State --

    /**
     * Handles the [CameraState.SetupCamera]
     */
    private fun handleStateSetupCamera() = this.bindCameraUseCases()

    /**
     * Handles the [CameraState.PreviewReady]
     */
    private fun handleStatePreviewReady() {}

    /**
     * Handles the [CameraState.Error]
     * @param state a [CameraState.Error]
     */
    private fun handleStateError(state: CameraState.Error) {
        MessageTools.showMessageWithSnackbar(
            this.mRootView.fragment_camera_CoordinatorLayout,
            state.errorMessage
        )
    }

    // -- Camera --

    /**
     * Binds the camera to all use cases
     */
    private fun bindCameraUseCases() {
        if (
            this.checkCameraPermission()
            && this.checkWriteExternalStoragePermission()
        ) {
            this.mCameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
                .apply {
                    this.addListener(
                        Runnable {
                            val cameraProvider = this.get().apply {
                                // Must unbind the use-cases before rebinding them
                                this.unbindAll()
                            }

                            this@CameraFragment.bindPreviewAndImageCapture(cameraProvider)
                        },
                        ContextCompat.getMainExecutor(this@CameraFragment.requireContext())
                    )
                }

            this.mViewModel.previewReady()
        }
        else {
            this.mViewModel.errorPermissionDenied()
        }
    }

    /**
     * Binds the [Preview] and [ImageCapture]
     */
    private fun bindPreviewAndImageCapture(cameraProvider: ProcessCameraProvider) {
        // Metrics
        val metrics = DisplayMetrics().also {
            this.mRootView.fragment_camera_PreviewView.display.getRealMetrics(it)
        }

        // Ratio
        val screenAspectRatio = this.getAspectRatio(metrics.widthPixels, metrics.heightPixels)

        // Resolution
        //val resolution = Size(metrics.widthPixels, metrics.heightPixels)

        // Rotation
        val rotation = this.mRootView.fragment_camera_PreviewView.display.rotation

        // CameraSelector
        val cameraSelector = CameraSelector.Builder()
                                           .requireLensFacing(this.mCameraState.mLensFacing)
                                           .build()

        // Use case: Preview
        this.mPreview = Preview.Builder()
                               .setTargetAspectRatio(screenAspectRatio)
                               .setTargetRotation(rotation)
                               .build()

        // Use case: Image Capture
        this.mImageCapture = ImageCapture.Builder()
                                         .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                         .setTargetAspectRatio(screenAspectRatio)
                                         .setTargetRotation(rotation)
                                         .build()

        try {
            // Camera connect to the Fragment's Lifecycle
            this.mCamera = cameraProvider.bindToLifecycle(
                this.viewLifecycleOwner,
                cameraSelector,
                this.mPreview, this.mImageCapture
            )
        } catch(e: Exception) {
            Timber.e("Use case binding failed: ${e.message}")
        }

        // PreviewView
        this.mPreview?.setSurfaceProvider(
            this.mRootView.fragment_camera_PreviewView.createSurfaceProvider(this.mCamera?.cameraInfo)
        )
    }

    /**
     * Takes a picture
     */
    private fun takePicture() {
//        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(""))

        this.mImageCapture?.takePicture(
            this.mCameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    MessageTools.showMessageWithSnackbar(
                        this@CameraFragment.mRootView.fragment_camera_CoordinatorLayout,
                        "Take Picture"
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    Timber.e("Image capture failed: ${exception.message}")
                }
            }
        )
    }

    /**
     *  [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *  @param width    a [Int] that contains the preview width
     *  @param height   a [Int] that contains the preview height
     *  @return suitable aspect ratio
     */
    private fun getAspectRatio(width: Int, height: Int): Int {
        val previewRatio = maxOf(width, height).toDouble() / minOf(width, height).toDouble()

        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }

        return AspectRatio.RATIO_16_9
    }
}