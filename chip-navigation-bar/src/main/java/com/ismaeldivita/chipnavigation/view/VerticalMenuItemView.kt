package com.ismaeldivita.chipnavigation.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.ismaeldivita.chipnavigation.R
import com.ismaeldivita.chipnavigation.model.MenuItem
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
    private val doubleSpace = resources.getDimension(R.dimen.cnb_space_2).toInt()
    private val originalTypeFace: Typeface
    private var badgeCount = -1
    private var radius = 0f

    private companion object {
        private const val BACKGROUND_CORNER_ANIMATION_DURATION: Long = 250
        private const val BULLET = '\u2B24'
    }

    init {
        View.inflate(getContext(), R.layout.cnb_vertical_menu_item, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        originalTypeFace = countLabel.typeface
    }

    override fun bind(item: MenuItem) {
        id = item.id
        isEnabled = item.enabled
        radius = item.menuStyle.radius

        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        contentDescription = item.contentDescription ?: item.title

        item.menuStyle.textAppearance?.let(title::setTextAppearance)
        title.text = item.title
        title.setColorStateListAnimator(
            color = item.textColor,
            unselectedColor = item.menuStyle.unselectedColor
        )

        item.menuStyle.textAppearance?.let(countLabel::setTextAppearance)
        countLabel.setColorStateListAnimator(
            color = item.textColor,
            unselectedColor = item.menuStyle.unselectedColor
        )
        icon.layoutParams.width = item.menuStyle.iconSize
        icon.layoutParams.height = item.menuStyle.iconSize
        icon.setBadgeColor(item.menuStyle.badgeColor)
        icon.setImageResource(item.icon)
        icon.setColorStateListAnimator(
            color = item.iconColor,
            unselectedColor = item.menuStyle.unselectedColor,
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
        containerForeground.cornerRadius = radius
        container.updateLayoutParams<MarginLayoutParams> {
            marginStart = doubleSpace
        }
        icon.updateLayoutParams<MarginLayoutParams> {
            marginStart = 0
            marginEnd = 0
        }

        if (isSelected) {
            containerBackground.cornerAnimation(0f, radius).start()
        } else {
            containerBackground.cornerRadius = radius
        }
    }

    private fun styleContainerForExpandedState() {
        val cornerArray = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
        } else {
            floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
        }

        title.alpha = 0f
        title.visibility = View.VISIBLE
        title.animate()
            .alpha(1f)
            .setStartDelay(200)
            .start()

        countLabel.visibility = View.VISIBLE
        container.updateLayoutParams<MarginLayoutParams> { marginStart = 0 }
        icon.updateLayoutParams<MarginLayoutParams> {
            marginStart = doubleSpace
            marginEnd = doubleSpace
        }

        containerForeground.cornerRadii = cornerArray

        if (isSelected) {
            containerBackground.cornerAnimation(radius, 0f).start()
        } else {
            containerBackground.cornerRadii = cornerArray
        }
    }

    private fun GradientDrawable.cornerAnimation(from: Float, to: Float): Animator =
        ObjectAnimator.ofFloat(from, to).apply {
            addUpdateListener {
                val corner = it.animatedValue as Float
                val cornerArray = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
                    floatArrayOf(corner, corner, radius, radius, radius, radius, corner, corner)
                } else {
                    floatArrayOf(radius, radius, corner, corner, corner, corner, radius, radius)
                }

                cornerRadii = cornerArray
            }
            duration = BACKGROUND_CORNER_ANIMATION_DURATION
        }
}