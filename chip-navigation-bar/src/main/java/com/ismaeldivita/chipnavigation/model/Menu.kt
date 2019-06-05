package com.ismaeldivita.chipnavigation.model

import androidx.annotation.ColorInt

internal data class Menu(
    val items: List<MenuItem>,
    @ColorInt val badgeColor: Int,
    @ColorInt val disabledColor: Int,
    @ColorInt val unselectedColor: Int
)