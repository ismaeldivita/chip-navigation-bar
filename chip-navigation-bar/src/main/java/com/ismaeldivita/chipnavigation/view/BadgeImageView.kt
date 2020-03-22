package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView

internal class BadgeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val badge = BadgeDrawable(context)

    init {
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (visibility == View.VISIBLE) {
                badge.updateBadgeBounds(Rect().apply(::getDrawingRect))
            }
        }
    }

    fun setBadgeColor(@ColorInt color: Int) {
        badge.setColor(color)
    }

    fun showBadge(count: Int) {
        val bounds = Rect().apply(::getDrawingRect)

        badge.count = count

        if (!bounds.isEmpty) {
            badge.updateBadgeBounds(bounds)
        }
        overlay.add(badge)
        invalidate()
    }

    fun dismissBadge() {
        overlay.remove(badge)
        invalidate()
    }

}