package ismaeldivita.bubblenavigation.view

import android.content.Context
import android.widget.FrameLayout
import ismaeldivita.bubblenavigation.model.MenuItem

internal abstract class MenuItemView(context: Context) : FrameLayout(context) {

    abstract fun bind(item: MenuItem)

}