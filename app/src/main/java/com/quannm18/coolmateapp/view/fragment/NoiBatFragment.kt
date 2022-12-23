package com.quannm18.coolmateapp.view.fragment

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.CommonUtils
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.activity.DetailProductActivity
import com.quannm18.coolmateapp.view.activity.UserActivity
import com.quannm18.coolmateapp.view.adapter.home.NoiBatAdapter
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_noi_bat.*

class NoiBatFragment : BaseFragment() {
    private val productViewModel: ProductViewModel by activityViewModels()
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
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }

    override fun layoutID(): Int = R.layout.fragment_noi_bat

    override fun initData() {
    }

    override fun initView() {
        rcvNoiBat.apply {
            adapter = noiBatAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            CommonUtils.setAnimRecycleView(this)
        }
    }

    override fun listenLiveData() {
        getData()
    }

    override fun listener() {
        imgSortFullViewNoiBat.setOnClickListener {
            rcvNoiBat.layoutManager = LinearLayoutManager(requireContext())

        }

        imgSortGridViewNoiBat.setOnClickListener {
            rcvNoiBat.layoutManager =
                GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        }

        imgSortToolNoiBat.setOnClickListener {
            rcvNoiBat.layoutManager = LinearLayoutManager(requireContext())
        }

        swipeRefreshNoiBat.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        Log.e(javaClass.simpleName, "getData: ${sessionManager.fetchAuthToken()}")
        productViewModel.getProduct(authToken = "Bearer ${sessionManager.fetchAuthToken()}")
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        loadingDialog.dismissDialog()
                        it.data?.let { res ->
                            noiBatAdapter.submitList(res)
                        }
                        swipeRefreshNoiBat.isRefreshing = false
                    }
                    Status.ERROR -> {
                        loadingDialog.dismissDialog()
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        Log.e(UserActivity.TAG, "refreshData:  ${it}")
                    }
                    Status.LOADING -> {
                        loadingDialog.startLoadingDialog()
                        swipeRefreshNoiBat.isRefreshing = true
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
                            requireContext(),
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