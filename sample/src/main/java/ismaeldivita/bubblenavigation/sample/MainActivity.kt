package ismaeldivita.bubblenavigation.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ismaeldivita.bubblenavigation.BubbleNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_menu.setOnItemSelectedListener(object : BubbleNavigationView.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                Log.i("Item Selected", id.toString())
            }
        })

        button.setOnClickListener {
            if (enabled) {
                bottom_menu.collapse()
                bottom_menu_2.collapse()
            } else {
                bottom_menu.expand()
                bottom_menu_2.expand()
            }
            enabled = !enabled
            bottom_menu.setItemEnabled(R.id.menu4, enabled)
            bottom_menu.setHideOnScroll(enabled)

            bottom_menu_2.setItemEnabled(R.id.menu4, enabled)
            bottom_menu_2.setHideOnScroll(enabled)
        }

        hideBT.setOnClickListener {
            bottom_menu.hide()
            bottom_menu_2.hide()
        }

        showBT.setOnClickListener {
            bottom_menu.show()
            bottom_menu_2.show()
        }
    }

}
