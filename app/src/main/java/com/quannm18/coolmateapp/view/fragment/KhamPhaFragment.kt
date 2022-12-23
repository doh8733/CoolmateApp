package com.quannm18.coolmateapp.view.fragment

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseFragment
import com.quannm18.coolmateapp.model.rss.Explorer
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.activity.WebViewActivity
import com.quannm18.coolmateapp.view.adapter.home.ExplorerAdapter
import com.quannm18.coolmateapp.viewmodel.ExplorerViewModel
import kotlinx.android.synthetic.main.fragment_kham_pha.*

class KhamPhaFragment : BaseFragment() {
    private lateinit var explorerAdapter: ExplorerAdapter
    private val explorerViewModel: ExplorerViewModel by viewModels()
    override fun layoutID(): Int = R.layout.fragment_kham_pha

    override fun initData() {

    }

    override fun initView() {

    }

    override fun listenLiveData() {
        getAllExplorer()
    }

    override fun listener() {

    }

    private fun getAllExplorer() {
        var listExplorer = mutableListOf<Explorer>()
        explorerViewModel.getExplorer().observe(viewLifecycleOwner) {
            it?.let { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data?.let { it1 ->
                            listExplorer.add(it1)
                            rcvExplorer.layoutManager = LinearLayoutManager(requireContext())
                            explorerAdapter = ExplorerAdapter(requireContext()) { item ->
                                val intent = Intent(requireContext(), WebViewActivity::class.java)
                                intent.putExtra("linkUrl", item.url)
                                startActivity(intent)
                            }
                            explorerAdapter.setData(it1.items)
                            rcvExplorer.adapter = explorerAdapter
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(javaClass.simpleName, "getAllExplorer: $it")
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }
}