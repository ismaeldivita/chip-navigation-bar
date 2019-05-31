/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ismaeldivita.bubblenavigation.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import ismaeldivita.bubblenavigation.BubbleNavigationView
import ismaeldivita.bubblenavigation.behavior.HideOnScrollBehavior.State.*

internal class HideOnScrollBehavior(
    context: Context,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<BubbleNavigationView>(context, attrs) {

    companion object {
        const val DEFAULT_ENTER_DURATION = 225L
        const val DEFAULT_EXIT_DURATION = 175L
    }

    private enum class State {
        STATE_SCROLLED_DOWN,
        STATE_SCROLLED_UP
    }

    var scrollEnabled = false
    var enterAnimationDuration = DEFAULT_ENTER_DURATION
    var exitAnimationDuration = DEFAULT_EXIT_DURATION

    private var height = 0
    private var currentState: State = STATE_SCROLLED_UP
    private var currentAnimator: ViewPropertyAnimator? = null

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: BubbleNavigationView,
        layoutDirection: Int
    ): Boolean {
        val paramsCompat = child.layoutParams as ViewGroup.MarginLayoutParams
        height = child.measuredHeight + paramsCompat.bottomMargin + child.paddingBottom
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BubbleNavigationView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = scrollEnabled && axes == ViewCompat.SCROLL_AXIS_VERTICAL;

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BubbleNavigationView,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        if (currentState != STATE_SCROLLED_DOWN && dyConsumed > 0) {
            slideDown(child)
        } else if (currentState != STATE_SCROLLED_UP && dyConsumed < 0) {
            slideUp(child)
        }
    }

    fun slideUp(child: BubbleNavigationView) {
        currentAnimator?.cancel()
        child.clearAnimation()
        currentState = STATE_SCROLLED_UP

        animateChildTo(
            child = child,
            targetY = 0,
            duration = enterAnimationDuration,
            interpolator = LinearOutSlowInInterpolator()
        )
    }

    fun slideDown(child: BubbleNavigationView) {
        currentAnimator?.cancel()
        child.clearAnimation()
        currentState = STATE_SCROLLED_DOWN

        animateChildTo(
            child = child,
            targetY = height,
            duration = exitAnimationDuration,
            interpolator = FastOutLinearInInterpolator()
        )
    }

    private fun animateChildTo(
        child: BubbleNavigationView,
        targetY: Int,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        currentAnimator = child
            .animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                })
    }

}
