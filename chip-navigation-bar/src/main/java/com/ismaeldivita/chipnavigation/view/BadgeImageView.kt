package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt

class BadgeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    @ColorInt
    var badgeColor: Int = Color.RED

    @ColorInt
    var badgeStrokeColor: Int = Color.TRANSPARENT

    private val badge by lazy { BadgeDrawable(context) }

    fun showBadge(count: Int) {
        val rect = Rect().also(::getDrawingRect)

        badge.setColor(badgeColor)
        badge.updateBadgeBounds(rect, count)
        badge.setStroke(badgeStrokeColor)
        overlay.add(badge)
    }

    fun dismissBadge() {
        overlay.remove(badge)
    }

}