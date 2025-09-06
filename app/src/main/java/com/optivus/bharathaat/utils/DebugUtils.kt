package com.optivus.bharathaat.utils

import android.content.Context
import android.content.Intent
import com.optivus.bharathaat.ui.debug.SeedDataActivity

object DebugUtils {
    
    /**
     * Launch the data seeding activity
     * Use this to populate Firestore with sample product data for testing
     */
    fun launchSeedDataActivity(context: Context) {
        val intent = Intent(context, SeedDataActivity::class.java)
        context.startActivity(intent)
    }
    
    /**
     * Log debug information
     */
    fun debugLog(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }
}
