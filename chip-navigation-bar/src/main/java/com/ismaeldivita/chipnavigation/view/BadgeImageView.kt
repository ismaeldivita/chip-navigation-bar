package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt

internal class BadgeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    @ColorInt
    var badgeColor: Int = Color.RED

    @ColorInt
    var badgeStrokeColor: Int = Color.TRANSPARENT

    private val badge = BadgeDrawable(context)

    init {
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (visibility == View.VISIBLE) {
                badge.updateBadgeBounds(Rect().apply(::getDrawingRect))
            }
        }
    }

    fun showBadge(count: Int) {
        val bounds = Rect().apply(::getDrawingRect)

        badge.setColor(badgeColor)
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