package com.example.sample.composeasyncstudy.page.third

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ThirdViewModel: ViewModel() {
    val observable: Observable<Int> = Observable.fromIterable(listOf(1,2,3,4,5,6))
        .zipWith(Observable.interval(3, TimeUnit.SECONDS)) { item, _ -> item }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun streamStart() {

    }
}