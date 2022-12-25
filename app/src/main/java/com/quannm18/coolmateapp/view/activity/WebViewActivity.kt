package com.quannm18.coolmateapp.view.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.quannm18.coolmateapp.R
import com.quannm18.coolmateapp.base.BaseActivity
import com.quannm18.coolmateapp.utils.CommonUtils.Companion.configWebView
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {
    override fun layoutID(): Int = R.layout.activity_web_view

    override fun initData() {

    }

    override fun initView() {
        intent?.let {
            it.getStringExtra("linkUrl")?.let { link->
                webView.apply {
                    configWebView()
                    webView.settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                    @SuppressLint("ResourceAsColor")
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        progressBar.progress = 0
                        progressBar.visibility = View.VISIBLE
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (view == webView) {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            if (view == webView) {
                                progressBar.progress = newProgress
                            }
                        }
                    }
                }.loadUrl(link)
            }
        }
    }

    override fun listenLiveData() {

    }

    override fun listeners() {

    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            finish()
        }
    }
}