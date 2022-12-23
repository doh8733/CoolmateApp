package com.quannm18.coolmateapp.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class FadePageTransformer : ViewPager2.PageTransformer {
    companion object {
        const val MIN_SCALE = 0.75f
        const val MIN_ALPHA = 0.3f
    }

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        if (position < -1) {
            page.alpha = MIN_ALPHA
        } else if (position <= 0) {
            page.alpha = 1f
            page.translationX = MIN_ALPHA
            page.scaleX = 1f
            page.scaleY = 1f
        } else if (position <= 1) {
            page.alpha = 1 - position
            page.translationX = pageWidth * -position
        } else {
            page.alpha = MIN_ALPHA
        }
    }
}