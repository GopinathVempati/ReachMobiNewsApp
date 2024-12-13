package com.reachmobi.news.presentation.ui.nav

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Favourite : Screen("favourite")
}
