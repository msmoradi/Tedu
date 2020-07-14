package com.phelat.tedu.entity

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes

data class Graph(
    @NavigationRes val graphId: Int,
    @IdRes val startDestination: Int
)