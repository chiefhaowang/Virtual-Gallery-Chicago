package com.example.gallerychicago

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

data class NavBarItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
)

// NavBarConfig will initialize the icon and convert icon to vector image
object NavBarConfig {
    @Composable
    fun getNavBarItems(): List<NavBarItem> {
        val homeIcon: ImageVector = Icons.Filled.Home
        val accountIcon: ImageVector = Icons.Filled.AccountCircle

        // using vectorResource to convert the xml format icon to imageVector
        val reportIcon: ImageVector = ImageVector.vectorResource(id = R.drawable.svg_report)
        val artworkIcon: ImageVector = ImageVector.vectorResource(id = R.drawable.svg_gallery)

        return listOf(
            NavBarItem(
                label = "Home",
                icon = homeIcon,
                route = Routes.Home.value
            ),
            NavBarItem(
                label = "Artwork",
                icon = artworkIcon,
                route = Routes.Exhibition.value
            ),
            NavBarItem(
                label = "Report",
                icon = reportIcon,
                route = Routes.ReportScreen.value
            ),
            NavBarItem(
                label = "Profile",
                icon = accountIcon,
                route = Routes.UserProfile.value
            )
        )
    }
}
