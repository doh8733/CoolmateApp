package com.quannm18.coolmateapp.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.bus.ListenerValidate
import com.quannm18.coolmateapp.model.address.Address
import com.quannm18.coolmateapp.model.enum.MyEnum
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.Constants.Companion.REQUEST_CODE
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.AddressAdapter
import com.quannm18.coolmateapp.view.dialog.DialogAsk
import com.quannm18.coolmateapp.view.dialog.DialogShowValidate
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.GSonViewModel
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LocationActivity : BaseActivity() {
    private val gSonViewModel: GSonViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val addressAdapter: AddressAdapter by lazy {
        AddressAdapter()
    }
    private val addressString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val listenerDialogSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private var mAddressChoose = ""
    private var locationRequest: LocationRequest? = null
    private var textAddress = ""
    override fun layoutID(): Int = R.layout.activity_location

    override fun initData() {
        lifecycleScope.launchWhenCreated {
            addressAdapter.initData(gSonViewModel.getListCityAddress())
        }
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setFastestInterval(2000)
    }

    override fun initView() {
        refreshActivity()
        rcvAddress.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(this@LocationActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@LocationActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        managerSaveLocal.getAddress()?.let {
            tvAllAddress.visibility = View.VISIBLE
            tvAllAddress.text = it.toString()
            tvCity.visibility = View.GONE
            btnChonLai.visibility = View.VISIBLE
            rcvAddress.visibility = View.GONE
            isHaveData(true)
        }

        sessionManager.getUserInfo().user?.address?.let {
            if (it.length > 10) {
                tvAllAddress.visibility = View.VISIBLE
                tvAllAddress.text = it.toString()
                tvCity.visibility = View.GONE
                btnChonLai.visibility = View.VISIBLE
                rcvAddress.visibility = View.GONE
                isHaveData(true)
            }
        }
        marginNavigationBar(listOf(btnGetCurrentLocation))
    }

    override fun listenLiveData() {
        addressAdapter.event.observe(this) {
            when (it) {
                is Address -> {
                    lifecycleScope.launch {
                        when (it.type) {
                            "0" -> {
                                tvCity.text = "${MyEnum.LocationEnum.TinhTP}" + it.name
                                addressAdapter.initData(gSonViewModel.getListDistrictAddress(it.code))
                                tvDistricts.visibility = View.VISIBLE
                            }
                            "1" -> {
                                tvDistricts.text = "${MyEnum.LocationEnum.QuanHuyen}" + it.name
                                addressAdapter.initData(gSonViewModel.getListCommuneAddress(it.code))
                                tvCommune.visibility = View.VISIBLE
                            }
                            else -> {
                                tvCommune.text = "${MyEnum.LocationEnum.XaPhuong}" + it.name
                                mAddressChoose = it.nameWithType
                                rcvAddress.visibility = View.INVISIBLE
                                isHaveData(true)
                                edDetail.visibility = View.VISIBLE
                                btnSuccess.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
        addressString.observe(this) {
            tvAllAddress.visibility = View.VISIBLE
            btnChonLai.visibility = View.VISIBLE
            isHaveData(true)
            tvCity.visibility = View.GONE
            tvCommune.visibility = View.GONE
            tvDistricts.visibility = View.GONE
            rcvAddress.visibility = View.GONE
            btnSuccess.visibility = View.GONE
            edDetail.visibility = View.GONE
            tvAllAddress.text = "$it"
        }
        tvAllAddress.addTextChangedListener {
            textAddress = it.toString()
        }
        listenerDialogSuccess.observe(this) {
            if (it) {
                userViewModel.putAddress(
                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                    address = managerSaveLocal.getAddress().toString()
                ).observe(this) { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loadingDialog.dismissDialog()
                            finish()
                        }
                        Status.LOADING -> {
                            loadingDialog.startLoadingDialog()
                        }
                        Status.ERROR -> {
                            Log.e(TAG, "ERROR: $resource")
                            loadingDialog.dismissDialog()
                        }
                    }
                }
            }
        }
    }

    override fun listeners() {
        btnGetCurrentLocation.setOnClickListener {
            textAddress = ""
            getCurrentLocation()
        }

        btnFinishedAddAddress.setOnClickListener {
            DialogAsk(this, "Xác nhận", "Bạn chắc chắn địa chỉ là chính xác?").apply {
                show()
                event.observe(this@LocationActivity) {
                    if (it) {
                        managerSaveLocal.saveAddress(textAddress)
                        listenerDialogSuccess.postValue(true)
                        Toast.makeText(this@LocationActivity, "Lưu thành công!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        listenerDialogSuccess.postValue(false)
                    }
                    dismiss()
                }
            }
        }
        btnBackLocation.setOnClickListener {
            onBackPressed()
        }

        btnSuccess.setOnClickListener {
            if (edDetail.text.toString().trim().isNotEmpty()) {
                tvAllAddress.visibility = View.VISIBLE
                btnChonLai.visibility = View.VISIBLE
                isHaveData(true)
                tvCity.visibility = View.GONE
                tvCommune.visibility = View.GONE
                tvDistricts.visibility = View.GONE
                rcvAddress.visibility = View.GONE
                btnSuccess.visibility = View.GONE
                edDetail.visibility = View.GONE
                tvAllAddress.text =
                    "${edDetail.text}, " +
                            "${
                                tvCommune.text.toString()
                                    .replace("${MyEnum.LocationEnum.XaPhuong}", "")
                            }, " +
                            "${
                                tvDistricts.text.toString()
                                    .replace("${MyEnum.LocationEnum.QuanHuyen}", "")
                            }, " +
                            "${tvCity.text.toString().replace("${MyEnum.LocationEnum.TinhTP}", "")}"
            } else {
                DialogShowValidate(
                    this,
                    ListenerValidate("Vui lòng nhập địa chỉ chi tiết nơi bạn sống!")
                ).apply {
                    show()
                }
            }
        }

        btnChonLai.setOnClickListener {
            textAddress = ""
            managerSaveLocal.deleteAddress()
            refreshActivity()
        }

    }

    private fun getAddressInfo(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            Log.e(TAG, "lat $latitude, long $longitude")
            val geocoder = Geocoder(this@LocationActivity, Locale.getDefault())
            val addresses: List<android.location.Address> =
                geocoder.getFromLocation(latitude, longitude, 1) as List<android.location.Address>

            val address = addresses[0].getAddressLine(0)
            Log.e(TAG, "${address} ")
            addressString.postValue(address)
//            setResult(REQUEST_CODE, Intent().putExtra("address", address))
//            finish()
        }
    }

    companion object {
        val TAG = javaClass.simpleName
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    private fun getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (isGPSEnabled()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        LocationServices.getFusedLocationProviderClient(this@LocationActivity)
                            .requestLocationUpdates(locationRequest!!, object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    super.onLocationResult(locationResult)
                                    LocationServices.getFusedLocationProviderClient(this@LocationActivity)
                                        .removeLocationUpdates(this)
                                    if (locationResult != null && locationResult.locations.size > 0) {
                                        val index = locationResult.locations.size - 1
                                        val latitude = locationResult.locations[index].latitude
                                        val longitude = locationResult.locations[index].longitude
                                        getAddressInfo(latitude, longitude)
                                    }
                                }
                            }, Looper.getMainLooper())
                    }
                } else {
                    turnOnGPS()
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
        }
    }

    private fun turnOnGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(
            applicationContext
        )
            .checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Toast.makeText(this, "GPS is already tured on", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this, 2)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        var locationManager: LocationManager? = null
        var isEnabled = false
        if (locationManager == null) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isEnabled
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation()
                } else {
                    turnOnGPS()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation()
            }
        }
    }

    private fun refreshActivity() {
        try {
            lifecycleScope.launch {
                addressAdapter.initData(gSonViewModel.getListCityAddress())
            }
        } catch (e: Exception) {
        }
        tvAllAddress.visibility = View.GONE
        btnChonLai.visibility = View.GONE
        edDetail.visibility = View.GONE
        btnSuccess.visibility = View.GONE
        isHaveData(false)
        rcvAddress.visibility = View.VISIBLE
        tvCommune.visibility = View.GONE
        tvDistricts.visibility = View.GONE
        tvCity.text = "${MyEnum.LocationEnum.TinhTP}: Vui lòng chọn"
        tvDistricts.apply {
            visibility = View.GONE
            text = "${MyEnum.LocationEnum.QuanHuyen} Vui lòng chọn"
        }
        tvCommune.apply {
            visibility = View.GONE
            text = "${MyEnum.LocationEnum.XaPhuong} Vui lòng chọn"
        }
        tvCity.apply {
            visibility = View.VISIBLE
            text = "${MyEnum.LocationEnum.TinhTP} Vui lòng chọn"
        }
    }

    private fun isHaveData(isHave: Boolean) {
        if (isHave) {
            btnFinishedAddAddress.animate().alpha(1f)
            btnFinishedAddAddress.isEnabled = true
        } else {
            btnFinishedAddAddress.animate().alpha(0.5f)
            btnFinishedAddAddress.isEnabled = false
        }
    }

    override fun onBackPressed() {
        if (textAddress.trim() != "") {
            DialogAsk(
                this@LocationActivity,
                "Xác nhận",
                "Bạn chắc chắn địa chỉ là chính xác?"
            ).apply {
                show()
                event.observe(this@LocationActivity) {
                    if (it) {
                        managerSaveLocal.saveAddress(textAddress)
                        listenerDialogSuccess.postValue(true)
                    } else {
                        listenerDialogSuccess.postValue(false)
                    }
                    dismiss()
                }
            }
        } else {
            if (managerSaveLocal.getAddress() != null) {
                finish()
            } else {
                DialogAsk.newInstance(
                    this@LocationActivity,
                    "Xác nhận",
                    "Bạn chưa chọn địa chỉ!\n Xác nhận thoát!"
                ).apply {
                    show()
                    event.observe(this@LocationActivity) {
                        if (it) {
                            finish()
                        }
                        dismiss()
                    }
                }
            }
        }
    }
}