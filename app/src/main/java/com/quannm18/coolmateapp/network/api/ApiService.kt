package com.quannm18.coolmateapp.network.api

import com.quannm18.coolmateapp.model.banner.ResponseBanner
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.model.cart.ResponseGetCart
import com.quannm18.coolmateapp.model.favorite.ResponseFavorite
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.model.itemcart.ResponseItemCart
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.model.order.ResponsePostOrder
import com.quannm18.coolmateapp.model.product.Product
import com.quannm18.coolmateapp.model.rss.Explorer
import com.quannm18.coolmateapp.model.user.*
import com.quannm18.coolmateapp.model.voucher.PostAddVoucher
import com.quannm18.coolmateapp.model.voucher.ResponseAddVoucher
import com.quannm18.coolmateapp.model.voucher.ResponseVoucher
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //user
    @GET("users")
    suspend fun getListUser(): List<UserInfo>

    @GET("users/{id}")
    suspend fun getUserByID(@Path("id") id: String): UserInfo

    @POST("auth/login")
    suspend fun login(@Body userLogin: UserLogin): ResponseLogin

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("birthday") birthday: String,
        @Field("address") address: String
    ): ResponseRegister

    @GET("auth/me")
    suspend fun me(@Header("Authorization") authToken: String): ResponseLogin

    @POST("users/change-avatar")
    suspend fun changeAvatar(
        @Header("Authorization") authToken: String,
        @Part media: MultipartBody.Part
    ): UserChangeAvatar

    @Headers("Content-Type: application/json")
    @PUT("users")
    suspend fun putTokenNotification(
        @Header("Authorization") authToken: String,
        @Body notification: PostNotification
    ): UserInfo

    @Headers("Content-Type: application/json")
    @PUT("users")
    suspend fun putAddress(
        @Header("Authorization") authToken: String,
        @Body userInfo: UserInfo
    ): UserInfo

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @PUT("users")
    suspend fun editInfoUser(
        @Header("Authorization") token: String,
        @Field("fullName") fullName: String,
        @Field("gender") gender: String,
        @Field("birthday") birthday: String,
        @Field("address") address: String,
        @Field("phone") phoneNumber: String,
        @Field("chatLink") chatLink: String,
        @Field("avatar") avatar: String,
        @Field("password") password: String,
        @Field("phoneActive") phoneActive: String
    ): Response<ResponseLogin>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @PUT("users")
    suspend fun putChatLink(
        @Header("Authorization") token: String,
        @Field("chatLink") chatLink: String
    ): Response<ResponseLogin>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @PUT("users")
    suspend fun putAddress(
        @Header("Authorization") token: String,
        @Field("address") address: String
    ): Response<ResponseLogin>
    //otp password
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("auth/reset-password-get-otp")
    suspend fun getOtpFromEmail(
        @Query("email") email: String
    ): Response<Any>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("auth/reset-password-confirm")
    suspend fun postResetPassword(
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmPassword") confirmPassword: String,
    ): Response<Any>

    //product
    @GET("api/product")
    suspend fun getProduct(@Header("Authorization") authToken: String): List<Product>

    @GET("api/product/{id}")
    suspend fun getProductByID(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): Product

    //filter
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("api/product")
    suspend fun getDataFilter(
        @Header("Authorization") authToken: String,
        @Query("productName") productName: String?,
        @Query("priceTo") priceTo: Int?,
        @Query("priceFrom") priceFrom: Int?,
        @Query("style") style: String? = "",
        @Query("catalog") catalog: String? = "",
        @Query("material") material: String? = "",
        @Query("purpose") purpose: String? = "",
        @Query("feature") feature: String? = "",
    ): List<Product>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("api/product?orderBy=createdAt&order=DESC")
    suspend fun getDataFinding(
        @Header("Authorization") authToken: String,
        @Query("productName") productName: String = ""
    ): List<Product>

    //cart
    @POST("api/cart")
    suspend fun addToCart(@Header("Authorization") authToken: String, @Body addCart: AddCart)

    @GET("api/cart/{id}")
    suspend fun getCartByID(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): AddCart

    @GET("api/cart")
    suspend fun getCart(@Header("Authorization") authToken: String): List<ResponseGetCart>

    @DELETE("api/cart/{id}")
    suspend fun deleteCart(
        @Header("Authorization") authToken: String,
        @Path(value = "id") cartId: String
    ): Any

    //itemcart
    @GET("api/item-carts")
    suspend fun getAllItemCart(@Header("Authorization") authToken: String): List<ResponseItemCart>

    @Headers("Content-Type: application/json")
    @PUT("api/item-carts/{idItemCart}")
    suspend fun updateItemCart(
        @Header("Authorization") authToken: String,
        @Body putItemCart: PutItemCart,
        @Path("idItemCart") idItemCart: String
    ): UserInfo


    //orders
    @POST("api/oders/create-by-item-carts")
    suspend fun postOrders(
        @Header("Authorization") authToken: String,
        @Body postOrder: OrderPost
    ): ResponsePostOrder

    @GET("api/oders/{id}")
    suspend fun getInfoBillByID(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): ResponsePostOrder

    //explorer
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("rss")
    suspend fun getExplorer(): Explorer

    //banner
    @GET("rss/ImgBanner")
    suspend fun getListBanner(): List<ResponseBanner>

    //voucher
    @GET("api/voucher/{userId}&{status}")
    suspend fun getVoucherByUserId(
        @Header("Authorization") authToken: String,
        @Path("userId") id: String,
        @Path("status") status: String = "Còn mã"
    ): List<ResponseVoucher>

    @POST("favoriteVoucher")
    suspend fun addToWalletVoucher(
        @Header("Authorization") authToken: String,
        @Body postAddVoucher: PostAddVoucher
    ): ResponseAddVoucher

    //favorite

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @DELETE("users/like/{id}")
    suspend fun deleteFavoriteProduct(
        @Header("Authorization") token: String,
        @Path(value = "id") id: String
    ): List<ResponseFavorite>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("users/like/favorite")
    suspend fun getFavoriteProduct(
        @Header("Authorization") token: String
    ): List<ResponseFavorite>

    @POST("users/like/{productId}")
    suspend fun postFavorite(
        @Header("Authorization") token: String, @Path("productId") productId: String
    ): List<ResponseFavorite>

}