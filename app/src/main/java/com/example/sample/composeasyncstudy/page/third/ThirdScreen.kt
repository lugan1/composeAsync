package com.example.sample.composeasyncstudy.page.third

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ThirdScreen(thirdViewModel: ThirdViewModel = viewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        var isSubscribe by remember { mutableStateOf(false) }

        Button(onClick = { isSubscribe = true }) {
            Text("start")
        }

        if(isSubscribe) {
            val number by thirdViewModel.observable.subscribeAsState(initial = 0)
            Text(text = "observable $number")
        }
    }
}
