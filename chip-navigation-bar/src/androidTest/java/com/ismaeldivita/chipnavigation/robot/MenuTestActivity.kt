package com.ismaeldivita.chipnavigation.robot

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.MenuRes
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.ismaeldivita.chipnavigation.test.R

class MenuTestActivity : Activity() {

    companion object {
        const val RESULT_EMPTY_VALUE = "0"
    }

    private val result by lazy { findViewById<TextView>(R.id.result) }
    private val menu by lazy { findViewById<ChipNavigationBar>(R.id.menu) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_horizontal)

        menu.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                assert(result.text != id.toString()) { "Duplicated event detected" }
                result.text = id.toString()
            }
        })
    }

    fun setOrientation(orientation: ChipNavigationBar.MenuOrientation) {
        menu.setMenuOrientation(orientation)
    }

    fun loadMenuResource(@MenuRes menuRes: Int) {
        menu.setMenuResource(menuRes)
    }

    fun disabledItem(itemId: Int) {
        if (itemId.toString() == result.text) {
            resetResultState()
        }
        menu.setItemEnabled(itemId, false)
    }

    fun enableItem(itemId: Int) {
        menu.setItemEnabled(itemId, true)
    }

    fun expand() {
        menu.expand()
    }

    fun collapse() {
        menu.collapse()
    }

    private fun resetResultState() {
        result.text = RESULT_EMPTY_VALUE
    }

}