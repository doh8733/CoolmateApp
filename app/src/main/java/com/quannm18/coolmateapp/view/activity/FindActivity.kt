package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.filter.GetFilter
import com.quannm18.coolmateapp.model.product.Product
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.home.NoiBatAdapter
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.DialogFilterBottomSheet
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_find.*

class FindActivity : BaseActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val sessionManager: SessionManager by lazy { SessionManager() }
    private var productList = mutableListOf<Product>()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private var productName: String? = null
    private val noiBatAdapter: NoiBatAdapter by lazy {
        NoiBatAdapter(
            likeItem = {
                likeItem(it.id)
            },
            clickItem = {
                val intent = Intent(this, DetailProductActivity::class.java)
                intent.putExtra("idProduct", it.id)
                startActivity(intent)
            }
        )
    }
    private var nullChar :String? = null

    override fun layoutID(): Int = R.layout.activity_find

    override fun initData() {
        Log.e("TAG", "initData: $productList")
    }

    override fun initView() {
        tilFind.clearFocus()
        marginStatusBar(listOf(imgBack))
        marginNavigationBar(listOf(rcvViewFind))

        rcvViewFind.apply {
            adapter = noiBatAdapter
            layoutManager = GridLayoutManager(this@FindActivity, 2)
        }
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        imgBack.setOnClickListener {
            finish()
        }
        onSearchAction()
        imgSortToolFind.setOnClickListener {
            DialogFilterBottomSheet(
                eventClick = {
                    getListDataFilter(it)
                }, productName
            ).show(
                supportFragmentManager,
                DialogFilterBottomSheet::class.simpleName.toString()
            )
        }
    }

    private fun onSearchAction() {
        tilFind?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                productName = s.toString()
                getDataFinding(s.toString())
            }

        })
    }

    private fun getListDataFilter(
        getFilter: GetFilter
    ) {
        productViewModel.getDataFilter(
            "Bearer ${sessionManager.fetchAuthToken()}",
            getFilter = getFilter
        ).observe(this) {
            it.let {
                when (it.status) {
                    Status.ERROR -> {
                        DialogAsk(this,"Lọc thất bại","không tìm thấy sản phẩm yêu cầu").show()
                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            noiBatAdapter.submitList(it)
                            Toast.makeText(this, "Tìm kiếm thành công", Toast.LENGTH_SHORT).show()
                            tvSearch.text = if (it.isNotEmpty()) {
                                "Chúng tôi tìm thấy ${it.size} tương tự"

                            } else {
                                "Không tìm thấy sản phẩm có từ khóa: $productName"
                            }
                            Log.e("TAG", "getListDataFilter: ${getFilter.productName}")
                        }

                    }
                    Status.LOADING -> {
                        Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getDataFinding(productName: String) {
        productViewModel.getDataFinding("Bearer ${sessionManager.fetchAuthToken()}", productName)
            .observe(this) {
                it.let {
                    when (it.status) {
                        Status.ERROR -> {
                            tvSearch.text = "Không tìm thấy sản phẩm có từ khóa: $productName"
                        }
                        Status.SUCCESS -> {
                            it.data?.let {
                                tvSearch.text = if (it.isNotEmpty()) {
                                    "Chúng tôi tìm thấy ${it.size} tương tự"

                                } else {
                                    "Không tìm thấy sản phẩm có từ khóa: $productName"
                                }
                                noiBatAdapter.submitList(it as MutableList<Product>)
                            }

                        }
                        Status.LOADING -> {
                        }
                    }
                }
            }
    }

    private fun likeItem(productId: String) {
        productViewModel.postFavorite("Bearer ${sessionManager.fetchAuthToken()}", productId)
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            this,
                            "Đã thêm vào yêu thích",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismissDialog()
                    }
                    Status.ERROR -> {
                        loadingDialog.dismissDialog()
                        Log.e(UserActivity.TAG, "refreshData: ${it.message} - ${it.data}")
                    }
                    Status.LOADING -> {
                        loadingDialog.startLoadingDialog()
                    }
                }
            }
    }
}