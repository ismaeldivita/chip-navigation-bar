package ismaeldivita.bubblenavigation.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ismaeldivita.bubblenavigation.BubbleNavigationView
import kotlinx.android.synthetic.main.activity_horizontal.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal)

        bottom_menu.setOnItemSelectedListener(object : BubbleNavigationView.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                Log.i("Item Selected", id.toString())
            }
        })
    }

}
