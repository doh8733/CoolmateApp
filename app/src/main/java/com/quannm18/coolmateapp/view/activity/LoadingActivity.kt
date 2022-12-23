package com.quannm18.coolmateapp.view.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.model.ItemSlider
import com.quannm18.coolmateapp.view.adapter.home.SliderAdapter
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.coroutines.*


class LoadingActivity : BaseActivity() {
    private val mAdapter: SliderAdapter = SliderAdapter()
    override fun layoutID(): Int = R.layout.activity_loading
    private lateinit var runnable: Runnable
    private var areNotificationsEnabled: Boolean = false
    private lateinit var handler: Handler

    override fun initData() {
        mAdapter.initMyAdapter(initList())
        areNotificationsEnabled =
            NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()
    }

    override fun initView() {
        marginNavigationBar(listOf(layoutOnBoardingIndicator))
        addBounceView(listOf(btnShoppingNow))
        vpMain.apply {
            adapter = mAdapter
        }
        setUpOnBoardingIndicator()
        setCurrentIndicator(0)

        vpMain.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == 2) {
                    btnShoppingNow.animate().alpha(1f)
                }
            }
        })
        startAutoSlider(3)

        addBounceView(listOf(btnShoppingNow))
    }

    private fun startAutoSlider(count: Int = 3) {
        handler = Handler()
        runnable = Runnable {
            var pos: Int = vpMain.currentItem
            pos += 1
            if (pos >= count) pos = 0
            vpMain.currentItem = pos
            handler.postDelayed(runnable, 2000)
        }
        handler.postDelayed(runnable, 2000)
    }

    override fun listenLiveData() {

    }

    override fun listeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (areNotificationsEnabled) {
                finish()
                startActivity(Intent(this, SplashActivity::class.java))
            } else {
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    private fun initList(): MutableList<ItemSlider> {
        val mList: MutableList<ItemSlider> = mutableListOf()
        mList.add(ItemSlider(0, R.drawable.slide0, "Anh 1"))
        mList.add(ItemSlider(1, R.drawable.slide1, "Anh 2"))
        mList.add(ItemSlider(2, R.drawable.slide2, "Anh 3"))
        return mList
    }

    private fun setUpOnBoardingIndicator() {
        val indicators: MutableList<ImageView> =
            MutableList(3) { ImageView(applicationContext) }
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(16, 0, 16, 0)
        for (i in 0..2) {
            indicators[i].setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_inactive
                )
            )
            indicators[i].layoutParams = layoutParams
            layoutOnBoardingIndicator.addView(indicators[i])
        }
    }

    fun setCurrentIndicator(index: Int) {
        val imgView = layoutOnBoardingIndicator.getChildAt(index) as ImageView
        when (index) {
            0 -> {
                val imgView1 = layoutOnBoardingIndicator.getChildAt(1) as ImageView
                val imgView2 = layoutOnBoardingIndicator.getChildAt(2) as ImageView
                imgView.setImageResource(R.drawable.onboarding_indicator_active)
                imgView1.setImageResource(R.drawable.onboarding_indicator_inactive)
                imgView2.setImageResource(R.drawable.onboarding_indicator_inactive)
            }
            1 -> {
                val imgView0 = layoutOnBoardingIndicator.getChildAt(0) as ImageView
                val imgView2 = layoutOnBoardingIndicator.getChildAt(2) as ImageView
                imgView.setImageResource(R.drawable.onboarding_indicator_active)
                imgView0.setImageResource(R.drawable.onboarding_indicator_inactive)
                imgView2.setImageResource(R.drawable.onboarding_indicator_inactive)
            }
            2 -> {
                val imgView0 = layoutOnBoardingIndicator.getChildAt(0) as ImageView
                val imgView1 = layoutOnBoardingIndicator.getChildAt(1) as ImageView
                imgView.setImageResource(R.drawable.onboarding_indicator_active)
                imgView0.setImageResource(R.drawable.onboarding_indicator_inactive)
                imgView1.setImageResource(R.drawable.onboarding_indicator_inactive)
            }
        }
        Log.e(javaClass.simpleName, "setCurrentIndicator: $index")
    }

    fun autoSlide() {
        runBlocking {
            withContext(Dispatchers.Main) {
                while (true) {
                    println("time reached")
                    delay(timeMillis = 2000)
                }
            }
        }
    }
}