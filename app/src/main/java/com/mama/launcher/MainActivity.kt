package com.mama.launcher

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private lateinit var web: WebView

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.webViewClient = WebViewClient()
        web.addJavascriptInterface(Bridge(), "Android")
        setContentView(web)
        web.loadUrl("file:///android_asset/index.html")
        web.requestFocus()
    }

    inner class Bridge {
        @JavascriptInterface
        fun open(pkg: String) {
            runOnUiThread {
                val i = packageManager.getLeanbackLaunchIntentForPackage(pkg)
                    ?: packageManager.getLaunchIntentForPackage(pkg)
                if (i != null) startActivity(i)
            }
        }
    }

    override fun onKeyDown(k: Int, e: KeyEvent?): Boolean {
        if (k == KeyEvent.KEYCODE_BACK) {
            web.evaluateJavascript("window.onBack&&window.onBack()", null)
            return true
        }
        return super.onKeyDown(k, e)
    }
}
