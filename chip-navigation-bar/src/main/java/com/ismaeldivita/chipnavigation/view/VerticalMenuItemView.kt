package com.ismaeldivita.chipnavigation.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import com.ismaeldivita.chipnavigation.R
import com.ismaeldivita.chipnavigation.model.MenuItem
import com.ismaeldivita.chipnavigation.util.onEndListener
import com.ismaeldivita.chipnavigation.util.setColorStateListAnimator
import com.ismaeldivita.chipnavigation.util.setCustomRipple
import com.ismaeldivita.chipnavigation.util.updateLayoutParams

internal class VerticalMenuItemView(context: Context) : MenuItemView(context) {

    private val title by lazy { findViewById<TextView>(R.id.cbn_item_title) }
    private val icon by lazy { findViewById<ImageView>(R.id.cnb_item_icon) }
    private val container by lazy { findViewById<View>(R.id.cbn_item_internal_container) }
    private val containerBackground = GradientDrawable()
    private val containerForeground = GradientDrawable()
    private val doubleSpace = resources.getDimension(R.dimen.cnb_double_space).toInt()

    private companion object {
        private const val BACKGROUND_CORNER_ANIMATION_DURATION: Long = 250
    }

    init {
        View.inflate(getContext(), R.layout.cnb_vertical_menu_item, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    override fun bind(item: MenuItem) {
        id = item.id
        isEnabled = item.enabled

        title.text = item.title
        title.setColorStateListAnimator(
            color = item.textColor,
            unselectedColor = item.unselectedColor,
            disabledColor = item.disabledColor
        )

        icon.setImageResource(item.icon)
        icon.setColorStateListAnimator(
            color = item.iconColor,
            unselectedColor = item.unselectedColor,
            disabledColor = item.disabledColor,
            mode = item.tintMode
        )
        containerBackground.setTint(item.backgroundColor)
        containerForeground.setTint(Color.BLACK)

        styleContainerForExpandedState()

        container.setCustomRipple(containerBackground, containerForeground)
    }

    override fun showBadge(count: Int) {

    }

    override fun dismissBadge() {

    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        icon.jumpDrawablesToCurrentState()
        title.jumpDrawablesToCurrentState()

        if (!enabled && isSelected) {
            isSelected = false
        }
    }

    fun expand() {
        styleContainerForExpandedState()
    }

    fun collapse() {
        styleContainerForCollapseState()
    }

    private fun styleContainerForCollapseState() {
        title.visibility = View.GONE
        containerForeground.cornerRadius = 1000f
        container.updateLayoutParams<LayoutParams> { marginStart = doubleSpace }

        if (isSelected) {
            containerBackground.cornerAnimation(0f, 1000f)
        } else {
            containerBackground.cornerRadius = 1000f
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                container.setCustomRipple(containerBackground, containerForeground)
            }
        }
    }

    private fun styleContainerForExpandedState() {
        val cornerRadii = floatArrayOf(0f, 0f, 1000f, 1000f, 1000f, 1000f, 0f, 0f)
        title.visibility = View.VISIBLE
        container.updateLayoutParams<LayoutParams> { marginStart = 0 }
        containerForeground.cornerRadii = cornerRadii

        if (isSelected) {
            containerBackground.cornerAnimation(1000f, 0f)
        } else {
            containerBackground.cornerRadii = cornerRadii
            restoreRippleMaskForLegacy()
        }
    }

    private fun GradientDrawable.cornerAnimation(from: Float, to: Float) {
        ObjectAnimator.ofFloat(from, to).apply {
            addUpdateListener {
                val corner = it.animatedValue as Float
                cornerRadii =
                    floatArrayOf(corner, corner, 1000f, 1000f, 1000f, 1000f, corner, corner)
            }
            onEndListener { restoreRippleMaskForLegacy() }
            duration = BACKGROUND_CORNER_ANIMATION_DURATION
            start()
        }
    }


    private fun restoreRippleMaskForLegacy() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            container.setCustomRipple(containerBackground, containerForeground)
        }
    }

}