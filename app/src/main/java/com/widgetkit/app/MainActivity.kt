package com.widgetkit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.widgetkit.core.ui.theme.WidgetKitTheme
import com.widgetkit.feature.gallery.GalleryScreen
import com.widgetkit.feature.mywidgets.MyWidgetsScreen
import com.widgetkit.widget.clock.ClockTickReceiver
import com.widgetkit.widget.countdown.CountdownWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ClockTickReceiver.schedule(this)
        CountdownWorker.enqueue(this)

        setContent {
            WidgetKitTheme {
                WidgetKitApp()
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Gallery : BottomNavItem("gallery", "Gallery", Icons.Outlined.Dashboard)
    data object MyWidgets : BottomNavItem("my_widgets", "My Widgets", Icons.Outlined.Widgets)
}

val bottomNavItems = listOf(BottomNavItem.Gallery, BottomNavItem.MyWidgets)

@androidx.compose.runtime.Composable
fun WidgetKitApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Gallery.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(BottomNavItem.Gallery.route) {
                GalleryScreen(
                    onConfigure = { widgetType ->
                        navController.navigate("config/$widgetType")
                    }
                )
            }
            composable(BottomNavItem.MyWidgets.route) {
                MyWidgetsScreen(
                    onEdit = { widgetId, widgetType ->
                        navController.navigate("config/$widgetType/$widgetId")
                    }
                )
            }
            composable("config/{widgetType}/{widgetId?}") { backStackEntry ->
                val widgetType = backStackEntry.arguments?.getString("widgetType") ?: return@composable
                val widgetId = backStackEntry.arguments?.getString("widgetId")
                com.widgetkit.feature.config.ConfigScreen(
                    widgetType = widgetType,
                    existingWidgetId = widgetId,
                    onBack = { navController.popBackStack() },
                    onPlaced = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
