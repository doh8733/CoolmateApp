package com.quannm18.coolmateapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application, private val handle: SavedStateHandle) :
    AndroidViewModel(application) {
    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    /*Save data*/
    fun saveDataStore() {

    }

    fun restoreData() {

    }
    /**/

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}