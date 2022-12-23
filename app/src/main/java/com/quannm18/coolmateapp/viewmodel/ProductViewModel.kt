package com.quannm18.coolmateapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.quannm18.coolmateapp.base.BaseViewModel
import com.quannm18.coolmateapp.model.filter.GetFilter
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import com.quannm18.coolmateapp.network.repository.ExplorerRepositoryAPI
import com.quannm18.coolmateapp.network.repository.FavoriteRepositoryAPI
import com.quannm18.coolmateapp.network.repository.ProductRepositoryAPI
import com.quannm18.coolmateapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class ProductViewModel(application: Application, handle: SavedStateHandle) : BaseViewModel(
    application,
    handle
) {
    private val explorerRepositoryAPI: ExplorerRepositoryAPI =
        ExplorerRepositoryAPI.newInstance(application)
    private val productRepositoryAPI: ProductRepositoryAPI =
        ProductRepositoryAPI.newInstance(application)
    private val favoriteRepositoryAPI: FavoriteRepositoryAPI =
        FavoriteRepositoryAPI.newInstance(application)

    fun getExplorer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(explorerRepositoryAPI.getExplorer()))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun postFavoriteProduct(token: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.postFavorite(token, id)))
        } catch (e: Exception) {
            println(e.stackTrace)
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
    }

    fun getAllProductsFromApi(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ApiConfigWithAuth.apiService.getProduct(token)))
        } catch (e: Exception) {
            println(e.stackTrace)
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
    }

    fun getDataFilter(authToken: String, getFilter: GetFilter) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productRepositoryAPI.getDataFilter(authToken, getFilter)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun getFavoriteProduct(authToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(favoriteRepositoryAPI.getFavoriteProduct(authToken)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }


    fun postFavorite(authToken: String, productId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(favoriteRepositoryAPI.postFavorite(authToken, productId)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun deleteFavoriteProduct(authToken: String, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(favoriteRepositoryAPI.deleteFavoriteProduct(authToken, id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "null"))
        }
    }


    fun getProduct(authToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productRepositoryAPI.getProduct(authToken)))
        } catch (e: Exception) {
            Log.e("err0r", "getProduct: ${e.stackTrace}")
            emit(Resource.error(null, e.message ?: "null"))
        }
    }

    fun getDataFinding(token: String,productName : String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = productRepositoryAPI.getDataFinding(authToken = token, productName = productName)))
        }catch (e :Exception){
            println(e.stackTrace)
            emit(Resource.error(data = null, message = e.message ?: "ERROR"))
        }
    }


}