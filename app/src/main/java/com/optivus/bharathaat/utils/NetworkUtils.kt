package com.optivus.bharathaat.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.net.InetSocketAddress
import java.net.Socket

object NetworkUtils {

    /**
     * Network connectivity checks
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            // Check for internet capability, but don't require validation immediately
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
             networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
             networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnectedOrConnecting == true
        }
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        }
    }

    fun isMobileDataConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected
        }
    }

    fun getNetworkType(context: Context): NetworkType {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE

            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.MOBILE
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                else -> NetworkType.OTHER
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return NetworkType.NONE

            return when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
                ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
                ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
                else -> NetworkType.OTHER
            }
        }
    }

    /**
     * Internet connectivity test
     */
    suspend fun hasInternetConnection(): Boolean {
        return try {
            // Try multiple approaches for better reliability
            
            // First, try Google DNS (8.8.8.8) on port 53
            try {
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)
                socket.connect(socketAddress, 3000)
                socket.close()
                return true
            } catch (e: Exception) {
                // If that fails, try Cloudflare DNS (1.1.1.1) on port 53
                try {
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("1.1.1.1", 53)
                    socket.connect(socketAddress, 2000)
                    socket.close()
                    return true
                } catch (e2: Exception) {
                    // If DNS ports are blocked, try HTTPS port with Google
                    try {
                        val socket = Socket()
                        val socketAddress = InetSocketAddress("www.google.com", 80)
                        socket.connect(socketAddress, 3000)
                        socket.close()
                        return true
                    } catch (e3: Exception) {
                        return false
                    }
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Network speed estimation
     */
    fun getNetworkSpeed(context: Context): NetworkSpeed {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return NetworkSpeed.UNKNOWN
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkSpeed.UNKNOWN

            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkSpeed.FAST
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    // Estimate based on cellular network
                    NetworkSpeed.MEDIUM
                }
                else -> NetworkSpeed.UNKNOWN
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return NetworkSpeed.UNKNOWN

            return when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkSpeed.FAST
                ConnectivityManager.TYPE_MOBILE -> {
                    when (networkInfo.subtype) {
                        in listOf(1, 2, 4, 7, 11) -> NetworkSpeed.SLOW // 2G
                        in listOf(3, 5, 6, 8, 9, 10, 12, 14, 15) -> NetworkSpeed.MEDIUM // 3G
                        in listOf(13, 19, 20) -> NetworkSpeed.FAST // 4G/LTE
                        else -> NetworkSpeed.MEDIUM
                    }
                }
                else -> NetworkSpeed.UNKNOWN
            }
        }
    }

    /**
     * URL validation and manipulation
     */
    fun isValidURL(url: String): Boolean {
        return try {
            val regex = Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
            regex.matches(url)
        } catch (e: Exception) {
            false
        }
    }

    fun addHttpsIfMissing(url: String): String {
        return when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            else -> "https://$url"
        }
    }

    fun extractHostFromUrl(url: String): String? {
        return try {
            val regex = Regex("^(?:https?://)?(?:www\\.)?([^/]+)")
            regex.find(url)?.groupValues?.get(1)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * API request helpers
     */
    fun buildQueryString(params: Map<String, String>): String {
        return params.entries.joinToString("&") { (key, value) ->
            "$key=${java.net.URLEncoder.encode(value, "UTF-8")}"
        }
    }

    fun parseQueryString(queryString: String): Map<String, String> {
        val params = mutableMapOf<String, String>()
        queryString.split("&").forEach { param ->
            val keyValue = param.split("=", limit = 2)
            if (keyValue.size == 2) {
                params[keyValue[0]] = java.net.URLDecoder.decode(keyValue[1], "UTF-8")
            }
        }
        return params
    }

    /**
     * Download utilities
     */
    fun getFileNameFromUrl(url: String): String? {
        return try {
            val segments = url.split("/")
            segments.lastOrNull()?.split("?")?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun getContentTypeFromExtension(extension: String): String {
        return when (extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "json" -> "application/json"
            "xml" -> "application/xml"
            else -> "application/octet-stream"
        }
    }

    /**
     * Error handling
     */
    fun getNetworkErrorMessage(throwable: Throwable): String {
        return when {
            throwable is java.net.UnknownHostException -> "No internet connection"
            throwable is java.net.SocketTimeoutException -> "Request timeout"
            throwable is java.net.ConnectException -> "Connection failed"
            throwable.message?.contains("timeout", ignoreCase = true) == true -> "Request timeout"
            throwable.message?.contains("network", ignoreCase = true) == true -> "Network error"
            else -> "Network error occurred"
        }
    }

    fun isNetworkError(throwable: Throwable): Boolean {
        return throwable is java.net.UnknownHostException ||
                throwable is java.net.SocketTimeoutException ||
                throwable is java.net.ConnectException ||
                throwable is java.io.IOException
    }
}

enum class NetworkType {
    NONE, WIFI, MOBILE, ETHERNET, OTHER
}

enum class NetworkSpeed {
    SLOW, MEDIUM, FAST, UNKNOWN
}
