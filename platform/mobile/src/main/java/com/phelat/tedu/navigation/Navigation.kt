package com.phelat.tedu.navigation

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavInflater
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.phelat.tedu.R
import com.phelat.tedu.entity.BottomNavigationTab
import com.phelat.tedu.entity.Graph

class Navigation(
    private val bottomNavigationView: BottomNavigationView,
    private val activity: FragmentActivity,
    private val graphs: Map<Int, Graph>,
    private val tabs: List<BottomNavigationTab>
) : BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private val assignedDestinationChanger = hashSetOf<Int>()

    private var currentTabIndex: Int = 0
    private var currentController: NavController? = null

    private val onNavigatedListener = NavController.OnDestinationChangedListener { controller, destination, _ ->
        if (destination.id == R.id.emptyFragment) {
            val id = getCurrentTabId()
            val navHostFragment = requireNotNull(
                activity.supportFragmentManager.findFragmentById(id)
            )
            integrateWithOtherGraphs(controller, navHostFragment.childFragmentManager, id)
            controller.navigate(tabs[currentTabIndex].graph.startDestination)
        }
    }

    init {
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemReselectedListener(this)
    }

    fun initial(savedInstanceState: Bundle?) {
        val tabIndex = getLatestTabIndex(savedInstanceState)
        switchTab(tabIndex)
    }

    private fun getLatestTabIndex(savedInstanceState: Bundle?): Int {
        val restoredTab = savedInstanceState?.getInt(CURRENT_TAB_INDEX_KEY, -1) ?: -1
        return if (restoredTab in 0..tabs.size) {
            restoredTab
        } else {
            DEFAULT_TAB_INDEX
        }
    }

    private fun switchTab(tabIndex: Int) {
        val previousTabIndex = currentTabIndex
        currentTabIndex = tabIndex
        bottomNavigationView.menu.findItem(tabIndex)?.isChecked = true
        currentController = tabs[tabIndex].tabController.apply {
            if (assignedDestinationChanger.contains(tabIndex).not()) {
                addOnDestinationChangedListener(onNavigatedListener)
                assignedDestinationChanger.add(tabIndex)
            }
        }
        if (previousTabIndex != tabIndex) {
            tabs[previousTabIndex].tabContainerView.isInvisible = true
        }
        tabs[tabIndex].tabContainerView.isVisible = true
    }

    fun onBackPressed() {
        currentController?.apply {
            if (currentDestination?.id == tabs[currentTabIndex].graph.startDestination) {
                if (currentTabIndex == DEFAULT_TAB_INDEX) {
                    activity.finish()
                } else {
                    switchTab(DEFAULT_TAB_INDEX)
                }
            } else {
                navigateUp()
            }
        } ?: run {
            activity.finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedTab = getTabIndexFromMenuItem(item)
        switchTab(selectedTab)
        return true
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_TAB_INDEX_KEY, currentTabIndex)
    }

    private fun integrateWithOtherGraphs(
        controller: NavController,
        childFragmentManager: FragmentManager,
        containerId: Int
    ) {
        val navProvider = NavigatorProvider().apply {
            addNavigator(NavGraphNavigator(this))
            addNavigator(ActivityNavigator(activity))
            addNavigator(DialogFragmentNavigator(activity, childFragmentManager))
            addNavigator(FragmentNavigator(activity, childFragmentManager, containerId))
        }
        val navInflater = NavInflater(activity, navProvider)
        val controllerGraph = controller.graph
        for (graph in graphs) {
            val inflatedGraph = navInflater.inflate(graph.value.graphId)
            controllerGraph.addAll(inflatedGraph)
        }
        controllerGraph.startDestination = R.id.emptyFragment
        controller.graph = controllerGraph
    }

    private fun getCurrentTabId(): Int {
        return when (currentTabIndex) {
            0 -> R.id.todoListTab
            1 -> R.id.settingsTab
            else -> throw IllegalStateException("Unknown tab index $currentTabIndex")
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        val startDest = tabs[getTabIndexFromMenuItem(item)].graph.startDestination
        currentController?.popBackStack(startDest, false)
    }

    private fun getTabIndexFromMenuItem(item: MenuItem): Int {
        for (i in 0 until bottomNavigationView.menu.size()) {
            if (item.itemId == bottomNavigationView.menu[i].itemId) {
                return i
            }
        }
        throw IllegalArgumentException("Unknown menu")
    }

    companion object {
        private const val DEFAULT_TAB_INDEX = 0
        private const val CURRENT_TAB_INDEX_KEY = "currentTabIndex"
    }
}