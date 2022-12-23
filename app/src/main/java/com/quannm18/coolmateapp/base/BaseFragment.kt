package com.quannm18.coolmateapp.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.github.hariprasanths.bounceview.BounceView
import com.quannm18.coolmateapp.MyApp.Companion.glide

abstract class BaseFragment : Fragment() {
    private lateinit var packageNameUninstall: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreate()
        return inflater.inflate(layoutID(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        listener()
        listenLiveData()
    }


    protected abstract fun layoutID(): Int
    open fun onCreate() {
        mActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                Log.d("UninstallResult", "${it.data?.getStringExtra("packageName")}")
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        uninstallSuccess(packageNameUninstall)
                        Log.d("TAG", "onActivityResult: user accepted the (un)install")
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.d("TAG", "onActivityResult: user canceled the (un)install")
                    }
                    Activity.RESULT_FIRST_USER -> {
                        Log.d("TAG", "onActivityResult: failed to (un)install")
                    }
                }
            }
    }

    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun listenLiveData()
    protected abstract fun listener()
    open fun uninstallSuccess(packageName: String) {}

    fun goToNewActivity(activity: Class<*>) {
        startActivity(Intent(requireActivity(), activity))
    }

    protected fun loadImage(res: Int, v: View) {
        glide.load(res).centerInside().into(v as ImageView)
    }

    protected fun loadImage(res: String?, v: View) {
        glide.load(res).centerInside().into(v as ImageView)
    }

    open fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun marginStatusBar(view: View) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.height = getStatusBarHeight()
        view.layoutParams = params
    }

    fun marginViewWithStatusBar(view: View) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = params.topMargin + getStatusBarHeight()
        view.layoutParams = params
    }

    fun paddingViewWithNavigationBar(view: View) {
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            getNavigationBarHeight() + view.paddingBottom
        )
    }

    fun marginViewWithNavigationBar(view: View) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.bottomMargin = params.bottomMargin + getNavigationBarHeight()
        view.layoutParams = params
    }

    fun boundView(listView: List<View>) {
        listView.map {
            BounceView.addAnimTo(it).setScaleForPushInAnim(1.01f, 1.01f)
        }
    }

    private var mActivityResult: ActivityResultLauncher<Intent>? = null

    fun uninstallApp(packageName: String) {
        packageNameUninstall = packageName
        val i = Intent(Intent.ACTION_DELETE)
        i.data = Uri.parse("package:${packageName}")
        i.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        i.putExtra("packageName", packageName)
        mActivityResult?.launch(i)
    }

    fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager? =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        var isHaveInternet: Boolean = false
        if (cm != null) {
            isHaveInternet =
                cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected ?: false
        }
        if (!isHaveInternet) {

        }
        return isHaveInternet
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivityResult = null
    }

}
