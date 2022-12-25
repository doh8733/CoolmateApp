package com.quannm18.coolmateapp.view.fragment

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.FadePageTransformer
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.activity.DetailProductActivity
import com.quannm18.coolmateapp.view.activity.UserActivity
import com.quannm18.coolmateapp.view.adapter.home.HotHeaderSliderAdapter
import com.quannm18.coolmateapp.view.adapter.home.NoiBatAdapter
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.BannerAndExplorerViewModel
import com.quannm18.coolmateapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_giam_gia.*

class GiamGiaFragment : BaseFragment() {
    private val productViewModel: ProductViewModel by activityViewModels()
    private val bannerAndExplorerViewModel: BannerAndExplorerViewModel by activityViewModels()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }
    private val noiBatAdapter: NoiBatAdapter by lazy {
        NoiBatAdapter(
            likeItem = {
                likeItem(it.id)
            },
            clickItem = {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("idProduct", it.id)
                startActivity(intent)
            }
        )
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val hotHeaderSliderAdapter: HotHeaderSliderAdapter by lazy {
        HotHeaderSliderAdapter()
    }

    override fun layoutID(): Int = R.layout.fragment_giam_gia

    override fun initData() {
        getData()
    }

    override fun initView() {
        rcvGiamGiaFragment.apply {
            adapter = noiBatAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            CommonUtils.setAnimRecycleView(this)
        }

        viewPager.apply {
            adapter = hotHeaderSliderAdapter
            dotsIndicator.attachTo(this)
            setPageTransformer(FadePageTransformer())
        }
    }

    override fun listenLiveData() {
        bannerAndExplorerViewModel.getListBanner().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { res ->
                        hotHeaderSliderAdapter.submitList(res)
                        startAutoSlider(res.size)
                    }
                    swipeRefreshGiamGia.isRefreshing = false
                }
                Status.ERROR -> {
                    Log.e(UserActivity.TAG, "refreshData: ${it.message} - ${it.data}")
                }
                Status.LOADING -> {
                    swipeRefreshGiamGia.isRefreshing = true
                }
            }
        }
    }

    private fun startAutoSlider(count: Int = 3) {
        handler = Handler()
        runnable = Runnable {
            var pos: Int = viewPager.currentItem
            pos += 1
            if (pos >= count) pos = 0
            viewPager.currentItem = pos
            handler.postDelayed(runnable, 3000)
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun listener() {
        swipeRefreshGiamGia.setOnRefreshListener {
            getData()
        }
    }

    val  page = 1
    private fun getData() {
        productViewModel.getProduct(authToken = "Bearer ${sessionManager.fetchAuthToken()}")
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { res ->
                            noiBatAdapter.submitList(res)
                        }
                        swipeRefreshGiamGia.isRefreshing = false
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        Log.e(UserActivity.TAG, "refreshData: ${it.message} - ${it.data}")
                    }
                    Status.LOADING -> {
                        swipeRefreshGiamGia.isRefreshing = true
                    }
                }
            }
    }

    private fun likeItem(productId: String) {
        productViewModel.postFavorite("Bearer ${sessionManager.fetchAuthToken()}", productId)
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        handler = Handler()
        runnable = Runnable() {}
        super.onDestroy()
    }
}