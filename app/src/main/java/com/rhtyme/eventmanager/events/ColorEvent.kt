package com.rhtyme.eventmanager.events

import android.support.annotation.ColorInt

data class ColorEvent(@ColorInt var colorCode: Int = 0, var colorName: String = "", var fromCache: Boolean = false)