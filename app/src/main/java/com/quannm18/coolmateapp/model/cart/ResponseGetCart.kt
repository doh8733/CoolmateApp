package com.quannm18.coolmateapp.model.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.model.product.Product

data class ResponseGetCart(
    val id: String,

    @SerializedName("userId")
    val userID: String,

    val carts: List<Cart>,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

data class Cart(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val products: Products,

    @SerializedName("userId")
    val userID: String
)

data class Products(
    @SerializedName("productId")
    val productID: String = "",

    val colorName: String = "",
    val sizeName: String = "",
    val quantity: Long = 0L,
    val product: Product = Product(),
)

@Entity(tableName = "cart")
data class CartProduct(
    @SerializedName("productId")
    @ColumnInfo(name = "product_id")
    var productID: String = "",
    @ColumnInfo(name = "cart_id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "color_name")
    var colorName: String = "",
    @ColumnInfo(name = "size_name")
    var sizeName: String = "",
    @ColumnInfo(name = "quantity")
    var quantity: Long = 0L,
    @ColumnInfo(name = "total_price")
    var totalPrice : Long = 0L,
    @ColumnInfo(name = "image")
    var linkImage : String = "",
    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemID: String = "",
    @ColumnInfo(name = "total_product")
    val totalProduct: Long = 0,
)

