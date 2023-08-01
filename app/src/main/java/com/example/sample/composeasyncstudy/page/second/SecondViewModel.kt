package com.example.sample.composeasyncstudy.page.second

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class SecondViewModel: ViewModel() {
    val _nums = mutableStateListOf<Int>()

    fun startScan() {
        val list = listOf<Int>(1,2,3,4,5,6)
        val observable = Observable.fromIterable(list)
        val subscribe = observable
            .throttleFirst(1000L, TimeUnit.MILLISECONDS)
            .subscribe {
            _nums.add(it)
        }
    }
}