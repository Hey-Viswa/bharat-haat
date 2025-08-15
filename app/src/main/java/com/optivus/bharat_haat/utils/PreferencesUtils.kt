package com.optivus.bharat_haat.utils

import android.content.Context
import android.content.SharedPreferences
import com.optivus.bharat_haat.constants.AppConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * PreferencesUtils - SharedPreferences management utilities
 *
 * Gson functionality is now enabled since Gson dependency is available in build.gradle.kts
 */
object PreferencesUtils {

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * String preferences
     */
    fun putString(context: Context, key: String, value: String) {
        getSharedPreferences(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        return getSharedPreferences(context).getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Boolean preferences
     */
    fun putBoolean(context: Context, key: String, value: Boolean) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        return getSharedPreferences(context).getBoolean(key, defaultValue)
    }

    /**
     * Integer preferences
     */
    fun putInt(context: Context, key: String, value: Int) {
        getSharedPreferences(context).edit().putInt(key, value).apply()
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return getSharedPreferences(context).getInt(key, defaultValue)
    }

    /**
     * Long preferences
     */
    fun putLong(context: Context, key: String, value: Long) {
        getSharedPreferences(context).edit().putLong(key, value).apply()
    }

    fun getLong(context: Context, key: String, defaultValue: Long = 0L): Long {
        return getSharedPreferences(context).getLong(key, defaultValue)
    }

    /**
     * Float preferences
     */
    fun putFloat(context: Context, key: String, value: Float) {
        getSharedPreferences(context).edit().putFloat(key, value).apply()
    }

    fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        return getSharedPreferences(context).getFloat(key, defaultValue)
    }

    /**
     * Object preferences using Gson
     */
    fun <T> putObject(context: Context, key: String, obj: T) {
        val gson = Gson()
        val json = gson.toJson(obj)
        putString(context, key, json)
    }

    inline fun <reified T> getObject(context: Context, key: String): T? {
        val json = getString(context, key)
        return if (json.isEmpty()) {
            null
        } else {
            try {
                val gson = Gson()
                gson.fromJson(json, T::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * List preferences using Gson
     */
    fun <T> putList(context: Context, key: String, list: List<T>) {
        val gson = Gson()
        val json = gson.toJson(list)
        putString(context, key, json)
    }

    inline fun <reified T> getList(context: Context, key: String): List<T> {
        val json = getString(context, key)
        return if (json.isEmpty()) {
            emptyList()
        } else {
            try {
                val gson = Gson()
                val type = object : TypeToken<List<T>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Set preferences using native SharedPreferences (no Gson needed)
     */
    fun putStringSet(context: Context, key: String, set: Set<String>) {
        getSharedPreferences(context).edit().putStringSet(key, set).apply()
    }

    fun getStringSet(context: Context, key: String): Set<String> {
        return getSharedPreferences(context).getStringSet(key, emptySet()) ?: emptySet()
    }

    /**
     * Remove and clear
     */
    fun remove(context: Context, key: String) {
        getSharedPreferences(context).edit().remove(key).apply()
    }

    fun clear(context: Context) {
        getSharedPreferences(context).edit().clear().apply()
    }

    fun contains(context: Context, key: String): Boolean {
        return getSharedPreferences(context).contains(key)
    }

    /**
     * User session management
     */
    fun saveUserToken(context: Context, token: String) {
        putString(context, AppConstants.KEY_USER_TOKEN, token)
    }

    fun getUserToken(context: Context): String {
        return getString(context, AppConstants.KEY_USER_TOKEN)
    }

    fun saveUserId(context: Context, userId: String) {
        putString(context, AppConstants.KEY_USER_ID, userId)
    }

    fun getUserId(context: Context): String {
        return getString(context, AppConstants.KEY_USER_ID)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        putBoolean(context, AppConstants.KEY_IS_LOGGED_IN, isLoggedIn)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getBoolean(context, AppConstants.KEY_IS_LOGGED_IN)
    }

    fun saveUserPhone(context: Context, phone: String) {
        putString(context, AppConstants.KEY_USER_PHONE, phone)
    }

    fun getUserPhone(context: Context): String {
        return getString(context, AppConstants.KEY_USER_PHONE)
    }

    fun saveUserEmail(context: Context, email: String) {
        putString(context, AppConstants.KEY_USER_EMAIL, email)
    }

    fun getUserEmail(context: Context): String {
        return getString(context, AppConstants.KEY_USER_EMAIL)
    }

    /**
     * App settings
     */
    fun setFirstTimeLaunch(context: Context, isFirstTime: Boolean) {
        putBoolean(context, AppConstants.KEY_FIRST_TIME_LAUNCH, isFirstTime)
    }

    fun isFirstTimeLaunch(context: Context): Boolean {
        return getBoolean(context, AppConstants.KEY_FIRST_TIME_LAUNCH, true)
    }

    fun setSelectedLanguage(context: Context, language: String) {
        putString(context, AppConstants.KEY_SELECTED_LANGUAGE, language)
    }

    fun getSelectedLanguage(context: Context): String {
        return getString(context, AppConstants.KEY_SELECTED_LANGUAGE, "en")
    }

    fun setNotificationEnabled(context: Context, enabled: Boolean) {
        putBoolean(context, AppConstants.KEY_NOTIFICATION_ENABLED, enabled)
    }

    fun isNotificationEnabled(context: Context): Boolean {
        return getBoolean(context, AppConstants.KEY_NOTIFICATION_ENABLED, true)
    }

    fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        putBoolean(context, AppConstants.KEY_DARK_MODE, enabled)
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        return getBoolean(context, AppConstants.KEY_DARK_MODE, false)
    }

    /**
     * Cart and Wishlist counts
     */
    fun setCartCount(context: Context, count: Int) {
        putInt(context, AppConstants.KEY_CART_COUNT, count)
    }

    fun getCartCount(context: Context): Int {
        return getInt(context, AppConstants.KEY_CART_COUNT, 0)
    }

    fun setWishlistCount(context: Context, count: Int) {
        putInt(context, AppConstants.KEY_WISHLIST_COUNT, count)
    }

    fun getWishlistCount(context: Context): Int {
        return getInt(context, AppConstants.KEY_WISHLIST_COUNT, 0)
    }

    /**
     * Search history
     */
    fun addToSearchHistory(context: Context, query: String) {
        val history = getSearchHistory(context).toMutableList()
        history.remove(query) // Remove if already exists
        history.add(0, query) // Add to top

        // Keep only last 10 searches
        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        putList(context, "search_history", history)
    }

    fun getSearchHistory(context: Context): List<String> {
        return getList(context, "search_history")
    }

    fun clearSearchHistory(context: Context) {
        remove(context, "search_history")
    }

    /**
     * Recently viewed products
     */
    fun addToRecentlyViewed(context: Context, productId: String) {
        val recentProducts = getRecentlyViewed(context).toMutableList()
        recentProducts.remove(productId) // Remove if already exists
        recentProducts.add(0, productId) // Add to top

        // Keep only last 20 products
        if (recentProducts.size > 20) {
            recentProducts.removeAt(recentProducts.size - 1)
        }

        putList(context, "recently_viewed", recentProducts)
    }

    fun getRecentlyViewed(context: Context): List<String> {
        return getList(context, "recently_viewed")
    }

    fun clearRecentlyViewed(context: Context) {
        remove(context, "recently_viewed")
    }

    /**
     * App cache management
     */
    fun setCacheTimestamp(context: Context, key: String, timestamp: Long = System.currentTimeMillis()) {
        putLong(context, "${key}_cache_timestamp", timestamp)
    }

    fun getCacheTimestamp(context: Context, key: String): Long {
        return getLong(context, "${key}_cache_timestamp", 0L)
    }

    fun isCacheValid(context: Context, key: String, validityDuration: Long): Boolean {
        val timestamp = getCacheTimestamp(context, key)
        return (System.currentTimeMillis() - timestamp) < validityDuration
    }

    fun clearCache(context: Context, key: String) {
        remove(context, key)
        remove(context, "${key}_cache_timestamp")
    }

    /**
     * Logout and clear user data
     */
    fun logout(context: Context) {
        remove(context, AppConstants.KEY_USER_TOKEN)
        remove(context, AppConstants.KEY_USER_ID)
        remove(context, AppConstants.KEY_USER_PHONE)
        remove(context, AppConstants.KEY_USER_EMAIL)
        setLoggedIn(context, false)
        setCartCount(context, 0)
        setWishlistCount(context, 0)
    }
}
