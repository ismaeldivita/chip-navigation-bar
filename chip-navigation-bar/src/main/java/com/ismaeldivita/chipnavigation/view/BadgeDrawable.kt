package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.text.TextPaint
import androidx.annotation.ColorInt
import com.ismaeldivita.chipnavigation.R
import kotlin.math.roundToInt

internal class BadgeDrawable(val context: Context) : Drawable() {

    companion object {
        private const val MAX_BADGE_COUNT = 99
    }

    var count: Int = 0
        set(value) {
            field = value
            parentBounds?.let(::updateBadgeBounds)
        }

    private var parentBounds: Rect? = null

    private val shapeDrawable by lazy {
        GradientDrawable().apply { shape = RECTANGLE }
    }

    private val textPaint by lazy {
        TextPaint().apply {
            isAntiAlias = true
            color = Color.WHITE
            textSize = context.resources.getDimension(R.dimen.cnb_badge_text_size)
            isFakeBoldText = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    fun setColor(@ColorInt color: Int) {
        shapeDrawable.setColor(color)
    }

    fun updateBadgeBounds(parentBounds: Rect) {
        this.parentBounds = parentBounds

        val size = if (count > 0) {
            context.resources.getDimensionPixelSize(R.dimen.cnb_badge_size)
        } else {
            context.resources.getDimensionPixelSize(R.dimen.cnb_badge_size_numberless)
        }
        val extraPadding = if (count > MAX_BADGE_COUNT) 1.5 else 1.0
        shapeDrawable.cornerRadius = parentBounds.height() * 0.5f
        shapeDrawable.setBounds(
            parentBounds.right - size.times(extraPadding).roundToInt(),
            0,
            parentBounds.right,
            parentBounds.top + size
        )
    }

    override fun setAlpha(alpha: Int) {
        shapeDrawable.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun draw(canvas: Canvas) {
        if (!shapeDrawable.bounds.isEmpty) {
            shapeDrawable.draw(canvas)

            if (count > 0) {
                drawText(canvas)
            }
        }
    }

    private fun drawText(canvas: Canvas) {
        val textBounds = Rect()
        val countText = if (count > MAX_BADGE_COUNT) {
            "$MAX_BADGE_COUNT+"
        } else {
            count.toString()
        }

        textPaint.getTextBounds(countText, 0, countText.length, textBounds)

        canvas.drawText(
            countText,
            shapeDrawable.bounds.exactCenterX() - textBounds.exactCenterX(),
            shapeDrawable.bounds.exactCenterY() + textBounds.height() / 2,
            textPaint
        )
    }
}