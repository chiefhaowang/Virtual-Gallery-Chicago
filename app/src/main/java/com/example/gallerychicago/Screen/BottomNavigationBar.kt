package com.example.gallerychicago.Screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Data.UserViewModel


@Composable
fun BottomNavigationBar(viewModel: ArtworkViewModel, userViewModel: UserViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation (backgroundColor= MaterialTheme.colorScheme.primary){
                val navBackStackEntry by
                navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavBarConfig.getNavBarItems().forEach { navItem ->
                    BottomNavigationItem(
                        icon = { Icon(navItem.icon, contentDescription =
                        null) },
                        label = { Text(navItem.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == navItem.route
                        } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                // Clearing everything up to the home route to avoid stack accumulation
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = "loginScreen",
            //startDestination = "FavouriteList",
            Modifier.padding(paddingValues)
        ) {
            composable(Routes.Home.value) {
                Home(navController)
            }
            composable(Routes.Exhibition.value) {
                Exhibition(navController, viewModel)
            }
            composable(Routes.ReportScreen.value)
            {
                ReportScreen(navController)
            }
            composable(Routes.UserProfile.value)
            {
                UserProfile(navController)
            }
            // Jump between pages
            composable("FavouriteList")
            {
                DisplayFavouriteList(navController, userViewModel)
            }
            composable("loginScreen") {
                LoginScreen(navController, userViewModel, viewModel)
            }
            composable("Registration") {
                Registration(userViewModel, navController)
            }
            //after successful login in
            composable(
                "imageDetails/{imageId}",
                arguments = listOf(navArgument("imageId") { type = NavType.IntType })
            ) { backStackEntry ->
                val imageId = backStackEntry.arguments?.getInt("imageId") ?: 0
                DisplayArtworkDetails(imageId, navController)
            }
//            composable(
//                "imageDetails/{imageId}",
//                arguments = listOf(navArgument("imageId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                val imageId = backStackEntry.arguments?.getInt("imageId") ?: 27992
//                println("Image ID in navigation is $imageId")
//                DisplayArtworkDetails(imageId)
//            }

        }
    }
}