package com.quannm18.coolmateapp.network.repository

import android.app.Application
import com.quannm18.coolmateapp.model.filter.GetFilter
import com.quannm18.coolmateapp.network.api.ApiConfigWithAuth
import retrofit2.http.Header

class ProductRepositoryAPI(var application: Application) {
    companion object {
        private var instance: ProductRepositoryAPI? = null
        fun newInstance(application: Application): ProductRepositoryAPI {
            if (instance == null) {
                instance = ProductRepositoryAPI(application)
            }
            return instance!!
        }
    }

    suspend fun getProduct(authToken: String) = ApiConfigWithAuth.apiService.getProduct(authToken)
    suspend fun getProductByID(authToken: String, id: String) =
        ApiConfigWithAuth.apiService.getProductByID(authToken, id)

    suspend fun getDataFilter(authToken: String, it: GetFilter) =
        ApiConfigWithAuth.apiService.getDataFilter(authToken, productName = it.productName,
        priceTo = it.priceTo,
        priceFrom = it.priceFrom,
        style = it.style,
        catalog = it.catalog,
        material = it.material,
        purpose = it.purpose,
        feature = it.feature)
    suspend fun getDataFinding(authToken: String, productName: String) =
        ApiConfigWithAuth.apiService.getDataFinding(authToken, productName)

}