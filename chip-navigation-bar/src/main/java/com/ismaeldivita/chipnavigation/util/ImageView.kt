package com.ismaeldivita.chipnavigation.util

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.StateListAnimator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.annotation.ColorInt

private const val ICON_STATE_ANIMATOR_DURATION: Long = 350

/**
 * Create an color transition animator between two colors for an ImageView
 *
 * @param from start color
 * @param to final color
 * @param mode PorterDuff.Mode used to set the ColorFilter
 * @param durationInMillis animator duration in milliseconds
 * @return a color transition [Animator]
 */
internal fun ImageView.colorAnimator(
    @ColorInt from: Int,
    @ColorInt to: Int,
    mode: PorterDuff.Mode?,
    durationInMillis: Long
): Animator = ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
    duration = durationInMillis
    addUpdateListener { animator ->
        val color = animator.animatedValue as Int
        mode?.let { setColorFilter(color, mode) } ?: run { setColorFilter(color) }
    }
}

/**
 * Create and attach a StateListAnimator to animate the icon color changes based on the
 * view state.
 *
 * @param color color for selected state
 * @param unselectedColor  color for default state
 * @param disabledColor color for disabled state
 * @param mode PorterDuff.Mode used on color filter
 */
internal fun ImageView.setColorStateListAnimator(
    @ColorInt color: Int,
    @ColorInt unselectedColor: Int,
    @ColorInt disabledColor: Int,
    mode: PorterDuff.Mode?
) {
    val stateList = StateListAnimator().apply {
        addState(
            intArrayOf(android.R.attr.state_selected),
            colorAnimator(unselectedColor, color, mode, ICON_STATE_ANIMATOR_DURATION)
        )
        addState(
            intArrayOf(android.R.attr.state_enabled.unaryMinus()),
            colorAnimator(disabledColor, disabledColor, mode, 1)
        )
        addState(
            intArrayOf(),
            colorAnimator(color, unselectedColor, mode, ICON_STATE_ANIMATOR_DURATION)
        )
    }

    stateListAnimator = stateList

    // Refresh the drawable state to avoid the unselected animation on view creation
    refreshDrawableState()
}