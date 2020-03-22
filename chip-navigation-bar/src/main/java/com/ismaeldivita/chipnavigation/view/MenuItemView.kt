package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ismaeldivita.chipnavigation.model.MenuItem
import com.ismaeldivita.chipnavigation.util.ALPHA_DISABLED

internal abstract class MenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    abstract fun bind(item: MenuItem)

    abstract fun showBadge(count: Int = 0)

    abstract fun dismissBadge()

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) 1.0f else  ALPHA_DISABLED
    }
}