package com.example.sample.composeasyncstudy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sample.composeasyncstudy.page.main.MainScreen
import com.example.sample.composeasyncstudy.page.second.SecondScreen
import com.example.sample.composeasyncstudy.page.third.ThirdScreen


@Composable
fun NavContainer() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "third") {
        composable("main") { MainScreen(navController = navController) }
        composable("second") { SecondScreen(navController = navController) }
        composable("third") { ThirdScreen() }
    }
}