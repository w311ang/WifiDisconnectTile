package com.example.wifidisconnect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class WifiDisconnectTileService : TileService() {

    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val wifiManager by lazy {
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    // 监听 Wi-Fi 状态变化，实时刷新 Tile
    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateTile()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        val filter = IntentFilter().apply {
            addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        }
        registerReceiver(wifiStateReceiver, filter)
        updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        try {
            unregisterReceiver(wifiStateReceiver)
        } catch (e: IllegalArgumentException) {
            // receiver 未注册，忽略
        }
    }

    override fun onClick() {
        super.onClick()
        if (isWifiConnected()) {
            disconnectWifi()
        } else {
            Toast.makeText(this, getString(R.string.wifi_not_connected), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 判断当前是否已连接到 Wi-Fi
     */
    private fun isWifiConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    /**
     * 断开 Wi-Fi 连接
     * Android 10+ 使用 bindProcessToNetwork(null) + requestNetwork 方式，
     * Android 9 及以下直接调用 WifiManager.disconnect()
     */
    private fun disconnectWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork != null) {
                connectivityManager.bindProcessToNetwork(null)
                connectivityManager.unregisterNetworkCallback(networkCallback)

                @Suppress("DEPRECATION")
                wifiManager.disconnect()
                Toast.makeText(this, getString(R.string.wifi_disconnected), Toast.LENGTH_SHORT).show()
            }
        } else {
            @Suppress("DEPRECATION")
            if (wifiManager.disconnect()) {
                Toast.makeText(this, getString(R.string.wifi_disconnected), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.wifi_disconnect_failed), Toast.LENGTH_SHORT).show()
            }
        }
        updateTile()
    }

    /**
     * 用于 Android 10+ 网络监听的回调（占位，防止 unregister 报错）
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {}

    /**
     * 刷新 Tile 的图标与状态文字
     */
    private fun updateTile() {
        val tile = qsTile ?: return
        if (isWifiConnected()) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = getString(R.string.tile_label_connected)
            tile.contentDescription = getString(R.string.tile_label_connected)
            tile.icon = Icon.createWithResource(this, R.drawable.ic_wifi_on)
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = getString(R.string.tile_label_disconnected)
            tile.contentDescription = getString(R.string.tile_label_disconnected)
            tile.icon = Icon.createWithResource(this, R.drawable.ic_wifi_off)
        }
        tile.updateTile()
    }
}
