package com.ismaeldivita.chipnavigation.sample

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.ismaeldivita.chipnavigation.sample.util.applyWindowInsets
import android.widget.ArrayAdapter

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = ListView(this).apply {
            id = android.R.id.list
            applyWindowInsets(top = true)
        }
        setContentView(list)

        val entities = arrayOf("Horizontal orientation", "Vertical orientation")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, entities)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(this, HorizontalModeActivity::class.java))
            1 -> startActivity(Intent(this, VerticalModeActivity::class.java))
        }
    }

}