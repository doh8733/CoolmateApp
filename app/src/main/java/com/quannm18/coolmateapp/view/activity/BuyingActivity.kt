package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.enum.MyEnum
import com.quannm18.coolmateapp.model.order.ResponsePostOrder
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.dialog.DialogCancelCartBottomSheet
import com.quannm18.coolmateapp.view.dialog.DialogHuyDon
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_buying.*
import kotlinx.android.synthetic.main.fragment_huy_don_bottom_sheet_dialog.*

class BuyingActivity : BaseActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val dialogCancelCartBottomSheet :DialogCancelCartBottomSheet by lazy {
        DialogCancelCartBottomSheet()
    }
    private var id: String = ""
    private var statusButton: STATUS_BUTTON = STATUS_BUTTON.HUYDON

    override fun layoutID(): Int = R.layout.activity_buying

    override fun initData() {

    }

    override fun initView() {
        Log.e(javaClass.simpleName, "initView: ${sessionManager.fetchAuthToken()}")
        "63a4e3a425ce5def44722b23"?.let {
            id = it
            refreshData(it)
        }
    }

    override fun listenLiveData() {

    }

    enum class STATUS_BUTTON {
        DATHANHTOAN,
        HUYDON,
        DATLAI,
        XEMSANPHAM,
        DOITRA
    }

    override fun listeners() {

        btnBackBuying.setOnClickListener {
            finish()
        }
        btnActionBill.setOnClickListener {
            when (statusButton) {
                STATUS_BUTTON.DATLAI, STATUS_BUTTON.XEMSANPHAM -> {
                    val intent = Intent(this@BuyingActivity, DetailProductActivity::class.java)
                    startActivity(intent)
                }
                STATUS_BUTTON.HUYDON -> {
                    DialogHuyDon.newInstance(this).apply {
                        show()
                        event.observe(this@BuyingActivity) {
                            if (it) {
                                DialogCancelCartBottomSheet().show(
                                    supportFragmentManager,
                                    DialogCancelCartBottomSheet::class.simpleName.toString()
                                )
                            }
                            dismiss()
                        }
                    }
                }
                STATUS_BUTTON.DATHANHTOAN -> {
                    val intent = Intent(this@BuyingActivity, BillHomeActivity::class.java)
                    startActivity(intent)
                }
                STATUS_BUTTON.DOITRA -> {
                    val intent = Intent(this@BuyingActivity, DetailProductActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this@BuyingActivity, DetailProductActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun refreshData(it: String) {
        cartViewModel.getInfoBillByID("Bearer ${sessionManager.fetchAuthToken()}", it)
            .observe(this@BuyingActivity) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loadingDialog.dismissDialog()

                    }
                    Status.LOADING -> {
                        Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT)
                            .show()
                        loadingDialog.startLoadingDialog()
                        resource.data?.let {
                            initViewData(it)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(javaClass.simpleName, "listenLiveData: ${resource}")
                        loadingDialog.dismissDialog()
                    }
                }
            }
    }

    private fun initViewData(responsePostOrder: ResponsePostOrder) {
        tvNameBill.text = "Đơn hàng\n ${responsePostOrder.id}"
        when (responsePostOrder.shippingStatus[responsePostOrder.shippingStatus.size - 1].shippingStatus) {
            MyEnum.ShippingStatus.CHOXACNHAN -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                chkDangXuLy.isChecked = true
                btnActionBill.text = "Huy đơn"
                btnActionBill.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_red)
            }
            MyEnum.ShippingStatus.DANGCHUANBI -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                showItem(
                    listOf(
                        chkDangDongGoi,
                        tvProcessDangDongGoi,
                        icProcessDangDongGoi,
                        tvSubProcessDangDongGoi,
                        progressBarDangDongGoi
                    )
                )
                btnActionBill.text = "Hủy đơn"
                btnActionBill.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_red)
                chkDangXuLy.isChecked = true
                chkDangDongGoi.isChecked = true
                statusButton = STATUS_BUTTON.HUYDON

            }
            MyEnum.ShippingStatus.DANGVANCHUYEN -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                showItem(
                    listOf(
                        chkDangDongGoi,
                        tvProcessDangDongGoi,
                        icProcessDangDongGoi,
                        tvSubProcessDangDongGoi,
                        progressBarDangDongGoi
                    )
                )
                showItem(
                    listOf(
                        chkDangGiao,
                        tvProcessDangGiao,
                        icProcessDangGiao,
                        tvSubProcessDangGiao,
                        progressBarDangGiao
                    )
                )
                chkDangXuLy.isChecked = true
                chkDangDongGoi.isChecked = true
                chkDangGiao.isChecked = true
                btnActionBill.text = "Xem lại sản phẩm"
                btnActionBill.background =
                    ContextCompat.getDrawable(this, R.drawable.custom_bg_btn_blue)
                statusButton = STATUS_BUTTON.XEMSANPHAM
            }
            MyEnum.ShippingStatus.DAGIAO -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                showItem(
                    listOf(
                        chkDangDongGoi,
                        tvProcessDangDongGoi,
                        icProcessDangDongGoi,
                        tvSubProcessDangDongGoi,
                        progressBarDangDongGoi
                    )
                )
                showItem(
                    listOf(
                        chkDangGiao,
                        tvProcessDangGiao,
                        icProcessDangGiao,
                        tvSubProcessDangGiao,
                        progressBarDangGiao
                    )
                )
                showItem(
                    listOf(
                        chkDaGiao,
                        tvProcessDaGiao,
                        icProcessDaGiao,
                        tvXemLichSu,
                        progressBarDaDuocGiao
                    )
                )
                chkDangXuLy.isChecked = true
                chkDangDongGoi.isChecked = true
                chkDangGiao.isChecked = true
                chkDaGiao.isChecked = true
                btnActionBill.text = "Đã thanh toán"
                btnActionBill.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_btn_history_buying)
                tvXemLichSu.isEnabled = true
                statusButton = STATUS_BUTTON.DATHANHTOAN
            }
            MyEnum.ShippingStatus.DANHAN -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                showItem(
                    listOf(
                        chkDangDongGoi,
                        tvProcessDangDongGoi,
                        icProcessDangDongGoi,
                        tvSubProcessDangDongGoi,
                        progressBarDangDongGoi
                    )
                )
                showItem(
                    listOf(
                        chkDangGiao,
                        tvProcessDangGiao,
                        icProcessDangGiao,
                        tvSubProcessDangGiao,
                        progressBarDangGiao
                    )
                )
                showItem(
                    listOf(
                        chkDaGiao,
                        tvProcessDaGiao,
                        icProcessDaGiao,
                        tvXemLichSu,
                        progressBarDaDuocGiao
                    )
                )
                chkDangXuLy.isChecked = true
                chkDangDongGoi.isChecked = true
                chkDangGiao.isChecked = true
                chkDaGiao.isChecked = true
                btnActionBill.text = "Đổi / Trả"
                btnActionBill.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_red)
                tvXemLichSu.isEnabled = true
                tvXemTinhTrangDoiTra.isEnabled = true
                statusButton = STATUS_BUTTON.DOITRA
            }
            MyEnum.ShippingStatus.BIHUY -> {

            }
            MyEnum.ShippingStatus.DATRA -> {
                showItem(
                    listOf(
                        chkDangXuLy,
                        tvProcessDangXuLy,
                        icProcessDangXuLy,
                        tvSubProcessDangXuLy,
                        progressBarDangXuLy
                    )
                )
                showItem(
                    listOf(
                        chkDangDongGoi,
                        tvProcessDangDongGoi,
                        icProcessDangDongGoi,
                        tvSubProcessDangDongGoi,
                        progressBarDangDongGoi
                    )
                )
                showItem(
                    listOf(
                        chkDangGiao,
                        tvProcessDangGiao,
                        icProcessDangGiao,
                        tvSubProcessDangGiao,
                        progressBarDangGiao
                    )
                )
                showItem(
                    listOf(
                        chkDaGiao,
                        tvProcessDaGiao,
                        icProcessDaGiao,
                        tvXemLichSu,
                        progressBarDaDuocGiao
                    )
                )
                showItem(
                    listOf(
                        chkTraHang,
                        tvProcessTraHang,
                        icProcessTraHang,
                        tvXemTinhTrangDoiTra,
                        progressBarTraHang
                    )
                )
                chkDangXuLy.isChecked = true
                chkDangDongGoi.isChecked = true
                chkDangGiao.isChecked = true
                chkDaGiao.isChecked = true
                chkTraHang.isChecked = true
                btnActionBill.text = "Mua lại"
                btnActionBill.background =
                    ContextCompat.getDrawable(this, R.drawable.custom_bg_btn_green)
                tvXemLichSu.isEnabled = true
                tvXemTinhTrangDoiTra.isEnabled = true
                statusButton = STATUS_BUTTON.DATLAI
            }
        }
    }


}