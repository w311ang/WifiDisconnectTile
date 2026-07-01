package com.example.wifidisconnect

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val wifiManager by lazy {
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isWifiConnected()) {
            @Suppress("DEPRECATION")
            val result = wifiManager.disconnect()
            val msg = if (result) "已断开 Wi‑Fi" else "断开 Wi‑Fi 失败"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "当前未连接 Wi‑Fi", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun isWifiConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}
