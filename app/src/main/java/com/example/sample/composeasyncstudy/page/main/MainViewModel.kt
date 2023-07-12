package com.example.sample.composeasyncstudy.page.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _textState = mutableStateOf("loading...")
    val textState: State<String> = _textState

    private val _textStateFlow = MutableStateFlow("loading...")
    val textStateFlow: StateFlow<String> = _textStateFlow

    val listFlow = MutableList(99) { i -> i }.asFlow().onEach { delay(2000L) }

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

    fun collectInViewModel() {
        viewModelScope.launch {
            listFlow.collect { _textStateFlow.emit("$it") }
        }
    }

}