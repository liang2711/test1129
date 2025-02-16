package com.zywl.test1229.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zywl.test1229.ui.files.files
import com.zywl.test1229.ui.home.HomeRoute
import com.zywl.test1229.ui.home.home
import com.zywl.test1229.ui.info.info

@Composable
fun SharedNavHost(
    startDestination: Any = HomeRoute
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        home(navController)
        info(navController)
        files(navController)
    }
}