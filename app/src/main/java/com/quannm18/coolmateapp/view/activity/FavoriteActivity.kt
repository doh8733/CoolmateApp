package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.utils.Status.*
import com.quannm18.coolmateapp.view.adapter.FavoriteAdapter
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : BaseActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            onSelectItem = {
                val intent = Intent(this, DetailProductActivity::class.java)
                intent.putExtra("idProduct", it.productId)
                startActivity(intent)
            },
            onDeleteResponseFavorite = {
                onDeleteFavoriteProduct(it.productId)
            }
        )
    }
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }

    override fun layoutID(): Int = R.layout.activity_favorite
    override fun initData() {
    }

    override fun initView() {
        marginStatusBar(listOf(imgBackFavorite, textView14))
        marginNavigationBar(listOf(btnAddToCart))

        rcViewFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }

    override fun listenLiveData() {
        getAllFavorite()
    }

    override fun listeners() {
        imgBackFavorite.setOnClickListener {
            finish()
        }

        viewRcViewFavorite.setOnRefreshListener {
            getAllFavorite()
        }
    }

    private fun getAllFavorite() {
        productViewModel.getFavoriteProduct("Bearer ${sessionManager.fetchAuthToken()}")
            .observe(this) {
                it?.let {
                    when (it.status) {
                        Status.ERROR -> {
                            viewRcViewFavorite.isRefreshing = false
                            loadingDialog.dismissDialog()
                            Log.e(javaClass.simpleName, "Error ${it}")
                        }
                        SUCCESS -> {
                            viewRcViewFavorite.isRefreshing = false
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                            loadingDialog.dismissDialog()
                            favoriteAdapter.submitList(it.data)
                        }
                        LOADING -> {
                            viewRcViewFavorite.isRefreshing = true
                            loadingDialog.startLoadingDialog()
                        }
                    }
                }
            }
    }

    private fun onDeleteFavoriteProduct(id: String) {
        productViewModel.deleteFavoriteProduct("Bearer ${sessionManager.fetchAuthToken()}", id)
            .observe(this) {
                it.let {
                    when (it.status) {
                        SUCCESS -> {
                            loadingDialog.dismissDialog()
                            getAllFavorite()
                            Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                        }
                        ERROR -> {
                            Log.e("ERROR", "${it.message}")
                            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                            loadingDialog.dismissDialog()
                        }
                        LOADING -> {
                            loadingDialog.startLoadingDialog()
                        }
                    }
                }
            }
    }

}