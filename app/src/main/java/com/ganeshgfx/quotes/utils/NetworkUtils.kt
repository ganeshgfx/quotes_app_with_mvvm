package com.ganeshgfx.quotes.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService


class NetworkUtils {
        fun isOnline(context: Context): Boolean {
           return checkNetwork(context)
        }
        private fun checkNetwork(context: Context): Boolean{
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.run {
                return this.getNetworkCapabilities(
                    this.activeNetwork
                )?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            }
            return false
        }
}