package com.quannm18.coolmateapp.view.activity

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.user.PostNotification
import com.quannm18.coolmateapp.network.auth.SessionManager
import com.quannm18.coolmateapp.utils.ManagerSaveLocal
import com.quannm18.coolmateapp.utils.PermissionUtil
import com.quannm18.coolmateapp.utils.Status
import com.quannm18.coolmateapp.view.adapter.home.HomeViewPagerAdapter
import com.quannm18.coolmateapp.view.dialog.DialogNhanThongBao
import com.quannm18.coolmateapp.view.dialog.DialogPhanHoiKhieuNai
import com.quannm18.coolmateapp.view.dialog.LoadingDialog
import com.quannm18.coolmateapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.view.*


class HomeActivity : BaseActivity() {
    private lateinit var mAdapter: HomeViewPagerAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val sessionManager: SessionManager by lazy {
        SessionManager()
    }
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val managerSaveLocal: ManagerSaveLocal by lazy {
        ManagerSaveLocal()
    }
    private val userViewModel: UserViewModel by viewModels()
    override fun layoutID(): Int = R.layout.activity_home

    override fun initData() {
        mAdapter = HomeViewPagerAdapter(this)
    }

    override fun initView() {
        paddingRootView(toolbar)
        vpMainHome.apply {
            adapter = mAdapter
            offscreenPageLimit = 4
        }
        TabLayoutMediator(tabLayout, vpMainHome) { a, b ->
            when (b) {
                0 -> {
                    a.text = getString(R.string.noi_bat)
                }
                1 -> {
                    a.text = getString(R.string.hot)
                }
                2 -> {
                    a.text = getString(R.string.giam_gia)
                }
                3 -> {
                    a.text = getString(R.string.kham_pha)
                }
            }
        }.attach()

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = null
        toolbar.logo = ContextCompat.getDrawable(this, R.drawable.logo)
        setUpLayout()
        navViewDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bill_order_drawer_item -> {
                    startActivity(Intent(this, BillHomeActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.favorite_drawer_item -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.location_drawer_item -> {
                    startActivity(Intent(this, LocationActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.chat_drawer_item -> {
                    startActivity(Intent(this, ChannelActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.payment_method_drawer_item -> {
                    startActivity(Intent(this, PaymentActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.voucher_drawer_item -> {
                    startActivity(Intent(this, VoucherActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.notification_drawer_item -> {
                    startActivity(Intent(this, NotificationActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
                R.id.helper_drawer_item -> {
                    DialogPhanHoiKhieuNai.newInstance(this).apply {
                        show()
                        event.observe(this@HomeActivity) {
                            if (it) {
                                startEmail()
                                dismiss()
                            } else {
                                dismiss()
                            }
                        }
                    }
                }
                R.id.info_drawer_item -> {
                    startActivity(Intent(this, InfoEditActivity::class.java))
                }
            }
            true
        }
        if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT) {
            if (!PermissionUtil.areNotificationsEnabled(this@HomeActivity)) {
                managerSaveLocal.saveEnableNotification(false)
            } else {
                managerSaveLocal.saveEnableNotification(true)
            }
        }
        if (Build.VERSION_CODES.TIRAMISU == Build.VERSION.SDK_INT) {
            if (!PermissionUtil.checkPermissionPostNotify(this@HomeActivity)) {
                managerSaveLocal.saveEnableNotification(false)
            } else {
                managerSaveLocal.saveEnableNotification(true)
            }
        }
        if (!managerSaveLocal.getEnableNotification()) {
            DialogNhanThongBao.newInstance(this).apply {
                show()
                event.observe(this@HomeActivity) {
                    if (it) {
                        if (Build.VERSION_CODES.R == Build.VERSION.SDK_INT) {
                            if (!PermissionUtil.areNotificationsEnabled(this@HomeActivity)) {
                                DialogNhanThongBao.newInstance(this@HomeActivity).apply {
                                    PermissionUtil.settingNotification(this@HomeActivity)
                                }.show()
                            }
                            return@observe
                        }
                        if (Build.VERSION_CODES.TIRAMISU == Build.VERSION.SDK_INT) {
                            PermissionUtil.checkPermissionPostNotify(this@HomeActivity)
                            return@observe
                        }
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(
                                        javaClass.simpleName,
                                        "Fetching FCM registration token failed",
                                        task.exception
                                    )
                                    return@OnCompleteListener
                                }
                                val token = task.result
                                userViewModel.postTokenNotification(token)
                            })
                        managerSaveLocal.saveEnableNotification(true)
                        dismiss()

                        Toast.makeText(applicationContext, "Xin cảm ơn!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        dismiss()
                    }
                }
            }
        } else {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(
                            javaClass.simpleName,
                            "Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }
                    val token = task.result
                    userViewModel.postTokenNotification(token)
                })
            managerSaveLocal.saveEnableNotification(true)
        }
    }

    override fun listenLiveData() {
        userViewModel.eventToken.observe(this) {
            if (it != null) {
                userViewModel.putTokenNotification(
                    "Bearer ${sessionManager.fetchAuthToken()}",
                    PostNotification("$it")
                ).observe(this) { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.e("TAG", "listenLiveData: ok")
                            loadingDialog.dismissDialog()
                        }
                        Status.LOADING -> {
                            Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()
                            loadingDialog.startLoadingDialog()
                        }
                        Status.ERROR -> {
                            Toast.makeText(
                                applicationContext,
                                "Hệ thống đang gặp vấn đề về thông báo!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    override fun listeners() {
        val navMain = navViewDrawer.getHeaderView(0)
        navMain.btnCloseHeader.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        navMain.imgLogoHeader.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        navMain.bgBtnEditInfo.setOnClickListener {
            startActivity(Intent(this, InfoEditActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification_item -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
            R.id.cart_item -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.favorite_item -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
            R.id.search_item -> {
                startActivity(Intent(this, FindActivity::class.java))
            }
            R.id.menu_item -> {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpLayout() {
        drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        drawerToggle.isDrawerIndicatorEnabled = false
        drawerToggle.setHomeAsUpIndicator(null)
        val width: Int = Resources.getSystem().displayMetrics.widthPixels
        val height: Int = Resources.getSystem().displayMetrics.heightPixels
        val widthOfNav = width * 0.9
        navViewDrawer.layoutParams.width = widthOfNav.toInt()
        navViewDrawer.requestLayout()
    }

    private fun startEmail() {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("namnhph14161@fpt.edu.vn"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về vấn đề")
        intent.putExtra(Intent.EXTRA_TEXT, "<Nội dung phản hồi>")
        intent.selector = selectorIntent
        startActivity(Intent.createChooser(intent, "Send email..."))
    }

}