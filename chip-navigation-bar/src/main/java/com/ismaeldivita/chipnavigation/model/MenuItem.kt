package com.ismaeldivita.chipnavigation.model

import android.graphics.PorterDuff
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

internal data class MenuItem(
    val id: Int,
    val title: CharSequence,
    @DrawableRes val icon: Int,
    val enabled: Boolean,
    val tintMode: PorterDuff.Mode?,
    @ColorInt val iconColor: Int,
    @ColorInt val textColor: Int,
    @ColorInt val backgroundColor: Int,
    @ColorInt val badgeColor: Int = 0,
    @ColorInt val disabledColor: Int = 0,
    @ColorInt val unselectedColor: Int = 0
)