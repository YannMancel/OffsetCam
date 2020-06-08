package com.mancel.yann.offsetcam.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

/**
 * Created by Yann MANCEL on 08/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.models
 *
 * Do not forget in build.gradle(:app):
 *      androidExtensions { experimental = true }
 */
@Parcelize
data class Picture(val file: File) : Parcelable