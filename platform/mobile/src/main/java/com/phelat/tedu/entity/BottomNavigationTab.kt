package com.phelat.tedu.entity

import android.view.View
import androidx.navigation.NavController

data class BottomNavigationTab(
    val graph: Graph,
    val tabContainerView: View,
    val tabController: NavController
)