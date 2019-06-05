package com.ismaeldivita.chipnavigation.util

import android.R
import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.StateListAnimator
import android.animation.ValueAnimator
import android.widget.TextView
import androidx.annotation.ColorInt

private const val TEXT_STATE_ANIMATOR_DURATION: Long = 350

/**
 * Create an color transition animator between two colors for the text
 *
 * @param from start color
 * @param to final color
 * @param mode PorterDuff.Mode used to set the ColorFilter
 * @param durationInMillis animator duration in milliseconds
 * @return a color transition [Animator]
 */
internal fun TextView.colorAnimator(
    @ColorInt from: Int,
    @ColorInt to: Int,
    durationInMillis: Long
): Animator = ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
    duration = durationInMillis
    addUpdateListener { animator ->
        setTextColor(animator.animatedValue as Int)
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
internal fun TextView.setColorStateListAnimator(
    @ColorInt color: Int,
    @ColorInt unselectedColor: Int,
    @ColorInt disabledColor: Int
) {
    val stateList = StateListAnimator().apply {
        addState(
            intArrayOf(R.attr.state_selected),
            colorAnimator(unselectedColor, color, TEXT_STATE_ANIMATOR_DURATION)
        )
        addState(
            intArrayOf(R.attr.state_enabled.unaryMinus()),
            colorAnimator(disabledColor, disabledColor, 1)
        )
        addState(
            intArrayOf(),
            colorAnimator(color, unselectedColor, TEXT_STATE_ANIMATOR_DURATION)
        )
    }

    stateListAnimator = stateList

    // Refresh the drawable state to avoid the unselected animation on view creation
    refreshDrawableState()
}