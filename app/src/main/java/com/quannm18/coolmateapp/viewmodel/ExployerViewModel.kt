package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.database.repository.UserDBRepository
import com.quannm18.coolmateapp.model.user.UserInfo
import com.quannm18.coolmateapp.network.repository.ExplorerRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExplorerViewModel(application: Application, handle: SavedStateHandle) : BaseViewModel(
    application,
    handle
) {
    private val explorerRepositoryAPI : ExplorerRepositoryAPI = ExplorerRepositoryAPI.newInstance(application)

    fun getExplorer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(explorerRepositoryAPI.getExplorer()))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }
}