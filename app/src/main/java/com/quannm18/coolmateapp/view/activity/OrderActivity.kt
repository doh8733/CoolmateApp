package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.quannm18.coolmateapp.MyApp
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.enum.MyEnum
import com.quannm18.coolmateapp.model.order.OrderPost
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ConvertData
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.zalopay.Api.CreateOrder
import com.quannm18.coolmateapp.view.adapter.OrderListAdapter
import com.quannm18.coolmateapp.view.dialog.DialogFailAnim
import com.quannm18.coolmateapp.view.dialog.DialogSuccess
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.CartViewModel
import com.quannm18.coolmateapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class OrderActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val orderListAdapter: OrderListAdapter by lazy {
        OrderListAdapter()
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val cartViewModel: CartViewModel by viewModels()
    private val listenerConvert: MutableLiveData<OrderPost> by lazy {
        MutableLiveData()
    }
    private var tokenPayment = ""

    override fun layoutID(): Int = R.layout.activity_order

    override fun initData() {

    }

    override fun initView() {
        marginStatusBar(listOf(btnBackOrder))
        marginNavigationBar(listOf(viewSubBuy))
        addBounceView(listOf(viewSubBuy))
        rcvProductOrder.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(this@OrderActivity)
        }

        sessionManager.getUserInfo()?.let {
            it.user?.let { userInfo ->
                userInfo.address.apply {
                    if (this.length <= 10) {
                        tvChooseAddressOrder.visibility = View.VISIBLE
                        tvNameAndPhoneOrder.visibility = View.GONE
                        tvPhuongOrder.visibility = View.GONE
                        imgIconAddressOrder.visibility = View.GONE
                        tvAddressOrder.visibility = View.INVISIBLE
                    } else {
                        tvChooseAddressOrder.visibility = View.GONE
                        tvNameAndPhoneOrder.visibility = View.VISIBLE
                        tvPhuongOrder.visibility = View.VISIBLE
                        imgIconAddressOrder.visibility = View.VISIBLE
                        tvAddressOrder.visibility = View.VISIBLE
                        tvAddressOrder.text = this
//                        tvPhuongOrder.text = this.substring(0, this.indexOf(","))
                        tvPhuongOrder.visibility = View.GONE
                    }
                }
                tvNameAndPhoneOrder.text =
                    "${userInfo.fullName} | ${userInfo.phone?.let { "+84${it.removeRange(0..1)}" }}"
            }
        }
    }

    override fun listenLiveData() {
        lifecycleScope.launchWhenCreated {
            cartViewModel.pagingChecked.collectLatest {
                orderListAdapter.submitData(it)
            }
        }
        cartViewModel.selectSum.observe(this) {
            if (it != null) {
                tvFinalTotalOrder.text = "${MyApp.dec.format(it)} VND"
                tvTongTienHang.text = "${MyApp.dec.format(it)} VND"
                tvTongThanhToan.text = "${MyApp.dec.format(it)} VND"
                val orderApi = CreateOrder()
                try {
                    val data: JSONObject = orderApi.createOrder("$it")
                    Log.e(javaClass.simpleName, "Amount: ${it} - Data: $data")

                    val code = data.getString("return_code")
                    Log.e(javaClass.simpleName, "code $code")

                    Toast.makeText(applicationContext, "code $code", Toast.LENGTH_SHORT).show()

                    if (code.equals("1")) {
                        tokenPayment = data.getString("zp_trans_token")
                        Log.e(
                            javaClass.simpleName,
                            "zp_trans_token ${data.getString("zp_trans_token")}"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        listenerConvert.observe(this) { orderPost ->
            if (orderPost != null) {
                Log.e(javaClass.simpleName, "listenLiveData: ${Gson().toJson(orderPost)}")

                if (orderPost.paymentMethod == MyEnum.PaymentMethodEnum.ATM.name) {
                    ZaloPaySDK.getInstance()
                        .payOrder(this, tokenPayment, "quannm18://app", object : PayOrderListener {
                            override fun onPaymentSucceeded(
                                transactionId: String?,
                                transToken: String?,
                                appTransID: String?
                            ) {
                                runOnUiThread {
                                    DialogSuccess.newInstance(
                                        this@OrderActivity,
                                        "Thành công",
                                        "Thanh toán thành công"
                                    ).apply {
                                        show()
                                        event.observe(this@OrderActivity) {
                                            dismiss()
                                            takeOrder(orderPost, tokenPayment)
                                        }
                                    }
                                    Log.e(
                                        javaClass.simpleName,
                                        "transactionId: $transactionId\n" +
                                                "transToken: $transToken\n" +
                                                "appTransID: $appTransID ",
                                    )
                                }
                            }

                            override fun onPaymentCanceled(
                                zpTransToken: String?,
                                appTransID: String?
                            ) {
                                DialogFailAnim.newInstance(
                                    this@OrderActivity,
                                    "Thất bại",
                                    "Thanh toán bị hủy"
                                ).apply {
                                    show()
                                    event.observe(this@OrderActivity) {
                                        dismiss()
                                    }
                                }
                                Log.e(
                                    javaClass.simpleName,
                                    "zpTransToken: $zpTransToken\n" +
                                            "appTransID: $appTransID ",
                                )
                            }

                            override fun onPaymentError(
                                zaloPayError: ZaloPayError?,
                                zpTransToken: String?,
                                appTransID: String?
                            ) {
                                DialogFailAnim.newInstance(
                                    this@OrderActivity,
                                    "Thất bại",
                                    "Đã xảy ra lỗi. Vui lòng thử lại hoặc đổi phương thức thanh toán!"
                                ).apply {
                                    show()
                                    event.observe(this@OrderActivity) {
                                        dismiss()
                                    }
                                }
                                Log.e(
                                    javaClass.simpleName,
                                    "ZaloPayError: $zaloPayError\n" +
                                            "zpTransToken: $zpTransToken\n" +
                                            "appTransID: $appTransID ",
                                )

                            }

                        })
                } else {
                    takeOrder(orderPost, tokenPayment)
                }
            }
        }
    }

    override fun listeners() {
        viewLocation.setOnClickListener {
            startActivity(Intent(this@OrderActivity, LocationActivity::class.java))
        }
        viewVoucher.setOnClickListener {
            startActivity(Intent(this@OrderActivity, VoucherActivity::class.java))
        }
        viewPTTT.setOnClickListener {
            startActivity(Intent(this@OrderActivity, PaymentActivity::class.java))
        }
        btnBackOrder.setOnClickListener {
            finish()
        }
        btnOrderNow.setOnClickListener {
            cartViewModel.selectAllChecked().observe(this) {
                if (it.isNotEmpty()) {
                    val orderPost: OrderPost = ConvertData.convertCartToOrder(it)
                    orderPost.customerName = sessionManager.getUserInfo().user?.fullName ?: ""
                    orderPost.paymentMethod = if (managerSaveLocal.getPaymentMethod() == 0) {
                        MyEnum.PaymentMethodEnum.ATM.name
                    } else {
                        MyEnum.PaymentMethodEnum.COD.name
                    }
                    orderPost.placeCustomer = managerSaveLocal.getAddress().toString()
                    listenerConvert.postValue(orderPost)
                }
            }
        }

    }

    override fun onResume() {

        loginViewModel.getUserByIDFromAPI("Bearer $sessionManager").observe(this) {
            Log.e(UserActivity.TAG, "refreshData: ${it.status}")
            when (it.status) {
                Status.SUCCESS -> {
                    loadingDialog.dismissDialog()
                    it.data?.let { userInfo ->
                        userInfo.address.apply {
                            if (this.length <= 10) {
                                tvChooseAddressOrder.visibility = View.VISIBLE
                                tvNameAndPhoneOrder.visibility = View.GONE
                                tvPhuongOrder.visibility = View.GONE
                                imgIconAddressOrder.visibility = View.GONE
                                tvAddressOrder.visibility = View.INVISIBLE
                            } else {
                                tvChooseAddressOrder.visibility = View.GONE
                                tvNameAndPhoneOrder.visibility = View.VISIBLE
                                tvPhuongOrder.visibility = View.VISIBLE
                                imgIconAddressOrder.visibility = View.VISIBLE
                                tvAddressOrder.visibility = View.VISIBLE
                                tvAddressOrder.text = this
//                        tvPhuongOrder.text = this.substring(0, this.indexOf(","))
                                tvPhuongOrder.visibility = View.GONE
                            }
                        }
                        tvNameAndPhoneOrder.text =
                            "${userInfo.fullName} | ${userInfo.phone?.let { "+84${it.removeRange(0..1)}" }}"
                    }
                }
                Status.ERROR -> {
                    loadingDialog.dismissDialog()
                }
                Status.LOADING -> {
                    loadingDialog.startLoadingDialog()
                }
            }
            managerSaveLocal.getPaymentMethod()?.let {
                if (it == 0) {
                    tvSubTitlePTTT.text = "Thanh toán bằng ZaloPay"
                } else {
                    tvSubTitlePTTT.text = "Thanh toán khi nhận hàng"
                }
            }
        }
        super.onResume()
    }

    fun takeOrder(mOrderPost: OrderPost, tokenPayment: String) {
        val orderPost = mOrderPost.copy()
        if (tokenPayment != null) {
            orderPost.idPayment = tokenPayment
            orderPost.paymentMethod = MyEnum.PaymentMethodEnum.ATM.name
        } else {
            orderPost.paymentMethod = MyEnum.PaymentMethodEnum.COD.name
        }
        cartViewModel.postOrders("Bearer ${sessionManager.fetchAuthToken()}", orderPost)
            .observe(this) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val intent = Intent(this, SuccessBuyingActivity::class.java)
                        orderPost?.let {
                            intent.putExtra("idBill", it.cartID)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            cartViewModel.deleteCartItem(
                                it,
                                "Bearer ${sessionManager.fetchAuthToken()}",
                            )
                        }
                    }
                    Status.LOADING -> {
                        Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(javaClass.simpleName, "listenLiveData: ${resource}")
                        startActivity(Intent(this, FailedBuyingActivity::class.java))
                    }
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

}