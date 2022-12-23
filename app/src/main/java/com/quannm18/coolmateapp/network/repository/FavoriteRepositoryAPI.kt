package com.quannm18.coolmateapp.network.repository

import android.app.Application
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.network.api.ApiConfig
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth

class FavoriteRepositoryAPI(var application: Application) {
    companion object {
        private var instance: FavoriteRepositoryAPI? = null
        fun newInstance(application: Application): FavoriteRepositoryAPI {
            if (instance == null) {
                instance = FavoriteRepositoryAPI(application)
            }
            return instance!!
        }
    }

    suspend fun getFavoriteProduct(token: String) = ApiConfigWithAuth.apiService.getFavoriteProduct(token)
    suspend fun postFavorite(token: String, id: String) = ApiConfigWithAuth.apiService.postFavorite(token,id)
    suspend fun deleteFavoriteProduct(token: String, id: String) = ApiConfigWithAuth.apiService.deleteFavoriteProduct(token, id)
}