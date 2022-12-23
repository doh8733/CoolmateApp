package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.bus.CartBus
import com.quannm18.coolmateapp.bus.ItemCart
import com.quannm18.coolmateapp.model.cart.CartProduct
import com.quannm18.coolmateapp.model.itemcart.PutItemCart
import com.quannm18.coolmateapp.model.itemcart.ResponseItemCart
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.SwipeToDeleteCallback
import com.quannm18.coolmateapp.view.adapter.CartPagingAdapter
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartActivity : BaseActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val cartPagingAdapter: CartPagingAdapter by lazy {
        CartPagingAdapter()
    }
    private val listenerDeleteCart: SingleLiveEvent<CartProduct> = SingleLiveEvent()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun layoutID(): Int = R.layout.activity_cart

    override fun initData() {
        getData()
        cartViewModel.delete()
    }

    override fun initView() {
        marginStatusBar(listOf(btnBackCart))
        marginNavigationBar(listOf(btnOrderCart))
        btnOrderCart.alpha = 0.5f
        rcvCart.apply {
            adapter = cartPagingAdapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rcvCart)
    }

    override fun listenLiveData() {
        lifecycleScope.launch {
            cartViewModel.pagingAll.collectLatest {
                cartPagingAdapter.submitData(it)
            }
        }
        cartViewModel.totalPrice.observe(this) {
            if (it == null) {
                btnOrderCart.alpha = 0.5f
                tvTotalCart.text = "0 VND"
            } else {
                it.let {
                    Log.e(javaClass.simpleName, "listenLiveData: $it")
                    if (it > 0) {
                        btnOrderCart.alpha = 1f
                        tvTotalCart.text = "${MyApp.dec.format(it)} VND"
                    } else {
                        btnOrderCart.alpha = 0.5f
                        tvTotalCart.text = "0 VND"
                    }
                }
            }
        }
        cartPagingAdapter.event.observe(this) {
            Log.e(javaClass.simpleName, "listenLiveData: $it")
            it?.let {
                when (it) {
                    is CartBus -> {
                        cartViewModel.setSelected(it.isChecked, it.id)
                    }
                    is CartProduct -> {
                        cartViewModel.delete(it.itemID)
                        listenerDeleteCart.postValue(it)
                    }
                    is ItemCart -> {
                        val intent = Intent(this, DetailProductActivity::class.java)
                        intent.putExtra("idProduct", it.cart.productID)
                        startActivity(intent)
                    }
                    is PutItemCart -> {
                        cartViewModel.updateItemCart(
                            "Bearer ${sessionManager.fetchAuthToken()}",
                            it,
                            it.itemCartId
                        ).observe(this) { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    loadingDialog.dismissDialog()
                                    getData()
                                }
                                Status.LOADING -> {
                                    loadingDialog.startLoadingDialog()
                                }
                                Status.ERROR -> {
                                    loadingDialog.dismissDialog()
                                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.e("deleteCart", "getData: $resource")
                                }
                            }
                        }
                    }
                }
            }
        }
        listenerDeleteCart.observe(this) {
            if (it != null) {
                cartViewModel.deleteCart("Bearer ${sessionManager.fetchAuthToken()}", it.itemID)
                    .observe(this) { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                loadingDialog.dismissDialog()
                            }
                            Status.LOADING -> {
                                loadingDialog.startLoadingDialog()
                            }
                            Status.ERROR -> {
                                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                                    .show()
                                loadingDialog.dismissDialog()
                            }
                        }
                    }
            }
        }
        btnBackCart.setOnClickListener {
            finish()
        }
    }

    override fun listeners() {
        swipeRcvCart.setOnRefreshListener {
            getData()
        }

        btnOrderCart.setOnClickListener {
            sessionManager.getUserInfo()?.let {
                it.user?.let { userInfo ->
                   if (userInfo.status.equals("ACTIVE",true)){
                       DialogAsk.newInstance(this, "Xác nhận", "Tài khoản bạn chưa được xác minh\nVui lòng xác minh thông tin để đặt hàng!").apply {
                           show()
                           event.observe(this@CartActivity){
                               if (it){
                                   startActivity(Intent(this@CartActivity, InfoEditActivity::class.java))
                               }
                               dismiss()
                           }
                       }
                       return@setOnClickListener
                   }else{
                       startActivity(Intent(this, OrderActivity::class.java))
                       return@setOnClickListener
                   }
                }
            }
            startActivity(Intent(this, OrderActivity::class.java))
        }
    }


    private fun getData() {
        cartViewModel.getAllItemCart("Bearer ${sessionManager.fetchAuthToken()}")
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { it1 ->
                            if (it1.isNotEmpty()) {
                                saveCartProductToDB(it1)
                            }
                        }
//                        Log.e("TAG", "getData: ${it.data}", )
                        swipeRcvCart.isRefreshing = false
                    }
                    Status.LOADING -> {
                        swipeRcvCart.isRefreshing = true
                    }
                    Status.ERROR -> {
                        swipeRcvCart.isRefreshing = false
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                        Log.e("getCart", "getData: $it")
                    }
                }
            }
    }

    private fun saveCartProductToDB(data: List<ResponseItemCart>) {
        if (data.size > 0) {
            lifecycleScope.launch(IO) {
                data.map {
                    managerSaveLocal.saveCartID(it.id)
                    it.let { cart ->
                        cartViewModel.insert(
                            CartProduct(
                                productID = cart.products.productID,
                                id = managerSaveLocal.getCartID().toString(),
                                name = cart.name,
                                colorName = cart.products.colorName,
                                sizeName = cart.products.sizeName,
                                quantity = cart.products.quantity,
                                itemID = cart.id,
                                totalPrice = (cart.products.quantity * cart.products.product.sellingPrice),
                                linkImage = cart.products.product.color.filter { it.name == cart.products.colorName }
                                    ?.get(0)?.image?.get(0).toString(),
                                totalProduct = cart.products.product.color.filter { it.name == cart.products.colorName }
                                    ?.get(0)?.size?.filter { it.name == cart.products.sizeName }
                                    ?.get(0)?.productCount?.toLong() ?: 0L
                            )
                        )
                    }
                }
            }
        }
    }
}