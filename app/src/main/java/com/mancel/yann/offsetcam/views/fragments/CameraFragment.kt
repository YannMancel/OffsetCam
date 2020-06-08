package com.mancel.yann.offsetcam.views.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.states.CameraState
import com.mancel.yann.offsetcam.utils.MessageTools
import com.mancel.yann.offsetcam.viewModels.CameraViewModel
import com.mancel.yann.offsetcam.viewModels.OffsetCamViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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

    /*
        See:
            [1] https://developer.android.com/training/camerax
            [2] https://codelabs.developers.google.com/codelabs/camerax-getting-started
            [3] https://github.com/android/camera-samples/tree/master/CameraXBasic
            [4] https://www.udemy.com/course/android-sensors-cas-pratiques-dapplications
     */

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var _viewModel: CameraViewModel
    private lateinit var _cameraState: CameraState

    private var _oldSystemUiVisibility: Int = 0

    private lateinit var _cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var _camera: Camera? = null
    private var _preview: Preview? = null
    private var _imageCapture: ImageCapture? = null
    private lateinit var _cameraExecutor: ExecutorService
    private var _imageProxy: ImageProxy? = null

    private val _disposables by lazy { CompositeDisposable() }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment--

    override fun getFragmentLayout(): Int = R.layout.fragment_camera

    override fun configureDesign() {
        this.configureListeners()
        this.configureCameraState()
    }

    override fun actionAfterPermission() = this.bindCameraUseCases()

    // -- Fragment --

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        this._cameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        this._rootView.fragment_camera_PreviewView.post {
            this._viewModel.setupCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        this._oldSystemUiVisibility = this.requireActivity().window.decorView.systemUiVisibility
        this.requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        (this.requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        this.requireActivity().window.decorView.systemUiVisibility = this._oldSystemUiVisibility
        (this.requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Disposes all subscribers
        this._disposables.dispose()

        // Shut down our background executor
        this._cameraExecutor.shutdown()
    }

    // -- Listeners --

    /**
     * Configures the listeners
     */
    private fun configureListeners() {
        // FAB
        this._rootView.fragment_camera_FAB.setOnClickListener {
            this.takePicture()
        }

        // Switch camera button
        this._rootView.fragment_camera_switch_button.setOnClickListener {
            this.switchCamera()
        }

        // Gallery button
        this._rootView.fragment_camera_gallery_button.setOnClickListener {
            this.navigateToGalleryFragment()
        }
    }

    // -- LiveData --

    /**
     * Configures the [CameraState]
     */
    private fun configureCameraState() {
        // Camera manager
        val cameraManager = this.requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager

        // Factory
        val factory = OffsetCamViewModelFactory(cameraManager.cameraIdList.size)

        // ViewModel
        this._viewModel = ViewModelProvider(this@CameraFragment, factory).get(
                              CameraViewModel::class.java
                          )

        // Camera state
        this._viewModel.getCameraState().observe(
            this.viewLifecycleOwner,
            Observer {
                this.updateUI(it)
            }
        )
    }

    // -- UI --

    /**
     * Updates the UI according a [CameraState]
     * @param state a [CameraState]
     */
    private fun updateUI(state: CameraState) {
        // Keep the current state
        this._cameraState = state

        // Specific state
        when (state) {
            is CameraState.SetupCamera -> this.handleStateSetupCamera()
            is CameraState.PreviewReady -> this.handleStatePreviewReady()
            is CameraState.Error -> this.handleStateError(state)
            is CameraState.Loading -> this.handleStateLoading()
            is CameraState.PictureSaved -> this.handleStatePictureSaved()
            is CameraState.LensFacingSwitch -> this.handleStateLensFacingSwitch()
        }

        // General state
        with(state) {
            // FAB
            this@CameraFragment._rootView.fragment_camera_FAB.isEnabled = this._buttonEnable

            // Switch camera
            this@CameraFragment._rootView.fragment_camera_switch_button.isVisible = this._switchCameraVisible
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
    private fun handleStatePreviewReady() { /* Do nothing here */ }

    /**
     * Handles the [CameraState.Error]
     * @param state a [CameraState.Error]
     */
    private fun handleStateError(state: CameraState.Error) {
        MessageTools.showMessageWithSnackbar(
            this._rootView.fragment_camera_CoordinatorLayout,
            state._errorMessage
        )
    }

    /**
     * Handles the [CameraState.Loading]
     */
    private fun handleStateLoading() { /* Do nothing here */ }

    /**
     * Handles the [CameraState.PictureSaved]
     */
    private fun handleStatePictureSaved() {
        ObjectAnimator.ofFloat(
            this._rootView.fragment_camera_gallery_button,
            View.ALPHA,
            0F, 1F
        )
        .apply {
            duration = 500
            repeatCount = 2
            repeatMode = ValueAnimator.REVERSE
        }
        .start()
    }

    /**
     * Handles the [CameraState.LensFacingSwitch]
     */
    private fun handleStateLensFacingSwitch() = this.handleStateSetupCamera()

    // -- Camera --

    /**
     * Binds the camera to all use cases
     */
    private fun bindCameraUseCases() {
        if (
            this.checkCameraPermission()
            && this.checkWriteExternalStoragePermission()
        ) {
            this._cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
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

            this._viewModel.previewReady()
        }
        else {
            this._viewModel.errorPermissionDenied(this.requireContext())
        }
    }

    /**
     * Binds the [Preview] and [ImageCapture]
     */
    private fun bindPreviewAndImageCapture(cameraProvider: ProcessCameraProvider) {
        // Metrics
        val metrics = DisplayMetrics().also {
            this._rootView.fragment_camera_PreviewView.display.getRealMetrics(it)
        }

        // Ratio
        val screenAspectRatio = this.getAspectRatio(metrics.widthPixels, metrics.heightPixels)

        // Rotation
        val rotation = this._rootView.fragment_camera_PreviewView.display.rotation

        // CameraSelector
        val cameraSelector = CameraSelector.Builder()
                                           .requireLensFacing(this._cameraState._cameraLensFacing)
                                           .build()

        // Use case: Preview
        this._preview = Preview.Builder()
                               .setTargetAspectRatio(screenAspectRatio)
                               .setTargetRotation(rotation)
                               .build()

        // Use case: Image Capture
        this._imageCapture = ImageCapture.Builder()
                                         .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                         .setTargetAspectRatio(screenAspectRatio)
                                         .setTargetRotation(rotation)
                                         .build()

        try {
            // Camera connect to the Fragment's Lifecycle
            this._camera = cameraProvider.bindToLifecycle(
                this.viewLifecycleOwner,
                cameraSelector,
                this._preview, this._imageCapture
            )
        } catch(e: Exception) {
            Timber.e("Use case binding failed: ${e.message}")
        }

        // PreviewView
        this._preview?.setSurfaceProvider(
            this._rootView.fragment_camera_PreviewView.createSurfaceProvider()
        )
    }

    /**
     * Takes a picture thanks to the "Image Capture" use case
     */
    private fun takePicture() {
        this._imageCapture?.takePicture(
            this._cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // To can close it after the asynchronous process
                    this@CameraFragment._imageProxy = image

                    // With AsyncTask
                    /*
                        this@CameraFragment._viewModel.savePictureWithAsyncTask(
                            image.planes[0].buffer,
                            image.imageInfo.rotationDegrees
                        )
                     */

                    // With Programming reactive
                    this@CameraFragment._viewModel.savePictureWithSingle(
                        image.planes[0].buffer,
                        image.imageInfo.rotationDegrees
                    )
                    .doOnSubscribe {
                        this@CameraFragment._viewModel.loading()
                    }
                    .doAfterTerminate {
                        /*
                            The application is responsible for calling {@link ImageProxy#close()}
                            to close the image.
                            See: ImageCapture.OnImageCapturedCallback class
                        */
                        this@CameraFragment._imageProxy?.close()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = {
                            MessageTools.showMessageWithSnackbar(
                                this@CameraFragment._rootView.fragment_camera_CoordinatorLayout,
                                "Picture saved"
                            )

                            this@CameraFragment._viewModel.pictureSaved()
                        },
                        onError = { e ->
                            Timber.d("onError")
                            MessageTools.showMessageWithSnackbar(
                                this@CameraFragment._rootView.fragment_camera_CoordinatorLayout,
                                "Error saving file :${e.localizedMessage}"
                            )
                        }
                    )
                    .addTo(this@CameraFragment._disposables)
                }

                override fun onError(exception: ImageCaptureException) {
                    Timber.e("Image capture failed: ${exception.message}")
                }
            }
        )
    }

    /**
     * Switches camera
     */
    private fun switchCamera() = this._viewModel.switchCameraLensFacing(this.resources)

    // -- Ratio --

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

    // -- Navigation to another fragment --

    /**
     * Navigates to another fragment
     */
    private fun navigateToGalleryFragment() {
        // By action (Safe Args)
        val action = CameraFragmentDirections.actionNavigationCameraFragmentToGalleryFragment()
        this.findNavController().navigate(action)
    }
}