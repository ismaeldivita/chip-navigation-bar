package com.ismaeldivita.chipnavigation.item

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.ismaeldivita.chipnavigation.ChipNavigationBar.MenuOrientation.HORIZONTAL
import com.ismaeldivita.chipnavigation.robot.MenuTestActivity
import com.ismaeldivita.chipnavigation.robot.menu
import com.ismaeldivita.chipnavigation.test.R
import org.junit.Rule
import org.junit.Test

@LargeTest
class HorizontalMenuTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MenuTestActivity::class.java)

    @Test
    fun testSelection() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, HORIZONTAL)

            noItemSelected(R.id.home, R.id.discover, R.id.search, R.id.settings)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.home)
            }

            item(R.id.search) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.search)
            }

            item(R.id.discover) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.discover)
            }

            item(R.id.settings) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.settings)
            }
        }
    }

    @Test
    fun testSelectionWithDisabledItems() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu_disabled, HORIZONTAL)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(false)
            }

            item(R.id.discover) {
                isSelected(false)
                click()
                isSelected(false)
            }

            item(R.id.search) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.search)
            }

            item(R.id.settings) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.settings)
            }
        }
    }

    @Test
    fun testDisableAnActiveItem() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, HORIZONTAL)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.home)
            }

            disableItem(R.id.home)

            noItemSelected(R.id.home, R.id.discover, R.id.search, R.id.settings)
        }
    }

}