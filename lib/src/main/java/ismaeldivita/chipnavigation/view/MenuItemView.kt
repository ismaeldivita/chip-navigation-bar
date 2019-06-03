package ismaeldivita.chipnavigation.view

import android.content.Context
import android.widget.FrameLayout
import ismaeldivita.chipnavigation.model.MenuItem

internal abstract class MenuItemView(context: Context) : FrameLayout(context) {

    abstract fun bind(item: MenuItem)

}