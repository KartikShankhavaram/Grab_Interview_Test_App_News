package com.kartik.grabinterviewtestapp_news.data.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

class NetworkUtils {
    companion object {
        private val networkConnected = MutableLiveData<Boolean>()
        private var initialized = false

        fun init(mContext: Context) {
            if(!initialized) {
                val cm: ConnectivityManager =
                    mContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val builder: NetworkRequest.Builder = NetworkRequest.Builder()

                cm.registerNetworkCallback(
                    builder.build(),
                    object : ConnectivityManager.NetworkCallback() {

                        override fun onAvailable(network: Network) {
                            networkConnected.postValue(true)
                        }

                        override fun onLost(network: Network) {
                            networkConnected.postValue(false)
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
