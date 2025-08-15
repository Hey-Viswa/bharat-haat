package com.optivus.bharat_haat.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import java.util.*

object DeviceUtils {

    /**
     * Device information
     */
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            "${capitalize(manufacturer)} $model"
        }
    }

    fun getDeviceModel(): String = Build.MODEL

    fun getDeviceManufacturer(): String = Build.MANUFACTURER

    fun getDeviceBrand(): String = Build.BRAND

    fun getAndroidVersion(): String = Build.VERSION.RELEASE

    fun getSDKVersion(): Int = Build.VERSION.SDK_INT

    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceId = "",
            deviceName = getDeviceName(),
            manufacturer = getDeviceManufacturer(),
            model = getDeviceModel(),
            brand = getDeviceBrand(),
            androidVersion = getAndroidVersion(),
            sdkVersion = getSDKVersion(),
            screenSize = "",
            screenDensity = ""
        )
    }

    /**
     * Screen information
     */
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getScreenDensityDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    fun getScreenWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getScreenSize(context: Context): Pair<Int, Int> {
        return Pair(getScreenWidth(context), getScreenHeight(context))
    }

    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * getScreenDensity(context)).toInt()
    }

    fun pxToDp(context: Context, px: Float): Int {
        return (px / getScreenDensity(context)).toInt()
    }

    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getScreenOrientation(context: Context): String {
        return when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> "Portrait"
            Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
            else -> "Unknown"
        }
    }

    /**
     * Memory information
     */
    fun getTotalRAM(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    fun getAvailableRAM(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    fun isLowMemory(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.lowMemory
    }

    fun getMemoryUsagePercentage(context: Context): Float {
        val total = getTotalRAM(context)
        val available = getAvailableRAM(context)
        val used = total - available
        return (used.toFloat() / total.toFloat()) * 100f
    }

    /**
     * Storage information
     */
    fun getTotalInternalStorage(): Long {
        val path = android.os.Environment.getDataDirectory()
        return path.totalSpace
    }

    fun getAvailableInternalStorage(): Long {
        val path = android.os.Environment.getDataDirectory()
        return path.freeSpace
    }

    fun getUsedInternalStorage(): Long {
        return getTotalInternalStorage() - getAvailableInternalStorage()
    }

    fun getStorageUsagePercentage(): Float {
        val total = getTotalInternalStorage()
        val used = getUsedInternalStorage()
        return (used.toFloat() / total.toFloat()) * 100f
    }

    /**
     * Network carrier information
     */
    @SuppressLint("MissingPermission")
    fun getCarrierName(context: Context): String? {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.networkOperatorName
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    fun getNetworkOperator(context: Context): String? {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.networkOperator
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    fun getSimState(context: Context): String {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (telephonyManager.simState) {
                TelephonyManager.SIM_STATE_ABSENT -> "No SIM"
                TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "Network Locked"
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> "PIN Required"
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> "PUK Required"
                TelephonyManager.SIM_STATE_READY -> "Ready"
                TelephonyManager.SIM_STATE_UNKNOWN -> "Unknown"
                else -> "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    /**
     * App information
     */
    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    fun getAppVersionCode(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            0L
        }
    }

    fun getPackageName(context: Context): String = context.packageName

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Hardware features
     */
    fun hasCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun hasFrontCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    fun hasFlash(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun hasGPS(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
    }

    fun hasBluetooth(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    fun hasWiFi(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)
    }

    fun hasNFC(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
    }

    fun hasFingerprint(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    fun hasTelephony(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    /**
     * System settings
     */
    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) != 0
    }

    fun isUSBDebuggingEnabled(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) != 0
    }

    /**
     * Language and locale
     */
    fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }

    fun getCurrentCountry(): String {
        return Locale.getDefault().country
    }

    fun getCurrentLocale(): String {
        return Locale.getDefault().toString()
    }

    fun isRTL(context: Context): Boolean {
        return context.resources.configuration.layoutDirection ==
               android.view.View.LAYOUT_DIRECTION_RTL
    }

    /**
     * Device capabilities
     */
    fun getDeviceCapabilities(context: Context): DeviceCapabilities {
        return DeviceCapabilities(
            hasCamera = hasCamera(context),
            hasFrontCamera = hasFrontCamera(context),
            hasFlash = hasFlash(context),
            hasGPS = hasGPS(context),
            hasBluetooth = hasBluetooth(context),
            hasWiFi = hasWiFi(context),
            hasNFC = hasNFC(context),
            hasFingerprint = hasFingerprint(context),
            hasTelephony = hasTelephony(context),
            isTablet = isTablet(context),
            totalRAM = getTotalRAM(context),
            totalStorage = getTotalInternalStorage(),
            screenWidth = getScreenWidth(context),
            screenHeight = getScreenHeight(context),
            screenDensity = getScreenDensity(context)
        )
    }

    /**
     * Rooting detection
     */
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )

        for (path in paths) {
            if (java.io.File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val bufferedReader = java.io.BufferedReader(java.io.InputStreamReader(process.inputStream))
            bufferedReader.readLine() != null
        } catch (t: Throwable) {
            false
        }
    }

    private fun capitalize(str: String): String {
        return str.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String,
    val manufacturer: String,
    val model: String,
    val brand: String,
    val androidVersion: String,
    val sdkVersion: Int,
    val screenSize: String,
    val screenDensity: String
)

data class DeviceCapabilities(
    val hasCamera: Boolean,
    val hasFrontCamera: Boolean,
    val hasFlash: Boolean,
    val hasGPS: Boolean,
    val hasBluetooth: Boolean,
    val hasWiFi: Boolean,
    val hasNFC: Boolean,
    val hasFingerprint: Boolean,
    val hasTelephony: Boolean,
    val isTablet: Boolean,
    val totalRAM: Long,
    val totalStorage: Long,
    val screenWidth: Int,
    val screenHeight: Int,
    val screenDensity: Float
)
