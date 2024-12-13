package com.reachmobi.news.presentation.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Headlines",
                icon = Icons.Filled.Newspaper,
                route = Screen.Main.route
            ),
            BottomNavigationItem(
                label = "Favourites",
                icon = Icons.Filled.Favorite,
                route = Screen.Favourite.route
            ),
        )
    }
}