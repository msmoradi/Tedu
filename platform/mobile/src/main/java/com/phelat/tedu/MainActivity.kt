package com.phelat.tedu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.phelat.tedu.androiddagger.inject
import com.phelat.tedu.di.component.MobileComponent
import com.phelat.tedu.entity.BottomNavigationTab
import com.phelat.tedu.entity.Graph
import com.phelat.tedu.navigation.Navigation
import com.phelat.tedu.settings.entity.UserInterfaceMode
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var navigation: Navigation

    @Inject
    lateinit var graphs: MutableMap<Int, Graph>

    override fun onCreate(savedInstanceState: Bundle?) {
        val userInterfaceMode = when (UserInterfaceMode.currentUserInterfaceMode) {
            is UserInterfaceMode.DarkMode -> AppCompatDelegate.MODE_NIGHT_YES
            is UserInterfaceMode.LightMode -> AppCompatDelegate.MODE_NIGHT_NO
            is UserInterfaceMode.Automatic -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(userInterfaceMode)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inject<MobileComponent>()

        navigation = Navigation(
            bottomNavigationView,
            activity = this,
            tabs = listOf(
                BottomNavigationTab(
                    graph = requireNotNull(graphs[R.navigation.navigation_todolist]),
                    tabContainerView = findViewById(R.id.todoListTabContainer),
                    tabController = findNavController(R.id.todoListTab)
                ),
                BottomNavigationTab(
                    graph = requireNotNull(graphs[R.navigation.navigation_settings]),
                    tabContainerView = findViewById(R.id.settingsTabContainer),
                    tabController = findNavController(R.id.settingsTab)
                )
            ),
            graphs = graphs
        )
        navigation.initial(savedInstanceState)
    }

    override fun onBackPressed() {
        navigation.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigation.onSaveInstanceState(outState)
    }
}