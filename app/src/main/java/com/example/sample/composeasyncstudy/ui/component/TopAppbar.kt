package com.example.sample.composeasyncstudy.ui.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, title: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if(navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = "메뉴")
                }
            }
        })
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(navController = rememberNavController(), "테스트 타이틀")
}