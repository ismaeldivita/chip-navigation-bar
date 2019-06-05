package com.ismaeldivita.chipnavigation.util

import android.animation.Animator

internal fun Animator.onEndListener(block: (Animator) -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            block(animation)
        }
    })
}
