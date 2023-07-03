package com.example.sample.composeasyncstudy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.CancellationException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScreen()
        }
    }
}

@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
    val name: String by mainViewModel.name.observeAsState("")
    HelloContent(name = name, onNameChange = { mainViewModel.onNameChange(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(mainViewModel: MainViewModel = viewModel()) {
    val snackBarHostState = remember { SnackbarHostState() }

    val isError by mainViewModel.error.observeAsState(false)

    if(isError) {
        //에러 발생시 스낵바 호출, CoroutineScope를 통해 코루틴을 실행한다.
        LaunchedEffect(isError) {
            try {
                snackBarHostState.showSnackbar(
                    message = "Error message",
                    actionLabel = "Retry message"
                )
            } catch (e: CancellationException) {
                Log.e("composeTest", "canceled!!")
            }
        }
    }
    
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Box(modifier = Modifier.padding(it)) {
            MainScreen()
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(private val savedInstanceState: SavedStateHandle) : ViewModel() {
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    //text 를 입력받아 TextView의 문구를 변경한다.
    //입력받은 text가 "error"일 경우 error LiveData를 true로 변경한다.
    fun onNameChange(newName: String) {
        _name.value = newName
        _error.value = newName == "error"
    }
}