package com.mancel.yann.offsetcam.animations

import android.view.View
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Yann MANCEL on 14/06/2020.
 * Name of the project: OffsetCam
 * Name of the package: com.mancel.yann.offsetcam.animations
 *
 * A class which implements [ViewPager2.PageTransformer].
 */
@RequiresApi(21)
class DepthPageTransformer : ViewPager2.PageTransformer {

    /*
        See:
            [1] https://developer.android.com/training/animation/screen-slide-2
     */

    // FIELDS --------------------------------------------------------------------------------------

    companion object {
        private const val MIN_SCALE = 0.75f

    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewPager2.PageTransformer interface --

    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    translationZ = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position

                    // Counteract the default slide transition
                    translationX = pageWidth * -position
                    // Move it behind the left page
                    translationZ = -1f

                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}