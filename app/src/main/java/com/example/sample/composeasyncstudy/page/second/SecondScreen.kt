package com.example.sample.composeasyncstudy.page.second

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sample.composeasyncstudy.ui.component.TopBar
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(navController: NavController, secondViewModel: SecondViewModel  = viewModel()) {
    Scaffold(topBar = { TopBar(navController = navController, title = "second page") }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "second page")
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { secondViewModel.startScan() }) {
                Text(text = "스캔시작")
            }
            Spacer(modifier = Modifier.height(20.dp))

            ScanResultColumn(nums = secondViewModel._nums)
        }
    }
}

@Composable
fun ScanResultColumn(nums : List<Int>) {
    LazyColumn {
        itemsIndexed(nums) { index, item ->
            Text(text = "index: $index, item: $item")
        }
    }
}

@Composable
fun ObservableListScreen() {
    var list by remember { mutableStateOf(listOf<Int>()) }
    val disposable = remember { CompositeDisposable() }

    DisposableEffect(Unit) {
        onDispose {
            disposable.clear()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            val observable = Observable.fromIterable(listOf(1,2,3,4,5,6))
            val subscriber = observable
                .zipWith(Observable.interval(3, TimeUnit.SECONDS), { item, _ -> item })
                .subscribe{
                list = list + it
            }
        }) {
            Text("Add Items")
        }

        LazyColumn{
            itemsIndexed(list) { index, item ->
                Text(text = "index: $index, item: $item")
            }
        }
    }
}

@Preview
@Composable
fun PreviewObservableListScreen() {
    ObservableListScreen()
}