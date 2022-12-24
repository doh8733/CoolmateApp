package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.quannm18.coolmateapp.MyApp.Companion.dec
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.product.Product
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.SingleLiveEvent
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.DetailColorAdapter
import com.quannm18.coolmateapp.view.adapter.DetailImageAdapter
import com.quannm18.coolmateapp.view.dialog.DialogDetailAddToCartBottomSheet
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.DetailViewModel
import com.quannm18.coolmateapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.coroutines.launch

class DetailProductActivity : BaseActivity() {
    private val listener: SingleLiveEvent<Product> = SingleLiveEvent()
    private val detailViewModel: DetailViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val detailImageAdapter: DetailImageAdapter by lazy {
        DetailImageAdapter()
    }
    private val detailColorAdapter: DetailColorAdapter by lazy {
        DetailColorAdapter()
    }
    private var productToSend: Product = Product()
    private lateinit var loadingDialog: LoadingDialog
    private var idProduct: String = ""

    override fun layoutID(): Int = R.layout.activity_detail_product

    override fun initData() {
        intent.getStringExtra("idProduct")?.let {
            getDetailProduct(id = it)
        }

    }

    override fun initView() {
        marginNavigationBar(listOf(btnAddToCartDetail))
        marginStatusBar(listOf(btnBackBuying, btnAddToFavoriteBuying))
        addBounceView(listOf(btnAddToCartDetail, btnBackBuying, btnAddToFavoriteBuying))

        loadingDialog = LoadingDialog(this)

        rcvListImageDetail.apply {
            adapter = detailImageAdapter
            layoutManager = LinearLayoutManager(this@DetailProductActivity)
        }
        rcvColorDetail.apply {
            adapter = detailColorAdapter
            layoutManager = LinearLayoutManager(this@DetailProductActivity).apply {
                orientation = HORIZONTAL
            }
        }
        rcvListImageDetail.requestDisallowInterceptTouchEvent(true)
        chkStatusDetail.isEnabled = false
    }

    override fun listenLiveData() {
        listener.observe(this) {
            Log.e("change", "onViewCreated: $it")
            it.apply {
                productToSend = this
                setData(this)
            }
        }
        detailImageAdapter.event.observe(this) {
            when (it) {
                is String -> {
                    CommonUtils.loadAvatarItem(it, imgHeaderDetailProduct)
                }
            }
        }
    }

    override fun listeners() {
        btnBackBuying.setOnClickListener {
            finish()
        }
        btnAddToFavoriteBuying.setOnClickListener {
            intent.getStringExtra("idProduct")?.let { it1 -> likeItem(it1) }
        }
        btnAddToCartDetail.setOnClickListener {
            detailViewModel.setProduct(productToSend.copy())
            DialogDetailAddToCartBottomSheet().show(
                supportFragmentManager,
                DialogDetailAddToCartBottomSheet::class.simpleName.toString()
            )
        }



        btnShareDetail.setOnClick {
//            btnNumberDetail.text = "999"
        }
    }

    private fun getDetailProduct(id: String) {
        Log.e(javaClass.simpleName, "id: $id")
        Log.e(javaClass.simpleName, "token: ${sessionManager.fetchAuthToken()}")
        detailViewModel.getProductByID("Bearer ${sessionManager.fetchAuthToken()}", id)
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBarLoading.visibility = View.GONE
                        listener.postValue(it.data!!)
                    }
                    Status.LOADING -> {
                        progressBarLoading.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        progressBarLoading.visibility = View.GONE
                    }
                }
            }
    }


    @SuppressLint("SetTextI18n")
    private fun setData(responseProductItem: Product?) {
        lifecycleScope.launch {
            responseProductItem?.apply {
                CommonUtils.loadAvatarItem(
                    this.color?.get(0)?.image?.get(0)
                        ?: "R.drawable.header_buying_activity",
                    imgHeaderDetailProduct
                )
                tvTitleNameDetailProduct.text = this.productName
                tvSubNameProductDetail.text = this.productName
                tvPriceDetailProduct.text = "${dec.format(this.sellingPrice)} VND"
                if (!"${dec.format(this.sellingPrice)} VND".equals(
                        "${dec.format(this.promotionalPrice.toLong())} VND",
                        true
                    )
                ) {
                    tvGiamGiaTien.visibility = View.VISIBLE
                    viewDiviGiamGia.visibility = View.VISIBLE
                    tvGiamGiaTien.text = "${dec.format(this.promotionalPrice.toLong())} VND"
                } else {
                    tvGiamGiaTien.visibility = View.GONE
                    viewDiviGiamGia.visibility = View.GONE
                }
                tvReviewDetail.text = "(${this.ratingAvg})"
                tvDesDetail.text = this.description
                tvStyleProductDetail.text = this.style
                tvCatalogProductDetail.text = this.catalog
                tvMaterialProductDetail.text = this.material
                tvFeatureProductDetail.text = this.feature.toText()

                if (this.productCount > 0) {
                    tvQuantityDetail.text = "Còn ${this.productCount}"
                    chkStatusDetail.isChecked = true
                    tvStatusDetail.text = "Còn hàng"
                    btnAddToCartDetail.isEnabled = true
                } else {
                    tvQuantityDetail.text = ""
                    chkStatusDetail.isChecked = false
                    tvStatusDetail.text = "Hết hàng"
                    btnAddToCartDetail.isEnabled = false
                }
                Log.e(javaClass.simpleName, "setData: ${this.color}")
                detailColorAdapter.submitList(this.color)
                detailImageAdapter.submitList(this.image)
                if (this.ratingAvg >= 0) {
                    imgStar1.setBackgroundResource(R.drawable.ic_star_gold_detail)
                } else if (this.ratingAvg >= 1) {
                    imgStar2.setBackgroundResource(R.drawable.ic_star_gold_detail)
                } else if (this.ratingAvg >= 2) {
                    imgStar3.setBackgroundResource(R.drawable.ic_star_gold_detail)
                } else if (this.ratingAvg >= 3) {
                    imgStar4.setBackgroundResource(R.drawable.ic_star_gold_detail)
                } else if (this.ratingAvg >= 4) {
                    imgStar5.setBackgroundResource(R.drawable.ic_star_gold_detail)
                }
//                it.color[0].isChecked = true
//                it.color[0].size?.let { it1 ->
//                    it.color[0].size?.get(0)?.isChecked = true
//                }
            }
        }
    }

    private fun likeItem(productId: String) {
        Log.e(javaClass.simpleName, "id: $productId")
        productViewModel.postFavorite("Bearer ${sessionManager.fetchAuthToken()}", productId)
            .observe(this) {
                Log.e("TAG", "likeItem: ${sessionManager.fetchAuthToken()}")
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                        loadingDialog.dismissDialog()
                    }
                    Status.ERROR -> {
                        Log.e(UserActivity.TAG, "refreshData: ${it.message} - ${it.data}")
                        Toast.makeText(this, "Bạn đã yêu thích sản phẩm này rồi", Toast.LENGTH_SHORT).show()
                        loadingDialog.dismissDialog()
                    }
                    Status.LOADING -> {
                        loadingDialog.startLoadingDialog()
                    }
                }
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.setOnClick(clickEvent: () -> Unit) {
        this.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                clickEvent.invoke()
            }
            false
        }
    }

    fun List<String>.toText(): String {
        val text: StringBuilder = java.lang.StringBuilder()
        this.forEach {
            if (text.isNotEmpty()) {
                text.append(", $it")
            } else {
                text.append(it)
            }
        }
        return text.toString()
    }

    override fun onPause() {
        Log.e("TAG", "onPause: ")
        super.onPause()
    }

    override fun onResume() {
        Log.e("TAG", "onResume: ")
        super.onResume()
    }
}