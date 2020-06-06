package com.mancel.yann.offsetcam.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mancel.yann.offsetcam.R
import com.mancel.yann.offsetcam.states.CameraState
import com.mancel.yann.offsetcam.utils.FileTools
import com.mancel.yann.offsetcam.utils.GraphicsTools
import timber.log.Timber
import java.io.FileOutputStream
import java.nio.ByteBuffer

/**
 * Created by Yann MANCEL on 11/04/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.viewModels
 *
 * A [ViewModel] subclass.
 */
class OffsetCamViewModel : ViewModel() {

    // FIELDS --------------------------------------------------------------------------------------

    private var _cameraState: MutableLiveData<CameraState>? = null
    private var _switchCameraVisible = true
    private var _cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --
    /**
     * Gets the [LiveData] of [CameraState]
     * @return the [LiveData] of [CameraState]
     */
    fun getCameraState(): LiveData<CameraState> {
        if (this._cameraState == null) {
            this._cameraState = MutableLiveData<CameraState>()
        }
        return this._cameraState!!
    }

    // -- CameraState --

    /**
     * Manages the permission denied
     * @param context a [Context]
     */
    fun errorPermissionDenied(context: Context) {
        this._cameraState?.value = CameraState.Error(
            _errorMessage = context.getString(R.string.error_camera_state),
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }

    /**
     * Manages the setup of the camera
     */
    fun setupCamera() {
        this._cameraState?.value = CameraState.SetupCamera(
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }

    /**
     * Manages the preview ready
     */
    fun previewReady() {
        this._cameraState?.value = CameraState.PreviewReady(
            switchCameraVisible = this._switchCameraVisible,
            cameraLensFacing = this._cameraLensFacing
        )
    }

    // -- Image --

    /**
     * Saves the picture thanks to the "Image Capture" use case of CameraX
     * @param buffer          a [ByteBuffer]
     * @param rotationDegrees an [Int] that contains the orientation of picture
     */
    fun savePicture(buffer: ByteBuffer, rotationDegrees: Int) {
        // Decodes ByteArray to Bitmap
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        
        val initialBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

        // Rotation of picture
        val tempBitmap =
            if (rotationDegrees != 0) {
                val matrix = Matrix().apply {
                    postRotate(rotationDegrees.toFloat())
                }
                Bitmap.createBitmap(
                    initialBitmap,
                    0,
                    0,
                    initialBitmap.width,
                    initialBitmap.height,
                    matrix,
                    true
                )
            }
            else {
                initialBitmap
            }

        // Filter
        val finalBitmap = GraphicsTools.filterBitmap(tempBitmap)

        // Save picture
        val outputFile = FileTools.createPictureFile()

        FileOutputStream(outputFile).use { fos ->
            finalBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                95,
                fos
            )
        }

        Timber.d("Saved file at $outputFile")
    }
}