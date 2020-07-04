package com.kartik.grabinterviewtestapp_news.data.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

class NetworkUtils {
    companion object {
        private val networkConnected = MutableLiveData<Boolean>()
        private var initialized = false
        private const val TAG = "NetworkUtils"

        fun init(mContext: Context) {
            if(!initialized) {
                val cm: ConnectivityManager =
                    mContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                cm.registerDefaultNetworkCallback(
                    object : ConnectivityManager.NetworkCallback() {

                        override fun onAvailable(network: Network) {
                            networkConnected.postValue(true)
                            Log.d(TAG, "onAvailable: internet available")
                        }

                        override fun onLost(network: Network) {
                            networkConnected.postValue(false)
                            Log.d(TAG, "onLost: internet lost")
                        }
                    }
                )
                initialized = true
            }
        }

        fun getNetworkStatus(): LiveData<Boolean> {
            if(initialized)
                return networkConnected
            else
                throw InitNotCalledException("NetworkUtils has not been initialized with context.")
        }
    }
}

class InitNotCalledException(s: String) : Exception(s) {

}
