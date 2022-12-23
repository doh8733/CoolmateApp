package com.quannm18.coolmateapp.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.bus.NumberBus
import com.quannm18.coolmateapp.model.cart.AddCart
import com.quannm18.coolmateapp.model.cart.ProductXCart
import com.quannm18.coolmateapp.model.product.Color
import com.quannm18.coolmateapp.model.product.Size
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.DetailColorAdapter
import com.quannm18.coolmateapp.view.adapter.DetailSizeAdapter
import com.quannm18.coolmateapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail_add_to_cart_bottom_sheet_dialog.*

class DialogDetailAddToCartBottomSheet : BottomSheetDialogFragment() {
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val listener: SingleLiveEvent<Any> = SingleLiveEvent()
    private val colorAdapter: DetailColorAdapter by lazy {
        DetailColorAdapter()
    }
    private val sizeAdapter: DetailSizeAdapter by lazy {
        DetailSizeAdapter()
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private var addCart = AddCart()
    private var productXCart = ProductXCart()
    private var maxProduct: Long = 0
    private fun initView() {
        addCart = AddCart()
        productXCart = ProductXCart()
        rcvColorBottomSheet.apply {
            adapter = colorAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        }
        rcvSizeBottomSheet.apply {
            adapter = sizeAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        }
    }

    private fun listenerLiveData() {
        detailViewModel.event.observe(viewLifecycleOwner) {
            it?.apply {
                addCart.name = productName
                productXCart.productId = id
                addCart.products = listOf(productXCart)
                CommonUtils.loadAvatarItem(image[0], imgPreviewBottomSheet)
                tvCountProduct.text = "Còn ${(productCount)}"
                maxProduct = productCount
                tvPriceBottomSheet.text = "${MyApp.dec.format(sellingPrice)} VND"
                colorAdapter.submitList(color)
                color[0].size?.let { sizeList ->
                    sizeAdapter.submitList(sizeList)
                }
            }
        }
        listener.observe(viewLifecycleOwner) {
            when (it) {
                is NumberBus -> {
                    productXCart.quantity = it.number
                    addCart.products = listOf(productXCart)
                    btnDownBottomSheet.isEnabled = it.number > 1
                    btnUpBottomSheet.isEnabled = it.number < maxProduct
                    btnNumberBottomSheet.text = it.number.toString()
                }
            }
        }

        colorAdapter.event.observe(viewLifecycleOwner) {
            when (it) {
                is Color -> {
                    val tempProductXCart = productXCart.copy()
                    tempProductXCart.colorName = ""
                    tempProductXCart.sizeName = ""
                    tempProductXCart.quantity = 1
                    productXCart = tempProductXCart
                    btnNumberBottomSheet.text = "1"
                    productXCart.colorName = it.name.toString()
                    addCart.products = listOf(productXCart)
                    sizeAdapter.submitList(it.size)
                    CommonUtils.loadAvatarItem(it.image?.get(0).toString(), imgPreviewBottomSheet)
                }
            }
        }

        sizeAdapter.event.observe(viewLifecycleOwner) {
            when (it) {
                is Size -> {
                    productXCart.sizeName = it.name
                    addCart.products = listOf(productXCart)
                    tvCountProduct.text = "Còn ${it.productCount}"
                    maxProduct = it.productCount.toLong()
                }
            }
        }
    }

    private fun listener() {
        btnDownBottomSheet.setOnClickListener {
            try {
                var i: Int = btnNumberBottomSheet.text.toString().toInt()
                listener.postValue(NumberBus(--i))
            } catch (e: Exception) {
            }
        }
        btnUpBottomSheet.setOnClickListener {
            try {
                var i: Int = btnNumberBottomSheet.text.toString().toInt()
                listener.postValue(NumberBus(++i))
            } catch (e: Exception) {
            }
        }
        btnAddToCartBottomSheet.setOnClickListener {
            Log.e("addTC", "addCart ${Gson().toJson(addCart)}")
            if (validateAddCart(productXCart) > 0) {
                addProductToCart(addCart)
            }
        }
    }

    private fun addProductToCart(addCart: AddCart) {
        detailViewModel.addToCart("Bearer ${sessionManager.fetchAuthToken()}", addCart = addCart)
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    Status.LOADING -> {
                        Log.e(javaClass.simpleName, "loading: ${it.message}")
                    }
                    Status.ERROR -> {

                        Log.e(javaClass.simpleName, "error: ${it.message}")
                    }
                }
            }
    }

    private fun validateAddCart(productXCart: ProductXCart): Int {
        if (productXCart.colorName.trim().isEmpty()) {
            Toast.makeText(context, "Vui lòng chọn màu", Toast.LENGTH_SHORT).show()
            return 0
        }
        if (productXCart.sizeName.trim().isEmpty()) {
            Toast.makeText(context, "Vui lòng chọn size", Toast.LENGTH_SHORT).show()
            return 0
        }
        return 1
    }

    override fun onCancel(dialog: DialogInterface) {

        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun getTheme(): Int = R.style.NoBackgroundDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_detail_add_to_cart_bottom_sheet_dialog, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listenerLiveData()
        listener()
    }


}