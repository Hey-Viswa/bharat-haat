package com.optivus.bharathaat.data.models

import com.google.firebase.firestore.PropertyName

/**
 * User data model for Firestore
 */
data class UserData(
    val uid: String = "",
    @get:PropertyName("display_name") @set:PropertyName("display_name")
    var displayName: String = "",
    val email: String = "",
    @get:PropertyName("photo_url") @set:PropertyName("photo_url")
    var photoUrl: String? = null,
    @get:PropertyName("is_email_verified") @set:PropertyName("is_email_verified")
    var isEmailVerified: Boolean = false,
    @get:PropertyName("phone_number") @set:PropertyName("phone_number")
    var phoneNumber: String? = null,
    val gender: String? = null,
    @get:PropertyName("date_of_birth") @set:PropertyName("date_of_birth")
    var dateOfBirth: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val pincode: String? = null,
    val occupation: String? = null,

    // Enhanced fields for marketplace
    val role: String = "buyer", // "buyer" or "seller"
    @get:PropertyName("business_name") @set:PropertyName("business_name")
    var businessName: String? = null,
    @get:PropertyName("business_description") @set:PropertyName("business_description")
    var businessDescription: String? = null,
    @get:PropertyName("business_address") @set:PropertyName("business_address")
    var businessAddress: String? = null,
    @get:PropertyName("business_phone") @set:PropertyName("business_phone")
    var businessPhone: String? = null,
    @get:PropertyName("is_verified_seller") @set:PropertyName("is_verified_seller")
    var isVerifiedSeller: Boolean = false,

    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Long = 0L,
    @get:PropertyName("updated_at") @set:PropertyName("updated_at")
    var updatedAt: Long = 0L
)
