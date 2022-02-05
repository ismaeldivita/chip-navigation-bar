package com.ismaeldivita.chipnavigation.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

/**
 * Create and set a StateListDrawable with Ripple effect.
 *
 * @param selectedBackground drawable for the background on selected state
 * @param mask drawable resource used on the ripple mask
 */
internal fun View.setCustomRipple(
    selectedBackground: Drawable,
    mask: Drawable
) {
    val highlightColor = context.getValueFromAttr(android.R.attr.colorControlHighlight)
    val colorStateList = ColorStateList.valueOf(highlightColor)
    val unselected = RippleDrawable(colorStateList, null, mask)
    val states = StateListDrawable()

    states.addState(intArrayOf(android.R.attr.state_selected), selectedBackground)
    states.addState(intArrayOf(), ColorDrawable(Color.TRANSPARENT))
    background = states
    foreground = unselected
}