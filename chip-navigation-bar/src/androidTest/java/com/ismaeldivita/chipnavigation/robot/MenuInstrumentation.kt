package com.ismaeldivita.chipnavigation.robot

import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.test.rule.ActivityTestRule
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.ismaeldivita.chipnavigation.R
import io.github.kakaocup.kakao.common.views.KSwipeView
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import com.ismaeldivita.chipnavigation.test.R as testR

class MenuInstrumentation(private val activity: MenuTestActivity) : Screen<MenuInstrumentation>() {

    private val result = KTextView { withId(testR.id.result) }
    private val menu = KView { withId(testR.id.menu) }
    private val scroll = KSwipeView { withId(testR.id.nestedScroll) }
    private lateinit var orientation: ChipNavigationBar.MenuOrientation

    fun loadMenuResource(@MenuRes id: Int, orientation: ChipNavigationBar.MenuOrientation) {
        this.orientation = orientation
        activity.runOnUiThread {
            activity.setOrientation(orientation)
            activity.loadMenuResource(id)
        }
    }

    fun noItemSelected(vararg id: Int) {
        result.hasText("0")
        id.forEach { item(it) { isSelected(false) } }
    }

    fun item(id: Int, block: MenuItemInstrumentation.() -> Unit) =
        MenuItemInstrumentation(id, orientation).apply { block() }

    fun disableItem(itemId: Int) {
        activity.runOnUiThread { activity.disabledItem(itemId) }
    }

    fun enableItem(itemId: Int) {
        activity.runOnUiThread { activity.enableItem(itemId) }
    }

    fun isDisplayed() {
        menu.isCompletelyDisplayed()
    }

    fun isHidden() {
        menu.isNotCompletelyDisplayed()
    }

    fun swipeUp() {
        scroll.swipeUp()
    }

    fun swipeDown() {
        scroll.swipeDown()
    }

    fun expand() {
        activity.runOnUiThread { activity.expand() }
    }

    fun collapse() {
        activity.runOnUiThread { activity.collapse() }
    }

    inner class MenuItemInstrumentation(
        private val id: Int,
        private val orientation: ChipNavigationBar.MenuOrientation
    ) : Screen<MenuItemInstrumentation>() {

        private val item = KView {
            withId(this@MenuItemInstrumentation.id)
        }

        private val title = KTextView {
            withParent { withParent { withId(this@MenuItemInstrumentation.id) } }
            withId(R.id.cbn_item_title)
        }

        private val icon = KImageView {
            withParent { withParent { withId(this@MenuItemInstrumentation.id) } }
            withId(R.id.cnb_item_icon)
        }

        fun isTitle(@StringRes text: Int) = title {
            hasText(text)
            isCompletelyDisplayed()
        }

        fun isTitleDisplayed(isDisplayed: Boolean) {
            if (isDisplayed) {
                title.isCompletelyDisplayed()
            } else {
                title.isNotCompletelyDisplayed()
            }
        }

        fun click() {
            icon.click()
        }

        fun isSelected(isSelected: Boolean) {
            if (isSelected) {
                item.isSelected()
                this@MenuInstrumentation.result.hasText(id.toString())

                if (orientation == ChipNavigationBar.MenuOrientation.HORIZONTAL) {
                    title.isCompletelyDisplayed()
                }
            } else {
                item.isNotSelected()
                this@MenuInstrumentation.result.hasNoText(id.toString())

                if (orientation == ChipNavigationBar.MenuOrientation.HORIZONTAL) {
                    title.isNotCompletelyDisplayed()
                }
            }
        }
    }
}


fun menu(rule: ActivityTestRule<MenuTestActivity>, block: MenuInstrumentation.() -> Unit) {
    MenuInstrumentation(rule.activity).apply { block() }
}