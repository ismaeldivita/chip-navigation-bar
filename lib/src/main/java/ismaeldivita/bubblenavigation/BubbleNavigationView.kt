package ismaeldivita.bubblenavigation

import android.content.Context
import android.transition.ChangeBounds
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.MenuRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ismaeldivita.bubblenavigation.behavior.HideOnScrollBehavior
import ismaeldivita.bubblenavigation.model.MenuParser
import ismaeldivita.bubblenavigation.util.getChildren
import ismaeldivita.bubblenavigation.view.HorizontalMenuItemView
import android.view.View
import ismaeldivita.bubblenavigation.util.applyWindowInsets
import ismaeldivita.bubblenavigation.util.beginDelayedTransitionOnParent
import ismaeldivita.bubblenavigation.util.forEachChild
import ismaeldivita.bubblenavigation.view.MenuItemView
import ismaeldivita.bubblenavigation.view.VerticalMenuItemView
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class BubbleNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), CoordinatorLayout.AttachedBehavior {

    private lateinit var orientationMode: MenuOrientation
    private val behavior: HideOnScrollBehavior
    private var listener: OnItemSelectedListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BubbleNavigationView)

        val menuResource = a.getResourceId(R.styleable.BubbleNavigationView_bnv_menuResource, -1)
        val hideOnScroll = a.getBoolean(R.styleable.BubbleNavigationView_bnv_hideOnScroll, false)
        val leftInset = a.getBoolean(R.styleable.BubbleNavigationView_bnv_addLeftInset, false)
        val topInset = a.getBoolean(R.styleable.BubbleNavigationView_bnv_addTopInset, false)
        val rightInset = a.getBoolean(R.styleable.BubbleNavigationView_bnv_addRightInset, false)
        val bottomInset = a.getBoolean(R.styleable.BubbleNavigationView_bnv_addBottomInset, false)
        val orientation = when (a.getInt(R.styleable.BubbleNavigationView_bnv_orientationMode, 0)) {
            0 -> MenuOrientation.HORIZONTAL
            1 -> MenuOrientation.VERTICAL
            else -> throw IllegalArgumentException()
        }

        a.recycle()

        behavior = HideOnScrollBehavior(context, attrs)
        setMenuOrientation(orientation)

        if (menuResource >= 0) {
            setMenuResource(menuResource)
        }

        setHideOnScroll(hideOnScroll)
        applyWindowInsets(leftInset, topInset, rightInset, bottomInset)

        isClickable = true
    }

    /**
     * Inflate a menu from the specified XML resource
     *
     * @param menuRes Resource ID for an XML layout resource to load
     */
    fun setMenuResource(@MenuRes menuRes: Int) {
        val menu = (MenuParser(context).parse(menuRes))
        val childListener: (View) -> Unit = { view ->
            val id = view.id
            setItemSelected(id)
            listener?.onItemSelected(id)
        }

        removeAllViews()

        menu.items.forEach {
            val itemView = createMenuItem().apply {
                bind(it)
                setOnClickListener(childListener)
            }
            addView(itemView)
        }
    }

    /**
     * Set the menu orientation
     *
     * @param mode orientation
     */
    fun setMenuOrientation(menuOrientation: MenuOrientation) {
        orientationMode = menuOrientation
        orientation = when (menuOrientation) {
            MenuOrientation.HORIZONTAL -> HORIZONTAL
            MenuOrientation.VERTICAL -> VERTICAL
        }
        setHideOnScroll(behavior.scrollEnabled)
    }

    /**
     * Set the enabled state of the menu item if the provided [id] was found
     *
     * @param id menu item id
     * @param isEnabled true if this view is enabled, false otherwise
     */
    fun setItemEnabled(id: Int, isEnabled: Boolean) {
        getItemById(id)?.isEnabled = isEnabled
    }

    /**
     * Remove the selected state from the current item and set the selected state to true
     * of the menu item for the [id]
     *
     * @param id menu item id
     */
    fun setItemSelected(id: Int) {
        val selectedItem = getSelectedItem()

        if (selectedItem?.id != id) {
            selectedItem?.isSelected = false
            getItemById(id)?.isSelected = true
        }
    }

    /**
     * Set the enabled state for the hide on scroll [CoordinatorLayout.Behavior].
     * The behavior is only active when orientation orientationMode is HORIZONTAL
     *
     * @param isEnabled True if this view is enabled, false otherwise
     */
    fun setHideOnScroll(isEnabled: Boolean) {
        behavior.scrollEnabled = isEnabled && orientationMode == MenuOrientation.HORIZONTAL
    }

    /**
     * Set the duration of the enter animation for the hide on scroll [CoordinatorLayout.Behavior]
     * Default value [HideOnScrollBehavior.DEFAULT_ENTER_DURATION]
     * The behavior is only active when orientation orientationMode is HORIZONTAL
     *
     * @param duration animation duration in milliseconds
     */
    fun setEnterAnimationDuration(duration: Long) {
        behavior.enterAnimationDuration = duration
    }

    /**
     * Set the duration of the exit animation for the hide on scroll [CoordinatorLayout.Behavior]
     * Default value [HideOnScrollBehavior.DEFAULT_EXIT_DURATION]
     * The behavior is only active when orientation is HORIZONTAL
     *
     * @param duration animation duration in milliseconds
     */
    fun setExitAnimationDuration(duration: Long) {
        behavior.exitAnimationDuration = duration
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
     * Show menu if the orientationMode is HORIZONTAL otherwise, do nothing
     */
    fun show() {
        if (orientationMode == MenuOrientation.HORIZONTAL) {
            behavior.slideUp(this)
        }
    }

    /**
     * Hide menu if the orientationMode is HORIZONTAL otherwise, do nothing
     */
    fun hide() {
        if (orientationMode == MenuOrientation.HORIZONTAL) {
            behavior.slideDown(this)
        }
    }

    /**
     * Collapse the menu items if orientationMode is VERTICAL otherwise, do nothing
     */
    fun collapse() {
        if (orientationMode == MenuOrientation.VERTICAL) {
            forEachChild {
                (it as? VerticalMenuItemView)?.collapse()
            }
        }
    }

    /**
     * Expand the menu items if orientationMode is VERTICAL otherwise, do nothing
     */
    fun expand() {
        if (orientationMode == MenuOrientation.VERTICAL) {
            forEachChild {
                (it as? VerticalMenuItemView)?.expand()
            }
        }
    }

    /**
     * Return the selected menu item id
     *
     * @return menu item id or -1 if none item is selected
     */
    fun getSelectedItemId(): Int = getSelectedItem()?.id ?: -1

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
    private fun getItemById(id: Int) = getChildren().firstOrNull { it.id == id }

    /**
     * Create a menu item view based on the menu orientationMode
     *
     * @return a new [MenuItemView] instance
     */
    private fun createMenuItem(): MenuItemView = when (orientationMode) {
        MenuOrientation.HORIZONTAL -> HorizontalMenuItemView(context)
        MenuOrientation.VERTICAL -> VerticalMenuItemView(context)
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> = behavior

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