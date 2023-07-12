package com.example.sample.composeasyncstudy.page.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        val viewModelText by remember { mainViewModel.textState }
        var composeText by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            mainViewModel.suspendFunction("hello World!")
        }


        Button(onClick = { navController.navigate("second") }) {
            Text(text = "두번째 페이지 이동")
        }
        Spacer(modifier = Modifier.height(32.dp))


        Text(text = viewModelText)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { mainViewModel.suspendFunction("changeEvent") }) {
            Text(text = "suspend in LaunchedEffect")
        }


        // 패턴1: Compose 내부에서 `LaunchedEffect`를 사용하여 `StateFlow`를 수집
        LaunchedEffect(key1 = mainViewModel) {
            mainViewModel.textStateFlow.collect {
                composeText = it
            }
        }
        Divider()
        Text(text = composeText)
        Button(onClick = { mainViewModel.collectInViewModel() }) {
            Text(text = "stateFlow collect in LaunchedEffect")
        }

        // 패턴2: `viewModelScope`를 사용하여 `StateFlow`를 수집
        Divider()
        Text(text = viewModelText)
        Button(onClick = { mainViewModel.collectInViewModel() }) {
            Text(text = "flow collect in viewModel")
        }


        // 패턴3: collectAsState 사용
        val textState by mainViewModel.textStateFlow.collectAsState()
        Divider()
        Text(text = textState)
        Button(onClick = { mainViewModel.collectInViewModel() }) {
            Text(text = "collectAsState")
        }



        // 패턴4: collectAsStateWithLifecycle 사용
        val textState2 by mainViewModel.textStateFlow.collectAsStateWithLifecycle()
        Divider()
        Text(text = textState2)
        Button(onClick = { mainViewModel.collectInViewModel() }) {
            Text(text = "collectAsStateWithLifecycle")
        }
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier
        .height(16.dp)
        .fillMaxWidth()
        .border(border = BorderStroke(width = 16.dp, color = Color.Black)))
    Spacer(modifier = Modifier.height(16.dp))
}