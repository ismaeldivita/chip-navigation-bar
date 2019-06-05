package com.ismaeldivita.chipnavigation.util

import android.view.View
import android.view.ViewGroup

/**
 * Update the LayoutParams applying the extension function [block] on the
 * current LayoutParams.
 *
 * @param block extension function
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : ViewGroup.LayoutParams> View.updateLayoutParams(block: T.() -> Unit) {
    val lp: T = (layoutParams as T).apply { block(this) }
    layoutParams = lp
}


/**
 * Iterate on all children views inside the ViewGroup all call the specified
 * function [block] with the child as its argument.
 *
 * @param block function to be called for each child view
 */
inline fun ViewGroup.forEachChild(block: (View) -> Unit) {
    for (i in 0 until childCount) {
        block(getChildAt(i))
    }
}

/**
 * Returns a [Sequence] over the child views in this view group.
 *
 * @return child view [Sequence]
 */
internal fun ViewGroup.getChildren(): Sequence<View> = object : Sequence<View> {
    override fun iterator() = this@getChildren.iterator()
}

/**
 * Create a [Iterator] over the views in this view group.
 *
 * @return child view [Iterator]
 */
private operator fun ViewGroup.iterator(): Iterator<View> = object : MutableIterator<View> {
    private var index = 0
    override fun hasNext() = index < childCount
    override fun next() = getChildAt(index++) ?: throw IndexOutOfBoundsException()
    override fun remove() = removeViewAt(--index)
}