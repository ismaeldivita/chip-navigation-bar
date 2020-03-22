package com.ismaeldivita.chipnavigation.model

import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import com.ismaeldivita.chipnavigation.R

class MenuStyle(context: Context, attr: TypedArray) {

    @ColorInt val badgeColor: Int
    @ColorInt val unselectedColor: Int
    @IdRes val textAppearance: Int?
    val radius: Float
    val iconSize: Int

    init {
        textAppearance = attr.getResourceId(R.styleable.ChipNavigationBar_cnb_textAppearance, -1)
            .takeIf { it > 0 }

        radius = attr.getDimension(R.styleable.ChipNavigationBar_cnb_radius, Float.MAX_VALUE)
        badgeColor = attr.getColor(R.styleable.ChipNavigationBar_cnb_badgeColor,
            ContextCompat.getColor(context, R.color.cnb_default_badge_color)
        )
        unselectedColor = attr.getColor(
            R.styleable.ChipNavigationBar_cnb_unselectedColor,
            ContextCompat.getColor(context, R.color.cnb_default_unselected_color)
        )
        iconSize = attr.getDimension(
            R.styleable.ChipNavigationBar_cnb_iconSize,
            context.resources.getDimension(R.dimen.cnb_icon_size)
        ).toInt()
    }
}