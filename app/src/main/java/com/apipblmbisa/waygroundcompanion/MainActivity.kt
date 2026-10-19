package com.apipblmbisa.waygroundcompanion

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.view.Gravity
import android.widget.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.net.Uri

class MainActivity : Activity() {
    private lateinit var input: EditText
    private lateinit var browser: WebView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 12)
            setBackgroundColor(Color.rgb(245, 247, 252))
        }

        val title = TextView(this).apply {
            text = "WAYGROUND"
            textSize = 25f
            setTextColor(Color.rgb(32, 54, 110))
            typeface = Typeface.DEFAULT_BOLD
        }
        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "Companion • Android"
            textSize = 14f
            setTextColor(Color.DKGRAY)
            setPadding(0, 4, 0, 18)
        }
        root.addView(subtitle)

        input = EditText(this).apply {
            hint = "Tempel tautan HTTPS di sini"
            singleLine = true
            textSize = 15f
            setPadding(12, 10, 12, 10)
        }
        root.addView(
            input,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val button = Button(this).apply {
            text = "BUKA HALAMAN"
            isAllCaps = false
        }
        root.addView(button)

        status = TextView(this).apply {
            text = "Masukkan tautan untuk membuka halaman web."
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8, 0, 8)
        }
        root.addView(status)

        browser = WebView(this)
        browser.settings.javaScriptEnabled = true
        browser.settings.domStorageEnabled = true
        browser.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
            ) {
                status.text = "Memuat halaman..."
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                status.text = url ?: "Halaman selesai dimuat."
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        root.addView(
            browser,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        button.setOnClickListener {
            var url = input.text.toString().trim()

            if (url.isEmpty()) {
                input.error = "Masukkan tautan terlebih dahulu"
                return@setOnClickListener
            }

            if (!url.startsWith("https://", true) &&
                !url.startsWith("http://", true)) {
                url = "https://$url"
            }

            val uri = Uri.parse(url)
            if (uri.scheme != "https" || uri.host.isNullOrBlank()) {
                status.text = "Gunakan tautan HTTPS yang valid."
                return@setOnClickListener
            }

            browser.loadUrl(uri.toString())
        }

        setContentView(root)
    }

    override fun onBackPressed() {
        if (::browser.isInitialized && browser.canGoBack()) {
            browser.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::browser.isInitialized) {
            browser.destroy()
        }
        super.onDestroy()
    }
}
