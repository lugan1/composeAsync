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


        // 패턴1: Compose 내부에서 `LaunchedEffect`를 사용하여 `StateFlow`를 수집
        // Compose의 생명주기에 따라, 이 LaunchedEffect는 Compose가 구성 해제되거나 Compose가 백스택으로 전환되면 취소됩니다.
        // 따라서, 앱이 백그라운드로 전환되더라도 이 작업은 메모리 누수를 발생시키지 않습니다.
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

        // 패턴2: `viewModelScope`를 사용하여 `StateFlow`를 수집
        // ViewModel의 생명주기에 따라, ViewModel이 클리어되기 전까지 이 작업은 계속 실행됩니다.
        // 따라서 Compose가 구성 해제되더라도, 이 코루틴은 계속 실행됩니다.
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


    // 패턴1: textStateFlow의 변경은 viewModelScope 내부에서 이루어지지만, 실제 수집은 Compose에서 발생
    // 이 함수는 Compose 구성 해제 또는 Compose가 백스택으로 전환될 때에도 영향받지 않습니다.
    // ViewModel이 클리어되기 전까지 계속 실행됩니다.
    fun emitFlow() {
        viewModelScope.launch {
            listOf("one", "two", "three", "four", "five", "six")
                .asFlow()
                .onEach { delay(2000L) }
                .collect{ _textStateFlow.emit("$it (after 2 second)") }
        }
    }


    // 패턴2: collectInViewModel에서 listFlow를 직접 수집하고, textState2를 업데이트
    // 이 함수는 Compose 구성 해제 또는 Compose가 백스택으로 전환될 때에도 영향받지 않습니다.
    // ViewModel이 클리어되기 전까지 계속 실행됩니다.
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

