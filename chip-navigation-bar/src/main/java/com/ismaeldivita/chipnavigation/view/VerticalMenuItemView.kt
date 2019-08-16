package com.ismaeldivita.chipnavigation.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.ismaeldivita.chipnavigation.R
import com.ismaeldivita.chipnavigation.model.MenuItem
import com.ismaeldivita.chipnavigation.util.onEndListener
import com.ismaeldivita.chipnavigation.util.setColorStateListAnimator
import com.ismaeldivita.chipnavigation.util.setCustomRipple
import com.ismaeldivita.chipnavigation.util.updateLayoutParams

internal class VerticalMenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MenuItemView(context, attrs) {

    private val title by lazy { findViewById<TextView>(R.id.cbn_item_title) }
    private val icon by lazy { findViewById<BadgeImageView>(R.id.cnb_item_icon) }
    private val countLabel by lazy { findViewById<TextView>(R.id.cbn_item_notification_count) }
    private val container by lazy { findViewById<View>(R.id.cbn_item_internal_container) }
    private val containerBackground = GradientDrawable()
    private val containerForeground = GradientDrawable()
    private val doubleSpace = resources.getDimension(R.dimen.cnb_double_space).toInt()
    private val originalTypeFace: Typeface
    private var badgeCount = -1

    private companion object {
        private const val BACKGROUND_CORNER_ANIMATION_DURATION: Long = 250
        private const val BULLET =  '\u2B24'
    }

    init {
        View.inflate(getContext(), R.layout.cnb_vertical_menu_item, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        originalTypeFace = countLabel.typeface
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

        countLabel.setColorStateListAnimator(
            color = item.textColor,
            unselectedColor = item.unselectedColor,
            disabledColor = item.disabledColor
        )

        icon.setBadgeColor(item.badgeColor)
        icon.setImageResource(item.icon)
        icon.setColorStateListAnimator(
            color = item.iconColor,
            unselectedColor = item.unselectedColor,
            disabledColor = item.disabledColor,
            mode = item.tintMode
        )
        containerBackground.setTint(item.backgroundColor)
        containerForeground.setTint(Color.BLACK)

        styleContainerForCollapseState()

        container.setCustomRipple(containerBackground, containerForeground)
    }

    override fun showBadge(count: Int) {
        badgeCount = count
        if (badgeCount > 0) {
            countLabel.typeface = originalTypeFace
            countLabel.text = badgeCount.toString()
        } else {
            countLabel.typeface = Typeface.DEFAULT
            countLabel.text = BULLET.toString()
        }

        if (!isExpanded()) {
            icon.showBadge(badgeCount)
        }
    }

    override fun dismissBadge() {
        badgeCount = -1
        icon.dismissBadge()
        countLabel.text = ""
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
        if (badgeCount >= 0) {
            icon.dismissBadge()
        }
    }

    fun collapse() {
        styleContainerForCollapseState()
        if (badgeCount >= 0) {
            icon.showBadge(badgeCount)
        }
    }

    private fun isExpanded(): Boolean = title.visibility == View.VISIBLE

    private fun styleContainerForCollapseState() {
        title.visibility = View.GONE
        countLabel.visibility = View.GONE
        containerForeground.cornerRadius = 1000f
        container.updateLayoutParams<MarginLayoutParams> { marginStart = doubleSpace }
        icon.updateLayoutParams<MarginLayoutParams> {
            marginStart = 0
            marginEnd = 0
        }

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
        countLabel.visibility = View.VISIBLE
        container.updateLayoutParams<MarginLayoutParams> { marginStart = 0 }
        icon.updateLayoutParams<MarginLayoutParams> {
            marginStart = doubleSpace
            marginEnd = doubleSpace
        }

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