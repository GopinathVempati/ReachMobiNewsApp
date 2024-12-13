package com.reachmobi.news

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.reachmobi.news.presentation.ui.nav.BottomNavigationBar
import com.reachmobi.news.presentation.ui.theme.NewsAppTheme
import com.reachmobi.news.presentation.viewmodels.NewsViewModel
import com.reachmobi.news.utils.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(this)

        setContent {
            NewsAppTheme {
                val viewModel: NewsViewModel = hiltViewModel()
                BottomNavigationBar(viewModel = viewModel)
            }
        }
    }
}
