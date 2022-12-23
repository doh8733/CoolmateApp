package com.quannm18.coolmateapp.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.model.filter.CloneDataFilter
import com.quannm18.coolmateapp.model.filter.GetFilter
import com.quannm18.coolmateapp.view.adapter.spinner.SpinnerMaterialCatalogAdapter
import kotlinx.android.synthetic.main.bottom_sheet_filter.*

class DialogFilterBottomSheet(
    var eventClick: (GetFilter) -> Unit
) :
    BottomSheetDialogFragment() {
    private var mListStyle: MutableList<String> = mutableListOf()
    private var mListCatalog: MutableList<String> = mutableListOf()
    private var mListMaterial: MutableList<String> = mutableListOf()
    private var mListPurpose: MutableList<String> = mutableListOf()
    private var mListFeature: MutableList<String> = mutableListOf()
    private var priceTo: Int = 0
    private var priceFrom: Int = 999999999

    private val cloneDataFilter: CloneDataFilter by lazy {
        CloneDataFilter()
    }

    fun initView() {
        mListStyle = cloneDataFilter.getAllStyle()
        mListMaterial = cloneDataFilter.getAllMaterial()
        mListCatalog = cloneDataFilter.getAllCatalog()
        mListPurpose = cloneDataFilter.getAllPurpose()
        mListFeature = cloneDataFilter.getAllFeature()

        spnSelectStyle.adapter =
            SpinnerMaterialCatalogAdapter(
                requireContext(),
                R.layout.item_selector,
                mListStyle,
                "Kiểu dáng"
            )

        spnCatalog.adapter =
            SpinnerMaterialCatalogAdapter(
                requireContext(),
                R.layout.item_selector,
                mListCatalog,
                "Thể loại"
            )

        spnMaterial.adapter =
            SpinnerMaterialCatalogAdapter(
                requireContext(),
                R.layout.item_selector,
                mListMaterial,
                "Chọn chất liệu"
            )

        spnPurpose.adapter =
            SpinnerMaterialCatalogAdapter(
                requireContext(),
                R.layout.item_selector,
                mListPurpose,
                "Phù hợp với"
            )

        spnFeature.adapter =
            SpinnerMaterialCatalogAdapter(
                requireContext(),
                R.layout.item_selector,
                mListPurpose,
                "Tính năng"
            )

        rdoGroupFilter.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rdoPrice1 -> {
                    priceTo = 0
                    priceFrom = 100000
                }
                R.id.rdoPrice2 -> {
                    priceTo = 100000
                    priceFrom = 200000
                }
                R.id.rdoPrice3 -> {
                    priceTo = 200000
                    priceFrom = 300000
                }
                R.id.rdoPrice4 -> {
                    priceTo = 300000
                    priceFrom = 0
                }
                else -> {
                    priceTo = 0
                    priceFrom = 999999999
                }
            }
        }
    }

    fun listener() {
        btnFilter.setOnClickListener {
            onCheckClickFilter()
            dismiss()
        }
        imgClose.setOnClickListener {
            dismiss()
        }
    }

    fun listenerLiveData() {

    }

    private fun onCheckClickFilter() {
        val style: String = mListStyle[spnSelectStyle.selectedItemPosition]
        val catalog: String = mListCatalog[spnCatalog.selectedItemPosition]
        val material: String = mListMaterial[spnMaterial.selectedItemPosition]
        val purpose: String = mListPurpose[spnPurpose.selectedItemPosition]
        val feature: String = mListFeature[spnFeature.selectedItemPosition]
        val getFilter = GetFilter(
            priceTo = priceTo ?: 0,
            priceFrom = priceFrom ?: 0,
            style = style,
            catalog = catalog,
            material = material,
            purpose = purpose,
            feature = feature
        )
        if (style == "Tất cả") {
            getFilter.style = ""
            Log.e("STYLE", "onCheckClickFilter: $style")
        }
        if (catalog == "Tất cả") {
            getFilter.catalog = ""
            Log.e("CATALOG", "onCheckClickFilter: $catalog")
        }
        if (material == "Tất cả") {
            getFilter.material = ""
            Log.e("MAR", "onCheckClickFilter: $material")
        }
        if (purpose == "Tất cả") {
            getFilter.purpose = ""
            Log.e("PURPOSE", "onCheckClickFilter: $purpose")
        }
        if (feature == "Tất cả") {
            getFilter.feature = ""
            Log.e("FEATURES", "onCheckClickFilter: $feature")
        }
        eventClick(
            getFilter
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.bottom_sheet_filter, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listenerLiveData()
        listener()
    }

}