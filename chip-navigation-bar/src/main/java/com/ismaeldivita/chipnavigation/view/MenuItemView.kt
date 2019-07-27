package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.widget.FrameLayout
import com.ismaeldivita.chipnavigation.model.MenuItem

internal abstract class MenuItemView(context: Context) : FrameLayout(context) {

    abstract fun bind(item: MenuItem)

    abstract fun showBadge(count: Int = 0)

    abstract fun dismissBadge()

}