package com.ismaeldivita.chipnavigation

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.IntRange
import androidx.annotation.MenuRes
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.ismaeldivita.chipnavigation.model.MenuParser
import com.ismaeldivita.chipnavigation.model.MenuStyle
import com.ismaeldivita.chipnavigation.util.applyWindowInsets
import com.ismaeldivita.chipnavigation.util.forEachChild
import com.ismaeldivita.chipnavigation.util.getChildren
import com.ismaeldivita.chipnavigation.view.HorizontalMenuItemView
import com.ismaeldivita.chipnavigation.view.MenuItemView
import com.ismaeldivita.chipnavigation.view.VerticalMenuItemView

class ChipNavigationBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var orientationMode: MenuOrientation
    private var listener: OnItemSelectedListener? = null
    private var minimumExpandedWidth: Int = 0
    private var isExpanded: Boolean = false
    private val menuStyle: MenuStyle
    private var customAnimationDuration: Long? = null

    @MenuRes
    private var menuRes = -1

    private val badgesState = mutableMapOf<Int, Int>()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ChipNavigationBar)

        val menuResource = a.getResourceId(R.styleable.ChipNavigationBar_cnb_menuResource, -1)
        val minExpanded = a.getDimension(R.styleable.ChipNavigationBar_cnb_minExpandedWidth, 0F)
        val leftInset = a.getBoolean(R.styleable.ChipNavigationBar_cnb_addLeftInset, false)
        val topInset = a.getBoolean(R.styleable.ChipNavigationBar_cnb_addTopInset, false)
        val rightInset = a.getBoolean(R.styleable.ChipNavigationBar_cnb_addRightInset, false)
        val bottomInset = a.getBoolean(R.styleable.ChipNavigationBar_cnb_addBottomInset, false)
        val orientation = when (a.getInt(R.styleable.ChipNavigationBar_cnb_orientationMode, 0)) {
            0 -> MenuOrientation.HORIZONTAL
            1 -> MenuOrientation.VERTICAL
            else -> MenuOrientation.HORIZONTAL
        }

        customAnimationDuration = a.getInt(R.styleable.ChipNavigationBar_cnb_animationDuration, -1)
            .takeIf { it >= 0 }
            ?.toLong()

        menuStyle = MenuStyle(context, a)

        a.recycle()
        setMenuOrientation(orientation)

        if (menuResource >= 0) {
            setMenuResource(menuResource)
        }

        setMinimumExpandedWidth(minExpanded.toInt())
        applyWindowInsets(leftInset, topInset, rightInset, bottomInset)
        collapse()
        isClickable = true
    }

    /**
     * Inflate a menu from the specified XML resource
     *
     * @param menuRes Resource ID for an XML layout resource to load
     */
    fun setMenuResource(@MenuRes menuRes: Int) {
        this.menuRes = menuRes

        val menu = (MenuParser(context).parse(menuRes, menuStyle))
        val childListener: (View) -> Unit = { view -> setItemSelected(view.id) }

        removeAllViews()

        menu.items.forEach {
            createMenuItem()
                .apply {
                    bind(it)
                    setOnClickListener(childListener)
                }
                .also(::addView)
        }

        when (orientationMode) {
            MenuOrientation.HORIZONTAL -> getHorizontalFlow()
            MenuOrientation.VERTICAL -> getVerticalFlow()
        }.apply { referencedIds = menu.items.map { it.id }.toIntArray() }
            .also(::addView)
    }

    /**
     * Set the menu orientation
     *
     * @param menuOrientation orientation
     */
    fun setMenuOrientation(menuOrientation: MenuOrientation) {
        orientationMode = menuOrientation
    }

    /**
     * Set the enabled state for the menu item with the provided [id]
     *
     * @param id menu item id
     * @param isEnabled true if this view is enabled, false otherwise
     */
    fun setItemEnabled(id: Int, isEnabled: Boolean) {
        getItemById(id)?.isEnabled = isEnabled
    }

    /**
     * Remove the selected state from the current item and set the selected state to true
     * for the menu item with the [id]
     *
     * @param id menu item id
     * @param isSelected true if this view is selected, false otherwise
     */
    fun setItemSelected(id: Int, isSelected: Boolean = true) {
        setItemSelected(id = id, isSelected = isSelected, dispatchAction = true)
    }

    /**
     * Set the minimum width for the vertical expanded state.
     *
     * @param minExpandedWidth width in pixels
     */
    fun setMinimumExpandedWidth(minExpandedWidth: Int) {
        minimumExpandedWidth = minExpandedWidth
    }

    /**
     * Register a callback to be invoked when a menu item is selected
     *
     * @param listener The callback that will run
     */
    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    /**
     * Register a callback to be invoked when a menu item is selected
     *
     * @param block The callback that will run
     */
    fun setOnItemSelectedListener(block: (Int) -> Unit) {
        setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                block(id)
            }
        })
    }

    /**
     * Set a custom animation duration
     *
     * @param duration Animation duration in ms
     */
    fun setDuration(duration: Long) {
        this.customAnimationDuration = duration
    }

    /**
     * Display a notification numberless badge for a menu item
     *
     * @param id menu item id
     */
    fun showBadge(id: Int) {
        badgesState[id] = 0
        getItemById(id)?.showBadge()
    }

    /**
     * Display a notification badge with a counter for a menu item
     * The maximum digits length to be displayed is 2 otherwise "+99" will be displayed
     *
     * @param id menu item id
     */
    fun showBadge(id: Int, @IntRange(from = 1) count: Int) {
        badgesState[id] = count
        getItemById(id)?.showBadge(count)
    }

    /**
     * Dismiss the displayed badge for a menu item
     *
     * @param id menu item id
     */
    fun dismissBadge(id: Int) {
        badgesState.remove(id)
        getItemById(id)?.dismissBadge()
    }

    /**
     * Collapse the menu items if orientationMode is VERTICAL otherwise, do nothing
     */
    fun collapse() {
        isExpanded = false

        if (orientationMode == MenuOrientation.VERTICAL) {
            forEachChild {
                it.minimumWidth = 0
                (it as? VerticalMenuItemView)?.collapse()
            }
        }
    }

    /**
     * Expand the menu items if orientationMode is VERTICAL otherwise, do nothing
     */
    fun expand() {
        isExpanded = true

        if (orientationMode == MenuOrientation.VERTICAL) {
            forEachChild {
                it.minimumWidth = minimumExpandedWidth
                (it as? VerticalMenuItemView)?.expand()
            }
        }
    }

    /**
     * Return a boolean if the menu is expanded on the VERTICAL orientationMode.
     *
     * @return true if expanded
     */
    fun isExpanded(): Boolean = when (orientationMode) {
        MenuOrientation.VERTICAL -> isExpanded
        MenuOrientation.HORIZONTAL -> false
    }

    /**
     * Return the selected menu item id
     *
     * @return menu item id or -1 if none item is selected
     */
    fun getSelectedItemId(): Int = getSelectedItem()?.id ?: -1

    /**
     * Remove the selected state from the current item and set the selected state to true
     * for the menu item with the [id]
     *
     * @param id menu item id
     * @param isSelected true if this view is selected, false otherwise
     * @param dispatchAction enable this action to dispatch listener events
     */
    fun setItemSelected(id: Int, isSelected: Boolean, dispatchAction: Boolean) {
        val selectedItem = getSelectedItem()

        when {
            isSelected && selectedItem?.id != id -> {
                selectedItem?.isSelected = false
                getItemById(id)?.let {
                    beginAnimation()
                    it.isSelected = true
                    if (dispatchAction) {
                        listener?.onItemSelected(id)
                    }
                }
            }
            !isSelected -> {
                getItemById(id)?.let {
                    beginAnimation()
                    it.isSelected = false
                }
            }
        }
    }

    /**
     * Return the current selected menu item
     *
     * @return the selected menu item view or null if none is selected
     */
    private fun getSelectedItem() = getChildren().firstOrNull { it.isSelected }

    /**
     * Return a menu item view with provided [id]
     *
     * @param id menu item id
     * @return the menu item view or null if the id was not found
     */
    private fun getItemById(id: Int) = getChildren()
        .filterIsInstance<MenuItemView>()
        .firstOrNull { it.id == id }

    /**
     * Create a menu item view based on the menu orientationMode
     *
     * @return a new [MenuItemView] instance
     */
    private fun createMenuItem(): MenuItemView = when (orientationMode) {
        MenuOrientation.HORIZONTAL -> HorizontalMenuItemView(context)
        MenuOrientation.VERTICAL -> VerticalMenuItemView(context)
    }

    private fun getHorizontalFlow() = Flow(context).apply {
        setOrientation(Flow.HORIZONTAL)
        setHorizontalStyle(Flow.CHAIN_SPREAD)
        setHorizontalAlign(Flow.HORIZONTAL_ALIGN_START)
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    private fun getVerticalFlow() = Flow(context).apply {
        setOrientation(Flow.VERTICAL)
        setHorizontalAlign(Flow.HORIZONTAL_ALIGN_START)
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun beginAnimation() {
        val transition = AutoTransition().apply {
            customAnimationDuration?.let(::setDuration)
        }
        TransitionManager.beginDelayedTransition(this, transition)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return State(superState, Bundle()).apply {
            menuId = menuRes
            selectedItem = getSelectedItemId()
            badges = badgesState
            expanded = isExpanded
            enabled = getChildren().map { it.id to it.isEnabled }.toMap()
            animationDuration = customAnimationDuration ?: -1
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is State -> {
                super.onRestoreInstanceState(state.superState)

                if (state.menuId != -1) {
                    setMenuResource(state.menuId)
                }

                if (state.selectedItem != -1) {
                    setItemSelected(
                        id = state.selectedItem,
                        isSelected = true,
                        dispatchAction = false
                    )
                }

                if (state.expanded) {
                    expand()
                } else {
                    collapse()
                }

                state.badges.forEach { (itemId, count) ->
                    if (count > 0) {
                        showBadge(itemId, count)
                    } else {
                        showBadge(itemId)
                    }
                }

                state.enabled.forEach { (itemId, isEnabled) ->
                    if (!isEnabled) {
                        getItemById(itemId)?.isEnabled = isEnabled
                    }
                }


                if (state.animationDuration >= 0) {
                    setDuration(state.animationDuration)
                }
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    /**
     * Interface definition for a callback to be invoked when a menu item is selected
     */
    interface OnItemSelectedListener {
        /**
         * Called when a item has been selected
         *
         * @param id menu item id
         */
        fun onItemSelected(id: Int)
    }

    enum class MenuOrientation {
        HORIZONTAL,
        VERTICAL
    }
}