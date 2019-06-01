package ismaeldivita.bubblenavigation.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ismaeldivita.bubblenavigation.BubbleNavigationView
import ismaeldivita.bubblenavigation.sample.util.colorAnimation

class HorizontalModeActivity : AppCompatActivity() {

    private val container by lazy { findViewById<View>(R.id.container) }
    private val title by lazy { findViewById<TextView>(R.id.title) }
    private val menu by lazy { findViewById<BubbleNavigationView>(R.id.bottom_menu) }

    private var lastColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal)

        lastColor = ContextCompat.getColor(this, R.color.blank)

        menu.setOnItemSelectedListener(object : BubbleNavigationView.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                val option = when (id) {
                    R.id.home -> R.color.home to "Home"
                    R.id.activity -> R.color.activity to "Activity"
                    R.id.favorites -> R.color.favorites to "Favorites"
                    R.id.settings -> R.color.settings to "Settings"
                    else -> R.color.white to ""
                }
                val color = ContextCompat.getColor(this@HorizontalModeActivity, option.first)
                container.colorAnimation(lastColor, color)
                lastColor = color

                title.text = option.second
            }
        })
    }

}
