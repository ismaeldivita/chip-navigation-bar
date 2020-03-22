package com.ismaeldivita.chipnavigation.model

import androidx.annotation.ColorInt
import androidx.annotation.IdRes

class MenuStyle(
    @ColorInt val badgeColor: Int,
    @ColorInt val unselectedColor: Int,
    @IdRes val textAppearance: Int?,
    val radius: Float
)