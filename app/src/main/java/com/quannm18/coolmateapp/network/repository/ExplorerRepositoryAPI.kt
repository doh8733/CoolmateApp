package com.quannm18.coolmateapp.network.repository

import android.app.Application
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth

class ExplorerRepositoryAPI(var application: Application) {
    companion object {
        private var instance: ExplorerRepositoryAPI? = null
        fun newInstance(application: Application): ExplorerRepositoryAPI {
            if (instance == null) {
                instance = ExplorerRepositoryAPI(application)
            }
            return instance!!
        }
    }

    suspend fun getExplorer() = ApiConfig.apiService.getExplorer()
    suspend fun getListBanner() = ApiConfig.apiService.getListBanner()
}