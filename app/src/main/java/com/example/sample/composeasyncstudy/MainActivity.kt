package com.example.sample.composeasyncstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}



@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        val text by remember { mainViewModel.textState }
        var textFlow by remember { mutableStateOf("") }
        val text2 by remember { mainViewModel.textState2 }

        LaunchedEffect(Unit) {
            mainViewModel.suspendFunction("hello World!")
        }

        LaunchedEffect(key1 = mainViewModel) {
            mainViewModel.textStateFlow.collect {
                textFlow = it
            }
        }

        Text(text = text)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { mainViewModel.suspendFunction("changeEvent") }) {
            Text(text = "Click me (suspend)")
        }

        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().border(border = BorderStroke(width = 16.dp, color = Color.Black)))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = textFlow)
        Button(onClick = { mainViewModel.emitFlow() }) {
            Text(text = "Click me (stateFlow collect in view)")
        }

        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().border(border = BorderStroke(width = 16.dp, color = Color.Black)))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text2)
        Button(onClick = { mainViewModel.collectInViewModel() }) {
            Text(text = "Click me (flow collect in viewModel)")
        }
    }
}

class MainViewModel: ViewModel() {
    private val _textState = mutableStateOf("loading...")
    val textState: State<String> = _textState

    private val _textStateFlow = MutableStateFlow("loading...")
    val textStateFlow = _textStateFlow.asStateFlow()

    private val _textState2 = mutableStateOf("loading...")
    val textState2: State<String> = _textState2

    init {
        viewModelScope.launch {
            suspendFunction("hello World!")
        }
    }

    fun suspendFunction(value: String) {
        viewModelScope.launch {
            delay(5000L)
            _textState.value = "$value (after 2 second)"
        }
    }

    fun emitFlow() {
        viewModelScope.launch {
            listOf("one", "two", "three", "four", "five", "six")
                .asFlow()
                .onEach { delay(2000L) }
                .collect{ _textStateFlow.emit("$it (after 2 second)") }
        }
    }

    fun collectInViewModel() {
        val listFlow = listOf("one", "two", "three", "four", "five", "six").asFlow()

        viewModelScope.launch {
            listFlow.onEach { delay(2000L) }
                .collect{ _textState2.value = "$it (after 2 second)" }
        }
    }
}



@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}

