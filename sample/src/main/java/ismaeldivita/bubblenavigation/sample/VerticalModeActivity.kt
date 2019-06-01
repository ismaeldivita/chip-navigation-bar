package ismaeldivita.bubblenavigation.sample

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ismaeldivita.bubblenavigation.BubbleNavigationView
import ismaeldivita.bubblenavigation.sample.util.applyWindowInsets
import ismaeldivita.bubblenavigation.sample.util.colorAnimation

class VerticalModeActivity : AppCompatActivity() {

    private val container by lazy { findViewById<ViewGroup>(R.id.container) }
    private val title by lazy { findViewById<TextView>(R.id.title) }
    private val button by lazy { findViewById<ImageView>(R.id.expand_button) }
    private val menu by lazy { findViewById<BubbleNavigationView>(R.id.bottom_menu) }

    private var lastColor: Int = 0
    private var isExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)

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
                val color = ContextCompat.getColor(this@VerticalModeActivity, option.first)
                container.colorAnimation(lastColor, color)
                lastColor = color
                title.text = option.second
            }
        })

        button.setOnClickListener {
            if (isExpanded) {
                TransitionManager.beginDelayedTransition(container, ChangeBounds())
                menu.collapse()
                button.setImageResource(R.drawable.ic_arrow_right)
            } else {
                TransitionManager.beginDelayedTransition(container, ChangeBounds())
                menu.expand()
                button.setImageResource(R.drawable.ic_arrow_left)
            }
            isExpanded = !isExpanded
        }

        button.applyWindowInsets(bottom = true)

    }

}
