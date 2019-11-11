package com.ismaeldivita.chipnavigation

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import kotlinx.android.parcel.Parcelize

internal class State : View.BaseSavedState {

    @Parcelize
    private class BadgeState(val itemId: Int, val count: Int) : Parcelable

    private var bundle: Bundle? = null

    var menuId: Int
        get() = bundle?.getInt(MENU_ID) ?: -1
        set(value) {
            bundle?.putInt(MENU_ID, value)
        }

    var selectedItem: Int
        get() = bundle?.getInt(SELECTED_ITEM) ?: -1
        set(value) {
            bundle?.putInt(SELECTED_ITEM, value)
        }

    var badges: Map<Int, Int>
        get() = bundle?.getParcelableArrayList<BadgeState>(BADGES)
            ?.associate { it.itemId to it.count }
            ?: emptyMap()

        set(value) {
            val badgeStates = value.map { (item, count) -> BadgeState(item, count) }
            bundle?.putParcelableArrayList(BADGES, ArrayList(badgeStates))
        }

    var expanded: Boolean
        get() = bundle?.getBoolean(EXPANDED) ?: false
        set(value) {
            bundle?.putBoolean(EXPANDED, value)
        }

    constructor(superState: Parcelable?) : super(superState)

    constructor(superState: Parcelable?, bundle: Bundle) : super(superState) {
        this.bundle = bundle
    }

    constructor(source: Parcel, loader: ClassLoader?) : super(source) {
        bundle = source.readBundle(loader)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeBundle(bundle)
    }

    companion object {
        private const val SELECTED_ITEM = "selectedItem"
        private const val BADGES = "badges"
        private const val MENU_ID = "menuId"
        private const val EXPANDED = "expanded"

        @JvmField
        val CREATOR = object : Parcelable.ClassLoaderCreator<State> {

            override fun createFromParcel(source: Parcel) =
                State(source, null)

            override fun createFromParcel(
                source: Parcel,
                loader: ClassLoader
            ) = State(source, loader)

            override fun newArray(size: Int): Array<State> = arrayOf()
        }
    }

}