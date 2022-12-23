package com.quannm18.coolmateapp.view.activity

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.voucher.PostAddVoucher
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.VoucherAdapter
import com.quannm18.coolmateapp.view.dialog.DialogAddVoucher
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.VoucherViewModel
import kotlinx.android.synthetic.main.activity_voucher.*

class VoucherActivity : BaseActivity() {
    private val voucherAdapter: VoucherAdapter = VoucherAdapter()
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val voucherViewModel: VoucherViewModel by viewModels()
    override fun layoutID(): Int = R.layout.activity_voucher

    override fun initData() {

    }

    override fun initView() {
        marginStatusBar(listOf(imgBack))
        marginNavigationBar(listOf(btnAddVoucher))
        rcvVoucher.apply {
            adapter = voucherAdapter
            layoutManager = LinearLayoutManager(this@VoucherActivity)
        }
    }

    override fun listenLiveData() {
        loadData()
    }

    override fun listeners() {
        btnAddVoucher.setOnClickListener {
            DialogAddVoucher.newInstance(this).apply {
                show()
                event.observe(this@VoucherActivity) {
                    if (it) {
                        voucherViewModel.addToWalletVoucher(
                            "Bearer ${sessionManager.fetchAuthToken()}",
                            PostAddVoucher("")
                        ).observe(this@VoucherActivity) {
                            when (it.status) {
                                Status.SUCCESS -> {
                                    loadData()
                                    loadingDialog.dismissDialog()
                                }
                                Status.LOADING -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Loading",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadingDialog.startLoadingDialog()
                                }
                                Status.ERROR -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Có lỗi xảy ra!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadingDialog.dismissDialog()
                                    Log.e(javaClass.simpleName, "ERROR: $it")
                                }
                            }
                        }
                        dismiss()
                    } else {
                        dismiss()
                    }
                }
            }
        }
        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        sessionManager.getUserInfo().user?.id?.let {
            voucherViewModel.getVoucherByUserId(
                "Bearer ${sessionManager.fetchAuthToken()}",
                it
            ).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        voucherAdapter.submitList(it.data)
                        loadingDialog.dismissDialog()
                    }
                    Status.LOADING -> {
                        Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()
                        loadingDialog.startLoadingDialog()
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            applicationContext,
                            "Có lỗi xảy ra!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismissDialog()
                        Log.e(javaClass.simpleName, "ERROR: $it")
                    }
                }
            }
        }
    }
}