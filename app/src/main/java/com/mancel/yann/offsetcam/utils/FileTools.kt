package com.mancel.yann.offsetcam.utils

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Yann MANCEL on 06/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.utils
 */
object FileTools {

    // FIELDS --------------------------------------------------------------------------------------

    private const val FOLDER_NAME = "OffsetCam"

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the gallery [File]
     * @return a [File]
     */
    fun getGalleryFile(): File {
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(storageDir, FOLDER_NAME)
    }

    /**
     * Creates the picture [File]
     * @return a [File]
     */
    fun createPictureFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "OffsetCam_${timestamp}_"
        return File.createTempFile(filename, ".jpg", getGalleryFile())
    }
}