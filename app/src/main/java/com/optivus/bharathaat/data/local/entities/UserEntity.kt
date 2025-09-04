package com.optivus.bharathaat.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optivus.bharathaat.data.models.UserData

@Entity(tableName = "users")
@TypeConverters(UserEntityConverters::class)
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val phoneNumber: String?,
    val gender: String?,
    val dateOfBirth: String?,
    val address: String?,
    val city: String?,
    val state: String?,
    val pincode: String?,
    val occupation: String?,
    val role: String,
    val businessName: String?,
    val businessDescription: String?,
    val businessAddress: String?,
    val businessPhone: String?,
    val isVerifiedSeller: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val lastSyncedAt: Long = System.currentTimeMillis()
)

class UserEntityConverters {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.let {
            Gson().fromJson(it, object : TypeToken<List<String>>() {}.type)
        }
    }

    @TypeConverter
    fun fromListString(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }
}

// Extension functions for conversion
fun UserData.toEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        isEmailVerified = isEmailVerified,
        phoneNumber = phoneNumber,
        gender = gender,
        dateOfBirth = dateOfBirth,
        address = address,
        city = city,
        state = state,
        pincode = pincode,
        occupation = occupation,
        role = role,
        businessName = businessName,
        businessDescription = businessDescription,
        businessAddress = businessAddress,
        businessPhone = businessPhone,
        isVerifiedSeller = isVerifiedSeller,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserEntity.toUserData(): UserData {
    return UserData(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        isEmailVerified = isEmailVerified,
        phoneNumber = phoneNumber,
        gender = gender,
        dateOfBirth = dateOfBirth,
        address = address,
        city = city,
        state = state,
        pincode = pincode,
        occupation = occupation,
        role = role,
        businessName = businessName,
        businessDescription = businessDescription,
        businessAddress = businessAddress,
        businessPhone = businessPhone,
        isVerifiedSeller = isVerifiedSeller,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
