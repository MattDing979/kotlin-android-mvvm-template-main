package com.debk007.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.debk007.template.navigation.Detail
import com.debk007.template.navigation.Home
import com.debk007.template.navigation.NavGraph
import com.debk007.template.presentation.composable.BottomBar
import com.debk007.template.presentation.composable.BottomBarItem
import com.debk007.template.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Home",
            icon = Icons.Default.Home,
            route = Home
        ),
        BottomBarItem(
            title = "Detail",
            icon = Icons.Default.Menu,
            route = Detail(id = 110)
        )
    )

    Scaffold(
        bottomBar = { BottomBar(navController = navController, items = bottomBarItems) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavGraph(navController = navController)
        }
    }
}