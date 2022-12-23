package com.quannm18.coolmateapp.utils

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.EditorInfo
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.quannm18.coolmateapp.MyApp.Companion.glide
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.network.api.ApiConfig.Companion.BASE_URL


class CommonUtils(var context: Context) {
    val EXTRA_LAST_APP = "EXTRA_LAST_APP"

    lateinit var usageStatsManager: UsageStatsManager

    fun getLauncherTopApp(): String {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val taskInfoList = manager.getRunningTasks(1)
            if (taskInfoList != null && taskInfoList.isNotEmpty()) {
                return taskInfoList[0].topActivity!!.packageName
            }
        } else {
            val endTime: Long = System.currentTimeMillis()
            val beginTime = endTime - 100000

            var result = ""
            val event: UsageEvents.Event = UsageEvents.Event()
            val usageEvents = usageStatsManager.queryEvents(beginTime, endTime)
            grantPermission()
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType != UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.packageName
                }
            }

            if (!TextUtils.isEmpty(result)) {
                return result
            }
        }
        return ""
    }

    private fun grantPermission() {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(),
                context.packageName
            )
        }
        if (mode != AppOpsManager.MODE_ALLOWED) {
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }


    companion object {

        fun setAnimRecycleView(v: RecyclerView) {
            val animator = v.itemAnimator
            if (null != animator) {
                if (animator is SimpleItemAnimator) {
                    animator.supportsChangeAnimations = false
                }
            }
        }

        fun pxToDp(px: Int): Float {
            return px / Resources.getSystem().displayMetrics.density
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun View.createBitmap(): Bitmap {
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            Canvas(bitmap).apply {
                background?.draw(this) ?: this.drawColor(Color.WHITE)
                draw(this)
            }
            return bitmap
        }

        fun loadIconApp(packageName: String, imageView: ImageView) {
            try {
                glide.load(imageView.context.packageManager.getApplicationIcon(packageName))
                    .circleCrop().override(imageView.measuredWidth)
                    .into(object : CustomViewTarget<ImageView, Drawable>(imageView) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            imageView.setImageDrawable(resource)
                        }
                    })
            } catch (ex: PackageManager.NameNotFoundException) {
            }
        }
        fun loadAvatarItem(link: String, imageView: ImageView) {
            try {
                glide.load(BASE_URL+link)
                    .centerCrop().override(imageView.measuredWidth)
                    .into(object : CustomViewTarget<ImageView, Drawable>(imageView) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            imageView.setImageResource(R.drawable.item_home)
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            if (link!=null){
                                imageView.setImageDrawable(resource)
                            }else{
                                imageView.setImageResource(R.drawable.item_home)
                            }
                        }
                    })
            } catch (ex: PackageManager.NameNotFoundException) {
            }
        }
        fun loadAvatarInt(res: Int, imageView: ImageView) {
            try {
                glide.load(res).override(MATCH_PARENT)
                    .into(object : CustomViewTarget<ImageView, Drawable>(imageView) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            imageView.setImageResource(R.drawable.item_home)
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            if (res!=null){
                                imageView.setImageDrawable(resource)
                            }else{
                                imageView.setImageResource(R.drawable.item_home)
                            }
                        }
                    })
            } catch (ex: PackageManager.NameNotFoundException) {
            }
        }

        fun loadBanner(path: String, imageView: ImageView) {
            try {
                glide.load(BASE_URL+path).override(MATCH_PARENT)
                    .into(object : CustomViewTarget<ImageView, Drawable>(imageView) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            imageView.setImageResource(R.drawable.showbg)
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            if (path!=null){
                                imageView.setImageDrawable(resource)
                            }else{
                                imageView.setImageResource(R.drawable.showbg)
                            }
                        }
                    })
            } catch (ex: PackageManager.NameNotFoundException) {
            }
        }
        fun MotionLayout.setVisibility(view: Int, visibility: Int, isEnd: IS_END) {

            when (isEnd) {
                IS_END.BOTH -> {
                    getConstraintSet(R.id.end)
                        ?.setVisibility(
                            view,
                            visibility
                        )
                    getConstraintSet(R.id.start)
                        ?.setVisibility(
                            view,
                            visibility
                        )
                }
                IS_END.END -> {
                    getConstraintSet(R.id.end)
                        ?.setVisibility(
                            view,
                            visibility
                        )
                }
                IS_END.START -> {
                    getConstraintSet(R.id.start)
                        ?.setVisibility(
                            view,
                            visibility
                        )
                }
            }
        }
        fun MotionLayout.setMargin(view: Int, anchorId: Int, margin: Int, isEnd: IS_END) {
            when (isEnd) {
                IS_END.BOTH -> {
                    getConstraintSet(R.id.end)
                        ?.setMargin(
                            view,
                            anchorId,
                            margin
                        )
                    getConstraintSet(R.id.start)
                        ?.setMargin(
                            view,
                            anchorId,
                            margin
                        )
                }
                IS_END.END -> {
                    getConstraintSet(R.id.end)
                        ?.setMargin(
                            view,
                            anchorId,
                            margin
                        )
                }
                IS_END.START -> {
                    getConstraintSet(R.id.start)
                        ?.setMargin(
                            view,
                            anchorId,
                            margin
                        )
                }
            }
        }
        enum class IS_END {
            BOTH, END, START
        }
        fun WebView.configWebView() {
            settings.javaScriptEnabled = true
            //zoom
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            //cache
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            //render
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            settings.domStorageEnabled = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            settings.useWideViewPort = true
            settings.setEnableSmoothTransition(true)
            settings.loadsImagesAutomatically
            //for download faster
            settings.saveFormData = true
        }
    }
    fun Snackbar.gravityTop() {
        this.view.layoutParams = (this.view.layoutParams as FrameLayout.LayoutParams).apply {
            gravity = Gravity.TOP
        }
    }
    fun EditText.onDone(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                callback.invoke()
                return@setOnEditorActionListener true
            }
            false
        }
    }
}