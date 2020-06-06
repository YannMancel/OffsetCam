package com.mancel.yann.offsetcam.utils

import android.graphics.*

/**
 * Created by Yann MANCEL on 06/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.utils
 */
object GraphicsTools {

    // FIELDS --------------------------------------------------------------------------------------

    /**
     * Applies a filter on [Bitmap] in argument and provides the [Bitmap] with the filter
     * @param bitmap a [Bitmap]
     * @return a [Bitmap]
     */
    fun filterBitmap(bitmap: Bitmap) : Bitmap {
        val resultBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            bitmap.config
        )

        with(Canvas(resultBitmap)) {
            // Top middle of image
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                0, 0,
                bitmap.width, bitmap.height / 2
            )

            drawBitmap(croppedBitmap, 0F, 0F, null)

            val halfSize = RectF(
                0F,
                0F,
                bitmap.width.toFloat(),
                (bitmap.height / 2).toFloat()
            )

            val matrix = Matrix().apply {
                postScale(1F, -1F, halfSize.centerX(), halfSize.centerY())
                postTranslate(0F, halfSize.height())

                //postRotate(180F, halfSize.centerX(), halfSize.height())
            }

            val paint = Paint().apply {
                colorFilter = LightingColorFilter(Color.rgb(40, 84, 140), 0)
            }

            drawBitmap(croppedBitmap, matrix, paint)
        }

        return resultBitmap
    }
}